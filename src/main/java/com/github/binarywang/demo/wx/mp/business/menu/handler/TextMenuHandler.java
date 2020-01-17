package com.github.binarywang.demo.wx.mp.business.menu.handler;

import com.github.binarywang.demo.wx.mp.business.menu.session.ISession;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;

public interface TextMenuHandler{
    WxMpXmlOutMessage handler(WxMpXmlMessage msg, WxMpService service, ISession session);
}
