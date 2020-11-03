package com.qingcheng.consumer;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.qingcheng.PageUtil.PageHandler;
import com.qingcheng.service.goods.SkuService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class CreatePage implements MessageListener {

    @Autowired
    PageHandler pageHandler;

    @Reference
    SkuService skuService;

    @Override
    public void onMessage(Message message) {
        String s = new String(message.getBody());
        Map<String, String> map = JSON.parseObject(s,Map.class);
        String sku = null;
        String spu = null;

        if((spu=map.get("create"))!=null){
            pageHandler.createPage(spu);
        }

    }
}
