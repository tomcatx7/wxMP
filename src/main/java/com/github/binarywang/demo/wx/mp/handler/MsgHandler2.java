package com.github.binarywang.demo.wx.mp.handler;

import com.github.binarywang.demo.wx.mp.business.domin.FoodInfo;
import com.github.binarywang.demo.wx.mp.builder.ArticleBuilder;
import com.github.binarywang.demo.wx.mp.builder.TextBuilder;
import com.github.binarywang.demo.wx.mp.utils.IHttpclient;
import com.github.binarywang.demo.wx.mp.utils.JsonUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutNewsMessage;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static me.chanjar.weixin.common.api.WxConsts.XmlMsgType;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
@Slf4j
@Deprecated
public class MsgHandler2 extends AbstractHandler {

    private final IHttpclient iHttpclient;

    private volatile boolean isDebug = false;
    //美食网主页
    private static final String FOODURL = "http://www.ttmeishi.com/";
    //每日推荐接口
    private static final String DAILYFOODURL = "http://www.ttmeishi.com/tuijian/meiri/";

    private Random random = new Random();

    //根据食谱的数量产生一个随机数
    private int seed;

    private List<FoodInfo> foodlist = new ArrayList<>();

    /*
    /启动一个定时任务 每天定时清除菜单 在每晚0点清除一次
     */ {
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final String startTime = "2020-01-15 00:00:00";
        final long interval = 24 * (1000 * 60 * 60);//24h
        final Timer timer = new Timer();
        try {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    log.info("清除菜单 :{}", foodlist);
                    foodlist.clear();
                }
            }, sdf.parse(startTime), interval);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public MsgHandler2(IHttpclient iHttpclient) {
        this.iHttpclient = iHttpclient;
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
//
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
            if (wxMessage.getContent().contains("吃什么")) {
                List<FoodInfo> foodInfos = getFoodInfos(DAILYFOODURL);
                if (!foodInfos.isEmpty()) {
                    List<WxMpXmlOutNewsMessage.Item> items = new ArrayList<>();
                    FoodInfo foodInfo = foodInfos.get(random.nextInt(seed));
                    WxMpXmlOutNewsMessage.Item item = new WxMpXmlOutNewsMessage.Item();
                    items.add(item);
                    item.setTitle(foodInfo.getTitle());
                    item.setDescription("zxj的私家菜谱");
                    item.setPicUrl(foodInfo.getImgSrc());
                    //href=/CaiPu/0322ef9b84e4b2ff.htm 去掉一个/
                    item.setUrl(FOODURL+foodInfo.getHref().substring(1));
                    return new ArticleBuilder().build(items, wxMessage, weixinService);
                }
            } else {
                content = "不好意思，暂时没开发，请试着问我今晚吃什么？";
            }
        }
        return new TextBuilder().build(content, wxMessage, weixinService);
    }

    private boolean isDebug() {
        return this.isDebug;
    }

    private List<FoodInfo> getFoodInfos(String url) {
        if (!foodlist.isEmpty())
            return foodlist;
        try {
            log.info("发起请求  url :{}", url);
            //返回消息 gbk编码
            Document doc = Jsoup.parse(new String(
                iHttpclient.getBytes(url), "gbk"));

            //从html中提取有效信息
            Elements foodEls = doc.getElementsByClass("cp_meiri_li");
            for (Element foodEl : foodEls) {
                Element a = foodEl.getElementsByTag("a").first();
                Element img = a.getElementsByTag("img").first();

                String href = a.attr("href");
                String title = a.attr("title");
                String imgSrc = img.attr("src");
                FoodInfo foodInfo = new FoodInfo(href, title, imgSrc);
                foodlist.add(foodInfo);
            }
            if (!foodlist.isEmpty())
                seed = foodlist.size();
            log.info("添加食谱：{}", foodlist.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return foodlist;
    }

}
