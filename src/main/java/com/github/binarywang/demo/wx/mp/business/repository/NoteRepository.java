package com.github.binarywang.demo.wx.mp.business.repository;

import com.github.binarywang.demo.wx.mp.business.domin.NoteInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<NoteInfo,Long> {

    List<NoteInfo> findByUserId(String userId);

    void deleteByUserId(String userId);

    void deleteByUserIdAndCreateTime(String userId,String createTime);

}
