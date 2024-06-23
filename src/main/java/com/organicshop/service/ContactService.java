package com.organicshop.service;

import com.organicshop.domain.dto.binding.ContactBindingDto;
import com.organicshop.domain.dto.view.ContactMessagesViewDto;
import com.organicshop.domain.entities.ContactEntity;
import com.organicshop.exception.NotFoundObjectException;
import com.organicshop.repositories.ContactRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static com.organicshop.constants.Messages.*;

@Service
public class ContactService {

    private final ModelMapper modelMapper;
    private final ContactRepository contactRepository;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_TIME_NOW_PATTERN);

    public ContactService(ModelMapper modelMapper,
                          ContactRepository contactRepository) {
        this.modelMapper = modelMapper;
        this.contactRepository = contactRepository;
    }

    public void saveContactMessage(ContactBindingDto contactBinding) {

        ContactEntity contactToSave = mapToContactEntity(contactBinding);

        contactToSave.setCreatedOn(LocalDateTime.parse(dateTimeFormatter.format(LocalDateTime.now())));

        this.contactRepository.saveAndFlush(contactToSave);

    }

    public ContactEntity mapToContactEntity(ContactBindingDto contactBinding) {
        return this.modelMapper.map(contactBinding, ContactEntity.class);
    }

    public List<ContactMessagesViewDto> getAllMessages() {
        return this.contactRepository
                .findAll()
                .stream()
                .map(this::mapToContactMessagesViewDto)
                .collect(Collectors.toList());

    }

    private ContactMessagesViewDto mapToContactMessagesViewDto(ContactEntity contactEntity) {
        return this.modelMapper.map(contactEntity, ContactMessagesViewDto.class);
    }

    public ContactMessagesViewDto getMessageById(Long id) {
        final ContactEntity messageById = this.contactRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundObjectException(id, MESSAGE));

        return this.mapToContactMessagesViewDto(messageById);
    }

    public void deleteMessageById(Long messageId) {
        this.contactRepository.deleteById(messageId);
    }
}