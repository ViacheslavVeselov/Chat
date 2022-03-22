package com.example.chatserver.models;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "message_test") //TODO replace with message table
public class ChatMessage {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "text")
    private String text;

    @Column(name = "path_to_file")
    private String pathToFile;

    @Column(name = "chat_id")
    private UUID chatId;

    //TODO change name back to userId?
    @Column(name = "sender_id")
    private UUID senderId;

    //TODO add to db?
    @Column(name = "recipient_id")
    private UUID recipientId;

    @Column(name = "message_date_time")
    private Date messageDateTime;

    //TODO add to db
    private MessageStatus status;

    public ChatMessage() {}

    public ChatMessage(
            String text,
            String pathToFile,
            UUID chatId,
            UUID senderId,
            UUID recipientId,
            Date messageDateTime,
            MessageStatus status
    ) {
        this.text = text;
        this.pathToFile = pathToFile;
        this.chatId = chatId;
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.messageDateTime = messageDateTime;
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPathToFile() {
        return pathToFile;
    }

    public void setPathToFile(String pathToFile) {
        this.pathToFile = pathToFile;
    }

    public UUID getChatId() {
        return chatId;
    }

    public void setChatId(UUID chatId) {
        this.chatId = chatId;
    }

    public UUID getSenderId() {
        return senderId;
    }

    public void setSenderId(UUID senderId) {
        this.senderId = senderId;
    }

    public UUID getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(UUID recipientId) {
        this.recipientId = recipientId;
    }

    public Date getMessageDateTime() {
        return messageDateTime;
    }

    public void setMessageDateTime(Date messageDateTime) {
        this.messageDateTime = messageDateTime;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }
}
