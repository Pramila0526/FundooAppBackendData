package com.bridgelabz.fundooappbackend.note.service;
import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;
import com.bridgelabz.fundooappbackend.note.dto.NoteDto;
import com.bridgelabz.fundooappbackend.note.model.Note;
import com.bridgelabz.fundooappbackend.user.response.Response;

/*********************************************************************************************************
 * @author 	:Pramila Mangesh Tawari
 * Purpose	:Note Service Interface
 *
 ***********************************************************************************************************/
@Service
public interface NoteService {
	public Response addNewNote(NoteDto noteDto,String token) throws Exception;
	public Response updateNote(@Valid int id,NoteDto updateNoteDto, String token); 
	public Response deleteNote(int id, String token); 
//	public Response findNote(int id,String token);
//	public List<Note> showUserNotes(int id,String token);
	public Response getAllNotes(@RequestHeader String token);
	public Response sortByDescription(String token);
	public Response findNote(int id, String token);
	public Response sortByTitle(String token);
	public Response sortByDate(String token);
	public Response pinAndUnpin(@Valid int id,String token);
	public Response archieve(@Valid int id,String token);
	public Response trash(@Valid int id,String token);
	//public List<Note> showUserNotes(String token);

}
