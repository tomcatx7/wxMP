package com.github.binarywang.demo.wx.mp.utils;

import com.github.binarywang.demo.wx.mp.config.WxMpProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

@AllArgsConstructor
@Data
@Component
public class IHttpclient {

    private final WxMpProperties properties;

    private static final int TIMEOUT = 10000;

    public byte[] getBytes(String url, int timeout) throws IOException {
        byte[] bytes = null;
        Connection connect = Jsoup.connect(url).ignoreContentType(true).header("Accept", "*/*")
            .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0")
            .timeout(timeout);
        setProxy(connect);
        bytes = connect.execute().bodyAsBytes();
        return bytes;
    }

    public byte[] getBytes(String url) throws IOException {
        return getBytes(url, TIMEOUT);
    }

    public Connection.Response post(String url, String data) {
        Connection.Response res = null;
        try {
            Connection connect = Jsoup.connect(url).ignoreContentType(true).method(Connection.Method.POST).requestBody(data);
            setProxy(connect);
            res = connect.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    public void setProxy(Connection connection) {
        //取第一个配置文件代理配置
        String proxyHost = this.properties.getConfigs().get(0).getHttpProxyHost();
        String proxyPort = this.properties.getConfigs().get(0).getHttpProxyPort();
        if (!StringUtils.isEmpty(proxyHost)
            && !StringUtils.isEmpty(proxyPort))
            connection.proxy(proxyHost, Integer.valueOf(proxyPort));
    }

}
