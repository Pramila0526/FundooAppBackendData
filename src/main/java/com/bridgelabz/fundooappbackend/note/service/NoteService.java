package com.bridgelabz.fundooappbackend.note.service;
import java.util.List;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.RequestHeader;
import com.bridgelabz.fundooappbackend.note.dto.NoteDto;
import com.bridgelabz.fundooappbackend.note.model.Note;
import com.bridgelabz.fundooappbackend.user.response.Response;

/*********************************************************************************************************
 * @author 	:Pramila Mangesh Tawari
 * Purpose	:Note Service Interface
 *
 ***********************************************************************************************************/
public interface NoteService {
	public Response addNewNote(NoteDto noteDto,String token);
	public Response updateNote(@Valid int id,NoteDto updateNoteDto, String token); 
	public Response deleteNote(int id, String token); 
//	public Response findNote(int id,String token);
//	public List<Note> showUserNotes(int id,String token);
	public Response getAllNotes(@RequestHeader String token);
	public List<Note> sortByDescription(String token);
}
