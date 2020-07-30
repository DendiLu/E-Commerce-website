package com.qingcheng.service.goods;
import com.qingcheng.entity.PageResult;
import com.qingcheng.pojo.goods.Sku;

import java.util.*;

/**
 * sku业务逻辑层
 */
public interface SkuService {

    public void saveAllPriceToRedis();

    public void savePriceToRedisById(String id,Integer price);


    public void deletePriceFromRedis(String skuId);

    public Integer findPrice(String skuId);


    public List<Sku> findAll();


    public PageResult<Sku> findPage(int page, int size);


    public List<Sku> findList(Map<String,Object> searchMap);


    public PageResult<Sku> findPage(Map<String,Object> searchMap,int page, int size);


    public Sku findById(String id);

    public void add(Sku sku);


    public void update(Sku sku);


    public void delete(String id);

}
