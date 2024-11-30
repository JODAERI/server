package com.sherpa.jodaeri.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import javax.swing.text.Document;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentRetrievalService {
    private final ElasticsearchRestTemplate elasticsearchTemplate;

    public List<String> searchDocuments(String query) {
        try {
            Query searchQuery = new NativeSearchQueryBuilder()
                    .withQuery(QueryBuilders.matchQuery("content", query))
                    .build();
            SearchHits<Document> hits = elasticsearchTemplate.search(searchQuery, Document.class);
            return hits.stream()
                    .map(hit -> hit.getContent().toString())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("문서 검색 실패: ", e);
            return List.of();
        }
    }
}