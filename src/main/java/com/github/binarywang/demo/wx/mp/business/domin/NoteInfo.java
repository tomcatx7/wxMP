package com.github.binarywang.demo.wx.mp.business.domin;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "wx_mp_noteInfo")
@Data
public class NoteInfo {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)//主键生成策略
    private long id;

    private String userId;

    private String createTime;

    @Column(columnDefinition ="TEXT")
    private String content;


}
