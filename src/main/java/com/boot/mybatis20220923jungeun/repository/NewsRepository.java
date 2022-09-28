package com.boot.mybatis20220923jungeun.repository;

import com.boot.mybatis20220923jungeun.domain.News;
import com.boot.mybatis20220923jungeun.domain.NewsFile;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NewsRepository {
    public int save(News news);     //메소드명으로 넘어감  : ctrl + B
    public int saveFiles(List<NewsFile> newsFileList);
    public News getNews(int news_id);

}
