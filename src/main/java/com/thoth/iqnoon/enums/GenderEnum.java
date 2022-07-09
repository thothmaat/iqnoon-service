package com.thoth.iqnoon.enums;

import org.apache.commons.lang.ObjectUtils;

public enum GenderEnum {

    FEMALE(0,"女"),
    MALE(1,"男");

    private Integer code;
    private String description;

    GenderEnum(Integer code,String description){
        this.code = code;
        this.description = description;
    }

    public static String getDescription(Integer code){
        for (GenderEnum gender : GenderEnum.values()) {
            if(ObjectUtils.equals(gender.code,code)){
                return gender.description;
            }
        }
        return null;
    }
}
