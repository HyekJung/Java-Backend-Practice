package me.hyekjung.springbootdeveloper.controller;

import me.hyekjung.springbootdeveloper.domain.Article;
import me.hyekjung.springbootdeveloper.dto.AddArticleRequest;
import me.hyekjung.springbootdeveloper.dto.UpdateArticleRequest;
import me.hyekjung.springbootdeveloper.repository.BlogRepository;
import me.hyekjung.springbootdeveloper.service.BlogService;
import org.junit.jupiter.api.Assertions;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

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
        when(blogRepository.save(any())).thenReturn(request.toEntity());

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
        Page<Article> resultPage = blogService.getAll(Pageable.unpaged()); //페이징 처리하지 않음

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
        Article resultArticle = blogService.getById(mockid);

        //then
        assertThat(mockArticle).isNotNull();
        assertThat(mockArticle).isEqualTo(resultArticle);

    }
    @DisplayName("getByIdNotFound: id 조회에 실패한다.")
    @Test
    public void getByIdNotFound() throws Exception {
        //given
        long mockid = 2L;
        when(blogRepository.findById(mockid)).thenReturn(Optional.empty());

        //when
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                blogService.getById(mockid));

        //then
        assertEquals("not found : " + mockid, exception.getMessage());
<<<<<<< HEAD
=======
    }

    @DisplayName("deleteChange: 블로그 글 삭제 상태 변경에 성공한다.") //D
    @Test
    public void deleteChange() throws Exception {
        // given
        long mockId = 1L;
        Article mockArticle = new Article("title", "content", "test@example.com", "010-1234-5678", "username", "password");
        when(blogRepository.findById(mockId)).thenReturn(Optional.of(mockArticle));

        //when
        blogService.deleteChange(mockId);

        //then
        assertTrue(mockArticle.isDeletedAt());
    }

    @DisplayName("deleteChangeNotFound: 블로그 글 삭제 상태 변경에 실패한다.") //D
    @Test
    public void deleteChangeNotFound() throws Exception  {
        // given
        long mockId = 2L;
        when(blogRepository.findById(mockId)).thenReturn(Optional.empty());

        //when
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                blogService.deleteChange(mockId));

        //then
        assertEquals("id를 찾을 수 없습니다." + mockId, exception.getMessage());
>>>>>>> a0b5677f8bd6aa7dc608892a3d0d8b6dd9166e17
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
        assertEquals("성공적으로 수정되었습니다.", updateArticle.getMessage());
    }
}