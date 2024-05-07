<<<<<<< HEAD
package me.hyekjung.springbootdeveloper.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import me.hyekjung.springbootdeveloper.domain.Article;

import java.time.LocalDateTime;

@Getter
public class ArticleResponse {
    private final String title;
    private final String content;
    private final String email;
    private final String phoneNumber;
    private final String userName;
    private final String password;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final String message;

    public ArticleResponse(Article article, String message){
        this.title = article.getTitle();
        this.content = article.getContent();
        this.email = article.getEmail();
        this.phoneNumber = article.getPhoneNumber();
        this.userName = article.getUserName();
        this.password = article.getPassword();
        this.createdAt = article.getCreatedAt();
        this.updatedAt = article.getUpdatedAt();
        this.message = message;
    }
}
=======
package me.hyekjung.springbootdeveloper.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import me.hyekjung.springbootdeveloper.domain.Article;

import java.time.LocalDateTime;

@Getter
public class ArticleResponse {
    private final String title;
    private final String content;
    private final String email;
    private final String phoneNumber;
    private final String userName;
    private final String password;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final String message;

    public ArticleResponse(Article article, String message){
        this.title = article.getTitle();
        this.content = article.getContent();
        this.email = article.getEmail();
        this.phoneNumber = article.getPhoneNumber();
        this.userName = article.getUserName();
        this.password = article.getPassword();
        this.createdAt = article.getCreatedAt();
        this.updatedAt = article.getUpdatedAt();
        this.message = message;
    }
}
>>>>>>> 90b5f7648b73c14a1cbdac4e4f7dab41bbe1ee8a
