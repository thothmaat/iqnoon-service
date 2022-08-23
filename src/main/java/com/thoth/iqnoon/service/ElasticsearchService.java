package com.thoth.iqnoon.service;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ElasticsearchService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;


    private static final Logger logger = LoggerFactory.getLogger(ElasticsearchService.class);
    public SearchResponse search(QueryBuilder searchQueryBuilder,String index){
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(searchQueryBuilder);
        SearchRequest request = new SearchRequest();
        request.indices(index);
        request.source(searchSourceBuilder);
        SearchResponse response = null;
        try{
            response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        }catch (IOException e){
            logger.warn("elasticsearch search exception:{}",searchQueryBuilder,e);
        }
        return response;
    }


    public Integer count(QueryBuilder countQueryBuilder,String index){
        CountRequest request = new CountRequest();
        request.indices(index);
        request.query(countQueryBuilder);
        CountResponse countResponse = null;
        try{
            countResponse = restHighLevelClient.count(request, RequestOptions.DEFAULT);
        }catch (IOException e){
            logger.warn("elasticsearch count exception:{}",countQueryBuilder,e);
        }
        assert countResponse != null;
        return (int)countResponse.getCount();
    }


    public SearchResponse searchAll(QueryBuilder searchQueryBuilder,String index){
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(searchQueryBuilder);
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(count(searchQueryBuilder,index));
        SearchRequest request = new SearchRequest();
        request.indices(index);
        request.source(searchSourceBuilder);
        SearchResponse response = null;
        try{
            response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        }catch (IOException e){
            logger.warn("elasticsearch searchAll exception:{}",searchSourceBuilder,e);
        }
        return response;
    }
}
