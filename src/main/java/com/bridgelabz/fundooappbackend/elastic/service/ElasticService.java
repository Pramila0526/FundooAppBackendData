package com.bridgelabz.fundooappbackend.elastic.service;
import org.springframework.stereotype.Service;
import com.bridgelabz.fundooappbackend.note.model.Note;

@Service
public interface ElasticService {

	public String createNote(Note note) throws Exception;
   }
