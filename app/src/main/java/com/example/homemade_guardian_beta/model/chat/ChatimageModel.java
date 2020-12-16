package com.example.homemade_guardian_beta.model.chat;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

//사진
public class ChatimageModel implements Serializable {
    public Map<String, String> users = new HashMap<>() ; //이거 뭘 뜻하는지 모륵겠음

    public static class FileInfo {
        public String FileName;
        public String FileSize;
    }
}
