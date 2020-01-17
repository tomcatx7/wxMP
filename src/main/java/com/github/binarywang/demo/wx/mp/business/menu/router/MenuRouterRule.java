package com.github.binarywang.demo.wx.mp.business.menu.router;

import com.github.binarywang.demo.wx.mp.business.menu.constant.MenuType;
import com.github.binarywang.demo.wx.mp.business.menu.handler.TextMenuHandler;
import com.github.binarywang.demo.wx.mp.business.menu.session.ISession;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.apache.commons.lang3.StringUtils;

public class MenuRouterRule {
    private final MenuRouter menuRouterBuilder;

    private TextMenuHandler handler;

    private MenuType menuType;

    public MenuRouterRule(MenuRouter menuRouter) {
        this.menuRouterBuilder = menuRouter;
    }

    public MenuRouterRule type(MenuType menuType) {
        this.menuType = menuType;
        return this;
    }

    public MenuRouterRule setHandler(TextMenuHandler handler) {
        this.handler = handler;
        return this;
    }

    public MenuRouter end() {
        this.menuRouterBuilder.addRule(this);
        return this.menuRouterBuilder;
    }

    /**
     * 根据所选菜单匹配
     * @param menu
     * @return
     */
    public boolean test(String menu) {
        return !StringUtils.isEmpty(menu) && menu.equals(this.menuType.value());
    }

    public WxMpXmlOutMessage service(WxMpXmlMessage wxMpXmlMessage, WxMpService weixinService, ISession session) {
        return this.handler.handler(wxMpXmlMessage, weixinService,session);
    }
}
