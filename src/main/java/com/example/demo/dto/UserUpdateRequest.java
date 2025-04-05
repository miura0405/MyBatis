package com.example.demo.dto;

import java.io.Serializable;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;


import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * ユーザー情報更新リクエストデータ
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UserUpdateRequest extends UserAddRequest implements Serializable {

    /**
     * ユーザーID
     */
    @NotNull
    private Long id; // Long から Integer に修正
}