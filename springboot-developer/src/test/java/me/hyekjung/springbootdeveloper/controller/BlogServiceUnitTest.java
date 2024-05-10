package me.hyekjung.springbootdeveloper.controller;

import me.hyekjung.springbootdeveloper.domain.Article;
import me.hyekjung.springbootdeveloper.dto.AddArticleRequest;
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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(MockitoExtension.class)
class BlogServiceUnitTest {

    @Mock
    protected BlogRepository blogRepository;

    @InjectMocks
    protected BlogService blogService;

    @DisplayName("saveArticle: 블로그 글 저장에 성공한다.")
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

    @DisplayName("getAllArticles: 블로그 글 목록 조회에 성공한다.")
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
/*
    @DisplayName("addArticle: 블로그 글 추가에 성공한다.")
    @Test
    public void addArticle() throws Exception {
        // given
        final String url = "/api/articles";
        final String title = "title";
        final String content = "content";
        final String email = "hyekjung@naver.com";
        final String phoneNumber = "010-0000-0000";
        final String userName = "혜정";
        final String password = "pW12345!@";

        final AddArticleRequest userRequest = new AddArticleRequest(title, content, email, phoneNumber, userName, password);

        final String requestBody = objectMapper.writeValueAsString(userRequest);

        // when
        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        // then
        result.andExpect(status().isCreated());

        List<Article> articles = blogRepository.findAll();

        assertThat(articles.size()).isEqualTo(1);
        assertThat(articles.get(0).getTitle()).isEqualTo(title);
        assertThat(articles.get(0).getContent()).isEqualTo(content);
        assertThat(articles.get(0).getEmail()).isEqualTo(email);
        assertThat(articles.get(0).getPhoneNumber()).isEqualTo(phoneNumber);
        assertThat(articles.get(0).getUserName()).isEqualTo(userName);
        assertThat(articles.get(0).getPassword()).isEqualTo(password);
        assertThat(articles.get(0).getCreatedAt()).isNotNull();
    }

    @DisplayName("updateArticle: 블로그 글 수정에 성공한다.")
    @Test
    public void updateArticle() throws Exception {
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

        final String newTitle = "new title";
        final String newContent = "new content";
        final String newEmail = "hyekjung@naver.com";
        final String newPhoneNumber = "010-0000-0000";
        final String newUser = "혜정";
        final String newPassword = "pW12345!@";

        UpdateArticleRequest request = new UpdateArticleRequest(newTitle, newContent, newEmail, newPhoneNumber,
                newUser, newPassword, savedArticle.getMessage());

        // when
        ResultActions result = mockMvc.perform(put(url, savedArticle.getId()).contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result.andExpect(status().isOk());

        Article article = blogRepository.findById(savedArticle.getId()).get();

        assertThat(article.getTitle()).isEqualTo(newTitle);
        assertThat(article.getContent()).isEqualTo(newContent);
    }

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