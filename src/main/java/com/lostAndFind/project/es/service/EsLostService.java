package com.lostAndFind.project.es.service;

import com.lostAndFind.project.es.entity.EsLost;
import com.lostAndFind.project.es.entity.EsPick;
import com.lostAndFind.project.model.entity.Lost;
import org.springframework.data.elasticsearch.core.SearchHits;

import java.util.List;

/**
 * @author yc_
 * @version 1.0
 */
public interface EsLostService {
    public void save(EsLost esLost);
    //es修改数据
    public String esUpdate(Lost lost);
    public void esDelete(List<Long> idds);
    //根据title或desc查询
    public EsLost searchById(String id);
    //根据title或desc查询
    public List<EsLost> searchProduct(String keyword);
    //根据title查询并高亮
    public SearchHits<EsLost> searchLostNameWithHits(String keyword);
    //根据title或desc查询并高亮
    public SearchHits<EsLost> searchLostNameOrTagOrDescWithHits(String keyword);

}
