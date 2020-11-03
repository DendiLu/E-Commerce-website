package com.qingcheng.consumer;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.qingcheng.IndexUtil.EsHandler;
import com.qingcheng.pojo.goods.Goods;
import com.qingcheng.pojo.goods.Sku;
import com.qingcheng.pojo.goods.Spu;
import com.qingcheng.service.goods.SpuService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

public class UpdateIndex implements MessageListener {
    @Reference
    SpuService spuService;
    @Autowired
    EsHandler esHandler;
    @Override
    public void onMessage(Message message) {
        String s = new String(message.getBody());
        Map<String,String> map = JSON.parseObject(s, Map.class);
        String spu = null;
        if((spu=map.get("create"))!=null){
            //查询商品信息
            Goods goodsById = spuService.findGoodsById(spu);

            //获取sku列表
            List<Sku> skuList = goodsById.getSkuList();
            esHandler.insertIndex(skuList);

        }
    }
}
