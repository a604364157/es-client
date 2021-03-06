package com.jjx.esclient.auto.autoindex;

import com.jjx.esclient.annotation.ESMetaData;
import com.jjx.esclient.auto.util.EnableESTools;
import com.jjx.esclient.index.ElasticsearchIndex;
import com.jjx.esclient.util.IndexTools;
import com.jjx.esclient.util.MetaData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * 用于扫描ESMetaData注解的类，并自动创建索引mapping
 * 启动时调用，但如果需要让spring知道哪些bean配置了ESMetaData注解，需要ElasticProcessor
 *
 * @author admin
 * @date 2019-01-30 18:43
 **/
@Configuration
public class CreateIndex<T, E extends ApplicationEvent> implements ApplicationListener<E>, ApplicationContextAware {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ElasticsearchIndex<T> elasticsearchIndex;

    private ApplicationContext applicationContext;

    /**
     * 扫描ESMetaData注解的类，并自动创建索引mapping
     *
     * @param e event
     */
    @Override
    public void onApplicationEvent(E e) {
        Map<String, Object> beansWithAnnotationMap = this.applicationContext.getBeansWithAnnotation(ESMetaData.class);
        beansWithAnnotationMap.forEach((beanName, bean) -> {
                    try {
                        if (!elasticsearchIndex.exists(bean.getClass())) {
                            elasticsearchIndex.createIndex(bean.getClass());
                            if (EnableESTools.isPrintRegMsg()) {
                                MetaData metaData = IndexTools.getMetaData(bean.getClass());
                                logger.info("创建索引成功，索引名称：" + metaData.getIndexName() + "索引类型：" + metaData.getIndexType());
                            }
                        }
                    } catch (Exception ex) {
                        logger.error("创建索引不成功", ex);
                    }
                }
        );
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
