package com.example.arom1.service;


import com.example.arom1.common.exception.BaseException;
import com.example.arom1.common.response.BaseResponseStatus;
import com.example.arom1.dto.MyPageDto;
import com.example.arom1.entity.Image;
import com.example.arom1.entity.Member;
import com.example.arom1.repository.ImageRepository;
import com.example.arom1.repository.MemberRepository;
import com.example.arom1.util.AmazonS3Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class MyPageService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AmazonS3Util amazonS3Util;

    @Autowired
    private ImageRepository imageRepository;

    // 내 정보 불러오기
    public MyPageDto getMyPage(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NON_EXIST_USER));

        return MyPageDto.toMyPageDto(member);

    }

    // 내 정보 업데이트
    public MyPageDto updateById(Long id, MyPageDto myPageDto) {
        Member updatedMember = memberRepository.findById(id)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NON_EXIST_USER));

        updatedMember.updateMyPage(myPageDto);

        memberRepository.save(updatedMember);

        return myPageDto;
    }


    // ---------------- 이미지 기능 ---------------------
    // 이미지 업로드
    public void updateImage(Long id, MultipartFile multipartFile) throws IOException {
        Member updatedMember = memberRepository.findById(id)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NON_EXIST_USER));

        String extend = multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf("."));
        String url = amazonS3Util.uploadFile(multipartFile,extend);

        Image newImage = Image.builder()
                .url(url)
                .member(updatedMember)
                .build();

        updatedMember.uploadImage(newImage);

        imageRepository.save(newImage);
    }

    //이미지 삭제
    public void deleteImage(Long id, String fileName) {
        Member updatedMember = memberRepository.findById(id)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NON_EXIST_USER));

        Image image = imageRepository.findByUrl(
                amazonS3Util.getUrl(fileName))
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NO_IMAGE_BY_URL));

        amazonS3Util.deleteFile(fileName);

        updatedMember.removeImage(image);

        imageRepository.delete(image);
    }


}
