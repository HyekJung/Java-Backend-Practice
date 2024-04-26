package me.hyekjung.springbootdeveloper.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
//@AllArgsConstructor
@Getter
public class UpdateArticleRequest extends BaseEntity{
    @Size(max = 200, message = "제목은 200자를 초과할 수 없습니다.")
    private String title;
    @Size(max = 1000, message = "내용은 1000자를 초과할 수 없습니다.")
    private String content;
    @Email
    private String email;
    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message= "휴대폰 번호 양식이 일치하지 않습니다.")
    private String phoneNumber;
    @NotBlank
    private String userName;
    //1. 대소문자로만 구성, 2. 숫자 5개이상, 3. 특수문자가 *!@#$% 2개이상 포함
    //@Pattern(regexp = "", message = "비밀번호는 대소문자 조합, 5개 이상의 숫자, 그리고 특수문자(*!@#$%) 2개 이상을 포함해야 합니다.")
    private String password;

    public UpdateArticleRequest(String title, String content, String email, String phoneNumber, String userName, String password){
        this.title = title;
        this.content = content;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.userName = userName;
        this.password = password;
    }
}
