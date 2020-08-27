package com.example.homemade_guardian_beta.chat.common;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import java.text.SimpleDateFormat;
import java.util.Date;

//자주사용하는 함수를 Util로 묶어둠
public class ChatUtil {
    private static final ChatUtil ourInstance = new ChatUtil();

    static ChatUtil getInstance() {
        return ourInstance;
    }

    private ChatUtil() {
    }

    //토스트띄울떄 사용하는 함수 showMessage(어디다가 띄울지, 뭐라고 띄울지)
    public static void showMessage(Context context, String msg) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    //edittext를 포커스 할때 사용하는 함수
    //https://satisfactoryplace.tistory.com/9 참고
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    //파일의 시간을 출력하는 함수
    public static String getUniqueValue() {
        SimpleDateFormat ft = new SimpleDateFormat("yyyyMMddhhmmssSSS");
        return ft.format(new Date()) + (int) (Math.random()*10);
    }

    //파일의 크기를 1024bite로 맞춤
    public static String size2String(Long filesize) {
        Integer unit = 1024;
        if (filesize < unit){
            return String.format("%d bytes", filesize);
        }
        int exp = (int) (Math.log(filesize) / Math.log(unit));

        return String.format("%.0f %sbytes", filesize / Math.pow(unit, exp), "KMGTPE".charAt(exp-1));
    }

    //디렉토리를 찾는 경로 함수
    public static String getRootPath() {
        String sdPath;
        String ext1 = Environment.getExternalStorageState();
        if (ext1.equals(Environment.MEDIA_MOUNTED)) {
            sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            sdPath = Environment.MEDIA_UNMOUNTED;
        }
        return sdPath;
    }

    //퍼미션 체크 함수 (onPermissionGranted이거랑 비슷한듯??)
    public  static boolean isPermissionGranted(Activity activity, String permission) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (activity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
                Log.v("태그","Permission is granted");
                return true;
            } else {
                Log.v("태그","Permission is revoked");
                ActivityCompat.requestPermissions(activity, new String[]{permission}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("태그","Permission is granted");
            return true;
        }
    }
}
