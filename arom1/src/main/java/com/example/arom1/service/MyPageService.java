package com.example.arom1.service;


import com.example.arom1.common.exception.BaseException;
import com.example.arom1.common.response.BaseResponseStatus;
import com.example.arom1.dto.request.MyPageRequest;
import com.example.arom1.dto.response.MyPageResponse;
import com.example.arom1.entity.Image;
import com.example.arom1.entity.Member;
import com.example.arom1.entity.security.MemberDetail;
import com.example.arom1.repository.ImageRepository;
import com.example.arom1.repository.MemberRepository;
import com.example.arom1.common.util.AmazonS3Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MyPageService {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private AmazonS3Util amazonS3Util;
    @Autowired
    private ImageRepository imageRepository;

    // 내 정보 불러오기
    public MyPageResponse getMyPage(Long id) {
        return MyPageResponse.of(findMember(id));
    }

    // 내 정보 업데이트
    public MyPageResponse updateById(Long id, MyPageRequest myPageRequest) {
        Member updatedMember = findMember(id);
        updatedMember.updateMyPage(myPageRequest);
        memberRepository.save(updatedMember);
        return MyPageResponse.of(updatedMember);
    }


    // ---------------- 이미지 기능 ---------------------
    //이미지 리스트 가져오기
    public List<String> getImages(Long id) throws BaseException {
        return findMember(id)
                .getImages()
                .stream()
                .map(Image::getUrl)
                .collect(Collectors.toList());
    }

    // 이미지 업로드
    public void updateImage(Long id, MultipartFile multipartFile) throws IOException {
        Member updatedMember = findMember(id);

        String url = amazonS3Util.uploadFile(multipartFile);

        Image newImage = Image.builder()
                .url(url)
                .member(updatedMember)
                .build();

        updatedMember.uploadImage(newImage);
        imageRepository.save(newImage);
    }

    //이미지 삭제
    public void deleteImage(Long id, String filename) {
        Member updatedMember = findMember(id);

        Image image = imageRepository.findByUrl(
                amazonS3Util.getUrl(filename))
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NO_IMAGE_BY_URL));

        amazonS3Util.deleteFile(filename);

        updatedMember.removeImage(image);
        imageRepository.delete(image);
    }

    private Member findMember(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_MEMBER));
    }

}
