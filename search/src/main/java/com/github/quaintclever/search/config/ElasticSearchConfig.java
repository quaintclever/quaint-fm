package com.github.quaintclever.search.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;

/**
 * <p>
 * desc: es 配置类
 * </p>
 *
 * @author quaint
 * @since 07 December 2020
 */
@Configuration
public class ElasticSearchConfig extends AbstractElasticsearchConfiguration {

    @Value("${spring.data.elasticsearch.cluster-nodes}")
    private String clusterNodes;

    @Override
    public RestHighLevelClient elasticsearchClient() {
        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo(clusterNodes == null ? "localhost:9200" : clusterNodes)
                .build();
        return RestClients.create(clientConfiguration).rest();
    }
}
