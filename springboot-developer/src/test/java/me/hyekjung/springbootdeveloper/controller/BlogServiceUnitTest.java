package me.hyekjung.springbootdeveloper.controller;

import me.hyekjung.springbootdeveloper.domain.Article;
import me.hyekjung.springbootdeveloper.dto.AddArticleRequest;
import me.hyekjung.springbootdeveloper.dto.UpdateArticleRequest;
import me.hyekjung.springbootdeveloper.repository.BlogRepository;
import me.hyekjung.springbootdeveloper.service.BlogService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(MockitoExtension.class)
class BlogServiceUnitTest {

    @Mock
    protected BlogRepository blogRepository;

    @InjectMocks
    protected BlogService blogService;

    @DisplayName("saveArticle: 블로그 글 저장에 성공한다.") //C
    @Test
    public void saveArticle() {
        // given
        AddArticleRequest request = new AddArticleRequest("title", "content", "test@example.com", "010-1234-5678", "username", "password");

        // when
        when(blogRepository.save(any())).thenReturn(new Article("title", "content", "test@example.com", "010-1234-5678", "username", "password"));

        Article savedArticle = blogService.save(request);

        // then
        assertThat(savedArticle).isNotNull();
        assertThat(savedArticle.getTitle()).isEqualTo("title");
        assertThat(savedArticle.getContent()).isEqualTo("content");
    }

    @DisplayName("getAllArticles: 블로그 글 목록 조회에 성공한다.") //R
    @Test
    public void getAllArticles() {
        // given
        Page<Article> mockPage = mock(Page.class);
        when(blogRepository.findAll(any(Pageable.class))).thenReturn(mockPage);

        //when
        Page<Article> resultPage = blogService.findAll(Pageable.unpaged()); //페이징 처리하지 않음

        //then
        assertThat(resultPage).isNotNull();
    }


    @DisplayName("getById: id 조회에 성공한다.")
    @Test
    public void getById() throws Exception {
        //given
        long mockid = 1L;
        Article mockArticle = new Article("title", "content", "test@example.com", "010-1234-5678", "username", "password");
        when(blogRepository.findById(mockid)).thenReturn(Optional.of(mockArticle));

        //when
/*        AddArticleRequest request = new AddArticleRequest("title", "content", "test@example.com", "010-1234-5678", "username", "password");
        blogService.save(request);*/
        Article resultArticle = blogService.findById(mockid);

        //then
        assertThat(mockArticle).isNotNull();
        assertThat(mockArticle).isEqualTo(resultArticle);

    }

    @DisplayName("deleteArticle: 블로그 글 삭제에 성공한다.") //D
    @Test
    public void deleteArticle() throws Exception {
        // given
        long mockId = 1L;

        //when
        blogService.delete(mockId);

        //then
        verify(blogRepository, times(1)).deleteById(mockId);
    }

    @DisplayName("updateArticle: 블로그 글 수정에 성공한다.") //U
    @Test
    public void updateArticle() throws Exception {
        // given
        Long mockId = 1L;
        UpdateArticleRequest request = new UpdateArticleRequest("title1", "content", "test@update.com", "010-1234-5678", "userName", "password", "");
/*        Article mockArticle = Article.builder() //수정되기 전 기존 글
                            .title("title")
                            .content("content")
                            .email("test@example.com")
                            .phoneNumber("010-1234-5678")
                            .userName("userName")
                            .password("password")
                            .build();*/
        Article mockArticle = new Article("title", "content", "test@update.com", "010-1234-5678", "userName", "password");
        mockArticle.setCreatedAt(LocalDateTime.now()); //생성시간이 자동으로 반영되지 않아 수동으로 입력

        when(blogRepository.findById(mockId)).thenReturn(Optional.of(mockArticle));

        //when
        Article updateArticle = blogService.update(mockId, request);

        //then
        assertThat(updateArticle).isNotNull();
        assertEquals(request.getTitle(), updateArticle.getTitle());
        assertEquals(request.getContent(), updateArticle.getContent());
        assertEquals(request.getEmail(), updateArticle.getEmail());
        assertEquals(request.getPhoneNumber(), updateArticle.getPhoneNumber());
        assertEquals(request.getUserName(), updateArticle.getUserName());
        assertEquals(request.getPassword(), updateArticle.getPassword());

    }

/*
    @DisplayName("deleteArticle: 블로그 글 삭제 상태 변경에 성공한다.")
    @Test
    public void deleteChange() throws Exception {
        // given
        final String url = "/api/articles/{id}";

        final String title = "title";
        final String content = "content";
        final String email = "hyekjung@naver.com";
        final String phoneNumber = "010-0000-0000";
        final String userName = "혜정";
        final String password = "pW12345!@";

        Article savedArticle = blogRepository.save(Article.builder()
                .title(title)
                .content(content)
                .email(email)
                .phoneNumber(phoneNumber)
                .userName(userName)
                .password(password)
                .build());

        // when
        mockMvc.perform(delete(url, savedArticle.getId()))
                .andExpect(status().isOk());

        // then
        boolean isDelete = blogRepository.findById(savedArticle.getId()).get().isDeletedAt();
        assertTrue(isDelete);
    }

    @DisplayName("deleteArticle: 블로그 글 삭제에 성공한다.")
    @Test
    public void deleteArticle() throws Exception {
        // given
        final String url = "/api/articles/{id}/delete";
        final String title = "title";
        final String content = "content";
        final String email = "hyekjung@naver.com";
        final String phoneNumber = "010-0000-0000";
        final String userName = "혜정";
        final String password = "pW12345!@";

        Article savedArticle = blogRepository.save(Article.builder()
                .title(title)
                .content(content)
                .email(email)
                .phoneNumber(phoneNumber)
                .userName(userName)
                .password(password)
                .build());

        // when
        mockMvc.perform(delete(url, savedArticle.getId()))
                .andExpect(status().isOk());

        // then
        List<Article> articles = blogRepository.findAll();

        assertThat(articles).isEmpty();
    }*/
}