package com.example.homemade_guardian_beta.chat.model;

import java.util.HashMap;
import java.util.Map;

//이 모델은 파일만 사용하는건가?
public class ChatModel {
    public Map<String, String> users = new HashMap<>() ; //이거 뭘 뜻하는지 모륵겠음
    public Map<String, String> messages = new HashMap<>() ; //이거 사용안하는건가?

    public static class FileInfo {
        public String FileName;
        public String FileSize;
    }
}
