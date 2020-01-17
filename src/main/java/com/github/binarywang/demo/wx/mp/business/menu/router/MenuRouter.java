package com.github.binarywang.demo.wx.mp.business.menu.router;

import com.github.binarywang.demo.wx.mp.builder.TextBuilder;
import com.github.binarywang.demo.wx.mp.business.menu.constant.MenuType;
import com.github.binarywang.demo.wx.mp.business.menu.session.ISession;
import com.github.binarywang.demo.wx.mp.business.menu.session.ISessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 菜单路由
 * 根据菜单项，路由到相应的模块去处理业务。
 */
public class MenuRouter {

    private ISessionManager iSessionManager;

    private List<MenuRouterRule> menuRouterRules = new ArrayList<>();

    public MenuRouterRule rule() {
        return new MenuRouterRule(this);
    }

    public void addRule(MenuRouterRule rule) {
        this.menuRouterRules.add(rule);
    }

    public ISessionManager getiSessionManager() {
        return iSessionManager;
    }

    public void setiSessionManager(ISessionManager iSessionManager) {
        this.iSessionManager = iSessionManager;
    }

    /**
     * 根据用户当前会话正在使用的菜单项路由到相应的模块
     *
     * @param wxMpXmlMessage
     * @param weixinService
     * @return
     */
    public WxMpXmlOutMessage route(WxMpXmlMessage wxMpXmlMessage, WxMpService weixinService) {
        WxMpXmlOutMessage res = new TextBuilder().build("功能暂时未开发,你可以问我 \n1)今天吃什么\n2)我要记笔记\n3)和我聊天吧", wxMpXmlMessage, weixinService);
        if (this.menuRouterRules.isEmpty())
            return res;
        String content = wxMpXmlMessage.getContent();
        ISession session = iSessionManager.getSession(wxMpXmlMessage.getFromUser());
        /**
         * 每次访问刷新session
         */
        session.refreshActivTime();

        if (StringUtils.isEmpty(session.getUsedMenu())) {
            //当前会话没有正在使用的菜单功能
            if (content.contains(MenuType.FOODMENU.value())) {
                session.setUsedMenu(MenuType.FOODMENU.value());
            } else if (content.contains(MenuType.NOTEMENU.value())) {
                session.setUsedMenu(MenuType.NOTEMENU.value());
                //第一次选择菜单，欢迎语句
                return new TextBuilder().build("嗨！你现在可以开始记笔记啦，请输入你要记录的内容{}", wxMpXmlMessage, weixinService);
            } else if (content.contains(MenuType.CHATMENU.value())) {
                session.setUsedMenu(MenuType.CHATMENU.value());
                //第一次选择菜单，欢迎语句
                return new TextBuilder().build("嗨,现在可以和我聊天啦!", wxMpXmlMessage, weixinService);
            } else {
                return res;
            }
        }

        Iterator<MenuRouterRule> iterator = this.menuRouterRules.iterator();
        while (iterator.hasNext()) {
            MenuRouterRule rule = iterator.next();
            //根据菜单路由到相应模块
            if (rule.test(session.getUsedMenu())) {
                res = rule.service(wxMpXmlMessage, weixinService, session);
            }
        }

        return res;
    }
}
