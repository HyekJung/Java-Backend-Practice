package me.hyekjung.springbootdeveloper.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import me.hyekjung.springbootdeveloper.domain.Article;
import jakarta.validation.constraints.Max;
import lombok.*;

@NoArgsConstructor // 기본 생성자 추가
@AllArgsConstructor // 모든 필드 값을 파라미터로 받는 생성자 추가
@Getter
@Data

public class AddArticleRequest {

    @Size(max = 200, message = "제목은 200자를 초과할 수 없습니다.")
    private String title;
    @Size(max = 1000, message = "내용은 1000자를 초과할 수 없습니다.")
    private String content;
    private String email;
    private String phoneNumber;
    private String user;
    //대소문자로 이뤄져야하고 숫자가 5개이상 특수문자가 *!@#$% 2개이상 포함
    @Pattern(regexp = "")
    private String password;


    public Article toEntity(){ // 생성자를 사용해 객체 생성
        return Article.builder().title(title).content(content).build();
    }
}
