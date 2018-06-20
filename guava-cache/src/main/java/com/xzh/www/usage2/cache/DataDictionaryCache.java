package com.xzh.www.usage2.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.xzh.www.usage2.constants.DBConstants;
import com.xzh.www.usage2.constants.SystemConstants;
import com.xzh.www.usage2.service.DataDictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author: xuzh1
 * @Date: 2018-06-20 22:47
 **/
@Component     //此处必须添加@Component注解
public class DataDictionaryCache implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private DataDictionaryService dataDictionaryService;

    public static LoadingCache<String, Map<String, String>> dataDictionaryCache;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        dataDictionaryCache = CacheBuilder.newBuilder()
                .maximumSize(SystemConstants.MAX_NUM_SIZE)
                .refreshAfterWrite(30, TimeUnit.MINUTES)
                .build(new CacheLoader<String, Map<String, String>>() {
                    @Override
                    public Map<String, String> load(String cacheType) throws Exception {
                        if (DBConstants.DATA_DICTIONARY_CACHE_KEY.equals(cacheType)) {
                            return dataDictionaryService.getAllDataDictionaryCache();
                        }else{
                            return new HashMap<String,String>();
                        }
                    }
                });
        try{
            dataDictionaryCache.get(DBConstants.DATA_DICTIONARY_CACHE_KEY);
        }catch (Exception e){
            System.out.println("get server disable dictionary cache info error");
            e.printStackTrace();
        }
    }


}
