package com.exp;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.Optional;


public class Gui extends JFrame {
    private JTextField uaText, dataText, targetsPathText;
    private JMenuBar fileMenu, helpMenu, proxyMenu;
    private JMenuItem importTargetsMenuItem, exportMenuItem, aboutMenuItem, httpMenuItem;
    private JPanel topPanel, westPanel, eastPanel;
    private JLabel targetsLabel, pathLabel, cookiesLabel, uaLabel, dataLabel, headLabel, countLabel,timeLabel;
    private JButton runButton, stopButton, clearButton;
    private JComboBox<String> threadBox, enctypeBox,timeBox;
    private JTextArea dataTextArea, targetsTextArea, headTextArea, cookiesTextArea;
    private JScrollPane targetsScroll, tableScroll, headScrollPane, cookiesScrollPane, dataScrollPane;
    private JTable table;
    private Vector<String> columnNames;
    private DefaultTableModel dataModel;
    private ButtonGroup requestsButtonGroup;
    private JRadioButton getRadio, postRadio, headRadio;
    private JCheckBox followBox, fingerprint_switch;
    private ExecutorService pool;
    private int total, doneCount;
    private JPopupMenu rightMenu;
    private String targetUrl, host, portString, eholePath;
    private int port;
    private boolean proxyPower;
    private JMenu file, help, proxy;
    private HashMap<String, String> bodyData;
    private String customBrowserPath;
    private JComboBox<String> retryBox;

    public Gui() {
        loadEholeSettings();
        loadCustomBrowserPath();
        setupUI();
        setupEvents();
    }

    private void setupUI() {
        setTitle("WEB批量请求器");
        setBounds(400, 300, 1250, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setupMenu();
        setupPanels();

        setVisible(true);
    }

    private void setupMenu() {
        fileMenu = new JMenuBar();
        file = new JMenu("设置/文件");
        importTargetsMenuItem = new JMenuItem("导入目标");
        exportMenuItem = new JMenuItem("导出结果");
        file.add(importTargetsMenuItem);
        file.add(exportMenuItem);
        fileMenu.add(file);
        JMenuItem setEholeMenuItem = new JMenuItem("设置 EHole 路径");
        setEholeMenuItem.addActionListener(e -> setEholePath());
        file.add(setEholeMenuItem);
        fileMenu.add(file);

        helpMenu = new JMenuBar();
        help = new JMenu("帮助");
        aboutMenuItem = new JMenuItem("关于");
        help.add(aboutMenuItem);
        helpMenu.add(help);

        proxyMenu = new JMenuBar();
        proxy = new JMenu("代理设置");
        httpMenuItem = new JMenuItem("添加HTTP代理");
        proxy.add(httpMenuItem);
        proxyMenu.add(proxy);


        topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(fileMenu);
        topPanel.add(proxyMenu);
        topPanel.add(helpMenu);
    }

    private void setupPanels() {
        // 初始化 westPanel 和 eastPanel
        westPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        eastPanel = new JPanel(new BorderLayout(0, 0));

        // 初始化并设置 westPanel 和 eastPanel 的组件
        setupWestPanelComponents();
        setupEastPanelComponents();

        // 将面板添加到内容窗格中
        getContentPane().add(topPanel, BorderLayout.NORTH);
        getContentPane().add(westPanel, BorderLayout.WEST);
        getContentPane().add(eastPanel, BorderLayout.EAST);

        // 设置面板的首选大小
        westPanel.setPreferredSize(new Dimension(490, 530));
        eastPanel.setPreferredSize(new Dimension(740, 530));
    }


    private void setupWestPanelComponents() {
        targetsLabel = new JLabel("targets:");
        pathLabel = new JLabel("自定义路径:/api");
        pathLabel.setPreferredSize(new Dimension(400, pathLabel.getPreferredSize().height));
        headLabel = new JLabel("自定义Header:");
        headLabel.setPreferredSize(new Dimension(400, headLabel.getPreferredSize().height));
        cookiesLabel = new JLabel("自定义Cookies:");
        cookiesLabel.setPreferredSize(new Dimension(400, cookiesLabel.getPreferredSize().height));
        uaLabel = new JLabel("自定义User-Agent:");
        uaLabel.setPreferredSize(new Dimension(400, uaLabel.getPreferredSize().height));
        dataLabel = new JLabel("post_data:");
        dataLabel.setPreferredSize(new Dimension(400, dataLabel.getPreferredSize().height));
        runButton = createButton("Run");
        stopButton = createButton("Stop");
        clearButton = createButton("Clear");
        countLabel = new JLabel("0/5");
        threadBox = new JComboBox<>(new String[]{"--请选择线程--", "1", "5", "10", "25", "50", "75", "100", "500"});
        targetsTextArea = createTextArea(8, 51, "https://www.baidu.com\nhttps://www.qq.com\nhttps://www.sina.com.cn/\nhttp://weather.sina.com.cn\nhttps://www.nday.top\n");
        targetsScroll = new JScrollPane(targetsTextArea);
        targetsScroll = new JScrollPane(targetsTextArea);
        targetsScroll.setPreferredSize(new Dimension(510, 100));
        targetsScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        targetsPathText = createTextField(51, "");
        headTextArea = createTextArea(2, 51, "X-Forwarded-for: 127.0.0.1\nReferer: 127.0.0.1");
        headScrollPane = new JScrollPane(headTextArea);
        headScrollPane.setPreferredSize(new Dimension(510, 50));
        headScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        cookiesTextArea = createTextArea(2, 51, "SESSION=5mzjswjg5couyv5pvqf6xmu9g1b0oboresgrvyh");
        cookiesScrollPane = new JScrollPane(cookiesTextArea);
        cookiesScrollPane.setPreferredSize(new Dimension(510, 50));
        cookiesScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        uaText = createTextField(51, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36(KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36");
        requestsButtonGroup = new ButtonGroup();
        getRadio = new JRadioButton("get", true);
        postRadio = new JRadioButton("post");
        headRadio = new JRadioButton("head");
        requestsButtonGroup.add(getRadio);
        requestsButtonGroup.add(postRadio);
        requestsButtonGroup.add(headRadio);
        followBox = new JCheckBox("取消302跳转");
        fingerprint_switch =new JCheckBox("开启指纹识别");
        dataTextArea = createTextArea(4, 51, "");
        dataTextArea.setEditable(false);
        dataScrollPane = new JScrollPane(dataTextArea);
        dataScrollPane.setPreferredSize(new Dimension(510, 100));
        dataScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        enctypeBox = new JComboBox<>(new String[]{"application/x-www-form-urlencoded", "application/json", "multipart/form-data"});
        enctypeBox.setEnabled(false);
        retryBox = new JComboBox<>(new String[]{"--请选择重试次数--", "1次", "2次", "3次", "4次", "5次"});
        timeLabel = new JLabel("默认5S");
        retryBox.setSelectedIndex(0);
        timeBox = new JComboBox<>(new String[]{"--请选择超时时间(ms)--", "500", "1000", "2000", "3000", "4000"});
        timeLabel = new JLabel("默认5S");

        addComponentsToWestPanel();
    }


    private void setupEastPanelComponents() {
        eastPanel.setLayout(new BorderLayout());
        columnNames = new Vector<>(Arrays.asList("Target", "Status", "Title", "Location/Website", "length", "Fingerprint"));
        dataModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 1 || columnIndex == 4) {
                    return Integer.class; // status 和 contentLength 列返回 Integer 类型
                }
                return String.class;
            }
        };
        table = new JTable(dataModel);
        table.setAutoCreateRowSorter(true);

        // 设置自定义排序器
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(dataModel);
        table.setRowSorter(sorter);
        // 设置 status 列的排序器
        sorter.setComparator(1, Comparator.comparing(o -> {
            if (o instanceof Integer) {
                return (Integer) o;
            } else {
                System.err.println("Unexpected data type in status column: " + o.getClass());
                return Integer.MIN_VALUE;
            }
        }));
        // 设置 contentLength 列的排序器
        sorter.setComparator(4, Comparator.comparing(o -> {
            if (o instanceof Integer) {
                return (Integer) o;
            } else {
                System.err.println("Unexpected data type in length column: " + o.getClass());
                return Integer.MIN_VALUE;
            }
        }));

        // 调整每一列的宽度
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(180);
        columnModel.getColumn(1).setPreferredWidth(30);
        columnModel.getColumn(2).setPreferredWidth(100);
        columnModel.getColumn(3).setPreferredWidth(100);
        columnModel.getColumn(4).setPreferredWidth(30);
        columnModel.getColumn(5).setPreferredWidth(160);

        tableScroll = new JScrollPane(table);
        tableScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        eastPanel.add(table.getTableHeader(), BorderLayout.NORTH);
        eastPanel.add(tableScroll, BorderLayout.CENTER);
    }


    private void addComponentsToWestPanel() {
        westPanel.add(targetsLabel);
        westPanel.add(targetsScroll);
        westPanel.add(pathLabel);
        westPanel.add(targetsPathText);
        westPanel.add(headLabel);
        westPanel.add(headScrollPane);
        westPanel.add(cookiesLabel);
        westPanel.add(cookiesScrollPane);
        westPanel.add(uaLabel);
        westPanel.add(uaText);
        westPanel.add(dataLabel);
        westPanel.add(dataScrollPane);
        westPanel.add(enctypeBox);
        westPanel.add(getRadio);
        westPanel.add(postRadio);
        westPanel.add(headRadio);
        westPanel.add(followBox);
        westPanel.add(fingerprint_switch);
        westPanel.add(threadBox);
        westPanel.add(retryBox);
        westPanel.add(timeBox);
        westPanel.add(timeLabel);
        westPanel.add(runButton);
        westPanel.add(stopButton);
        westPanel.add(clearButton);
        westPanel.add(countLabel);
    }


    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setEnabled(true);
        button.setBorderPainted(true);
        return button;
    }

    private JTextArea createTextArea(int rows, int cols, String defaultText) {
        JTextArea textArea = new JTextArea(rows, cols);
        textArea.addFocusListener(new DefaultTextarea(defaultText, textArea));
        textArea.setLineWrap(true);
        return textArea;
    }

    private JTextField createTextField(int cols, String defaultText) {
        JTextField textField = new JTextField(defaultText, cols);
        textField.addFocusListener(new DefaultText(defaultText, textField));
        return textField;
    }

    private void setupEvents() {
        setupRadioButtons();
        runButton.addActionListener(e -> onRunButtonClick());
        stopButton.addActionListener(e -> onStopButtonClick());
        clearButton.addActionListener(e -> onClearButtonClick());
        importTargetsMenuItem.addActionListener(e -> onImportTargets());
        exportMenuItem.addActionListener(e -> onExportResults());
        aboutMenuItem.addActionListener(new about());
        httpMenuItem.addActionListener(new proxy());
        setupTableEvents();
    }

    private void setupRadioButtons() {
        ActionListener requestsListener = e -> {
            switch (e.getActionCommand()) {
                case "post":
                    dataTextArea.setEditable(true);
                    enctypeBox.setEnabled(true);
                    break;
                default:
                    dataTextArea.setEditable(false);
                    enctypeBox.setEnabled(false);
                    dataTextArea.setText("");
                    break;
            }
        };
        getRadio.addActionListener(requestsListener);
        postRadio.addActionListener(requestsListener);
        headRadio.addActionListener(requestsListener);
    }

    private void onRunButtonClick() {
        runButton.setEnabled(false);
        bodyData = new HashMap<>();
        List<String> targets = Arrays.stream(targetsTextArea.getText().split("\n")).collect(Collectors.toList());
        String cookiesBody = cookiesTextArea.getText();
        String uaBody = uaText.getText();
        String headBody = headTextArea.getText();
        String targetsPathBody = targetsPathText.getText();

        List<String> processedTargets = processTargets(targets, targetsPathBody);
        total = processedTargets.size();

        if (postRadio.isSelected()) {
            String dataBody = dataTextArea.getText();
            String enctypeBody = (String) enctypeBox.getSelectedItem();
            httpData(processedTargets, cookiesBody, uaBody, headBody, "post", dataBody, enctypeBody);
        } else if (headRadio.isSelected()) {
            httpData(processedTargets, cookiesBody, uaBody, headBody, "head", null, null);
        } else if (getRadio.isSelected()) {
            httpData(processedTargets, cookiesBody, uaBody, headBody, "get", null, null);
        }

        doneCount = 0;
        dataModel.setRowCount(0);
    }

    private List<String> processTargets(List<String> targets, String targetsPathBody) {
        return targets.stream().flatMap(target -> {
            if (targetsPathBody.equals("默认为空")) {
                if (target.startsWith("http://") || target.startsWith("https://")) {
                    return Arrays.stream(new String[]{target});
                } else {
                    return Arrays.stream(new String[]{"https://" + target, "http://" + target});
                }
            } else {
                if (target.startsWith("http://") || target.startsWith("https://")) {
                    return Arrays.stream(new String[]{target + targetsPathBody});
                } else {
                    return Arrays.stream(new String[]{"https://" + target + targetsPathBody, "http://" + target + targetsPathBody});
                }
            }
        }).collect(Collectors.toList());
    }

    private void onStopButtonClick() {
        pool.shutdownNow();
        runButton.setEnabled(true);
    }

    private void onClearButtonClick() {
        dataModel.setRowCount(0);
        countLabel.setText(0 + "/" + total);
    }

    private void onImportTargets() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new FileNameExtensionFilter("txt", "txt"));
        fileChooser.showOpenDialog(null);
        File selectFile = fileChooser.getSelectedFile();
        if (selectFile != null) {
            List<String> strings = TxtFile.readTxtFile(selectFile.getPath().trim());
            if (strings != null && !strings.isEmpty()) {
                total = strings.size();
                countLabel.setText("0/" + total);
                targetsTextArea.setText(String.join("\n", strings));
            } else {
                JOptionPane.showMessageDialog(null, "选择文件有误,请选择txt文件导入", "error", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void onExportResults() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("保存文件");
        fileChooser.setSelectedFile(new File("results.csv"));
        int saveDialog = fileChooser.showDialog(null, "保存文件");

        if (saveDialog == JFileChooser.APPROVE_OPTION) {
            String filename = fileChooser.getSelectedFile().toString();
            if (!filename.endsWith(".csv")) {
                filename += ".csv";
            }
            File file = new File(filename);
            if (file.exists()) {
                int i = JOptionPane.showConfirmDialog(null, "该文件已经存在，确定要覆盖吗？");
                if (i != JOptionPane.YES_OPTION) {
                    return;
                }
            }
            try {
                exportTable(table, file);
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, ex.getMessage(), "error", JOptionPane.WARNING_MESSAGE);
            }
        }
    }
    // 双击打开浏览器功能实现
    private void setupTableEvents() {
        table.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3 || e.getClickCount() == 2) {
                    int focusedRowIndex = table.rowAtPoint(e.getPoint());
                    if (focusedRowIndex == -1) {
                        return;
                    }

                    // 设置选中行并获取目标 URL
                    table.setRowSelectionInterval(focusedRowIndex, focusedRowIndex);
                    String url = getTargetUrl(focusedRowIndex);
                    if (url != null) {
                        targetUrl = url; // 更新 targetUrl

                        if (e.getButton() == MouseEvent.BUTTON3) {
                            showRightClickMenu(e);
                        } else if (e.getClickCount() == 2) {
                            // 使用自定义浏览器打开
                            if (customBrowserPath != null && !customBrowserPath.isEmpty()) {
                                openUrlInCustomBrowser(customBrowserPath, targetUrl);
                            } else {
                                // 直接使用默认浏览器打开
                                openUrlInBrowser(targetUrl);
                            }
                        }
                    }
                }
            }
        });
    }

    private String getTargetUrl(int rowIndex) {
        Object value = table.getValueAt(rowIndex, 0); // 假设 URL 在第一列
        return (value instanceof String) ? (String) value : null;
    }

    //右键输出结果菜单栏
    private void showRightClickMenu(MouseEvent e) {
        rightMenu = new JPopupMenu();

        JMenuItem copyMenuItem = new JMenuItem("复制目标地址");
        copyMenuItem.addActionListener(evt -> copyToClipboard(targetUrl));
        rightMenu.add(copyMenuItem);

        JMenuItem customBrowserMenuItem = new JMenuItem("使用自定义浏览器打开");
        customBrowserMenuItem.addActionListener(evt -> {
            if (customBrowserPath != null && !customBrowserPath.isEmpty()) {
                openUrlInCustomBrowser(customBrowserPath, targetUrl);
            } else {
                customBrowserPath = getCustomBrowserPath(); // 获取自定义浏览器路径
                if (customBrowserPath != null && !customBrowserPath.isEmpty()) {
                    saveCustomBrowserPath(customBrowserPath); // 保存路径
                    openUrlInCustomBrowser(customBrowserPath, targetUrl);
                } else {
                    // 如果没有自定义路径，使用默认浏览器打开
                    openUrlInBrowser(targetUrl);
                }
            }
        });
        rightMenu.add(customBrowserMenuItem);

        JMenuItem viewMenuItem = new JMenuItem("预览Body");
        viewMenuItem.addActionListener(evt -> viewBody(targetUrl));
        rightMenu.add(viewMenuItem);

        JMenuItem switchBrowserMenuItem = new JMenuItem("切换浏览器");
        switchBrowserMenuItem.addActionListener(evt -> {
            String newBrowserPath = getCustomBrowserPath(); // 获取新的浏览器路径
            if (newBrowserPath != null && !newBrowserPath.trim().isEmpty()) {
                customBrowserPath = newBrowserPath;
                saveCustomBrowserPath(customBrowserPath); // 保存新的路径
            }
        });
        rightMenu.add(switchBrowserMenuItem);

        rightMenu.show(table, e.getX(), e.getY());
    }
    //修改浏览器功能实现
    private String getCustomBrowserPath() {
        return JOptionPane.showInputDialog("请输入浏览器路径：\n" +
                "切换为默认浏览器需要输入Default_browser然后重启工具\n" +
                "输入浏览器绝对路径就不需要重启");
    }
    //根据操作系统选择调用浏览器的方式
    private void openUrlInCustomBrowser(String browserPath, String targetUrl) {
        try {
            String osName = System.getProperty("os.name").toLowerCase();
            ProcessBuilder processBuilder;

            if (osName.contains("mac")) {
                // macOS
                processBuilder = new ProcessBuilder("open", "-a", browserPath, targetUrl);
            } else if (osName.contains("win")) {
                // Windows
                processBuilder = new ProcessBuilder(browserPath, targetUrl);
            } else {
                JOptionPane.showMessageDialog(null, "不支持的操作系统: " + osName);
                return;
            }

            processBuilder.start();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "无法打开浏览器: " + ex.getMessage());
        }
    }
    //保存浏览器设置实现
    private void saveCustomBrowserPath(String path) {
        Properties properties = new Properties();
        properties.setProperty("browserPath", path);
        try (OutputStream output = new FileOutputStream("config.properties")) {
            properties.store(output, null);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "无法保存浏览器路径: " + ex.getMessage());
        }
    }
    //加载浏览器设置
    private void loadCustomBrowserPath() {
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream("config.properties")) {
            properties.load(input);
            String path = properties.getProperty("browserPath");
            if ("Default_browser".equals(path)) {
                customBrowserPath = null;
            } else {
                customBrowserPath = path;
            }
        } catch (IOException ex) {
            customBrowserPath = null;
        }
    }


    private void copyToClipboard(String text) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection selection = new StringSelection(text);
        clipboard.setContents(selection, selection);
    }

    private void openUrlInBrowser(String url) {
        try {
            java.net.URI uri = java.net.URI.create(url);
            java.awt.Desktop dp = java.awt.Desktop.getDesktop();
            if (dp.isSupported(java.awt.Desktop.Action.BROWSE)) {
                dp.browse(uri);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void viewBody(String url) {
        JDialog viewBodyFrame = new JDialog();
        viewBodyFrame.setBounds(795, 350, 500, 500);
        viewBodyFrame.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        viewBodyFrame.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        viewBodyFrame.setTitle(url);

        JTextArea bodyTextArea = new JTextArea(bodyData.get(url));
        JScrollPane bodyScroll = new JScrollPane(bodyTextArea);
        bodyScroll.setPreferredSize(new Dimension(450, 430));
        bodyScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        viewBodyFrame.add(bodyScroll);
        viewBodyFrame.setVisible(true);
    }

    private void httpData(List<String> targets, String cookiesBody, String uaBody, String headBody, String method, String dataBody, String enctypeBody) {
        int threadCount = threadBox.getSelectedItem().equals("--请选择线程--") ? 5 : Integer.parseInt((String) threadBox.getSelectedItem());
        int timeout = timeBox.getSelectedItem().equals("--请选择超时时间(ms)--") ? 5000 : Integer.parseInt((String) timeBox.getSelectedItem());
        pool = Executors.newFixedThreadPool(threadCount);
        boolean follow = followBox.isSelected();

        // 获取复选框的状态
        boolean eholeSwitch = fingerprint_switch.isSelected(); // 从复选框获取值
        int retryCount = retryBox.getSelectedIndex();

        // 共享的警告标志
        final Object lock = new Object(); // 用于同步
        final boolean[] warningShown = {false}; // 使用数组来实现可变性

        for (String target : targets) {
            SwingWorker<Vector<Object>, Void> worker = new SwingWorker<Vector<Object>, Void>() {
                @Override
                protected Vector<Object> doInBackground() {
                    Vector<Object> list = new Vector<>();
                    int attempts = 0;
                    boolean success = false;

                    while (attempts <= retryCount && !success) {
                        try {
                            List<String> responseData = proxyPower
                                    ? Http.Response(target, cookiesBody, uaBody, headBody, method, dataBody, enctypeBody, follow, host, port,timeout)
                                    : Http.Response(target, cookiesBody, uaBody, headBody, method, dataBody, enctypeBody, follow,timeout);
                            // 输出数据
                            list.add(target); // target
                            list.add(responseData.get(0)); // Status
                            list.add(Re.Title(responseData.get(1))); // title
                            list.add(responseData.get(2)); // location
                            list.add(responseData.get(3)); // content-length

                            // 根据 eholeSwitch 的值决定是否执行指纹扫描
                            if (eholeSwitch && threadCount > 10) {
                                synchronized (lock) { // 确保只有一个线程可以进入此代码块
                                    if (!warningShown[0]) { // 只有在警告未显示的情况下才弹窗
                                        JOptionPane.showMessageDialog(null, "是不是有病，线程大于10不运行指纹检测，小心电脑带不起", "警告", JOptionPane.WARNING_MESSAGE);
                                        warningShown[0] = true; // 设置为已显示
                                    }
                                }
                                list.add(" ");
                            } else if (eholeSwitch) {
                                String fingerprint = executeEhole(target); // 调用 EHole
                                list.add(fingerprint);
                            } else {
                                list.add(" "); // 或者其他默认值
                            }


                            bodyData.put(target, responseData.get(1));
                            success = true; // 设置成功标志，退出循环
                        } catch (Exception e) {
                            attempts++;
                            if (attempts <= retryCount) {
                                System.out.println("连接被拒绝，正在尝试重连...（尝试次数: " + attempts + "）");
                                try {
                                    Thread.sleep(1000); // 重试前暂停1秒
                                } catch (InterruptedException ie) {
                                    Thread.currentThread().interrupt();
                                }
                            } else {
                                e.printStackTrace();
                                list.add(target);
                                list.add("<html><span style=\"color: red;\">无法访问</span></html>");
                                list.add(e.getMessage());
                                bodyData.put(target, "");
                            }
                        }
                    }
                    return list;
                }

                @Override
                protected void done() {
                    try {
                        dataModel.addRow(get());
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                    countLabel.setText(++doneCount + "/" + total);
                    if (doneCount == total) {
                        runButton.setEnabled(true);
                    }
                }
            };
            pool.execute(worker);
        }
    }


    private String executeEhole(String target) {
        ehole eholeInstance = new ehole(eholePath); // 创建 ehole 类的实例
        return eholeInstance.executeCommand(target); // 调用 executeCommand 方法
    }


    private void setEholePath() {
        JTextField pathField = new JTextField(eholePath != null ? eholePath : "");

        Object[] message = {
                "EHole 路径:", pathField
        };

        int option = JOptionPane.showConfirmDialog(null, message, "设置 EHole", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            eholePath = pathField.getText().trim();
            saveEholePath(eholePath);
        }
    }

    private void saveEholePath(String path) {
        Properties properties = new Properties();
        properties.setProperty("eholePath", path);
        try (OutputStream output = new FileOutputStream("config.properties", true)) {
            properties.store(output, null);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "无法保存 EHole 设置: " + ex.getMessage());
        }
    }

    private void loadEholeSettings() {
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream("config.properties")) {
            properties.load(input);
            eholePath = properties.getProperty("eholePath");
        } catch (IOException ex) {
            eholePath = null;
        }
    }


    public void exportTable(JTable table, File file) throws IOException {
        try (BufferedWriter bWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "GBK"))) {
            TableModel model = table.getModel();

            // 写入列名
            for (int i = 0; i < model.getColumnCount(); i++) {
                bWriter.write(model.getColumnName(i));
                if (i < model.getColumnCount() - 1) {
                    bWriter.write(",");
                }
            }
            bWriter.newLine();

            // 写入行数据
            for (int i = 0; i < model.getRowCount(); i++) {
                for (int j = 0; j < model.getColumnCount(); j++) {
                    String value = Optional.ofNullable(model.getValueAt(i, j)).map(Object::toString).orElse("");

                    // 处理包含逗号的值
                    if (value.contains(",")) {
                        value = "\"" + value + "\"";
                    }

                    // 处理换行符
                    value = value.replace("\n", " ").replace("\r", " ");

                    bWriter.write(value);
                    if (j < model.getColumnCount() - 1) {
                        bWriter.write(",");
                    }
                }
                bWriter.newLine();
            }
        }
    }
    class about implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(null, "哈哈哈哈红红火火恍恍惚惚", "关于", JOptionPane.WARNING_MESSAGE);//弹框提示
        }
    }

    class proxy implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            final JDialog proxyFrame = new JDialog();
            proxyFrame.setBounds(795, 350, 335, 118);
            proxyFrame.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
            proxyFrame.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);

            proxyFrame.setTitle("代理设置");

            final JLabel hostJLabel = new JLabel();
            hostJLabel.setText("地址:");
            final JTextField hostJText = new JTextField(11);

            JLabel portJLabel = new JLabel();
            portJLabel.setText("端口:");
            final JTextField portJText = new JTextField(5);

            final JButton defineJButton = new JButton("确定");
            JButton cancelJButton = new JButton("取消");

            proxyFrame.add(hostJLabel);
            proxyFrame.add(hostJText);

            proxyFrame.add(portJLabel);
            proxyFrame.add(portJText);

            proxyFrame.add(defineJButton);
            proxyFrame.add(cancelJButton);
            // 确认按钮事件
            defineJButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    host = hostJText.getText();
                    portString = portJText.getText();

                    if (!"".equals(host) && "".equals(portString)) {
                        JOptionPane.showMessageDialog(defineJButton, "端口不能为空", "error", JOptionPane.WARNING_MESSAGE);
                    }
                    if (!"".equals(portString) && "".equals(host)) {
                        JOptionPane.showMessageDialog(defineJButton, "代理服务器不能为空", "error", JOptionPane.WARNING_MESSAGE);
                    }
                    if ("".equals(host) && "".equals(portString)) {
                        proxyPower = false;
                        proxyFrame.dispose();
                    }
                    if (!"".equals(host) && !"".equals(portString)) {
                        proxyPower = true;
                        port = Integer.valueOf(portString);
                        proxyFrame.dispose();
                    }

                }
            });
            hostJText.setText(host);
            portJText.setText(portString);

            // 取消按钮事件
            cancelJButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    proxyFrame.dispose();
                }
            });
            proxyFrame.setVisible(true);
        }
    }

}