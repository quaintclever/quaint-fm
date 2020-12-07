package com.github.quaintclever.test.es;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.time.LocalDateTime;

/**
 * <p>
 * desc:
 * </p>
 *
 * @author quaint
 * @since 07 December 2020
 */
public interface DemoArticleRepository extends ElasticsearchRepository<DemoArticleRepository.DemoArticleIndex,Integer> {

    @Data
    @Document(indexName = "demo_article")
    class DemoArticleIndex {

        @Id
        private Integer id;
        /**
         * 文章名称
         */
        @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
        private String title;
        /**
         * 内容
         */
        @Field(type = FieldType.Text)
        private String content;
        /**
         * 浏览量
         */
        @Field(type = FieldType.Integer)
        private Integer pageViews;
        /**
         * 创建时间
         */
        @Field(type = FieldType.Date, format = DateFormat.basic_date_time)
        private LocalDateTime createTime;
    }

}
