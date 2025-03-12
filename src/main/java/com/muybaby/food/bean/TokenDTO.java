package com.muybaby.food.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenDTO {
    private Integer userId;
    private String username;
    private String role;
    private String token;


}