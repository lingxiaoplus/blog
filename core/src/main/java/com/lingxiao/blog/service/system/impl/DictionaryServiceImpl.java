package com.lingxiao.blog.service.system.impl;

import com.lingxiao.blog.bean.po.Dictionary;
import com.lingxiao.blog.enums.ExceptionEnum;
import com.lingxiao.blog.exception.BlogException;
import com.lingxiao.blog.global.RedisConstants;
import com.lingxiao.blog.mapper.DictionaryMapper;
import com.lingxiao.blog.service.system.DictionaryService;
import com.lingxiao.blog.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @author lingxiao
 */
@Service
public class DictionaryServiceImpl implements DictionaryService {

    @Autowired
    private DictionaryMapper dictionaryMapper;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public Dictionary getDictionaryByCode(String code) {
        Dictionary dictionary = new Dictionary();
        dictionary.setCode(code);
        return dictionaryMapper.selectOne(dictionary);
    }

    @Override
    public Dictionary getDictionaryById(String id) {
        return dictionaryMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Dictionary> getDictionariesByPId(String pId) {
        Dictionary dictionary = new Dictionary();
        dictionary.setParentId(pId);
        return dictionaryMapper.select(dictionary);
    }

    @Override
    public List<Dictionary> getDictionariesByName(String name) {
        Dictionary select = new Dictionary();
        select.setName(name);
        Dictionary dictionary = dictionaryMapper.selectOne(select);
        if (dictionary != null){
            Dictionary parent = new Dictionary();
            parent.setParentId(dictionary.getId());
            return dictionaryMapper.select(parent);
        }
        return Collections.emptyList();
    }

    @Override
    public Dictionary getDictionaryByNameAndCode(String name, String code) {
        String key = String.format(RedisConstants.KEY_BACK_DICTIONARY_NAME_CODE, name, code);
        Dictionary cache = redisUtil.getValueByKey(key);
        if (cache != null){
            return cache;
        }
        Dictionary select = new Dictionary();
        select.setName(name);
        Dictionary dictionary = dictionaryMapper.selectOne(select);
        if (dictionary != null){
            Dictionary parent = new Dictionary();
            parent.setParentId(dictionary.getId());
            parent.setCode(code);
            Dictionary result = dictionaryMapper.selectOne(parent);
            redisUtil.pushValue(key,result);
            return result;
        }
        return null;
    }

    @Override
    public void addDictionary(Dictionary dictionary) {
        int insert = dictionaryMapper.insert(dictionary);
        if (insert != 1){
            throw new BlogException(ExceptionEnum.ILLEGA_ARGUMENT);
        }
    }
}
