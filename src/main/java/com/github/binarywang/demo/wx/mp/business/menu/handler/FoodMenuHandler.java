package com.github.binarywang.demo.wx.mp.business.menu.handler;

import com.github.binarywang.demo.wx.mp.business.domin.FoodInfo;
import com.github.binarywang.demo.wx.mp.builder.ArticleBuilder;
import com.github.binarywang.demo.wx.mp.builder.TextBuilder;
import com.github.binarywang.demo.wx.mp.business.menu.session.ISession;
import com.github.binarywang.demo.wx.mp.utils.IHttpclient;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutNewsMessage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Component
public class FoodMenuHandler implements TextMenuHandler {

    private final IHttpclient iHttpclient;

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

    public FoodMenuHandler(IHttpclient iHttpclient) {
        this.iHttpclient = iHttpclient;
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

    @Override
    public WxMpXmlOutMessage handler(WxMpXmlMessage wxMpXmlMessage, WxMpService wxMpService, ISession session) {
        //每次处理完，当前使用菜单重置
        session.setUsedMenu(null);
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
            item.setUrl(FOODURL + foodInfo.getHref().substring(1));
            return new ArticleBuilder().build(items, wxMpXmlMessage, wxMpService);
        }

        return new TextBuilder().build("服务器异常,请稍后再试", wxMpXmlMessage, wxMpService);

    }
}
