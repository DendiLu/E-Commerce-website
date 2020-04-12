package com.qingcheng.pojo.goods;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
@Table(name="tb_goods_log")
public class GoodsLog implements Serializable {
    @Id
    private String spuId;
    private String skuId;
    private String logInfo;
    private Date date;

    public String getSpuId() {
        return spuId;
    }

    public void setSpuId(String spuId) {
        this.spuId = spuId;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public String getLogInfo() {
        return logInfo;
    }

    public void setLogInfo(String logInfo) {
        this.logInfo = logInfo;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "GoodsLog{" +
                "spuId='" + spuId + '\'' +
                ", skuId='" + skuId + '\'' +
                ", logInfo='" + logInfo + '\'' +
                ", date=" + date +
                '}';
    }
}
