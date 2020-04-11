package com.qingcheng.service.goods;
import com.qingcheng.entity.PageResult;
import com.qingcheng.pojo.goods.Goods;
import com.qingcheng.pojo.goods.Spu;

import java.util.*;

/**
 * spu业务逻辑层
 */
public interface SpuService {


    public List<Spu> findAll();


    public PageResult<Spu> findPage(int page, int size);


    public List<Spu> findList(Map<String,Object> searchMap);


    public PageResult<Spu> findPage(Map<String,Object> searchMap,int page, int size);


    public Spu findById(String id);

    public void add(Spu spu);


    public void update(Spu spu);


    public void delete(String id);

    public void saveGoods(Goods goods);

    public Goods findGoodsById(String id);

    /**
     *
     * @param id spuid
     * @param status 审核状态
     * @param message 审核备注
     */
    public void audit(String id,String status,String message);

    public void downShelf(String id);

    public void upShelf(String id);

    public int upShelfBatch(String[] ids);

    public int downShelfBatch(String[] ids);

}
