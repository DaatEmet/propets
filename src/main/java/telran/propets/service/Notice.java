package telran.propets.service;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import telran.propets.dto.NoticeDto;


@Service
public class Notice {
	
	private static final String WELCOME = "Welcome to service ProPets. We found posts, maybe it iteresting you";

	@Autowired
    public JavaMailSender emailSender;
	
	ObjectMapper mapper = new ObjectMapper();

	@KafkaListener(topics = "${cloudkarafka.topicNotification}")
	public void getMessage(String message) throws JsonMappingException, JsonProcessingException, MessagingException {
		NoticeDto notice = mapper.readValue(message, NoticeDto.class);
		String subject = "Propets";
		sendSimpleMessage("daatemet85@gmail.com", subject, WELCOME + "\n" + notice.getIds());
	}
	
	public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }
	
}
