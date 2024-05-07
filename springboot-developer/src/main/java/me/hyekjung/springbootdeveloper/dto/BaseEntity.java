<<<<<<< HEAD
package me.hyekjung.springbootdeveloper.dto;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass // 상속 시 컬럼으로 인식A
@EntityListeners(AuditingEntityListener.class) //생성, 수정 시간 자동으로 반영하도록 설정
public abstract class BaseEntity {

    @CreatedDate //생성
    private LocalDateTime createdAt;
    @LastModifiedDate //수정
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private boolean deletedAt; //삭제 여부

}
=======
package me.hyekjung.springbootdeveloper.dto;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass // 상속 시 컬럼으로 인식A
@EntityListeners(AuditingEntityListener.class) //생성, 수정 시간 자동으로 반영하도록 설정
public abstract class BaseEntity {

    @CreatedDate //생성
    private LocalDateTime createdAt;
    @LastModifiedDate //수정
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private boolean deletedAt; //삭제 여부

}
>>>>>>> 90b5f7648b73c14a1cbdac4e4f7dab41bbe1ee8a
