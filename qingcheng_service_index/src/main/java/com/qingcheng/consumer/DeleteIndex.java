package com.qingcheng.consumer;

import com.alibaba.fastjson.JSON;
import com.qingcheng.IndexUtil.EsHandler;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class DeleteIndex implements MessageListener {
    @Autowired
    EsHandler esHandler;
    @Override
    public void onMessage(Message message) {
        String s = new String(message.getBody());
        Map<String,String> map = JSON.parseObject(s, Map.class);
        String spu = null;
        if((spu=map.get("delete"))!=null){
            esHandler.deleteIndex(spu);
        }

    }
}
