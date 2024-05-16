package me.hyekjung.springbootdeveloper.service;

import lombok.RequiredArgsConstructor;
import me.hyekjung.springbootdeveloper.domain.Article;
import me.hyekjung.springbootdeveloper.dto.AddArticleRequest;
import me.hyekjung.springbootdeveloper.dto.DeleteArticleRequest;
import me.hyekjung.springbootdeveloper.dto.UpdateArticleRequest;
import me.hyekjung.springbootdeveloper.repository.BlogRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Properties;

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
    public Page<Article> getAll(Pageable pageable){ //페이지 정보
        return blogRepository.findAll(pageable);
    }

    // 글 조회에 필요한 Id
    @Transactional(readOnly = true) // 성능 향상
    public Article getById(long id) {
        return blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));
    }

    // 수정 가능 날짜 알려주는 메서드
    public String getMessageArticle(long id){
        Article article = getById(id);

        //날짜 계산
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime createdAt = article.getCreatedAt();
        LocalDateTime lastDay = createdAt.plusDays(10);

        Duration duration = Duration.between(now, lastDay); // 남은 날짜

        return "게시물 수정까지 " + duration.toDays() + "일 남았습니다.";
    }

    @Transactional
    // 삭제 상태 변경 메서드
    public void deleteChange(long id) {
        Article article = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("id를 찾을 수 없습니다." + id));
        article.setDeletedAt(true); //삭제 시간 변경 = 상태
        blogRepository.save(article);
    }

    @Transactional
    // 실제 삭제하는 메서드
    public void delete(long id) {
        blogRepository.deleteById(id);
    }

    // 수정하는 메서드
    @Transactional
    public Article update(long id, UpdateArticleRequest request){
        Article article = getById(id);

        LocalDateTime now = LocalDateTime.now(); // 수정 시각
        LocalDateTime createdAt = article.getCreatedAt(); // 글 생성일
        LocalDateTime futureTen = createdAt.plusDays(10); // 생성한지 10일 되면 수정 불가능
        LocalDateTime futureNine = createdAt.plusDays(9); // 9일되면 알람

        boolean isFutureTen = now.isAfter(futureTen); // 수정시각과 설정날짜와의 비교
        boolean isFutureNine = now.isAfter(futureNine);

        // 수정 조건(알람 포함) 추가
        if(isFutureTen){
            throw new IllegalArgumentException("글을 [등록, 수정]한 지 10일이 지나 더 이상 수정할 수 없습니다.");
        }else if(isFutureNine){
            request.setMessage("마지막 수정일입니다. 하루가 지나면 수정할 수 없습니다.");
            article.update(request.getTitle(), request.getContent(), request.getEmail(), request.getPhoneNumber(),
                    request.getUserName(), request.getPassword(), request.getMessage());
            return article;
        }else{ // 예외가 없을 때 수정
            request.setMessage("성공적으로 수정되었습니다.");
            article.update(request.getTitle(), request.getContent(), request.getEmail(), request.getPhoneNumber(),
                    request.getUserName(), request.getPassword(), request.getMessage());
            return article;
        }
    }

    //제목 검색하는 메서드
//    @Query(value = "SELECT * FROM ARTICLE WHERE TITLE LIKE ?")
//    //@Transactional(readOnly = true)
//    public List<Article> searchByTitle(String title){
//        return
//    }
}
