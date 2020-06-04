package com.shebangs.supplier.server;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class HttpHandler {

    private final static String TAG = "HttpHandler";

    public final static String ContentType_APP = "application/x-www-form-urlencoded";
    public final static String ContentType_MUL = "multipart/form-data";

    public final static String RequestMode_POST = "POST";
    public final static String RequestMode_GET = "GET";


    public static String handlerHttpRequest(String url, Map<String, String> params, String requestMode, String contentType) {
        String response;

        //根据contentType组件请求参数
        StringBuilder request = new StringBuilder();
        switch (contentType) {
            case ContentType_MUL:       //采用json
                for (Map.Entry<String, String> json : params.entrySet()) {
                    request.append(json.getKey());
                    request.append(":");
                    request.append(json.getValue());
                    request.append(",");
                }
                if (request.length() > 0) {
                    request.deleteCharAt(request.lastIndexOf(","));
                }
                break;
            case ContentType_APP:       //采用拼接
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    request.append(entry.getKey());
                    request.append("=");
                    request.append(entry.getValue());
                    request.append("&");
                }
                if (request.length() > 0) {
                    request.deleteCharAt(request.lastIndexOf("&"));
                }
                break;
            default:
                return null;
        }

        //根据requestMode来决定发送模式
        switch (requestMode) {
            case RequestMode_POST:
                response = connectToServerInPost(url, request.toString(), contentType);
                break;
            case RequestMode_GET:
                response = connectToServerInGet(url, request.toString(), contentType);
                break;
            default:
                return null;
        }
        return response;
    }

    private static String connectToServerInGet(String url, String request, String contentType) {
        URL serverUrl;
        HttpURLConnection urlConnection = null;
        String response = null;

        try {
            //组建url
            serverUrl = new URL(url + (request.length() > 0 ? ("?" + request) : ""));
            urlConnection = (HttpURLConnection) serverUrl.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(5000);

            //读取响应
            int responseCode = urlConnection.getResponseCode();
            if (responseCode == 200) {
                BufferedReader rd = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String line;
                StringBuilder responseStrings = new StringBuilder();
                while ((line = rd.readLine()) != null) {
                    responseStrings.append(line);
                }
                rd.close();
                response = responseStrings.toString();
            } else {
                Log.e(TAG, "connectToServer: 服务器响应异常：" + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }
        return response;
    }


    private static String connectToServerInPost(String url, String request, String contentType) {
        URL serverUrl;
        HttpURLConnection urlConnection = null;
        String response = null;

        try {
            //组建url
            serverUrl = new URL(url);
            urlConnection = (HttpURLConnection) serverUrl.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", contentType + "; charset=UTF-8");//"multipart/form-data");
//            urlConnection.setRequestProperty("token", User.getInstance().getToken());
            urlConnection.setUseCaches(false);
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setConnectTimeout(5000);

            //发送请求
            OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8");//JsonKey.CharSet);
            wr.write(request);
            wr.flush();
            wr.flush();
            wr.flush();
            wr.close();

            //读取响应
            int responseCode = urlConnection.getResponseCode();
            if (responseCode == 200) {
                BufferedReader rd = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String line;
                StringBuilder responseStrings = new StringBuilder();
                while ((line = rd.readLine()) != null) {
                    responseStrings.append(line);
                }
                rd.close();
                response = responseStrings.toString();
            } else {
                Log.e(TAG, "connectToServer: 服务器响应异常：" + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }

        return response;
    }
}
