package com.example.arom1.dto;


import com.example.arom1.entity.Eatery;
import com.example.arom1.entity.Member;
import com.example.arom1.entity.Review;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@Getter
public class ReviewDto {
    private Long id;

    @NotBlank
    private String content;

    @NotNull
    @Range(min=1, max=5, message = "별점은 1점 이상 5점 이하여야 합니다.")
    private double rating;

    private int views;

    private int likes;

    private int dislikes;

    @NotNull
    private long member_id;

    @NotNull
    private long eatery_id;

    @Builder
    private ReviewDto(Long id,String content, double rating, int views, int likes, int dislikes, long member_id, long eatery_id) {
        this.id=id;
        this.content=content;
        this.rating=rating;
        this.views=views;
        this.likes=likes;
        this.dislikes=dislikes;
        this.member_id=member_id;
        this.eatery_id=eatery_id;
    }

}

