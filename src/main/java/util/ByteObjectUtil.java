package util;


import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;

import java.io.*;
import java.util.logging.Logger;

/**
 * @version 1.0
 * @ClassName ByteObjectUtil
 * @Description: TODO
 * @Auther: lvwei
 * @Date: 2018/11/14 17:25
 */
public class ByteObjectUtil {


    private static final Logger logger = Logger.getLogger(ByteObjectUtil.class.getName());
    public static byte[] writeInto(Object obj) {
        ByteArrayOutputStream bos = null;
        ObjectOutputStream oos = null;
        try {
            bos = new ByteArrayOutputStream();

            oos = new ObjectOutputStream(bos);
            //读取对象并转换成二进制数据
            oos.writeObject(obj);
            return bos.toByteArray();
        } catch (IOException e) {
            logger.warning("对象转换成二级制数据失败, {}"+ e);
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static Object restore(byte[] b) {
        ByteArrayInputStream bis = null;
        ObjectInputStream ois = null;
        try {
            //读取二进制数据并转换成对象
            bis = new ByteArrayInputStream(b);
            ois = new ObjectInputStream(bis);
            return ois.readObject();
        } catch (Exception e) {
            logger.warning("二进制数据转回对象失败, {}"+e);
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

}



