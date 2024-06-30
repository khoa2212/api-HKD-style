package com.example.user_service.entity;

import com.example.user_service.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "url_tokens")
public class URLToken extends BaseEntity {
    @Column(nullable = false)
    private String tokenValue;
    @Column(nullable = false)
    private LocalDateTime expireAt;
    @Enumerated(EnumType.STRING)
    private URLTokenType tokenType;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
