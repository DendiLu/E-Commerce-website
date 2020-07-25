package com.qingcheng.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.qingcheng.pojo.goods.Goods;
import com.qingcheng.pojo.goods.Sku;
import com.qingcheng.pojo.goods.Spu;
import com.qingcheng.service.goods.CategoryService;
import com.qingcheng.service.goods.SpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/item")
public class ItemController {
    @Reference
    private SpuService spuService;

    @Reference
    private CategoryService categoryService;

    @Value("${pagePath}")
    private String pagePath;

    @Autowired
    private TemplateEngine templateEngine;

    @GetMapping("/createPage")
    public void createPage(String spuId) {
        //查询商品信息
        Goods goodsById = spuService.findGoodsById(spuId);
        //获取spu信息(就是spuId?)
        Spu spu = goodsById.getSpu();
        //获取sku列表
        List<Sku> skuList = goodsById.getSkuList();

        ArrayList<String> categoryList = new ArrayList<>();
        categoryList.add(categoryService.findById(spu.getCategory1Id()).getName());  //一级分类名称
        categoryList.add(categoryService.findById(spu.getCategory2Id()).getName());   //二级分类
        categoryList.add(categoryService.findById(spu.getCategory3Id()).getName());  //三级分类

        Map<String, String> skuUrlMap = new HashMap<>();   //sku 地址 映射
        for (Sku sku : skuList) {
            if ("1".equals(sku.getStatus())) {
                String specJson = JSON.toJSONString(JSON.parseObject(sku.getSpec()), SerializerFeature.MapSortField);
                skuUrlMap.put(specJson, sku.getId() + ".html");
            }
        }

        //批量生成页面
        for (Sku sku : skuList) {
            //创建上下文和模型
            Context context = new Context();
            Map<String, Object> dataModel = new HashMap<>();
            dataModel.put("spu", spu);
            dataModel.put("sku", sku);
            dataModel.put("categoryList", categoryList);
            dataModel.put("skuImages", sku.getImages().split(",")); //sku图片列表 uri
            dataModel.put("spuImages", spu.getImage().split(",")); //spu图片列表

            Map paraItems = JSON.parseObject(spu.getParaItems());  //参数列表,商品详情页内容
            dataModel.put("paramItems", paraItems);

            Map<String, String> specItems = (Map) JSON.parseObject(sku.getSpec());           //规格列表，商品详情页内容
            dataModel.put("specItems", specItems);
            //{"颜色":["深色","蓝色"],"尺码":["27","28","29"]}
            Map<String, List> specMap = (Map) JSON.parseObject(spu.getSpecItems());   //规格选项，用于切换sku与详情页url

            //将上述选项修改为{"颜色":[{"option":"深色","checked":true},{"option":"蓝色","checked":false}]，...}
            for (String key : specMap.keySet()) {
                List<String> list = specMap.get(key);
                List<Map> mapList = new ArrayList<>();
                for (String value : list) {
                    Map map = new HashMap();
                    map.put("option", value);
                    if (value.equals(specItems.get(key))) {      //当前修改的json对象中value等于当前生成的详情页对应选项，则为选中项
                        map.put("checked", true);
                    } else {
                        map.put("checked", false);                //否则为未选中
                    }
                    Map<String, String> spec = (Map) JSON.parseObject(sku.getSpec());   //当前sku的规格项，方便转json
                    spec.put(key, value);                                                //当前要修改url的sku规格选项
                    String specJson = JSON.toJSONString(spec, SerializerFeature.MapSortField); //按照map的key进行排序，如果键值对都相同，则保证json值相同
                    map.put("url", skuUrlMap.get(specJson));
                    mapList.add(map);
                }
                specMap.put(key, mapList);
            }
            dataModel.put("specMap", specMap);

            context.setVariables(dataModel);
            //准备文件
            File dir = new File(pagePath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File dest = new File(dir, sku.getId() + ".html");
            //生成文件
            PrintWriter writer = null;
            try {
                writer = new PrintWriter(dest, "UTF-8");
                templateEngine.process("item", context, writer);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
    }
}
