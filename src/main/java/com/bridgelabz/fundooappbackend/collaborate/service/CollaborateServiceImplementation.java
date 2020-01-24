package com.bridgelabz.fundooappbackend.collaborate.service;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import com.bridgelabz.fundooappbackend.collaborate.collaboratemessage.CollaborateMessages;
import com.bridgelabz.fundooappbackend.collaborate.dto.CollaborateDto;
import com.bridgelabz.fundooappbackend.collaborate.model.Collaborate;
import com.bridgelabz.fundooappbackend.collaborate.repository.CollaborateRepository;
import com.bridgelabz.fundooappbackend.note.message.Messages;
import com.bridgelabz.fundooappbackend.note.model.Note;
import com.bridgelabz.fundooappbackend.note.repository.NotesRepository;
import com.bridgelabz.fundooappbackend.note.utility.TokensUtility;
import com.bridgelabz.fundooappbackend.user.exception.custom.UserNotFoundException;
import com.bridgelabz.fundooappbackend.user.response.Response;
/*********************************************************************************************************
 * @author 	:Pramila Mangesh Tawari
 * Purpose	:Collaborate Implementation Class To Perform Collaboration
 *
 ***********************************************************************************************************/
@Service
public class CollaborateServiceImplementation implements CollaborateService {

	@Autowired
	private CollaborateRepository collaborateRepository;
	
	@Autowired
	private TokensUtility tokenUtility;
	
	@Autowired
	private NotesRepository noteRepository;
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	public Response Collaborate(CollaborateDto collaboratorDto, String token) {
		System.out.println("1");
		Collaborate collaborator = mapper.map(collaboratorDto, Collaborate.class);
		System.out.println("2");
		String useremail = tokenUtility.getUserToken(token);
		System.out.println("3");
		if (useremail == null) {
			throw new UserNotFoundException(Messages.USER_NOT_EXISTING);
		}
		System.out.println("5");
		collaborator.setSenderMail(useremail);
		System.out.println("6");

		Note note = noteRepository.findById(collaboratorDto.getNoteId());
		if (note == null) {
			throw new NoteNOTFoundException(CollaborateMessages.NOTE_NOT_FOUND);
		}

		javaMailSender.send(NoteMessageUtility.sendMail(collaborator.getSenderMail(), collaboratorDto.getReceiverMail(),
				"this is the note" + note));

		System.out.println("7");
		collaborateRepository.save(collaborator);
		System.out.println("8");

		return new Responses(CollaborateMessages.OK, null, CollaborateMessages.COLLABORATOR_ADDED);
	}
}
