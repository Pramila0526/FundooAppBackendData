package com.bridgelabz.fundooappbackend.note.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundooappbackend.note.dto.NoteDto;
import com.bridgelabz.fundooappbackend.note.service.NoteServiceImplementation;
import com.bridgelabz.fundooappbackend.user.message.Messages;
import com.bridgelabz.fundooappbackend.user.response.Response;
/*********************************************************************************************************
 * @author 	:Pramila Mangesh Tawari
 * Purpose	:Notes Controller For Generating API's
 *
 ***********************************************************************************************************/
@RestController   
@RequestMapping("/note")
public class NoteController 
{
      @Autowired
      NoteServiceImplementation notesServiceImplementation;
	
      // Testing API
      @GetMapping("/demoo")
      public String demo()
      {
    	  return "Hello User!!";
      }
      
      // Adding New Note
    @PostMapping("/addnewnote")
  	public ResponseEntity<Response> addNewNote(@RequestBody NoteDto noteDto,@RequestHeader String token)
  	{
  		return new ResponseEntity<Response>(notesServiceImplementation.addNewNote(noteDto,token), HttpStatus.OK); // give response for user 200
  	}
    
    // Updating a Note
    @PutMapping("/updatenote/{id}")
  	public ResponseEntity<Response> updateNote(@PathVariable int id,@RequestBody NoteDto updateNoteDto,@RequestHeader String token)
  	{
  		return new ResponseEntity<Response>(notesServiceImplementation.updateNote(id,updateNoteDto, token), HttpStatus.OK); // give response for user 200
  	}
    
    // Delete a Note
    @DeleteMapping("/{id}")
  	public ResponseEntity<Response> deleteNote(@PathVariable int id,@RequestHeader String token)
  	{
  		return new ResponseEntity<Response>(notesServiceImplementation.deleteNote(id, token), HttpStatus.OK); // give response for user 200
  	}
    
        // Getting all Notes
    @GetMapping("/getallnotes")
	public Response getAllNotes(@RequestHeader String token) 
	{
		return new Response(Messages.OK,null,notesServiceImplementation.getAllNotes(token));
	}
    
    // Sorting notes By title
    @GetMapping("/sortbytitle")
	public Response sortByTitle(@RequestHeader String token) 
	{
		return new Response(Messages.OK,null,notesServiceImplementation.sortByTitle(token));
	}
    
    // Sorting notes By Description
    @GetMapping("/sortbydescription")
	public Response sortByDescription(@RequestHeader String token) 
	{
		return new Response(Messages.OK,null,notesServiceImplementation.sortByDescription(token));
	}
    
 // Sorting notes By Date
    @GetMapping("/sortbydate")
	public Response sortByDate(@RequestHeader String token) 
	{
		return new Response(Messages.OK,null,notesServiceImplementation.sortByDate(token));
	}
}







/*
 * //Finding a note
 * 
 * @GetMapping("/findnote/{id}") public ResponseEntity<Response>
 * findNote(@PathVariable int id, @RequestHeader String token) { return new
 * ResponseEntity<Response>(notesServiceImplementation.findNote(id,token),
 * HttpStatus.OK); }
 * 
 * // Displaying all the notes of particular user
 * 
 * @GetMapping("/showallnotes/{id}") public Response showUserNotes(@PathVariable
 * int id,@RequestHeader String token) { return new
 * Response(Messages.OK,null,notesServiceImplementation.showUserNotes(id,token))
 * ; }
 */