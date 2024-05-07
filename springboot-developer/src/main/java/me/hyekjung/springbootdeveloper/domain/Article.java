package me.hyekjung.springbootdeveloper.domain;

import jakarta.persistence.*;
import lombok.*;
import me.hyekjung.springbootdeveloper.dto.BaseEntity;

import java.time.LocalDateTime;

@Entity // 엔티티로 지정
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article extends BaseEntity {

    @Id // id 필드를 기본키로 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본키를 자동으로 1씩 증가
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "title", nullable = false) // 'title'이라는 not null 칼럼과 매핑
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phoneNumber", nullable = false)
    private String phoneNumber;

    @Column(name = "userName", nullable = false)
    private String userName;

    @Column(name = "password", nullable = false)
    private String password;

    private String message;

    @Builder // 빌더 패턴으로 객체 생성
    // 생성자 위에 입력하면 빌더 패턴 방식으로 객체를 생성할 수 있어 편리
    public Article(String title, String content, String email, String phoneNumber, String userName, String password) {
        this.title = title;
        this.content = content;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.userName = userName;
        this.password = password;
    }

    public void update(String title, String content, String email, String phoneNumber, String userName, String password, String message){
        this.title = title;
        this.content = content;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.userName = userName;
        this.password = password;
        this.message = message;
    }
}
