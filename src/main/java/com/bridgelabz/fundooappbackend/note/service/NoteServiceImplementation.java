package com.bridgelabz.fundooappbackend.note.service;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundooappbackend.elastic.service.ElasticService;
import com.bridgelabz.fundooappbackend.note.dto.NoteDto;
import com.bridgelabz.fundooappbackend.note.message.Messages;
import com.bridgelabz.fundooappbackend.note.model.Note;
import com.bridgelabz.fundooappbackend.note.repository.NotesRepository;
import com.bridgelabz.fundooappbackend.note.utility.TokensUtility;
import com.bridgelabz.fundooappbackend.user.exception.custom.InputNotFoundException;
import com.bridgelabz.fundooappbackend.user.exception.custom.NoteNotFoundException;
import com.bridgelabz.fundooappbackend.user.exception.custom.TokenException;
import com.bridgelabz.fundooappbackend.user.exception.custom.UserNotFoundException;
import com.bridgelabz.fundooappbackend.user.model.User;
import com.bridgelabz.fundooappbackend.user.repository.UserRepository;
import com.bridgelabz.fundooappbackend.user.response.Response;

/******************************************************************************************************************
 * @author :Pramila Mangesh Tawari Purpose :To perform Operations on Note
 *
 *********************************************************************************************************/

@Service
@PropertySource("message.properties")
public class NoteServiceImplementation implements NoteService {

	@Autowired
	private NotesRepository notesRepository;

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private TokensUtility tokenUtility;

	@Autowired
	private UserRepository repository;

	@Autowired
	private Environment environment;
	
	@Autowired
	private ElasticService elasticService;

	/**
	 * @return Function to add a New Note
	 * @throws Exception 
	 *
	 *************************************************************************************************/
	public Response addNewNote(NoteDto noteDto, String token) throws Exception {

		if (noteDto.getTitle().isEmpty() || noteDto.getDescription().isEmpty()
				|| (noteDto.getTitle().isEmpty() && noteDto.getDescription().isEmpty())) {
			throw new InputNotFoundException(Messages.INPUT_NOT_FOUND);
		}

		Note note = mapper.map(noteDto, Note.class);

		String userEmail = tokenUtility.getUserToken(token);

		User user = repository.findByEmail(userEmail);

		if (user == null) {
			throw new UserNotFoundException(Messages.USER_NOT_EXISTING);
		}

		note.setUser(user);

		notesRepository.save(note); // Storing Users Data in Database
		elasticService.createNote(note);
		return new Response(Integer.parseInt(environment.getProperty("status.ok.code")),
				environment.getProperty("status.success.notecreated"), environment.getProperty("success.status"));
	}

	/**
	 * @return Function to Update a note
	 *
	 *************************************************************************************************/
	public Response updateNote(@Valid int id, NoteDto updateNoteDto, String token) {

		if (updateNoteDto.getTitle().isEmpty() || updateNoteDto.getDescription().isEmpty()
				|| (updateNoteDto.getTitle().isEmpty() && updateNoteDto.getDescription().isEmpty())) {
			throw new InputNotFoundException(Messages.INPUT_NOT_FOUND);
		}

		Note note = mapper.map(updateNoteDto, Note.class);

		String useremail = tokenUtility.getUserToken(token);

		User user = repository.findByEmail(useremail);

		if (user == null) {
			throw new UserNotFoundException(Messages.USER_NOT_EXISTING);
		}

		note = notesRepository.findById(id);

		if (note == null) {
			return new Response(Integer.parseInt(environment.getProperty("status.badrequest.code")),
					environment.getProperty("status.success.notenotfound"), environment.getProperty("failure.status"));
		}
		note.setUser(user);

		notesRepository.save(note);

		return new Response(Integer.parseInt(environment.getProperty("status.ok.code")),
				environment.getProperty("status.success.noteupdated"), environment.getProperty("success.status"));
	}

	/**
	 * @return Function to Delete a note
	 *
	 *************************************************************************************************/
	public Response deleteNote(int id, String token) {

		String usertoken = tokenUtility.getUserToken(token);

		User user = repository.findByEmail(usertoken);

		if (user == null) {
			throw new UserNotFoundException(Messages.USER_NOT_EXISTING);
		}

		Note note = notesRepository.findById(id);

		if (note == null) {
			throw new NoteNotFoundException(Messages.NOTE_NOT_FOUND);
		}

		note.setUser(user);

		notesRepository.delete(note);

		return new Response(Integer.parseInt(environment.getProperty("status.ok.code")),
				environment.getProperty("status.success.notedeleted"), environment.getProperty("success.status"));
	}

	/**
	 * @return Function get all notes
	 *
	 *************************************************************************************************/
	public Response getAllNotes(String token) {

		String usertoken = tokenUtility.getUserToken(token);
		
		if (usertoken.isEmpty()) {
			throw new TokenException(Messages.INVALID_TOKEN);
		}
		
		User user = repository.findByEmail(usertoken);

		if (user == null) {
			throw new UserNotFoundException(Messages.USER_NOT_EXISTING);
		}
		
		//System.out.println(note);
		//	return note;
		return new Response(Integer.parseInt(environment.getProperty("status.ok.code")),
			environment.getProperty("status.success.allnotes"),  notesRepository.findAll());
		
	}
	/**
	 * @return Find a note of one particulat user by id
	 *
	 *************************************************************************************************/
	public Response findNote(int id, String token) {
		String usertoken = tokenUtility.getUserToken(token);
		if (usertoken.isEmpty()) {
			throw new TokenException(Messages.INVALID_TOKEN);
		}
		User user = repository.findByEmail(usertoken);

		if (user == null) {
			throw new UserNotFoundException(Messages.USER_NOT_EXISTING);
		}

		Note note = notesRepository.findById(id);
		if (note == null) {
			throw new NoteNotFoundException(Messages.NOTE_NOT_FOUND);
		}
		return new Response(Integer.parseInt(environment.getProperty("status.ok.code")),
				environment.getProperty("status.success.notefound"), note);

	}
	/**
	 * @return Function to sort notes by title
	 *
	 *************************************************************************************************/
	public List<Note> sortByTitle(String token) {
		
		String usertoken = tokenUtility.getUserToken(token);
		
		System.out.println(usertoken);
		
		if (usertoken.isEmpty()) {
			throw new TokenException(Messages.INVALID_TOKEN);
		}
		User user = repository.findByEmail(usertoken);
		
		if (user == null) {
			throw new UserNotFoundException(Messages.USER_NOT_EXISTING);
		}
				
		List<Note> list = notesRepository.findAll().stream().filter(e -> e.getUser().getId() == user.getId())
				.collect(Collectors.toList());

		list = list.stream().sorted((list1, list2) -> list1.getTitle().compareTo(list2.getTitle()))
				.collect(Collectors.toList());
		return list;
	}

	/**
	 * @return Function to sort notes by Description
	 *
	 *************************************************************************************************/
	public List<Note> sortByDescription(String token) {
		
		String usertoken = tokenUtility.getUserToken(token);
		
		System.out.println(usertoken);
		
		if (usertoken.isEmpty()) {
			throw new TokenException(Messages.INVALID_TOKEN);
		}
		
		User user = repository.findByEmail(usertoken);
		
		if (user == null) {
			throw new UserNotFoundException(Messages.USER_NOT_EXISTING);
		}
		
		List<Note> list = notesRepository.findAll().stream().filter(e -> e.getUser().getId() == user.getId())
				.collect(Collectors.toList());

		list = list.stream().sorted((list1, list2) -> list1.getDescription().compareTo(list2.getDescription()))
				.collect(Collectors.toList());
		return list;
	}

/**
 * @return Function to sort notes by Date
 *
 *************************************************************************************************/
	public List<Note> sortByDate(String token) {
		
		String usertoken = tokenUtility.getUserToken(token);
		
		System.out.println(usertoken);
		
		if (usertoken.isEmpty()) {
			throw new TokenException(Messages.INVALID_TOKEN);
		}
		
		User user = repository.findByEmail(usertoken);
		
		if (user == null) {
			throw new UserNotFoundException(Messages.USER_NOT_EXISTING);
		}
		
		System.out.println("user1" + user);
				
		List<Note> list = notesRepository.findAll().stream().filter(e -> e.getUser().getId() == user.getId())
				.collect(Collectors.toList());

		list = list.stream()
				.sorted((list1, list2) -> list1.getNoteRegistrationDate().compareTo(list2.getNoteRegistrationDate()))
				.collect(Collectors.toList());
		
		return list;
	}

/**
 * @return Function Pin and Unpin the Notes
 *
 *************************************************************************************************/
    public Response pinAndUnpin(@Valid int id,String token) {
		
		String userToken = tokenUtility.getUserToken(token);
				
		if (userToken.isEmpty()) {
			throw new TokenException(Messages.INVALID_TOKEN);
		}
		
		Note note = notesRepository.findById(id);
		
		if (note == null) {
			throw new NoteNotFoundException(Messages.NOTE_NOT_FOUND);
		}
		
		if(note.isPin() == false)
		{
			note.setPin(true);
			notesRepository.save(note);
			return new Response(Integer.parseInt(environment.getProperty("status.ok.code")),
					environment.getProperty("status.success.pin"), environment.getProperty("success.status"));
		}
		else 
		{
			note.setPin(false);
			notesRepository.save(note);
			return new Response(Integer.parseInt(environment.getProperty("status.ok.code")),
					environment.getProperty("status.success.unpin"), environment.getProperty("success.status"));
		}
	}
    
/**
 * @return Function archieve the Notes
 *
 *************************************************************************************************/
 public Response archieve(@Valid int id,String token) {
		
		String userToken = tokenUtility.getUserToken(token);
				
		if (userToken.isEmpty()) {
			throw new TokenException(Messages.INVALID_TOKEN);
		}
		
		Note note = notesRepository.findById(id);
		
		if (note == null) {
			throw new NoteNotFoundException(Messages.NOTE_NOT_FOUND);
		}
		
		if(note.isArchieve() == false)
		{
			note.setArchieve(true);
			notesRepository.save(note);
			return new Response(Integer.parseInt(environment.getProperty("status.ok.code")),
					environment.getProperty("status.success.archieve"), environment.getProperty("success.status"));
		}
		else 
		{
			note.setArchieve(false);
			notesRepository.save(note);
			return new Response(Integer.parseInt(environment.getProperty("status.ok.code")),
					environment.getProperty("status.success.disarchieve"), environment.getProperty("success.status"));
		}
	}
 
 /**
  * @return Function Trash the Notes
  *
  *************************************************************************************************/
 public Response trash(@Valid int id,String token) {
		
		String userToken = tokenUtility.getUserToken(token);
				
		if (userToken.isEmpty()) {
			throw new TokenException(Messages.INVALID_TOKEN);
		}
		
		Note note = notesRepository.findById(id);
		
		if (note == null) {
			throw new NoteNotFoundException(Messages.NOTE_NOT_FOUND);
		}
		
		if(note.isTrash() == false)
		{
			note.setTrash(true);
			notesRepository.save(note);
			return new Response(Integer.parseInt(environment.getProperty("status.ok.code")),
					environment.getProperty("status.success.trash"), environment.getProperty("success.status"));
		}
		else 
		{
			note.setTrash(false);
			notesRepository.save(note);
			return new Response(Integer.parseInt(environment.getProperty("status.ok.code")),
					environment.getProperty("status.success.distrash"), environment.getProperty("success.status"));
		}
	}
}

/*
*//**
	 * @return Function to find a note
	 *
	 *************************************************************************************************/
/*
 * public Response findNote(int id, String token) { String usertoken =
 * tokenUtility.getUserToken(token); if (usertoken.isEmpty()) { throw new
 * TokenException(Messages.INVALID_TOKEN); } User user =
 * repository.findByEmail(usertoken);
 * 
 * if (user == null) { throw new
 * UserNotFoundException(Messages.USER_NOT_EXISTING); }
 * 
 * Note note = notesRepository.findById(id);
 * 
 * if (note == null) { throw new NoteNotFoundException(Messages.NOTE_NOT_FOUND);
 * } return new
 * Response(Integer.parseInt(environment.getProperty("status.ok.code")
 * ),environment.getProperty("status.success.notefound"),note);
 * 
 * }
 * 
 *//**
	 * @return Function to Show a particular user's note
	 *
	 *************************************************************************************************/

/*
 * public List<Note> showUserNotes (int id, String token) { String usertoken =
 * tokenUtility. getUserToken( token); if (usertoken. isEmpty()) { throw new
 * TokenException (Messages. INVALID_TOKEN ); } User user = repository.
 * findByEmail( usertoken); Note note = notesRepository .findById(id) ; return
 * notesRepository .findAll(); }
 * 
 */