package com.xbaimiao.bansetop;

import java.io.*;

/**
 * @Author xbaimiao
 * @Date 2021/11/7 13:25
 */
public class IO {

    public static void saveResource(String name, String path) {
        File file = new File(path);
        if (file.getParentFile() != null && !file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            toFile(file, ChangeClass.class.getClassLoader().getResourceAsStream(name));
        }
    }

    public static void toFile(File file, InputStream inputStream) {
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];

            int len;
            while ((len = inputStream.read(buf)) > 0) {
                out.write(buf, 0, len);
            }

            out.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
