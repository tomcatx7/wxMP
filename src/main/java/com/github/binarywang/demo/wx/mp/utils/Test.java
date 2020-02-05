package com.github.binarywang.demo.wx.mp.utils;

import com.github.binarywang.demo.wx.mp.business.domin.FoodInfo;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutNewsMessage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadPoolExecutor;

public class Test {

    public static void main(String[] args) throws IOException {

        String filePath = "C:\\N-5CG63036DL-Data\\xiaozhon\\Desktop\\test.txt";
        Document doc = Jsoup.parse(new File(filePath), "gbk");
        Elements foodEls = doc.getElementsByClass("cp_meiri_li");
        List<FoodInfo> foodlist = new ArrayList();
        for (Element foodEl : foodEls) {
            Element a = foodEl.getElementsByTag("a").first();
            Element img = a.getElementsByTag("img").first();

            String href = a.attr("href");
            String title = a.attr("title");
            String imgSrc = img.attr("src");
            FoodInfo foodInfo = new FoodInfo(href,title,imgSrc);
            foodlist.add(foodInfo);
        }

        for (FoodInfo foodInfo : foodlist) {
            WxMpXmlOutNewsMessage.Item item = new WxMpXmlOutNewsMessage.Item();
            item.setTitle(foodInfo.getTitle());
            item.setDescription("desc");
            item.setPicUrl(foodInfo.getImgSrc());
            item.setUrl(foodInfo.getHref());
            System.out.println(foodInfo);
        }
    }

}
