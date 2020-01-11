package com.example.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@RedisHash("CustomerAccount")
public class CustomerAccount implements Serializable {

    @Id
    private Long id;
    @NotBlank(message = "Name can not empty")
    private String name;
    @NotBlank(message = "Email can not be empty")
    @Email(message = "Email address not valid")
    private String email;

    public CustomerAccount(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
