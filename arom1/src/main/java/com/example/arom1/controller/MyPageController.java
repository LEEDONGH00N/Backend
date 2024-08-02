package com.example.arom1.controller;


import com.example.arom1.common.exception.BaseException;
import com.example.arom1.common.response.BaseResponse;
import com.example.arom1.dto.response.MyPageDto;
import com.example.arom1.entity.MemberDetail;
import com.example.arom1.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;

    //내 정보 불러오기
    @GetMapping("/mypage")
    public BaseResponse<MyPageDto> getMyPage(@AuthenticationPrincipal MemberDetail memberDetail) {
        Long id = 1L;
        try {
            return new BaseResponse<>(myPageService.getMyPage(id));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    //내 정보 수정하기
    @PutMapping("/mypage")
    public BaseResponse<MyPageDto> updateMyPage(@RequestBody MyPageDto myPageDto, @AuthenticationPrincipal MemberDetail memberDetail) {
        Long id = 1L;
        try {
            return new BaseResponse<>(myPageService.updateById(id, myPageDto));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    //이미지 리스트 가져오기
    @GetMapping("/mypage/images")
    public BaseResponse<List<String>> getImages(@AuthenticationPrincipal MemberDetail memberDetail) {
        Long id = 1L;
        try {
            return new BaseResponse<>(myPageService.getImages(id));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }


    //이미지 업로드하기
    @PostMapping(path = "/mypage/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResponse<List<String>> uploadImage(
            @RequestPart(value = "file", required = false) MultipartFile multipartFile, @AuthenticationPrincipal MemberDetail memberDetail)
            throws IOException {
        Long id = 1L;
        try {
            myPageService.updateImage(id, multipartFile);
            return new BaseResponse<>(myPageService.getImages(id));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }

    }

    //이미지 삭제하기
    @DeleteMapping(path = "/mypage/images")
    public BaseResponse<List<String>> deleteImage(@RequestParam String filename, @AuthenticationPrincipal MemberDetail memberDetail) {
        Long id = 1L;
        try {
            myPageService.deleteImage(id, filename);
            return new BaseResponse<>(myPageService.getImages(id));

        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    //다운로드 기능은 없어도 될 듯
//    @GetMapping(path = "/mypage/{filename}")
//    public BaseResponse<String> getImage(@PathVariable String filename) throws IOException {
//        Long id = 1L;
//        imageUtil.download(filename);
//
//        return new BaseResponse<>(BaseResponseStatus.SUCCESS);
//    }

}
