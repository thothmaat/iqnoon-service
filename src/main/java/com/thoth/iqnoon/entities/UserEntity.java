package com.thoth.iqnoon.entities;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@TableName("t_user")
public class UserEntity {

    @TableId
    private String userId;
    private String userName;
    private String password;
    private String telephone;
    private String email;
    private String userStatus;
    private String idCardNumber;
    private String weChat;
    private String gender;
    private Integer age;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    private String createBy;
    private String updateBy;
    private String deleteFlag;

}
