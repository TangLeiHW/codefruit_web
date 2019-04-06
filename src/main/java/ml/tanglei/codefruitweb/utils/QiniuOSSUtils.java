package ml.tanglei.codefruitweb.utils;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import ml.tanglei.codefruitweb.common.StaticParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;

/**
 * 七牛文件对象存储工具类
 */
public class QiniuOSSUtils {

    private final static Logger logger = LoggerFactory.getLogger(QiniuOSSUtils.class);

    /**
     * 上传文件到七牛
     * @param inputStream
     * @return
     */
    public static String uploadFile(InputStream inputStream) {
        String fileName = "";
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone0());
        UploadManager uploadManager = new UploadManager(cfg);
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = null;
        try {
            //校验
            Auth auth = Auth.create(StaticParam.QINIU_ACCESSKEY,StaticParam.QINIU_SECRETKEY);
            String upToken = auth.uploadToken(StaticParam.QINIU_BUCKET);
            //上传文件
            Response response = uploadManager.put(inputStream, key, upToken,null,null);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            fileName = putRet.hash;
        } catch (Exception e) {
            logger.error("文件上传失败",e);
        }
        return fileName;
    }

    /**
     * 删除文件
     * @param fileName
     */
    public static void removeFile(String fileName) {
        //构造一个带指定Zone对象的配置类
        Configuration cgf = new Configuration(Zone.zone0());
        //权限认证
        Auth auth = Auth.create(StaticParam.QINIU_ACCESSKEY,StaticParam.QINIU_SECRETKEY);
        BucketManager bucketManager = new BucketManager(auth,cgf);
        try {
            //删除文件
           bucketManager.delete(StaticParam.QINIU_BUCKET, fileName);
        } catch (QiniuException e) {
            logger.error("删除七牛云文件失败，错误码：" + e.code() + ",错误原因：" + e.response.toString());
        }
    }

    public static void main(String[] args) {
        //File file = new File("C:\\Users\\TangLei\\Desktop\\paascloud-project.png");
        //System.out.println(uploadFile(file));
        removeFile("FiU7trNaP8MWRYSUG6kQtq4NbqhS");
    }

}
