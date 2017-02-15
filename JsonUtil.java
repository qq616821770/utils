package com.pingan.pinganwifiboss.util;

import com.google.gson.Gson;
import com.pingan.pinganwifiboss.view.activity.PicSelectActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import cn.core.log.Lg;
import cn.core.net.http.ServiceResponse;

public class JsonUtil {

    public static JSONObject resolveJson(String param) {
        try {
            JSONObject json = new JSONObject(param);
            return json;
        } catch (JSONException e) {
            Lg.d(param + "json 解析出错" + e);
            return null;
        }
    }

    public static void saveJson(ServiceResponse response, String fileName) {
        File file = new File(PicSelectActivity.FILE_SAVEPATH + fileName);

        if (isExists(fileName) && !isOverdue(fileName)) {
            return;
        }
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
            objectOutputStream.writeObject(response);
            objectOutputStream.flush();
            objectOutputStream.close();
        } catch (FileNotFoundException e) {
           Lg.w(e);
        } catch (IOException e) {
           Lg.w(e);
        } finally {

        }
    }

    public static ServiceResponse getJson(String fileName) {
        File file = new File(PicSelectActivity.FILE_SAVEPATH + fileName);
        if (!file.exists()) return null;
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
            return (ServiceResponse) objectInputStream.readObject();
        } catch (IOException e) {
           Lg.w(e);
        } catch (ClassNotFoundException e) {
           Lg.w(e);
        }
        return null;
    }

    public static boolean deleteJson(String fileName) {
        File file = new File(PicSelectActivity.FILE_SAVEPATH + fileName);
        if (!file.exists()) return true;

        return file.delete();
    }

    private static boolean isExists(String fileName) {
        //判断本地是否有此json文件
        File file = new File(PicSelectActivity.FILE_SAVEPATH + fileName);
        return file.exists();
    }

    private static boolean isOverdue(String fileName) {
        //超过一天属于过期
        File file = new File(PicSelectActivity.FILE_SAVEPATH + fileName);

        long currentTimeMillis = System.currentTimeMillis();
        long makeFileTime = file.lastModified();
        if ((currentTimeMillis - makeFileTime) > 86400000) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 对象转json
     *
     * @param obj
     * @return
     * @author chenlong
     */
    public static String toJson(Object obj) {
        String json = null;
        if (null == obj) {
            return json;
        }
        try {
            Gson gson = new Gson();
            json = gson.toJson(obj);
        } catch (Exception e) {
            Lg.e(e);
        }
        return json;
    }
}
