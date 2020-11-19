package com.lingxiao.blog.service.system;

import com.lingxiao.blog.bean.po.Dictionary;

import java.util.List;

public interface DictionaryService {
    Dictionary getDictionaryByCode(String code);
    Dictionary getDictionaryById(String id);
    List<Dictionary> getDictionariesByPId(String pId);
    List<Dictionary> getDictionariesByName(String name);
    Dictionary getDictionaryByNameAndCode(String name,String code);
    void addDictionary(Dictionary dictionary);
}
