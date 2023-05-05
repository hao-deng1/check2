package com.lostAndFind.project.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface LoginService {
    void getAuthCode(HttpServletRequest request, HttpServletResponse response) throws IOException;

    Boolean verifyCode(String inputVerify, HttpServletRequest request);
}
