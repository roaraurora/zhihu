package com.zhihu.demo.util;


import javax.imageio.ImageIO;
import java.awt.*;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;


/**
 * @author 邓超
 * @description 通用工具类
 * @create 2018/9/19
 */
public class GeneralUtils {
	
    /**
     * 数组是否为空
     * @param t
     * @param <T>
     * @return
     */
    public static <T> boolean isEmpty(T[] t){
        return t == null || t.length == 0;
    }

    /**
     * 获取32位无符号小写UUID
     * @return
     */
    public static String getLowerUUID(){
        return UUID.randomUUID().toString().replace("-","").toLowerCase();
    }
    

    /**
     * 对字符串进行Base64编码
     * @param data
     * @return
     */
    public static String base64Encode(String data){
        return new String(Base64.getEncoder().encode(data.getBytes(StandardCharsets.UTF_8)),StandardCharsets.UTF_8);
    }
	 /**
     * 判断文件是否是图片文件
     * @param inputStream
     * @return
     */
    public static boolean isImage(InputStream inputStream) {
		if (inputStream == null) {
            return false;
        }
        Image image;
        try {
        	image = ImageIO.read(inputStream);
            return !(image == null || image.getWidth(null) <= 0 || image.getHeight(null) <= 0);
        } catch (Exception e) {
            return false;
        }
	}
}
