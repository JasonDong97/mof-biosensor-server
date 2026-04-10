package com.miqroera.biosensor.domain.constants;

import lombok.Getter;

@Getter
public enum UploadType {

    AVATAR("用户头像", "/avatar"),
    COMMON("通用文件", "/common"),
    ;

    private final String name;

    private final String path;

    UploadType(String name, String path) {
        this.name = name;
        this.path = path;
    }

}
