package com.lostAndFind.project.es.repository;

import com.lostAndFind.project.es.entity.EsPick;
import org.springframework.data.elasticsearch.annotations.Highlight;
import org.springframework.data.elasticsearch.annotations.HighlightField;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * @author yc_
 * @version 1.0
 */
public interface EsPickRepository extends ElasticsearchRepository<EsPick, String> {

    //根据title和description查询产品,注意函数名称里面的名称一定要和属性名称一致，否则会报错
    List<EsPick> findByPickNameOrDescriptionOrTag(String pickName, String description,String tag);

    List<EsPick> findByIdd(Long idd);

    @Highlight(fields = {
            @HighlightField(name = "pickName")
    })
    @Query("{\"match\":{\"pickName\":\"?0\"}}")
    SearchHits<EsPick> findPickName(String keyword);

    @Highlight(fields = {
            @HighlightField(name = "pickName"),
            @HighlightField(name = "tag"),
            @HighlightField(name = "description")
    })
    @Query("{\"bool\":" +
            "{\"should\":[" +
            "{\"term\":{\"pickName\": \"?0\"}}," +
            "{\"term\":{\"tag\": \"?0\"}}," +
            "{\"term\":{\"description\": \"?0\"}}" +
            "]}}")
    SearchHits<EsPick> findPickNameOrTagOrDesc2(String keyword);
}
