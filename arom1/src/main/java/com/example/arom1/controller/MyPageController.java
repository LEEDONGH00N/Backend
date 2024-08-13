package com.example.arom1.controller;


import com.example.arom1.common.exception.BaseException;
import com.example.arom1.common.response.BaseResponse;
import com.example.arom1.dto.request.MyPageRequest;
import com.example.arom1.dto.response.MyPageResponse;
import com.example.arom1.entity.security.MemberDetail;
import com.example.arom1.service.MemberService;
import com.example.arom1.service.MyPageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;
    private final MemberService memberService;

    //회원 탈퇴 메서드
    @DeleteMapping("/deleteid")
    public BaseResponse<String> cancelAccount(@AuthenticationPrincipal MemberDetail memberDetail) {
        try {
            memberService.deleteMember(memberDetail);
            return new BaseResponse<>("회원 탈퇴가 완료되었습니다.");
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    //내 정보 불러오기
    @GetMapping("/mypage")
    public BaseResponse<MyPageResponse> getMyPage(@AuthenticationPrincipal MemberDetail memberDetail) {
        try {
            Long id = memberDetail.getId();
            return new BaseResponse<>(myPageService.getMyPage(id));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    //내 정보 수정하기
    @PostMapping("/mypage")
    public BaseResponse<MyPageResponse> updateMyPage(@Valid @RequestBody MyPageRequest myPageRequest, BindingResult bindingResult,
                                                     @AuthenticationPrincipal MemberDetail memberDetail) {
        if(bindingResult.hasErrors()) {
            List<String> messages = new ArrayList<>();
            bindingResult.getAllErrors().forEach(error ->  messages.add(error.getDefaultMessage()));
            for(String msg : messages) System.out.println(msg);
            return new BaseResponse<>(false, HttpStatus.BAD_REQUEST.value(), messages.get(0));
        }

        try {
            Long id = memberDetail.getId();
            return new BaseResponse<>(myPageService.updateById(id, myPageRequest));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    //이미지 리스트 가져오기
    @GetMapping("/mypage/images")
    public BaseResponse<List<String>> getImages(@AuthenticationPrincipal MemberDetail memberDetail) {
        try {
            Long id = memberDetail.getId();
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
        Long id = memberDetail.getId();
        try {
            myPageService.updateImage(id, multipartFile);
            return new BaseResponse<>(myPageService.getImages(id));
        } catch (NullPointerException | MultipartException e) {
            return new BaseResponse<>(false, HttpStatus.BAD_REQUEST.value(), e.getMessage());
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    //이미지 삭제하기
    @DeleteMapping(path = "/mypage/images")
    public BaseResponse<List<String>> deleteImage(@RequestParam String filename, @AuthenticationPrincipal MemberDetail memberDetail) {
        Long id = memberDetail.getId();
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
