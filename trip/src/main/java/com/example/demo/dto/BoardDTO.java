package com.example.demo.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString

public class BoardDTO {
    private int postId;
    private String title;
    private String content;
    private String author;
    private String password;
    private String createdAt;
    private String countryName;
    private int countryId;
    private List<Integer> tags;
    private List<String> tagNames;
    // 태그 이름을 콤마로 연결한 문자열 (화면 출력용)
    private String tagNamesStr;
}
