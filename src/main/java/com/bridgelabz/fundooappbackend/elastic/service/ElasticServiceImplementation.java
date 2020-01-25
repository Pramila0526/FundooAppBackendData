package com.bridgelabz.fundooappbackend.elastic.service;

import java.util.Map;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bridgelabz.fundooappbackend.note.model.Note;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ElasticServiceImplementation implements ElasticService {

	private RestHighLevelClient client;

	private ObjectMapper objectMapper;

	@Autowired
	public ElasticServiceImplementation(RestHighLevelClient client, ObjectMapper objectMapper) {
		this.client = client;
		this.objectMapper = objectMapper;
	}

	static String INDEX = "notedata"; // database
	static String TYPE = "note"; // table

	public String createNote(Note note) throws Exception {

        System.out.println("in elastic");
        Map<String, Object> documentMapper = objectMapper.convertValue(note, Map.class);

        IndexRequest indexRequest = new IndexRequest(INDEX, TYPE,
        				String.valueOf(note.getId()))
        .source(documentMapper); //.index(INDEX).type(TYPE);

        System.out.println("****"+indexRequest);
        System.out.println("after request");
        IndexResponse indexResponse = client.index(indexRequest,
        					RequestOptions.DEFAULT);

        System.out.println("****"+indexResponse);
        System.out.println("note is :"+indexResponse.getResult().name());
        return indexResponse.getResult().name();
	}
   }
