package com.organicshop.domain.dto.view;

import java.time.LocalDateTime;

public class ContactMessagesViewDto {

    private Long id;
    private String name;
    private String email;
    private String subject;
    private String description;
    private LocalDateTime createdOn;


    public ContactMessagesViewDto() {
    }

    public Long getId() {
        return id;
    }

    public ContactMessagesViewDto setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ContactMessagesViewDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public ContactMessagesViewDto setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getSubject() {
        return subject;
    }

    public ContactMessagesViewDto setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ContactMessagesViewDto setDescription(String description) {
        this.description = description;
        return this;
    }
    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public ContactMessagesViewDto setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
        return this;
    }
}
