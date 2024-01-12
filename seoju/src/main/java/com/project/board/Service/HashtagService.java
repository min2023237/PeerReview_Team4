package com.project.board.Service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class HashtagService {
    // 단일 게시물 내 해시태그 추출
    public List<String> getHashtags(String content) {
        List<String> hashtags = new ArrayList<>();
        Pattern pattern = Pattern.compile("#\\w+");
        Matcher matcher = pattern.matcher(content);

        while (matcher.find()) {
            hashtags.add(matcher.group());
        }

        return hashtags;
    }
}
