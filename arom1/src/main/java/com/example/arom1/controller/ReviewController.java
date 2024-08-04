package com.example.arom1.controller;

import com.example.arom1.common.exception.BaseException;
import com.example.arom1.common.response.BaseResponse;
import com.example.arom1.common.response.BaseResponseStatus;
import com.example.arom1.dto.ReviewDto;
import com.example.arom1.dto.request.SeoulEateryDto;
import com.example.arom1.dto.response.EateryResponse;
import com.example.arom1.dto.response.ReviewResponse;
import com.example.arom1.entity.Eatery;
import com.example.arom1.entity.Review;
import com.example.arom1.repository.ReviewRepository;
import com.example.arom1.service.ReviewService;
import com.fasterxml.jackson.databind.ser.Serializers;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ReviewController {

    @Autowired
    private ReviewService reviewService;


    @GetMapping("/eatery/{eateryId}/review")
    public BaseResponse<?> getReviews(@PathVariable Long eateryId) throws IOException {
        try{
            List<ReviewResponse> reviewResponseList = reviewService.findAllReviews(eateryId);
            return new BaseResponse<>(reviewResponseList);
        }
        catch (BaseException e){
            return new BaseResponse<>(e);
        }
    }

    //작성
    @PostMapping("/eatery/{eateryId}/review")
    public BaseResponse<?> saveReview(@PathVariable Long eateryId, @Valid @RequestBody ReviewDto reviewDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            List<FieldError> errors = bindingResult.getFieldErrors();
            return new BaseResponse<>(HttpStatus.BAD_REQUEST.value());
        }
        try{

            ReviewResponse reviewResponse = reviewService.saveReview(reviewDto);
            return new BaseResponse<>(reviewResponse);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    //수정
    @PutMapping("/eatery/{eateryId}/review/{reviewId}")
    public BaseResponse<?> updateReview(@PathVariable Long eateryId, @PathVariable Long reviewId, @Valid @RequestBody ReviewDto reviewdto, BindingResult bindingResult ){
        if(bindingResult.hasErrors()){
            List<FieldError> errors = bindingResult.getFieldErrors();
            return new BaseResponse<>(HttpStatus.BAD_REQUEST.value());
        }
        try{
            ReviewResponse reviewResponse = reviewService.updateReview(reviewId, reviewdto);
            return new BaseResponse<>(reviewResponse);
        }
        catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    //삭제
    @DeleteMapping("/eatery/{eateryId}/review/{reviewId}")
    public BaseResponse<?> deleteReview(@PathVariable Long eateryId, @PathVariable Long reviewId,@RequestParam Long member_id){
        try{
            reviewService.deleteReview(reviewId, member_id);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS);
        }
        catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

}
