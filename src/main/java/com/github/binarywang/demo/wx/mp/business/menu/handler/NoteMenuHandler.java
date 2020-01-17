package com.github.binarywang.demo.wx.mp.business.menu.handler;

import com.github.binarywang.demo.wx.mp.builder.TextBuilder;
import com.github.binarywang.demo.wx.mp.business.menu.session.ISession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@AllArgsConstructor
public class NoteMenuHandler implements TextMenuHandler {

    Map<String, String> temStore = new HashMap<>();

    @Override
    public WxMpXmlOutMessage handler(WxMpXmlMessage msg, WxMpService service, ISession session) {
        String res = "";
        if (msg.getContent().equals("退出")) {
            //退出时，重置当前菜单
            session.setUsedMenu(null);
            res = "老板再见~";
        } else {
            temStore.put(msg.getFromUser(), msg.getContent());
            res = "hi~你刚才记录的内容是:\n{" + temStore.get(msg.getFromUser()) + "}\n记录完成，请输入退出";
        }
        return new TextBuilder().build(res, msg, service);
    }

}
