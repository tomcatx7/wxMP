package com.github.binarywang.demo.wx.mp.business.menu.handler;

import com.github.binarywang.demo.wx.mp.builder.TextBuilder;
import com.github.binarywang.demo.wx.mp.business.domin.NoteInfo;
import com.github.binarywang.demo.wx.mp.business.menu.session.ISession;
import com.github.binarywang.demo.wx.mp.business.repository.NoteRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@AllArgsConstructor
public class NoteMenuHandler implements TextMenuHandler {

    private final NoteRepository noteRepository;

    @Override
    public WxMpXmlOutMessage handler(WxMpXmlMessage msg, WxMpService service, ISession session) {
        String res = "";
        if ("退出".equals(msg.getContent())) {
            //退出时，重置当前菜单
            session.setUsedMenu(null);
            res = "老板再见~";
        } else if ("查看笔记".equals(msg.getContent())) {
            List<NoteInfo> notes = noteRepository.findByUserId(msg.getFromUser());
            if (!notes.isEmpty()) {
                StringBuffer sb = new StringBuffer();
                for (NoteInfo note : notes) {
                    String str = "时间:" + note.getCreateTime() + " 内容:" + note.getContent() + "\n";
                    sb.append(str);
                }
                res = sb.toString();
            }
        } else if (msg.getContent().contains("删除笔记")) {
            String content = msg.getContent();
            noteRepository.deleteByUserIdAndCreateTime(msg.getFromUser(),content.substring(content.indexOf(":")+1));
        } else {
            res = "写入成功，可选择\n1)查看笔记\n2)删除笔记:时间\n3)退出";
        }
        return new TextBuilder().build(res, msg, service);
    }

}
