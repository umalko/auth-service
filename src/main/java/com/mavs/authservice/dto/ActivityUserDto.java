package com.mavs.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityUserDto implements Serializable {

    private Integer id;
    private String email;
    private String phone;
    private String username;
}
