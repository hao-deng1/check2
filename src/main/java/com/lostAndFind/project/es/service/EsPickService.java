package com.lostAndFind.project.es.service;

import com.lostAndFind.project.es.entity.EsPick;
import com.lostAndFind.project.model.entity.Pick;
import org.springframework.data.elasticsearch.core.SearchHits;

import java.util.List;

/**
 * @author yc_
 * @version 1.0
 */
public interface EsPickService {
    public void save(EsPick esPick);
    //es修改数据
    public String esUpdate(Pick pick);
    public void esDelete(List<Long> idds);
    //根据title或desc查询
    public EsPick searchById(String id);
    //根据title或desc查询
    public List<EsPick> searchProduct(String keyword);
    //根据title查询并高亮
    public SearchHits<EsPick> searchPickNameWithHits(String keyword);
    //根据title或desc查询并高亮
    public SearchHits<EsPick> searchPickNameOrTagOrDescWithHits(String keyword);

}
