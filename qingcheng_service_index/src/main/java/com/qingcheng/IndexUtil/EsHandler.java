package com.qingcheng.IndexUtil;

import com.qingcheng.pojo.goods.Sku;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Component
public class EsHandler {

    @Autowired
    RestHighLevelClient client;

    public void insertIndex(List<Sku> skus) {
        BulkRequest bulkRequest = new BulkRequest();

        for (Sku sku : skus) {
            IndexRequest indexRequest = new IndexRequest("sku");
            Map skuMap = new HashMap();
            skuMap.put("sku", sku.getId());
            skuMap.put("spu", sku.getSpuId());
            skuMap.put("name", sku.getName());
            skuMap.put("brandName", sku.getBrandName());
            skuMap.put("categoryName", sku.getCategoryName());
            skuMap.put("price", sku.getPrice());
            skuMap.put("createTime", sku.getCreateTime());
            skuMap.put("saleNum", sku.getSaleNum());
            skuMap.put("commentNum", sku.getCommentNum());
            skuMap.put("image", sku.getImage());
            Map spec = new HashMap();
            String specString = sku.getSpec();
            String[] split = specString.split(",");
            for (String s : split) {
                String replace1 = s.replace("\'", "");
                String replace2 = replace1.replace("{", "");
                String replace = replace2.replace("}", "");
                String[] split1 = replace.split(":");
                if (split1.length > 1)
                    spec.put(split1[0], split1[1]);
            }
            skuMap.put("spec", spec);
            indexRequest.source(skuMap);
            bulkRequest.add(indexRequest);//可以多次添加
            try {
                BulkResponse response = client.bulk(bulkRequest, RequestOptions.DEFAULT);
                int status = response.status().getStatus();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void deleteIndex(String spu) {

        DeleteByQueryRequest deleteByQueryRequest = new DeleteByQueryRequest("sku");
        deleteByQueryRequest.setQuery(new TermQueryBuilder("spu", spu));
        try {
            BulkByScrollResponse bulkByScrollResponse = client.deleteByQuery(deleteByQueryRequest, RequestOptions.DEFAULT);
            List<BulkItemResponse.Failure> bulkFailures = bulkByScrollResponse.getBulkFailures();
            for(BulkItemResponse.Failure f:bulkFailures){
                System.out.println(f.getMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
