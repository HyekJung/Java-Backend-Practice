package me.hyekjung.springbootdeveloper.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteArticleRequest {
    private boolean deletedAt; //삭제요청
}
