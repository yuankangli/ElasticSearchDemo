package com.elastic.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

/**
 * 相关注解说明,请看
 * https://docs.spring.io/spring-data/elasticsearch/docs/current/reference/html/#elasticsearch.mapping
 */
@Document(indexName = "poetry", refreshInterval = "-1")
public class Poetry {

    @Id
    @Field(store = true, type = FieldType.Keyword)
    private String id;

    /**
     * 标题
     * store: 是否保存原文
     */
    @Field(analyzer = "ik_max_word", store = true, searchAnalyzer = "ik_smart", type = FieldType.Text)
    private String title;

    /**
     * 内容
     */
    @Field(analyzer = "ik_max_word", store = true, searchAnalyzer = "ik_smart", type = FieldType.Text)
    private String content;

    @Field(store = true, type = FieldType.Integer)
    private int userId;

    @Field(store = true, type = FieldType.Integer)
    private int weight;

    @Field(store = true, type = FieldType.Date, format = DateFormat.basic_date_time)
    private Date createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}