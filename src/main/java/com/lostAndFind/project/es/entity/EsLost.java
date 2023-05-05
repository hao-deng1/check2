package com.lostAndFind.project.es.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

/**
 * 丢失物品表
 * @TableName lost
 */

@Data
@Document(indexName = "lost",createIndex = true)
public class EsLost {
    @Id
    @Field(type = FieldType.Text)
    private String id;

    @Field(type = FieldType.Long)
    private Long userId;

    @Field(type = FieldType.Text)
    private String lostName;

    private Date lostDate;

    @Field(type = FieldType.Text)
    private String contact;

//    @Field(type = FieldType.Date,format = DateFormat.basic_date_time)
//    private Date reportTime;

    @Field(type = FieldType.Text)
    private String description;

    @Field(type = FieldType.Integer)
    private Integer isDelete;

    @Field(type = FieldType.Text)
    private String img;

    @Field(type = FieldType.Double)
    private Double business;

    @Field(type = FieldType.Double)
    private Double urgent;

    @Field(type = FieldType.Text)
    private String status;

    @Field(type = FieldType.Long)
    private Long idd;

    @Field(type = FieldType.Text)
    private String tag;

}