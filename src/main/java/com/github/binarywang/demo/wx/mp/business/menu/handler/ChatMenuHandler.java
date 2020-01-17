package com.github.binarywang.demo.wx.mp.business.menu.handler;

import com.github.binarywang.demo.wx.mp.builder.TextBuilder;
import com.github.binarywang.demo.wx.mp.business.menu.session.ISession;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.stereotype.Component;

@Component
public class ChatMenuHandler implements TextMenuHandler {

    @Override
    public WxMpXmlOutMessage handler(WxMpXmlMessage msg, WxMpService service, ISession session) {
        String res = "抱歉，正在开发中，别急嘛·····\n对我说拜拜退出聊天";
        String content = msg.getContent();
        if (content.equals("拜拜")) {
            res = "再见，我会想你的";
            session.setUsedMenu(null);
        }
        return new TextBuilder().build(res, msg, service);

    }
}
