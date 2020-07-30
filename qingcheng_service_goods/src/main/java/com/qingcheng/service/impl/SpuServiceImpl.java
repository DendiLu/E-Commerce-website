package com.qingcheng.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.qingcheng.dao.*;
import com.qingcheng.entity.PageResult;
import com.qingcheng.pojo.goods.*;
import com.qingcheng.service.goods.SkuService;
import com.qingcheng.service.goods.SpuService;
import com.qingcheng.util.CacheKey;
import com.qingcheng.util.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.text.SimpleDateFormat;
import java.util.*;

@Service(interfaceClass = SpuService.class)//加入事物后被多个接口代理,加入此注解指定接口
public class SpuServiceImpl implements SpuService {

    @Autowired
    private SpuMapper spuMapper;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private GoodsLogMapper goodsLogMapper;
    @Autowired
    private SkuService skuService;

    /**
     * 返回全部记录
     *
     * @return
     */
    public List<Spu> findAll() {
        return spuMapper.selectAll();
    }

    /**
     * 分页查询
     *
     * @param page 页码
     * @param size 每页记录数
     * @return 分页结果
     */
    public PageResult<Spu> findPage(int page, int size) {
        PageHelper.startPage(page, size);
        Page<Spu> spus = (Page<Spu>) spuMapper.selectAll();
        return new PageResult<Spu>(spus.getTotal(), spus.getResult());
    }

    /**
     * 条件查询
     *
     * @param searchMap 查询条件
     * @return
     */
    public List<Spu> findList(Map<String, Object> searchMap) {
        Example example = createExample(searchMap);
        return spuMapper.selectByExample(example);
    }

    /**
     * 分页+条件查询
     *
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    public PageResult<Spu> findPage(Map<String, Object> searchMap, int page, int size) {
        PageHelper.startPage(page, size);
        Example example = createExample(searchMap);
        Page<Spu> spus = (Page<Spu>) spuMapper.selectByExample(example);
        return new PageResult<Spu>(spus.getTotal(), spus.getResult());
    }

    /**
     * 根据Id查询
     *
     * @param id
     * @return
     */
    public Spu findById(String id) {
        return spuMapper.selectByPrimaryKey(id);
    }

    /**
     * 新增
     *
     * @param spu
     */
    public void add(Spu spu) {
        spuMapper.insert(spu);
    }

    /**
     * 修改
     *
     * @param spu
     */
    public void update(Spu spu) {
        spuMapper.updateByPrimaryKeySelective(spu);
    }

    /**
     * 删除
     *
     * @param id
     */
    public void delete(String id) {
        spuMapper.deleteByPrimaryKey(id);
    }

    @Autowired
    private CategoryBrandMapper categoryBrandMapper;

    /**
     * 保存商品
     *
     * @param goods
     */
    @Override
    @Transactional
    public void saveGoods(Goods goods) {
        //保存spu spu与sku一对多

        Spu spu = goods.getSpu();
        if (spu.getId() == null) {//新增商品
            spu.setId(idWorker.nextId() + "");
            spuMapper.insert(spu);
        } else {//修改商品
            //删除原来的sku列表
            Example example = new Example(Sku.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("spuId", spu.getId());
            skuMapper.deleteByExample(example);
            //执行spu的修改
            spuMapper.updateByPrimaryKey(spu);

        }
        //获取创建时间和修改时间
        Date date = new Date();
        //获取三级分类
        Category category = categoryMapper.selectByPrimaryKey(spu.getCategory3Id());
        //获取skulist
        List<Sku> skuList = goods.getSkuList();
        for (Sku sku : skuList) {
//            为每一个sku生成id skuid skuname 创建修改时间等属性
            if (sku.getId() == null) {
                sku.setId(idWorker.nextId() + "");
                sku.setCreateTime(date);
            }

            sku.setSpuId(spu.getId());

            //不启用规格的sku处理
            if (sku.getSpec() == null || "".equals(sku.getSpec())) {
                sku.setSpec("{}");
            }
            //sku名称 =spu名称+规格值列表
            String name = spu.getName();
            Map<String, String> map = JSON.parseObject(sku.getSpec(), Map.class);
            for (String value : map.values()) {
                name += " " + value;
            }
            sku.setName(name);

            sku.setUpdateTime(date);
            sku.setCategoryId(spu.getCategory3Id());
            sku.setCategoryName(category.getName());
            sku.setCommentNum(0);
            sku.setSaleNum(0);

            skuMapper.insert(sku);
            //重新将商品加载到redis
            skuService.savePriceToRedisById(sku.getId(),sku.getPrice());

        }
        //建立分类与品牌关联
        CategoryBrand categoryBrand = new CategoryBrand();
        categoryBrand.setCategoryId(spu.getCategory3Id());
        categoryBrand.setBrandId(spu.getBrandId());
        //防止重复
        int count = categoryBrandMapper.selectCount(categoryBrand);
        if (count == 0) {
            categoryBrandMapper.insert(categoryBrand);
        }

    }

    @Override
    public Goods findGoodsById(String id) {
        //查询spu
        Spu spu = spuMapper.selectByPrimaryKey(id);

        //查询sku列表
        Example example = new Example(Sku.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("spuId", id);
        List<Sku> skuList = skuMapper.selectByExample(example);
        //封装组合为实体类
        Goods goods = new Goods();
        goods.setSpu(spu);
        goods.setSkuList(skuList);

        return goods;
    }

    /**
     * @param id      spuid
     * @param status  审核状态
     * @param message 审核备注
     */
    @Override
    @Transactional
    public void audit(String id, String status, String message) {
        //修改状态 审核状态和上架状态
        Spu spu = new Spu();
        spu.setId(id);
        spu.setId(status);
        if ("1".equals(status)) {
            spu.setIsMarketable("1");
        }
        spuMapper.updateByPrimaryKeySelective(spu);
        //记录商品审核 学员实现

        //记录商品日志 学员实现
    }

    /**
     * 商品下架
     *
     * @param spuId
     */
    @Override
    public void downShelf(String spuId) {
        //修改状态
        Spu spu = new Spu();
        spu.setId(spuId);
        spu.setIsMarketable("0");
        spuMapper.updateByPrimaryKeySelective(spu);
        //记录商品日志
        GoodsLog goodsLog = goodsLogMapper.selectByPrimaryKey(spuId);
        Date currentDate = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String logTime = simpleDateFormat.format(currentDate);
        if(goodsLog==null){
            goodsLog = new GoodsLog();
            goodsLog.setLogInfo(logTime+" 商品下架 /n");
            goodsLogMapper.insertSelective(goodsLog);
        }else {
            String logInfo = goodsLog.getLogInfo()+logTime+" 商品下架 /n";
            goodsLog.setLogInfo(logInfo);
            goodsLogMapper.updateByPrimaryKeySelective(goodsLog);
        }
    }


    /**
     * 商品上架
     *
     * @param id
     */
    @Override
    public void upShelf(String id) {
        //修改状态
        Spu spu = spuMapper.selectByPrimaryKey(id);
        //查询是否通过审核 0通过 1未通过
        if(!"1".equals(spu.getStatus())){
            throw new RuntimeException("此商品未通过审核");
        }
        spu.setIsMarketable("1");
        spuMapper.updateByPrimaryKeySelective(spu);
        //记录商品日志
        GoodsLog goodsLog = goodsLogMapper.selectByPrimaryKey(id);
        Date currentDate = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String logTime = simpleDateFormat.format(currentDate);
        if(goodsLog==null){
            goodsLog = new GoodsLog();
            goodsLog.setLogInfo(logTime+" 商品上架 /n");
            goodsLogMapper.insertSelective(goodsLog);
        }else {
            String logInfo = goodsLog.getLogInfo()+logTime+" 商品上架 /n";
            goodsLog.setLogInfo(logInfo);
            goodsLogMapper.updateByPrimaryKeySelective(goodsLog);
        }

    }

    /**
     * 批量上架商品 要先检查审核是否通过
     * @param ids
     * @return
     */
    @Override
    public int upShelfBatch(String[] ids) {
        //修改状态
        Spu spu = new Spu();
        spu.setIsMarketable("1");
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", Arrays.asList(ids));
        criteria.andEqualTo("status","1");
        criteria.andEqualTo("isMarketable","0");
        int i = spuMapper.updateByExampleSelective(spu, example);
        //记录商品日志
        for(String id:ids){
            GoodsLog goodsLog = goodsLogMapper.selectByPrimaryKey(id);
            Date currentDate = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String logTime = simpleDateFormat.format(currentDate);
            if(goodsLog==null){
                goodsLog = new GoodsLog();
                goodsLog.setLogInfo(logTime+" 商品上架 /n");
                goodsLogMapper.insertSelective(goodsLog);
            }else {
                String logInfo = goodsLog.getLogInfo()+logTime+" 商品上架 /n";
                goodsLog.setLogInfo(logInfo);
                goodsLogMapper.updateByPrimaryKeySelective(goodsLog);
            }
        }

        return i;
    }

    /**
     * 批量下架商品
     * @param ids
     * @return
     */
    @Override
    public int downShelfBatch(String[] ids) {
        //修改状态
        Spu spu = new Spu();
        spu.setIsMarketable("0");
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", Arrays.asList(ids));
        criteria.andEqualTo("isMarketable","1");
        int i = spuMapper.updateByExampleSelective(spu, example);
        //记录商品日志
        for(String id:ids){
            GoodsLog goodsLog = goodsLogMapper.selectByPrimaryKey(id);
            Date currentDate = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String logTime = simpleDateFormat.format(currentDate);
            if(goodsLog==null){
                goodsLog = new GoodsLog();
                goodsLog.setLogInfo(logTime+" 商品下架 /n");
                goodsLogMapper.insertSelective(goodsLog);
            }else {
                String logInfo = goodsLog.getLogInfo()+logTime+" 商品下架 /n";
                goodsLog.setLogInfo(logInfo);
                goodsLogMapper.updateByPrimaryKeySelective(goodsLog);
            }
        }

        return i;
    }

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 逻辑删除商品,数据库is_delete字段设为1
     * @param spuIds
     */
    public int delete(String[] spuIds){
        int count = 0;
        if (spuIds !=null&& spuIds.length>0){
            for(String id: spuIds){
                //删除字段标1，逻辑删除
                Spu spu = spuMapper.selectByPrimaryKey(id);
                spu.setIsDelete("1");

                //查询属于spu的sku的条件
                Map map = new HashMap();
                map.put("skuId",id);
                List<Sku> skuList = skuService.findList(map);
                //从价格缓存中删除价格信息
                for(Sku sku: skuList){
                    redisTemplate.boundHashOps(CacheKey.SKU_PRICE).delete(sku.getId());
                }
                int i = spuMapper.updateByPrimaryKeySelective(spu);
                count+=i;
            }
        }
        return count;
    }

    /**
     * 从回收站回复逻辑删除的商品,数据库is_delete字段设为0
     * @param ids
     * @return
     */
    public int recycle(String[] ids){
        int count = 0;
        if (ids!=null&&ids.length>0){
            for(String id:ids){
                Spu spu = spuMapper.selectByPrimaryKey(id);
                spu.setIsDelete("0");
                int i = spuMapper.updateByPrimaryKeySelective(spu);
                count+=i;
            }
        }
        return count;
    }

    /**
     * 返回回收站中已删除的商品(逻辑删除)
     * @return List<Goods>
     */
    public List<Goods> findDeletedGoods(){
        List<Goods> goodsList = new ArrayList<>();
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isDelete","1");
        List<Spu> spus = spuMapper.selectByExample(example);
        spus.forEach(spu->{
            String spuId = spu.getId();
            Example example1 = new Example(Sku.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("spuId",spuId);
            List<Sku> skuList = skuMapper.selectByExample(example1);
            Goods goods = new Goods();
            goods.setSkuList(skuList);
            goods.setSpu(spu);
            goodsList.add(goods);
        });
        return goodsList;
    }

    /**
     * 构建查询条件
     *
     * @param searchMap
     * @return
     */
    private Example createExample(Map<String, Object> searchMap) {
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        if (searchMap != null) {
            // 主键
            if (searchMap.get("id") != null && !"".equals(searchMap.get("id"))) {
                criteria.andLike("id", "%" + searchMap.get("id") + "%");
            }
            // 货号
            if (searchMap.get("sn") != null && !"".equals(searchMap.get("sn"))) {
                criteria.andLike("sn", "%" + searchMap.get("sn") + "%");
            }
            // SPU名
            if (searchMap.get("name") != null && !"".equals(searchMap.get("name"))) {
                criteria.andLike("name", "%" + searchMap.get("name") + "%");
            }
            // 副标题
            if (searchMap.get("caption") != null && !"".equals(searchMap.get("caption"))) {
                criteria.andLike("caption", "%" + searchMap.get("caption") + "%");
            }
            // 图片
            if (searchMap.get("image") != null && !"".equals(searchMap.get("image"))) {
                criteria.andLike("image", "%" + searchMap.get("image") + "%");
            }
            // 图片列表
            if (searchMap.get("images") != null && !"".equals(searchMap.get("images"))) {
                criteria.andLike("images", "%" + searchMap.get("images") + "%");
            }
            // 售后服务
            if (searchMap.get("saleService") != null && !"".equals(searchMap.get("saleService"))) {
                criteria.andLike("saleService", "%" + searchMap.get("saleService") + "%");
            }
            // 介绍
            if (searchMap.get("introduction") != null && !"".equals(searchMap.get("introduction"))) {
                criteria.andLike("introduction", "%" + searchMap.get("introduction") + "%");
            }
            // 规格列表
            if (searchMap.get("specItems") != null && !"".equals(searchMap.get("specItems"))) {
                criteria.andLike("specItems", "%" + searchMap.get("specItems") + "%");
            }
            // 参数列表
            if (searchMap.get("paraItems") != null && !"".equals(searchMap.get("paraItems"))) {
                criteria.andLike("paraItems", "%" + searchMap.get("paraItems") + "%");
            }
            // 是否上架
            if (searchMap.get("isMarketable") != null && !"".equals(searchMap.get("isMarketable"))) {
                criteria.andLike("isMarketable", "%" + searchMap.get("isMarketable") + "%");
            }
            // 是否启用规格
            if (searchMap.get("isEnableSpec") != null && !"".equals(searchMap.get("isEnableSpec"))) {
                criteria.andLike("isEnableSpec", "%" + searchMap.get("isEnableSpec") + "%");
            }
            // 是否删除
            if (searchMap.get("isDelete") != null && !"".equals(searchMap.get("isDelete"))) {
                criteria.andLike("isDelete", "%" + searchMap.get("isDelete") + "%");
            }
            // 审核状态
            if (searchMap.get("status") != null && !"".equals(searchMap.get("status"))) {
                criteria.andLike("status", "%" + searchMap.get("status") + "%");
            }

            // 品牌ID
            if (searchMap.get("brandId") != null) {
                criteria.andEqualTo("brandId", searchMap.get("brandId"));
            }
            // 一级分类
            if (searchMap.get("category1Id") != null) {
                criteria.andEqualTo("category1Id", searchMap.get("category1Id"));
            }
            // 二级分类
            if (searchMap.get("category2Id") != null) {
                criteria.andEqualTo("category2Id", searchMap.get("category2Id"));
            }
            // 三级分类
            if (searchMap.get("category3Id") != null) {
                criteria.andEqualTo("category3Id", searchMap.get("category3Id"));
            }
            // 模板ID
            if (searchMap.get("templateId") != null) {
                criteria.andEqualTo("templateId", searchMap.get("templateId"));
            }
            // 运费模板id
            if (searchMap.get("freightId") != null) {
                criteria.andEqualTo("freightId", searchMap.get("freightId"));
            }
            // 销量
            if (searchMap.get("saleNum") != null) {
                criteria.andEqualTo("saleNum", searchMap.get("saleNum"));
            }
            // 评论数
            if (searchMap.get("commentNum") != null) {
                criteria.andEqualTo("commentNum", searchMap.get("commentNum"));
            }

        }
        return example;
    }

}
