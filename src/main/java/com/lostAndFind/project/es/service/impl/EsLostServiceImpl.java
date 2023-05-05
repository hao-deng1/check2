package com.lostAndFind.project.es.service.impl;

import com.lostAndFind.project.es.entity.EsLost;
import com.lostAndFind.project.es.repository.EsLostRepository;
import com.lostAndFind.project.es.service.EsLostService;
import com.lostAndFind.project.model.entity.Lost;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author yc_
 * @version 1.0
 */
@Slf4j
@Service
public class EsLostServiceImpl implements EsLostService {
    @Autowired
    private EsLostRepository esLostRepository;

    //es添加数据
    public void save(EsLost esLost){
        esLostRepository.save(esLost);
    }

    //es修改数据 no
    public String esUpdate(Lost lost){
        // 注意，es是根据id修改信息的，id必须是es里面的id,而非mysql数据库里面的id
        EsLost esLost = esLostRepository.findByIdd(lost.getId()).get(0);
        if (esLost!=null){
            BeanUtils.copyProperties(lost,esLost);
            esLostRepository.save(esLost);
            return "ok";
        }
        return "error";
    }
    public void esDelete(List<Long> idds){
        for (Long idd : idds) {
            EsLost esLost = esLostRepository.findByIdd(idd).get(0);
            // 注意，es是根据id删除信息的，id必须是es里面的id,而非mysql数据库里面的id
            esLostRepository.deleteById(esLost.getId());
        }
    }

    //根据id查询
    public EsLost searchById(String id) {
        Optional<EsLost> esProductList = esLostRepository.findById(id);
        return esProductList.get();
    }

    //根据title或desc查询
    public List<EsLost> searchProduct(String keyword) {
        return esLostRepository.findByLostNameOrDescriptionOrTag(keyword, keyword,keyword);
    }
    //根据title查询并高亮
    public SearchHits<EsLost> searchLostNameWithHits(String keyword) {
        return esLostRepository.findLostName(keyword);
    }
    //根据title或desc查询并高亮
    public SearchHits<EsLost> searchLostNameOrTagOrDescWithHits(String keyword) {
        return esLostRepository.findLostNameOrTagOrDesc2(keyword);
    }

}
