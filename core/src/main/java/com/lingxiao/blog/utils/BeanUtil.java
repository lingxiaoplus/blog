package com.lingxiao.blog.utils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Admin
 */
public class BeanUtil {
    /**
     * 获取默认字段工厂
     */
    private static final MapperFactory MAPPER_FACTORY = new DefaultMapperFactory.Builder().build();
    /**
     * 默认字段实例
     */
    private static final MapperFacade MAPPER_FACADE = MAPPER_FACTORY.getMapperFacade();


    /**
     * 映射实体（默认字段）
     * 这种映射就是DTO字段名称和实体对象DO之间字段名称一致
     * @param toClass 映射类对象 DTO对象
     * @param data    数据（对象）DO对象
     * @return 映射类对象
     */
    public static  <E, T> E map(Class<E> toClass, T data) {
        return MAPPER_FACADE.map(data, toClass);
    }

    /**
     * 映射实体（自定义配置）
     * @param toClass   映射类对象 DTO对象
     * @param data      数据（对象）DO对象
     * @param configMap 自定义配置 这个自定义配置我目前就理解为，如果需要映射的字段之间存在差异，需要通过一个对象来中中转
     * @return 映射类对象
     */
    /*public <E, T> E map(Class<E> toClass, T data, Map<String, String> configMap) {
        MapperFacade mapperFacade = this.getMapperFacade(toClass, data.getClass(), configMap);

        MAPPER_FACTORY.classMap(toClass,data.getClass()).field().byDefault().register();
        return mapperFacade.map(data, toClass);
    }*/

    /**
     * 映射集合（默认字段）
     * 映射为集合的形式
     * @param toClass 映射类对象 DTO对象
     * @param data    数据（集合） DO对象
     * @return 映射类对象
     */
    public <E, T> List<E> mapAsList(Class<E> toClass, Collection<T> data) {
        return MAPPER_FACADE.mapAsList(data, toClass);
    }



}
