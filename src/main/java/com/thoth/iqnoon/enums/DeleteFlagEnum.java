package com.thoth.iqnoon.enums;


public enum DeleteFlagEnum {

    NOT_DELETE("0","未删除"),
    DELETED("1","已删除");

    public final String code;
    public final String description;

    DeleteFlagEnum(String code, String description){
        this.code = code;
        this.description = description;
    }

    public static String getDescription(String code){
        for (DeleteFlagEnum gender : DeleteFlagEnum.values()) {
            if(gender.code.equals(code)){
                return gender.description;
            }
        }
        return null;
    }
}
