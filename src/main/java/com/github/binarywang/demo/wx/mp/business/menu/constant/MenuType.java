package com.github.binarywang.demo.wx.mp.business.menu.constant;

public enum  MenuType {
//    //每日推荐食谱菜单
//    public static final String FOODMENU = "foodMenu";
//    //笔记本菜单
//    public static final String NOTEMENU = "noteMenu";
    FOODMENU("今天吃什么"),NOTEMENU("我要记笔记"),CHATMENU("和我聊天吧"),YANZHIMENU("我要测颜值");
    private String rex;

    MenuType(String rex){
        this.rex = rex;
    }

    public String value(){
        return this.rex;
    }
}
