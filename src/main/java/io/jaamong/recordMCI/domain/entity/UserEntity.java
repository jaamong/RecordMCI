package io.jaamong.recordMCI.domain.entity;

import io.jaamong.recordMCI.domain.dto.Users;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity {

    @Column(name = "users_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;  // encryption store

    @Builder
    public UserEntity(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "username='" + username + '\'' +
                ", id=" + id +
                '}';
    }

    public Users toModel() {
        return Users.builder()
                .id(id)
                .username(username)
                .build();
    }
}
