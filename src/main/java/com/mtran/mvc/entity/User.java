package com.mtran.mvc.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_iam")
@RequiredArgsConstructor
@Data
@Getter
@Setter
public class User {
    @Id
    @Column(name = "id",nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "email",nullable = false,columnDefinition = "VARCHAR(100)")
    private String email;
    @Column(name = "password",nullable = false,columnDefinition = "VARCHAR(100)")
    private String password;
    @Column(name = "name",nullable = false,columnDefinition = "VARCHAR(100)")
    private String name;
    @Column(name = "phonenumber",nullable = false,columnDefinition = "VARCHAR(15)")
    private String phoneNumber;
    @Column(name = "dob",nullable = false)
    private LocalDateTime dob;
    @Column(name = "address",nullable = true,columnDefinition = "VARCHAR(200)")
    private String address;
    @Column(name = "image_url", nullable = true, columnDefinition = "VARCHAR(255)")
    private String imageUrl;
    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private String createdBy;
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdDate;
    @LastModifiedBy
    @Column(name = "updated_by")
    private String updatedBy;
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedDate;
}
