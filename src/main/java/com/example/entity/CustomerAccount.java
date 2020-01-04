package com.example.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@RedisHash("CustomerAccount")
public class CustomerAccount implements Serializable {

    @Id
    private Long id;
    private String name;
    private String email;

    public CustomerAccount(String name, String email) {
        this.name = name;
        this.email = email;
    }

    @Override
    public String toString() {
        return "{" +
                    "id: " + id + ", " +
                    "name: " + name + ", " +
                    "email: " + email +
                "}";
    }
}
