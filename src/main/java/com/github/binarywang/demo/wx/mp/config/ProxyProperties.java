package com.github.binarywang.demo.wx.mp.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Data
@Deprecated
public class ProxyProperties {
    /**
     * http代理url
     */
    private String url;
    /**
     * 代理端口
     */
    private String port;

}
