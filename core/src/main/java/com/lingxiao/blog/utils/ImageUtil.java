package com.lingxiao.blog.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.Map;

/**
 * @author Admin
 */
public class ImageUtil {
    public static boolean downloadImageWithHeaders(String imageUrl,String formatName, File localFile, Map<String, String> headers){
        InputStream stream = null;
        try {
            if (!localFile.exists()) {
                localFile.createNewFile();
                //Files.createFile(localFile.toPath());
            }
            URL url = new URL(imageUrl);
            URLConnection conn = url.openConnection();
            //设置头信息
            if (headers != null && !headers.isEmpty()) {
                headers.forEach(conn::setRequestProperty);
            }
            conn.setDoInput(true);
            stream = conn.getInputStream();
            BufferedImage bufferedImg = ImageIO.read(stream);
            if (bufferedImg != null) {
                ImageIO.write(bufferedImg, formatName, localFile);
                return true;
            } else {
                throw new RuntimeException("图片[$imageUrl]下载失败");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
}
