package com.github.binarywang.demo.wx.mp.utils;

import com.github.binarywang.demo.wx.mp.config.ProxyProperties;
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

    private final ProxyProperties proxyProperties;

    private static final int TIMEOUT = 10000;

    public byte[] getBytes(String url, int timeout) throws IOException {
        byte[] bytes = null;
        Connection connect = Jsoup.connect(url).header("Accept", "*/*")
            .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0")
            .timeout(timeout);
        if (!StringUtils.isEmpty(this.proxyProperties.getPort())
            && !StringUtils.isEmpty(this.proxyProperties.getUrl()))
            connect.proxy(this.proxyProperties.getUrl(), Integer.valueOf(this.proxyProperties.getPort()));
        bytes = connect.execute().bodyAsBytes();
        return bytes;
    }

    public byte[] getBytes(String url) throws IOException {
        return getBytes(url, TIMEOUT);
    }

}
