package com.lostAndFind.project.es.entity;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

/**
 * 招领物品表
 * @author Asus
 * @TableName pick
 */

@Data
@Document(indexName = "pick",createIndex = true)
public class EsPick {
    @Id
    @Field(type = FieldType.Text)
    private String id;

    @Field(type = FieldType.Long)
    private Long userid;

    @Field(type = FieldType.Text)
    private String pickName;

//    @Field(type = FieldType.Date,format = DateFormat.basic_date_time)
//    private Date pickDate;

    @Field(type = FieldType.Text)
    private String pickPlace;

    @Field(type = FieldType.Text)
    private String contact;

//    @Field(type = FieldType.Date,format = DateFormat.basic_date_time)
//    private Date reportTime;

    @Field(type = FieldType.Text)
    private String description;

    @Field(type = FieldType.Text)
    private String img;

    @Field(type = FieldType.Text)
    private String status;

    @Field(type = FieldType.Long)
    private Long idd;

    @Field(type = FieldType.Text)
    private String tag;

}