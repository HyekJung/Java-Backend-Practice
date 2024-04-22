package me.hyekjung.springbootdeveloper.dto;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass // 상속 시 컬럼으로 인식
@EntityListeners(AuditingEntityListener.class) //생성, 수정 시간 자동으로 반영하도록 설정
public abstract class BaseEntity {
    //@FutureOrPresent

    @CreatedDate //생성
    private LocalDateTime createdAt;
    @LastModifiedDate //수정
    private LocalDateTime updatedAt;

//    //@NotNull
//    private LocalDateTime createdAt;
//    //@NotNull
//    private LocalDateTime updatedAt;
}
