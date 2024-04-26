package me.hyekjung.springbootdeveloper.service;

import lombok.RequiredArgsConstructor;
import me.hyekjung.springbootdeveloper.domain.Article;
import me.hyekjung.springbootdeveloper.dto.AddArticleRequest;
import me.hyekjung.springbootdeveloper.dto.UpdateArticleRequest;
import me.hyekjung.springbootdeveloper.repository.BlogRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor // final이 붙거나 @NotNull이 붙은 필드의 생성자 추가
@Service // 빈으로 등록
public class BlogService {
    private final BlogRepository blogRepository;


    //블로그 추가 메서드
    public Article save(AddArticleRequest request){
        return blogRepository.save(request.toEntity());
    }

    @Transactional(readOnly = true)
    //전체 조회
    public Page<Article> findAll(Pageable pageable){ //페이지 정보
        return blogRepository.findAll(pageable);
    }

    // 글 조회에 필요한 Id
    @Transactional(readOnly = true) // 성능 향상
    public Article findById(long id) {
        return blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));
    }

    //삭제하는 메서드
    public void delete(long id) {
        blogRepository.deleteById(id);
    }

    @Transactional
    public void update(long id, UpdateArticleRequest request){
        Article article = findById(id);

        LocalDateTime createdAt = article.getCreatedAt(); // 글 생성일
        LocalDateTime futureTen = createdAt.plusDays(10); // 생성한지 10일 되면 수정 불가능
        LocalDateTime futureNine = createdAt.plusDays(9); // 9일되면 알람

        boolean isFutureTen = createdAt.isAfter(futureTen);
        boolean isFutureNine = createdAt.isAfter(futureNine);

        // 수정 조건(알람 포함) 추가
        if(isFutureTen){
            throw new IllegalArgumentException("글을 [등록, 수정]한 지 10일이 지나 더 이상 수정할 수 없습니다.");
        }else if(isFutureNine){
            throw new IllegalArgumentException("마지막 수정일입니다. 하루가 지나면 수정할 수 없습니다.");
        }else{ // 예외가 없을 때 수정
            article.update(request.getTitle(), request.getContent(), request.getEmail(), request.getPhoneNumber(),
                    request.getUserName(), request.getPassword());
            blogRepository.save(article);
        }
    }
}
