package com.exp;

import com.github.kevinsawicki.http.HttpRequest;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Http {

    public static ArrayList Response (String str,String cookie, String ua,String xHeaders,String methods,String dataBody,String enctypeBody,boolean follow,int timeout){
        ArrayList responseList = new ArrayList();
        int code,contentLength;
        String body,banner,redirectUrl = null;
        HttpRequest response = null;
        Map<String, String> headers = new HashMap<String, String>();
            if (ua.equals("")){
                ua = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 " +
                        "(KHTML, like Gecko) Chrome/128.0.0.0 Safari/537.36";
            }
            if (!"SESSION=5mzjswjg5couyv5pvqf6xmu9g1b0oboresgrvyh".equals(cookie)){
                headers.put("cookie",cookie);
            }



            headers.put("User-Agent",ua);
                if (!xHeaders.equals("X-Forwarded-for: 127.0.0.1\nReferer: 127.0.0.1") && !xHeaders.equals("")) {
                    System.out.println(xHeaders);
                    try {
                        String[] split1 = xHeaders.split("\n");
                        for (String s : split1) {
                            String[] split2 = s.split(": ");
                            headers.put(split2[0], split2[1]);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

        int timeoutMillis = timeout; // 设置超时时间为5秒
        int MAX_REDIRECTS = 5; //重定向次数
            if (methods == "post")
            {   headers.put("Content-type",enctypeBody);
                response = HttpRequest.post(str).headers(headers).trustAllHosts().trustAllCerts().send(dataBody).followRedirects(follow).connectTimeout(timeoutMillis).readTimeout(timeoutMillis);
            }
            if (methods == "head"){
                response = HttpRequest.head(str).headers(headers).trustAllCerts().trustAllHosts().followRedirects(follow).connectTimeout(timeoutMillis).readTimeout(timeoutMillis);
            }
            if(methods == "get"){
                response = HttpRequest.get(str).headers(headers).trustAllHosts().trustAllCerts().followRedirects(follow).connectTimeout(timeoutMillis).readTimeout(timeoutMillis);
            }
        code = response.code();
        body = response.body();
        banner = response.header("server");
        contentLength = response.contentLength();
        redirectUrl = response.header("Location");
        int redirectCount = 0;
        // 检查是否为302重定向
        if (code == 302 ) {
            while (redirectUrl != null && !follow && redirectCount < MAX_REDIRECTS) {
                response = HttpRequest.get(redirectUrl).headers(headers).trustAllHosts().trustAllCerts().connectTimeout(timeoutMillis).readTimeout(timeoutMillis);
                code = response.code();
                body = response.body();
                banner = "[Location]"+ redirectUrl;
                contentLength = response.contentLength();

                redirectCount++;

                if (code == 302) {
                    redirectUrl = response.header("Location");
                } else {
                    break;
                }
            }
        }

        if (redirectCount >= MAX_REDIRECTS) {
            System.out.println("Reached maximum redirect limit of " + MAX_REDIRECTS);
        }


        if (contentLength == -1) {
            try {
                contentLength = body.getBytes("UTF-8").length;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        responseList.add(code);
        responseList.add(body);
        responseList.add(banner);
        responseList.add(contentLength);
        return responseList;
    }

    // 重载方法
    public static ArrayList Response (String str,String cookie, String ua,String xHeaders,String methods,String dataBody,String enctypeBody,boolean follow,String host,int port,int timeout){
        ArrayList responseList = new ArrayList();
        int code,contentLength;
        String body,banner,redirectUrl;
        Map<String, String> headers = new HashMap<String, String>();
        HttpRequest response = null;
        if (ua.equals("")){
            ua = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 " +
                    "(KHTML, like Gecko) Chrome/128.0.0.0 Safari/537.36";
        }
        if (!"SESSION=5mzjswjg5couyv5pvqf6xmu9g1b0oboresgrvyh".equals(cookie)){
            headers.put("cookie",cookie);
        }

        headers.put("User-Agent",ua);
        if (!xHeaders.equals("X-Forwarded-for: 127.0.0.1\nReferer: 127.0.0.1") && !xHeaders.equals("")){
            System.out.println(xHeaders);
            try {
                String[] split1 = xHeaders.split("\n");
                for (String s : split1) {
                    String[] split2 = s.split(": ");
                    headers.put(split2[0], split2[1]);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        int timeoutMillis = timeout;
        if (methods == "post")
        {   headers.put("Content-type",enctypeBody);
            response = HttpRequest.post(str).useProxy(host, port).headers(headers).trustAllHosts().trustAllCerts().send(dataBody).followRedirects(follow).connectTimeout(timeoutMillis).readTimeout(timeoutMillis);
        }
        if (methods == "head"){
            response = HttpRequest.head(str).useProxy(host, port).headers(headers).trustAllCerts().trustAllHosts().followRedirects(follow).connectTimeout(timeoutMillis).readTimeout(timeoutMillis);
        }
        if(methods == "get"){
            response = HttpRequest.get(str).useProxy(host, port).headers(headers).trustAllHosts().trustAllCerts().followRedirects(follow).connectTimeout(timeoutMillis).readTimeout(timeoutMillis);
        }
        code = response.code();
        body = response.body();
        banner = response.header("server");
        contentLength = response.contentLength();
        redirectUrl = response.header("Location");
        // 检查是否为302重定向
        if (code == 302) {
            if (redirectUrl != null && !follow) {
                response = HttpRequest.get(redirectUrl).headers(headers).trustAllHosts().trustAllCerts().connectTimeout(timeoutMillis).readTimeout(timeoutMillis);
                code = response.code();
                body = response.body();
                banner = "[Location]"+redirectUrl;
                contentLength = response.contentLength();
            }
        }

        if (contentLength == -1) {
            try {
                contentLength = body.getBytes("UTF-8").length;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        responseList.add(code);
        responseList.add(body);
        responseList.add(banner);
        responseList.add(contentLength);
        return responseList;
    }
}
