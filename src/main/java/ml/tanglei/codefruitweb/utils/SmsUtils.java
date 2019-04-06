package ml.tanglei.codefruitweb.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import ml.tanglei.codefruitweb.common.StaticParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 阿里大于短信工具类
 */
public class SmsUtils {

    private final static Logger logger = LoggerFactory.getLogger(SmsUtils.class);

    /**
     * 发送消息
     * @param phoneNum
     * @param templateCode 短信模板编号
     * @param templateParam "{\"name\":\"Tom\", \"code\":\"123\"}"
     * @return
     */
    public static boolean sendMsg(String phoneNum, String templateCode,
                                  String templateParam){
        boolean result = false;
        try {
            //可自助调整超时时间
            System.setProperty(StaticParam.ALIDY_DEFAULTCONNECTTIMEOUT,StaticParam.ALIDY_TIMEOUT);
            System.setProperty(StaticParam.ALIDY_DEFAULTREADTIMEOUT,StaticParam.ALIDY_TIMEOUT);
            //初始化acsClient，暂时不支持多region
            IClientProfile profile = DefaultProfile.getProfile(StaticParam.ALIDY_REGION,StaticParam.QINIU_ACCESSKEY,
                    StaticParam.ALIDY_ACCESSKEYSECRET);
            DefaultProfile.addEndpoint(StaticParam.ALIDY_REGION,StaticParam.ALIDY_REGION,StaticParam.ALIDY_PRODUCT,
                    StaticParam.ALIDY_DOMAIN);
            IAcsClient acsClient = new DefaultAcsClient(profile);

            //组装请求对象
            SendSmsRequest request = new SendSmsRequest();
            //使用post提交
            request.setMethod(MethodType.POST);
            //必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,
            // 批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式；发送国际/港澳台消息时，
            // 接收号码格式为国际区号+号码，如“85200000000”
            request.setPhoneNumbers(phoneNum);
            //必填:短信签名-可在短信控制台中找到
            request.setSignName(StaticParam.ALIDY_SIGNNAME);
            //必填:短信模板-可在短信控制台中找到，发送国际/港澳台消息时，请使用国际/港澳台短信模版
            request.setTemplateCode(StaticParam.ALIDY_REGISTTEMPLETECODE);
            //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
            //友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,
            // 比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
            request.setTemplateParam(templateParam);
            //可选-上行短信扩展码(扩展码字段控制在7位或以下，无特殊需求用户请忽略此字段)
            //request.setSmsUpExtendCode("90997");
            //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
            //request.setOutId("yourOutId");
            //请求失败这里会抛ClientException异常
            SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
            if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
                result = true;
            }
        } catch (Exception e) {
            logger.error("发送短信失败",e);
        }
        return result;
    }
}
