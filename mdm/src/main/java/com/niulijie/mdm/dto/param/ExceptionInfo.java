package com.niulijie.mdm.dto.param;


import lombok.Data;

@Data
public class ExceptionInfo {

    private String declaringClass;
    private String methodName;
    private String fileName;
    private int    lineNumber;
    private String errorInfo;
}
