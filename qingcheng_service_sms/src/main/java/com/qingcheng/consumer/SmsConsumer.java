package com.qingcheng.consumer;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.CommonResponse;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;




public class SmsConsumer implements MessageListener {
    @Autowired
    private SmsUtil smsUtil;

    @Value("${smsTemplate}")
    private String smsTemplate;

    @Value("${smsCode}")
    private String code;

    @Override
    public void onMessage(Message message) {
        String s = new String(message.getBody());
        Map<String,String> map = JSON.parseObject(s, Map.class);
        String phone = map.get("phone");
        String smsCode = map.get("code");
        System.out.println("接受队列手机号： "+phone+", 验证码"+smsCode);
        //调用阿里云通信
        CommonResponse commonResponse = smsUtil.sendSms(phone, smsTemplate, code.replace("[value]", smsCode));
        if (commonResponse==null)
            throw new RuntimeException("短信发送失败");
    }
}
