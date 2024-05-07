package me.hyekjung.springbootdeveloper.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.hyekjung.springbootdeveloper.domain.Article;
import me.hyekjung.springbootdeveloper.dto.AddArticleRequest;
import me.hyekjung.springbootdeveloper.dto.UpdateArticleRequest;
import me.hyekjung.springbootdeveloper.repository.BlogRepository;
import me.hyekjung.springbootdeveloper.service.BlogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class BlogApiControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    BlogRepository blogRepository;

    @BeforeEach
    public void mockMvcSetUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
        blogRepository.deleteAll();
    }

    @DisplayName("getAllArticles: 블로그 글 목록 조회에 성공한다.")
    @Test
    public void getAllArticles() throws Exception {
        // given
        final String url = "/api/articles";
        final String title = "title";
        final String content = "content";
        final String email = "hyekjung@naver.com";
        final String phoneNumber = "010-0000-0000";
        final String userName = "혜정";
        final String password = "pW12345!@";

        blogRepository.save(Article.builder() //글 등록
                .title(title)
                .content(content)
                .email(email)
                .phoneNumber(phoneNumber)
                .userName(userName)
                .password(password)
                .build());

        // when
        final ResultActions resultActions = mockMvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value(content))
                .andExpect(jsonPath("$[0].title").value(title))
                .andExpect(jsonPath("$[0].content").value(content))
                .andExpect(jsonPath("$[0].email").value(email))
                .andExpect(jsonPath("$[0].phoneNumber").value(phoneNumber))
                .andExpect(jsonPath("$[0].userName").value(userName))
                .andExpect(jsonPath("$[0].password").value(password))
                .andExpect(jsonPath("$[0].createdAt").exists()); //생성시간이랑 일치하는지 정확히 비교가 어려워서
    }

    @DisplayName("getPageAllArticles: 블로그 글 페이징 목록 조회에 성공한다.")
    @Test
    public void getPageAllArticles() throws Exception {
        // given
//        final String url = "/api/articles?page=0&size=10&sortName=ASC";
        final String url = "/api/articles";

        final int page = 0;
        final int size = 10;
        final String sortName = "ASC";

        final String title = "title";
        final String content = "content";
        final String email = "hyekjung@naver.com";
        final String phoneNumber = "010-0000-000";
        final String userName = "혜정";
        final String password = "pW12345!@";

        //List<Article> savedArticle = new ArrayList<>(); // 글 목록 저장

        // 글 목록 생성 - 페이징 확인용 20개
        for(int i = 0; i < size * 2; i++){
            blogRepository.save(Article.builder() //글 등록
                    .title(title + String.valueOf(i))
                    .content(content + String.valueOf(i))
                    .email(email)
                    .phoneNumber(phoneNumber + String.valueOf(i))
                    .userName(userName + String.valueOf(i))
                    .password(password)
                    .build());
        }

        // when
        final ResultActions resultActions = mockMvc.perform(get(url)
                .param("page", String.valueOf(page)) //페이지 번호, 크기 전달
                .param("size", String.valueOf(size))
                .param("sortName", sortName)
                .accept(MediaType.APPLICATION_JSON));

        //System.out.println("url값을 찾아서: " + url);

        // then
        resultActions
                .andExpect(status().isOk());
//                .andExpect(jsonPath("$.content", hasSize(size*2)))
//                .andExpect(jsonPath("$.content").isArray()) // 목록(배열) 확인
//                .andExpect(jsonPath("$[0].title").value(title + "0")) // 페이지 번호
//                .andExpect(jsonPath("$[0].content").value(content + "0")); // 페이지 크기
    }

    @DisplayName("getArticle: 블로그 글 조회에 성공한다.")
    @Test
    public void getArticle() throws Exception {
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
        final ResultActions resultActions = mockMvc.perform(get(url, savedArticle.getId()));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value(content))
                .andExpect(jsonPath("$.title").value(title))
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.phoneNumber").value(phoneNumber))
                .andExpect(jsonPath("$.userName").value(userName))
                .andExpect(jsonPath("$.password").value(password))
                .andExpect(jsonPath("$.createdAt").exists());
    }

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
    }
}