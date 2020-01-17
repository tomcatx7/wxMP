package com.github.binarywang.demo.wx.mp.builder;

import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutNewsMessage;

import java.util.List;

public class ArticleBuilder extends AbstractBuilder {

    public WxMpXmlOutMessage build(List<WxMpXmlOutNewsMessage.Item> items, WxMpXmlMessage wxMessage, WxMpService service) {
        WxMpXmlOutMessage m = WxMpXmlOutMessage.NEWS().articles(items).fromUser(wxMessage.getToUser()).toUser(wxMessage.getFromUser()).build();
        return m;
    }

    @Override
    public WxMpXmlOutMessage build(String content, WxMpXmlMessage wxMessage, WxMpService service) {
        return null;
    }
}
