package com.boot.mybatis20220923jungeun.controller.api;

import com.boot.mybatis20220923jungeun.domain.News;
import com.boot.mybatis20220923jungeun.domain.NewsFile;
import com.boot.mybatis20220923jungeun.dto.CMRespDto;
import com.boot.mybatis20220923jungeun.dto.NewsReadRespDto;
import com.boot.mybatis20220923jungeun.dto.NewsWriteReqDto;
import com.boot.mybatis20220923jungeun.dto.NewsWriteRespDto;
import com.boot.mybatis20220923jungeun.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Slf4j
@RequestMapping("/api")
@RequiredArgsConstructor        //<= final 쓸 때 꼭 해줘야 한다
@RestController
public class NewsController {

    @Value("${file.path}")
    private String filePath;
    private final NewsRepository newsRepository;

    @PostMapping("/news")
    public ResponseEntity<?> write(NewsWriteReqDto newsWriteReqDto) {

        log.info("{}", newsWriteReqDto);
        //09.27 추가
//      log.info("fileName: {}", newsWriteReqDto.getFiles().get(0).getOriginalFilename());

        List<NewsFile> newsFileList = null;

        MultipartFile firstFile = newsWriteReqDto.getFiles().get(0);
//        log.info("{fileName: {}", firstFile.getOriginalFilename()); //파일이름 안나온다
        String firstFileName = firstFile.getOriginalFilename();

//        log.info("{}", filePath); 이렇게는 쓰지 않고 @Value("${file.path}")쓴다

        if(!firstFileName.isBlank()) {
            log.info("파일 입출력을 합니다.");

            newsFileList = new ArrayList<NewsFile>();

            for(MultipartFile file : newsWriteReqDto.getFiles()){
                String originFileName = file.getOriginalFilename();

                String uuid = UUID.randomUUID().toString();
                String extension = originFileName.substring(originFileName.lastIndexOf("."));
                String tempFileName = uuid + extension;

                Path uploadPath = Paths.get(filePath, "news/" + tempFileName);

//                log.info("fileName: {}", file.getOriginalFilename());


//                log.info("파일 업로드 경로: {}", uploadPath.toString());


                File f = new File(filePath + "news");
                if(!f.exists()) {
                    f.mkdirs(); //경로가 존재하지 않으면 만들어라
                }


                try {
                    Files.write(uploadPath, file.getBytes());
                } catch (IOException e) {   //파일이 있을 수 있고 없을 수 있어서 IOException
                    throw new RuntimeException(e);
                }



                NewsFile newsFile = NewsFile.builder()
                        .file_origin_name(originFileName)
                        .file_temp_name(tempFileName)
                        .build();

                newsFileList.add(newsFile);
            }
        }

        //news 객체
        News news = newsWriteReqDto.toEntity("으니");
        int result = newsRepository.save(news);

        if(result == 0) {
            return ResponseEntity.internalServerError().body(new CMRespDto<>(-1, "새 글 작성 실패", news));
        }
//        writer 객체를 만들지 않아서 임의로 만들어줌
//        news.setNews_writer("으니");

        if(newsFileList != null) {
            for(NewsFile newsFile : newsFileList) {
                newsFile.setNews_id(news.getNews_id());
                log.info("NewsFile 객체: {}", newsFile);
            }
            result = newsRepository.saveFiles(newsFileList);

            if(result != newsFileList.size()) {
                return ResponseEntity.internalServerError().body(new CMRespDto<>(-1, "파일 업로드 실패", newsFileList));
            }

        }

        NewsWriteRespDto newsWriteRespDto = news.toNewsWriteRespDto(newsFileList);

        return ResponseEntity.ok(new CMRespDto<>(1, "새 글 작성 완료", newsWriteRespDto));
    }

    @GetMapping("/news/{newsId}")
    public ResponseEntity<?> read(@PathVariable int newsId) {

        log.info("{}", newsId);

        News news = newsRepository.getNews(newsId);

        log.info("{}", news);


        NewsReadRespDto newsReadRespDto = news.toNewsReadRespDto();

        return ResponseEntity.ok(new CMRespDto<>(1, "게시글 불러오기 성공", newsReadRespDto));
    }


}
