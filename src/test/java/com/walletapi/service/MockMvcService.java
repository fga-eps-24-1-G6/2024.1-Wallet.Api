package com.walletapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@Service
public class MockMvcService {

    @Autowired
    private MockMvc mockMvc;

    public ResultActions post(String url, String requestBody) throws Exception {
        return mockMvc
                .perform(
                        org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody));
    }

    public ResultActions post(String url, HttpHeaders headers, String requestBody) throws Exception {
        return mockMvc
                .perform(
                        org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post(url)
                                .headers(headers)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody));
    }

    public ResultActions post(String url, HttpHeaders headers) throws Exception {
        return mockMvc
                .perform(
                        org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post(url)
                                .headers(headers)
                                .contentType(MediaType.APPLICATION_JSON));
    }

    public ResultActions put(String url, HttpHeaders headers, String requestBody) throws Exception {
        return mockMvc
                .perform(
                        org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put(url)
                                .headers(headers)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody));
    }

    public ResultActions get(String url, HttpHeaders headers) throws Exception {
        return mockMvc
                .perform(
                        org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get(url)
                                .headers(headers));
    }

    public ResultActions get(String url) throws Exception {
        return mockMvc
                .perform(
                        org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get(url));
    }


    public ResultActions patch(String url, HttpHeaders headers, String requestBody) throws Exception {
        return mockMvc
                .perform(
                        org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch(url)
                                .headers(headers)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody));
    }

    public ResultActions delete(String url) throws Exception {
        return mockMvc
                .perform(
                        org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete(url));
    }

    public ResultActions postWithRefererHeader(String url, String requestBody, String headerName, String headerValue) throws Exception {
        return mockMvc
                .perform(
                        org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(headerName, headerValue)
                                .content(requestBody));
    }

    public ResultActions multipart(String url, HttpHeaders headers, MockMultipartFile file) throws Exception {
        return mockMvc
                .perform(
                        MockMvcRequestBuilders.multipart(url)
                                .file(file)
                                .headers(headers)
                                .content(file.toString()));
    }
}
