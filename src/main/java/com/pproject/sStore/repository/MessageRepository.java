package com.pproject.sStore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pproject.sStore.model.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {

}
