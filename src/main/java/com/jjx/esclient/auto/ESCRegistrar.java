package com.jjx.esclient.auto;

import com.jjx.esclient.annotation.EnableESTools;
import com.jjx.esclient.auto.autoindex.ESIndexProcessor;
import com.jjx.esclient.auto.util.AbstractESCRegister;
import com.jjx.esclient.auto.util.GetBasePackage;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;

import java.util.stream.Stream;

/**
 * 作用1：将范围内的接口准备作为springbean进行处理（有beanFactory辅助）
 * 作用2：将实体类扫描并托管给spring管理
 *
 * @author admin
 * @date 2019-04-16 15:24
 **/
@Configuration
public class ESCRegistrar extends AbstractESCRegister implements BeanFactoryAware, ApplicationContextAware, ImportBeanDefinitionRegistrar, ResourceLoaderAware, EnvironmentAware {
    private ResourceLoader resourceLoader;
    private Environment environment;
    private ApplicationContext applicationContext;
    private BeanFactory beanFactory;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }


    /**
     * 模版方法模式
     *
     * @param annotationMetadata metadata
     * @param registry           registry
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry registry) {
        //扫描entity
        new ESIndexProcessor().scan(annotationMetadata, beanFactory, applicationContext);
        //扫描接口
        super.registerBeanDefinitions(beanFactory, environment, resourceLoader, annotationMetadata, registry);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public Stream<String> getBasePackage(AnnotationMetadata annotationMetadata) {
        GetBasePackage getBasePackage = new GetBasePackage(EnableESTools.class);
        return getBasePackage.getBasePackage(annotationMetadata);
    }
}
