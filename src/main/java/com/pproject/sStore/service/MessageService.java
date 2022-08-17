package com.pproject.sStore.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pproject.sStore.model.Message;
import com.pproject.sStore.repository.MessageRepository;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public List<Message> getAllMessage() {
        return messageRepository.findAll();
    }

    public Message newMessage(Message message) {
        return messageRepository.save(message);
    }

    public void delMessage(Long id) {
        messageRepository.deleteById(id);
    }

}
