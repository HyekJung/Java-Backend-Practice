package me.hyekjung.springbootdeveloper.service;

import lombok.RequiredArgsConstructor;
import me.hyekjung.springbootdeveloper.domain.Article;
import me.hyekjung.springbootdeveloper.dto.AddArticleRequest;
import me.hyekjung.springbootdeveloper.dto.UpdateArticleRequest;
import me.hyekjung.springbootdeveloper.repository.BlogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor // final이 붙거나 @NotNull이 붙은 필드의 생성자 추가
@Service // 빈으로 등록
public class BlogService {
    private final BlogRepository blogRepository;

    //블로그 추가 메서드
    public Article save(AddArticleRequest request){
        return blogRepository.save(request.toEntity());
    }

    public List<Article> findAll(){
        return blogRepository.findAll();
    }

    public Article findById(long id) {
        return blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));
    }

    //삭제하는 메서드
    public void delete(long id) {
        blogRepository.deleteById(id);
    }

    //트랜잭션 메서드 - 매칭한 메서드를 하나의 트랜잭션으로 묶는 역할
    @Transactional
    public Article update(long id, UpdateArticleRequest request){
        Article article = blogRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("not found: "+ id));

        article.update(request.getTitle(), request.getContent());

        return article;
    }
}
