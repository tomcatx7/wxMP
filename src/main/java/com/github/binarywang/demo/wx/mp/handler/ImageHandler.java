package com.github.binarywang.demo.wx.mp.handler;

import com.github.binarywang.demo.wx.mp.builder.TextBuilder;
import com.github.binarywang.demo.wx.mp.business.menu.constant.MenuType;
import com.github.binarywang.demo.wx.mp.business.menu.session.ISession;
import com.github.binarywang.demo.wx.mp.business.menu.session.ISessionManager;
import com.github.binarywang.demo.wx.mp.utils.IHttpclient;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@AllArgsConstructor
@Slf4j
public class ImageHandler extends AbstractHandler {
    /**
     * 微软小冰颜值测试接口 已废弃
     */
    //素材下载接口
    private static final String URL = "http://file.api.weixin.qq.com/cgi-bin/media/get";
    //微软小冰测颜值接口
    private static final String YANZHI_URL = "http://kan.msxiaobing.com/Api/ImageAnalyze/Process?service=yanzhi";
    //微软小冰测颜值图片上传接口
    private static final String YANZHI_UPLOAD_URL = "http://kan.msxiaobing.com/Api/Image/UploadBase64";

    private final ISessionManager sessionManager;

    private final IHttpclient iHttpclient;

    @SneakyThrows
    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMpXmlMessage, Map<String, Object> map, WxMpService wxMpService, WxSessionManager wxSessionManager) throws WxErrorException {
        String res = "";
        ISession session = sessionManager.getSession(wxMpXmlMessage.getFromUser());
        if (session != null) {
            if (session.getUsedMenu().equals(MenuType.YANZHIMENU.value())) {
                session.setUsedMenu(null);
                String mediaId = wxMpXmlMessage.getMediaId();
                String accessToken = wxMpService.getAccessToken();

                byte[] bytes = iHttpclient.getBytes(URL + "?" + "access_token=" + accessToken + "&media_id=" + mediaId);
                log.info("从服务器成功获取文件,mediaId={}", mediaId);
                //将图片base64编码后上传至微软服务器,返回图片链接
//                Connection.Response response = iHttpclient.post(YANZHI_UPLOAD_URL, Base64.encodeBase64String(bytes));
////                if (response != null) {
////                    Map<String, String> cookies = response.cookies();
////
////                    JsonObject jsonObject = new JsonObject();
////                    jsonObject.getAsJsonObject(response.body());
////                    String imgUrl = jsonObject.get("Host").toString() + jsonObject.get("Url").toString();
////
////                }
//                FileOutputStream fos = new FileOutputStream(new File("C:\\N-5CG63036DL-Data\\xiaozhon\\Desktop\\test.jpg"));
//                fos.write(bytes);
//                fos.close();
//                System.out.println("图片保存在本地");
                res = "颜值100分";
            }
        }
        return new TextBuilder().build(res, wxMpXmlMessage, wxMpService);
    }
}
