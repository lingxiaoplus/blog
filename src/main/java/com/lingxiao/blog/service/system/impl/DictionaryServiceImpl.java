package com.lingxiao.blog.service.system.impl;

import com.lingxiao.blog.bean.Dictionary;
import com.lingxiao.blog.enums.ExceptionEnum;
import com.lingxiao.blog.exception.BlogException;
import com.lingxiao.blog.mapper.DictionaryMapper;
import com.lingxiao.blog.service.system.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DictionaryServiceImpl implements DictionaryService {

    @Autowired
    private DictionaryMapper dictionaryMapper;

    @Override
    public Dictionary getDictionaryByCode(String code) {
        Dictionary dictionary = new Dictionary();
        dictionary.setCode(code);
        return dictionaryMapper.selectOne(dictionary);
    }

    @Override
    public Dictionary getDictionaryById(String id) {
        Dictionary dictionary = dictionaryMapper.selectByPrimaryKey(id);
        return dictionary;
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
        return null;
    }

    @Override
    public Dictionary getDictionaryByNameAndCode(String name, String code) {
        Dictionary select = new Dictionary();
        select.setName(name);
        Dictionary dictionary = dictionaryMapper.selectOne(select);
        if (dictionary != null){
            Dictionary parent = new Dictionary();
            parent.setParentId(dictionary.getId());
            parent.setCode(code);
            return dictionaryMapper.selectOne(parent);
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
