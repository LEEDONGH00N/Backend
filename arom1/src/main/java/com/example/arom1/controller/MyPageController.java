package com.example.arom1.controller;


import com.example.arom1.common.response.BaseResponse;
import com.example.arom1.common.response.BaseResponseStatus;
import com.example.arom1.dto.MyPageDto;
import com.example.arom1.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class MyPageController {

    @Autowired
    private final MyPageService myPageService;

    //내 정보 불러오기
    @GetMapping("/mypage")
    public BaseResponse<MyPageDto> getMyPage() {
        Long id = 1L;
        return new BaseResponse<>(myPageService.getMyPage(id));
    }

    //내 정보 수정하기
    @PutMapping("/mypage")
    public BaseResponse<MyPageDto> updateMyPage(@RequestBody MyPageDto myPageDto) {
        Long id = 1L;
        return new BaseResponse<>(myPageService.updateById(id, myPageDto));
    }


    //이미지 업로드하기
    @PostMapping(path = "/mypage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResponse<MyPageDto> uploadImage(
            //@RequestBody Long id,
            @RequestPart(value = "file", required = false) MultipartFile multipartFile)
            throws IOException {
        Long id = 1L;

        myPageService.updateImage(id, multipartFile);

        return new BaseResponse<>(myPageService.getMyPage(id));
    }

    //이미지 삭제하기
    @DeleteMapping(path = "/mypage/{fileName}")
    public BaseResponse<String> deleteImage(
            //@RequestBody Long id,
            @PathVariable String fileName) throws IOException {
        Long id = 1L;

        myPageService.deleteImage(id, fileName);

        return new BaseResponse<>(BaseResponseStatus.SUCCESS);
    }

    //다운로드 기능은 없어도 될 듯
//    @GetMapping(path = "/mypage/{fileName}")
//    public BaseResponse<String> getImage(@PathVariable String fileName) throws IOException {
//        Long id = 1L;
//        imageUtil.download(fileName);
//
//        return new BaseResponse<>(BaseResponseStatus.SUCCESS);
//    }

}
