package com.qingcheng.consumer;

import com.alibaba.fastjson.JSON;
import com.qingcheng.PageUtil.PageHandler;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class DeletePage implements MessageListener {
    @Autowired
    PageHandler pageHandler;

    @Override
    public void onMessage(Message message) {
        String s = new String(message.getBody());
        Map<String,String> map = JSON.parseObject(s, Map.class);
        String spu = null;
        if((spu=map.get("delete"))!=null){
            pageHandler.deletePage(spu);
        }
    }
}
