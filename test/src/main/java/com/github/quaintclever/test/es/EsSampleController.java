package com.github.quaintclever.test.es;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.sort.ScriptSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * desc:
 * </p>
 *
 * @author quaint
 * @since 07 December 2020
 */
@RestController
public class EsSampleController {

    @Autowired
    RestHighLevelClient restHighLevelClient;

//    @Autowired
//    ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Autowired
    DemoArticleRepository demoArticleRepository;


    @PostMapping("getEsData")
    public Boolean getEsData(String param) throws IOException {
        GetIndexRequest indexRequest = new GetIndexRequest(param);
        boolean exists = restHighLevelClient.indices().exists(indexRequest, RequestOptions.DEFAULT);
        return exists;
    }

    @PostMapping("getDemoArticleList")
    public DemoArticleDto getDemoArticleList(@RequestBody DemoArticleDto.Param param) {

        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        // bool 查询条件
        BoolQueryBuilder bool = new BoolQueryBuilder();
        if (StringUtils.isNotEmpty(param.getTitle())){
            bool.should(QueryBuilders.matchQuery("title",param.getTitle()));
        }
        if (StringUtils.isNotEmpty(param.getContent())){
            bool.should(QueryBuilders.matchQuery("content",param.getContent()));
        }

        // 过滤作品浏览数 大于1的
        bool.filter(QueryBuilders.rangeQuery("pageViews").gt(1));

        queryBuilder.withQuery(bool);

        // 数据排序, 随机排序, 随机后根据时间降序
        queryBuilder.withSort(SortBuilders.scriptSort(new Script("Math.random()"), ScriptSortBuilder.ScriptSortType.NUMBER));
        queryBuilder.withSort(SortBuilders.fieldSort("createTime").order(SortOrder.DESC));

        // 数据设置分页, 不设置默认为10条数据
        if (param.esStandardization()){
            queryBuilder.withPageable(PageRequest.of(param.getPageNum(),param.getPageSize()));
        }

        // 查询
        Page<DemoArticleRepository.DemoArticleIndex> search = demoArticleRepository.search(queryBuilder.build());
        return this.processIndex2Dto(search);
    }

    /**
     * 将indexList 转换为 dtoList 并返回 dto
     * @param search pageList
     * @return dto
     */
    private DemoArticleDto processIndex2Dto(Page<DemoArticleRepository.DemoArticleIndex> search){

        // 将 search 查询出来的数据 转换为 dto list
        List<DemoArticleDto.Result> results = search.getContent().stream().map(idx -> {
            DemoArticleDto.Result result = new DemoArticleDto.Result();
            BeanUtils.copyProperties(idx, result);
            return result;
        }).collect(Collectors.toList());

        // 封装返回dto
        DemoArticleDto reDto = new DemoArticleDto();
        reDto.setArticleList(results);
        reDto.setTotal(search.getTotalElements());
        return reDto;
    }

}
