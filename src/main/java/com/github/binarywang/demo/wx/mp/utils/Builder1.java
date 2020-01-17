package com.github.binarywang.demo.wx.mp.utils;

public class Builder1 {

    private String name;
    private String age;

    public static Builder1 create(){
        return new Builder1();
    }

    public Builder1 name(String name){
        this.name = name;
        return this;
    }

    public Builder1 age(String age){
        this.age = age;
        return this;
    }

    public Builder1 builder(){
        return this;
    }

    public static void main(String[] args) {
        Builder1.create().age("20").name("zxj").builder();
    }
}
