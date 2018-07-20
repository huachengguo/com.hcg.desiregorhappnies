package com.hcg.commonutil.HttpUtils;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;

import java.io.InputStream;
import java.net.URI;

public class HttpUtil {

    //不等陆网站
    public static String requestForResponse(String url) throws Exception
    {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost();
        post.setHeader("user-agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.110 Safari/537.36");
        post.setHeader("Host",urlTargetHost(url));
        URI uri = new URI(url);
        post.setURI(uri);
        CloseableHttpResponse response = httpClient.execute(post);
        HttpEntity entity = response.getEntity();
        return getRespString(entity);
    }

    public static String urlTargetHost(String url)
    {
        int substart = url.indexOf("//");
        String str = url.substring(substart+2);
        int subend= str.indexOf("/");
        return str.substring(0,subend);
    }

    @Test
    public static void main(String[] args){
        try {
            String s = requestForResponse("https://jingyan.baidu.com/article/a948d6516326930a2dcd2efe.html");
            System.out.println(s);
        }catch (Exception e)
        {
            System.out.println(e);
        }
    }

    private static String getRespString(HttpEntity entity) throws Exception {
        if (entity == null) {
            return null;
        }
        InputStream is = entity.getContent();
        StringBuffer strBuf = new StringBuffer();
        byte[] buffer = new byte[4096];
        int r = 0;
        while ((r = is.read(buffer)) > 0) {
            strBuf.append(new String(buffer, 0, r, "UTF-8"));
        }
        return strBuf.toString();
    }

}
