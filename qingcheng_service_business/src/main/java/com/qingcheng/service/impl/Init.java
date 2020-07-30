package com.qingcheng.service.impl;

import com.qingcheng.service.business.AdService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

public class Init implements InitializingBean {
    @Autowired
    private AdService adService;

    /**
     * 加载所有广告到redis，缓存预热
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        adService.saveAllAdToRedis();
    }
}
