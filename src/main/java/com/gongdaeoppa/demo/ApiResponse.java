package com.gongdaeoppa.demo;

public class ApiResponse {
    
    public int status;
    public String message;
    
    public ApiResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }
    
}
