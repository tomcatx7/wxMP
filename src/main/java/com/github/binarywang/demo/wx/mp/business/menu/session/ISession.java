package com.github.binarywang.demo.wx.mp.business.menu.session;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ISession {

    private long expireTime = 30 * (1000 * 60); //30min
    /**
     * 会话唯一标识 采用用户openID
     */
    private String sessionID;

    /**
     * 会话创建时间
     */
    private long createTime;
    /**
     * 会话上一次活跃时间
     */
    private long lastActivityTime;

    /**
     * 当前正在使用的菜单功能
     */
    private String usedMenu;

    /**
     * 上下文环境
     */
    private Map<String, Object> context = new HashMap<>();

    public void refreshActivTime() {
        this.lastActivityTime = System.currentTimeMillis();
    }

    public boolean isVaild() {
        return (System.currentTimeMillis() - lastActivityTime) < expireTime;
    }


}
