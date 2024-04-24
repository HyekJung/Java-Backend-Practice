package me.hyekjung.springbootdeveloper.controller;

import lombok.RequiredArgsConstructor;
import me.hyekjung.springbootdeveloper.domain.Article;
import me.hyekjung.springbootdeveloper.dto.AddArticleRequest;
import me.hyekjung.springbootdeveloper.dto.ArticleResponse;
import me.hyekjung.springbootdeveloper.dto.UpdateArticleRequest;
import me.hyekjung.springbootdeveloper.service.BlogService;
import org.hibernate.sql.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController // Http Response Body에 객체 데이터를 JSON 형식으로 반환하는 컨트롤러
public class BlogApiController {
    private final BlogService blogService;

    // 글 목록 조회
    @GetMapping("/api/articles")
    public ResponseEntity<List<ArticleResponse>> findAllArticles(){
        List<ArticleResponse> articles = blogService.findAll().stream()
                .map(ArticleResponse::new).toList();

        return ResponseEntity.ok().body(articles);
    }

    // 글 조회
    @GetMapping("/api/articles/{id}")
    //URL 경로에서 값 추출
    public ResponseEntity<ArticleResponse> findArticles(@PathVariable long id){
        Article article = blogService.findById(id);

        return ResponseEntity.ok().body(new ArticleResponse(article));
    }

    // 글 추가
    @PostMapping("/api/articles")
    // @RequestBody로 요청 본문 값 매핑
    public ResponseEntity<Article> addArticle(@Validated @RequestBody AddArticleRequest request){
        Article savedArticle = blogService.save(request);

        // 요청한 자원이 성공적으로 생성되었으며 저장된 블로그 글 정보를 응답 객체에 담아 전송
        return ResponseEntity.status(HttpStatus.CREATED).body(savedArticle);
    }

    //글 수정
    @PutMapping("/api/articles/{id}")
    public ResponseEntity<?> updateArticle(@PathVariable long id,
                                                 @RequestBody UpdateArticleRequest request) {

        Article optionalArticle = blogService.findById(id); // 해당 id의 글
        Article updatedArticle = blogService.update(id, request);

        LocalDateTime createdAt = optionalArticle.getCreatedAt(); // 글 생성일
        LocalDateTime future = createdAt.plusDays(10);

        boolean isFuture = createdAt.isAfter(future);
            if(isFuture){
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("글을 [등록, 수정]한 지 10일이 지나 더 이상 수정할 수 없습니다.");
            }else{
                return ResponseEntity.ok().body(updatedArticle);
            }
        }


//    // 글 삭제
//    @DeleteMapping("/api/articles/{id}")
//    public ResponseEntity<Void> deleteArticle(@PathVariable long id) {
//        blogService.delete(id);
//
//        return ResponseEntity.ok()
//                .build();
//    }
}


//class MessageDTO{ // 메세지로 따로 분리
//    String message;
//}
