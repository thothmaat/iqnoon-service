package com.thoth.iqnoon.enums;



public enum GenderEnum {

    FEMALE("FEMALE","女"),
    MALE("MALE","男"),
    UNKNOWN("UNKNOWN","保密");

    public final String code;
    public final String description;

    GenderEnum(String code,String description){
        this.code = code;
        this.description = description;
    }

    public static String getDescription(String code){
        for (GenderEnum gender : GenderEnum.values()) {
            if(gender.code.equals(code)){
                return gender.description;
            }
        }
        return null;
    }
}
