package com.github.binarywang.demo.wx.mp.handler;

import com.github.binarywang.demo.wx.mp.builder.TextBuilder;
import com.github.binarywang.demo.wx.mp.business.menu.router.MenuRouter;
import com.github.binarywang.demo.wx.mp.utils.JsonUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

import static me.chanjar.weixin.common.api.WxConsts.XmlMsgType;

@Slf4j
@Component
public class MsgHandler extends AbstractHandler {

    private final MenuRouter menuRouter;

    private volatile boolean isDebug = false;

    public MsgHandler(MenuRouter menuRouter) {
        this.menuRouter = menuRouter;
    }

    @SneakyThrows
    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                    Map<String, Object> context, WxMpService weixinService,
                                    WxSessionManager sessionManager) {

        if (!wxMessage.getMsgType().equals(XmlMsgType.EVENT)) {
            //TODO 可以选择将消息保存到本地
        }
        //当用户输入关键词如“你好”，“客服”等，并且有客服在线时，把消息转发给在线客服
//        try {
//            if (StringUtils.startsWithAny(wxMessage.getContent(), "你好", "客服")
//                && weixinService.getKefuService().kfOnlineList()
//                .getKfOnlineList().size() > 0) {
//                return WxMpXmlOutMessage.TRANSFER_CUSTOMER_SERVICE()
//                    .fromUser(wxMessage.getToUser())
//                    .toUser(wxMessage.getFromUser()).build();
//            }
//        } catch (WxErrorException e) {
//            e.printStackTrace();
//        }
        if (StringUtils.startsWithAny(wxMessage.getContent(), "start debug")) {
            this.isDebug = true;
        }
        if (StringUtils.startsWithAny(wxMessage.getContent(), "cancel debug")) {
            this.isDebug = false;
        }
        String content = "服务器异常，请稍后重试";
        if (isDebug()) {
            /**
             * 用于debug
             */
            content = "debug|收到信息内容：" + JsonUtils.toJson(wxMessage);
        } else {
            /**
             * 处理文字消息
             */
            return menuRouter.route(wxMessage, weixinService);
        }
        return new TextBuilder().build(content, wxMessage, weixinService);
    }

    private boolean isDebug() {
        return this.isDebug;
    }

}
