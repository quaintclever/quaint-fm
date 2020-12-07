package com.github.quaintclever.test.es;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * desc: 请求dto
 * </p>
 *
 * @author quaint
 * @since 07 December 2020
 */
@Data
public class DemoArticleDto {

    private List<Result> articleList;

    private Long total;

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class Param extends BasePageDto{
        private String title;
        private String content;
    }

    @Data
    public static class Result{
        private Integer id;
        private String title;
        private String content;
        private Integer pageViews;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime createTime;
    }

}
