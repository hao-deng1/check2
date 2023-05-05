package com.lostAndFind.project.es.service.impl;

import com.lostAndFind.project.es.entity.EsPick;
import com.lostAndFind.project.es.repository.EsPickRepository;
import com.lostAndFind.project.es.service.EsPickService;
import com.lostAndFind.project.model.entity.Pick;
import com.lostAndFind.project.service.PickService;
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
public class EsPickServiceImpl implements EsPickService {
    @Autowired
    private EsPickRepository esProductRepository;

    //es添加数据
    public void save(EsPick esPick) {
        esProductRepository.save(esPick);
    }

    //es修改数据
    public String esUpdate(Pick pick){
        // 注意，es是根据id修改信息的，id必须是es里面的id,而非mysql数据库里面的id
        EsPick byIdd = esProductRepository.findByIdd(pick.getId()).get(0);
        if (byIdd!=null){
            BeanUtils.copyProperties(pick,byIdd);
            esProductRepository.save(byIdd);
            return "ok";
        }
        return "error";
    }
    public void esDelete(List<Long> idds){
        for (Long idd : idds) {
            EsPick byIdd = esProductRepository.findByIdd(idd).get(0);
            // 注意，es是根据id删除信息的，id必须是es里面的id,而非mysql数据库里面的id
            esProductRepository.deleteById(byIdd.getId());
        }
    }

    //根据id查询
    public EsPick searchById(String id) {
        Optional<EsPick> esProductList = esProductRepository.findById(id);
        return esProductList.get();
    }

    //根据title或desc查询
    public List<EsPick> searchProduct(String keyword) {
        return esProductRepository.findByPickNameOrDescriptionOrTag(keyword, keyword,keyword);
    }
    //根据title查询并高亮
    public SearchHits<EsPick> searchPickNameWithHits(String keyword) {
        return esProductRepository.findPickName(keyword);
    }
    //根据title或desc查询并高亮
    public SearchHits<EsPick> searchPickNameOrTagOrDescWithHits(String keyword) {
        return esProductRepository.findPickNameOrTagOrDesc2(keyword);
    }

}
