package com.lostAndFind.project.es.repository;

import com.lostAndFind.project.es.entity.EsLost;
import com.lostAndFind.project.es.entity.EsPick;
import org.springframework.data.elasticsearch.annotations.Highlight;
import org.springframework.data.elasticsearch.annotations.HighlightField;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author yc_
 * @version 1.0
 */
public interface EsLostRepository extends ElasticsearchRepository<EsLost, String> {

    //根据title和description查询产品,注意函数名称里面的名称一定要和属性名称一致，否则会报错
    List<EsLost> findByLostNameOrDescriptionOrTag(String lostName, String description,String tag);

    List<EsLost> findByIdd(Long idd);

    @Highlight(fields = {
            @HighlightField(name = "lostName")
    })
    @Query("{\"match\":{\"lostName\":\"?0\"}}")
    SearchHits<EsLost> findLostName(String keyword);

    @Highlight(fields = {
            @HighlightField(name = "lostName"),
            @HighlightField(name = "tag"),
            @HighlightField(name = "description")
    })
    @Query("{\"bool\":" +
            "{\"should\":[" +
            "{\"term\":{\"lostName\": \"?0\"}}," +
            "{\"term\":{\"tag\": \"?0\"}}," +
            "{\"term\":{\"description\": \"?0\"}}]" +
            "}}")
    SearchHits<EsLost> findLostNameOrTagOrDesc2(String keyword);

}
