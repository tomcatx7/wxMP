package com.github.binarywang.demo.wx.mp.business.domin;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FoodInfo {
    //美食详情链接
    private String href;
    //美食标题
    private String title;
    //美食图片链接
    private String imgSrc;

}
