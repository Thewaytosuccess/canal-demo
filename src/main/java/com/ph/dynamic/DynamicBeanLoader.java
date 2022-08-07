package com.ph.dynamic;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

/**
 * 动态加载jar，并注入到spring container
 * @Import
 * @author cdl
 */
@Slf4j
public class DynamicBeanLoader implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    /**
     * 存储Jar文件基础路径
     */
    private String basePath;

    /**
     * 包名称集，多个名称则通过","逗号进行区分
     */
    private String jarNames;
    /**
     * 包前缀
     */
    private String packagePrefix;

    @SneakyThrows
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
                                        BeanDefinitionRegistry registry) {
        try {
            if(Objects.isNull(jarNames)){
                log.warn("加载jar包名称[jarNames]为空，如果需要加载额外jar则需要手动配置！");
                return;
            }

            String[] jarPaths = jarNames.split(",");
            URLClassLoader urlClassLoader = getUrlClassLoader(jarPaths);
            String[] packagePrefixes = packagePrefix.split(",");

            Arrays.stream(jarPaths).filter(e -> !StringUtils.isEmpty(e)).distinct().forEach(e ->
                getClassesNamesByJar(packagePrefixes, basePath + "/" + e).forEach(cls -> {
                    try {
                        registerBean(urlClassLoader.loadClass(cls), registry);
                    } catch (Exception ex) {
                        log.error("", ex);
                    }
                })
            );

            log.info("加载对应jar包成功！");
        }catch (Exception e){
            log.warn("指定插件目录没有加载对应合法jar包:",e);
        }
    }

    @SneakyThrows
    private URLClassLoader getUrlClassLoader(String[] names){
        int len = names.length;
        URL[] urls = new URL[len];
        for(int i = 0; i < len; ++i){
            urls[i] = new File(basePath + "/" + names[i]).toURI().toURL();
        }

        //类加载器只实例化一次
        return new URLClassLoader(urls);
    }

    @SneakyThrows
    private List<String> getClassesNamesByJar(String[] prefix, String path) {
        log.info("path = {}",path);

        List<String> list = new ArrayList<>();
        JarFile jar = new JarFile(path);
        Enumeration<JarEntry> entries = jar.entries();
        while(entries.hasMoreElements()){
            String name = entries.nextElement().getName();
            if(name.endsWith(".class")){
                list.add(name.replaceAll("/",".").replace(".class",""));
            }
        }

        log.info("扫描jar中的类：{}",list);
        List<String> result = new ArrayList<>();
        Arrays.stream(prefix).forEach(p ->
            result.addAll(list.stream().filter(e -> e.startsWith(p)).distinct().collect(Collectors.toList()))
        );
        return result;
    }

    /**
     * 注册BEAN
     */
    private void registerBean(Class<?> c, BeanDefinitionRegistry registry) {
        if (isSpringBeanClass(c)) {
            log.info("[{}] is spring bean ============ ",c.getName());
            registry.registerBeanDefinition(c.getName(),
                    BeanDefinitionBuilder.genericBeanDefinition(c).getBeanDefinition());
        }
    }

    /**
     * 方法描述 判断class对象是否带有spring的注解
     */
    private boolean isSpringBeanClass(Class<?> clazz) {
        if (Objects.isNull(clazz) || clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers())){
            return false;
        }

        return clazz.isAnnotationPresent(Component.class) ||
                clazz.isAnnotationPresent(Repository.class) ||
                clazz.isAnnotationPresent(Configuration.class) ||
                clazz.isAnnotationPresent(Service.class);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.basePath = environment.getProperty("basePath");
        this.packagePrefix = environment.getProperty("packagePrefix");
        this.jarNames = environment.getProperty("jarNames");
    }
}
