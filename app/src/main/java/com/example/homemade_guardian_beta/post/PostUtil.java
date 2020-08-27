package com.example.homemade_guardian_beta.post;

import android.app.Activity;
import android.util.Patterns;
import android.widget.Toast;
import java.net.URLConnection;

//BaseActivity와 같은 개념으로 여러군데에서 사용하는 특정 함수들을 모아서 선언해 놓은 Class이다.
//      Ex) 가장 많이 사용되는 것은 토스트를 생성하는 메소드이지만, isStorageUrl에서 파이어베이스의 스토리지 경로를 저장한 부분이 중요한 곳이다.

public class PostUtil {                                                                                     // part18 :  중복되는 코드 빼기 (7'30")
    public PostUtil(){/* */}

    public static final String INTENT_PATH = "path";
    public static final String INTENT_MEDIA = "media";
    public static final int GALLERY_IMAGE = 0;
    public static final int GALLERY_VIDEO = 1;

    public static void showToast(Activity activity, String Msg){ Toast.makeText(activity, Msg, Toast.LENGTH_SHORT).show(); }

    public static boolean isStorageUrl(String URL){ return Patterns.WEB_URL.matcher(URL).matches() && URL.contains("https://firebasestorage.googleapis.com/v0/b/homemade-guardian-beta.appspot.com/o/POSTS"); }
    // part17 : 스토리지 주소에 대한 등록 [SHA1코드같은..] (16'45")

    public static String storageUrlToName(String URL){ return URL.split("\\?")[0].split("%2F")[URL.split("\\?")[0].split("%2F").length - 1]; }
}

