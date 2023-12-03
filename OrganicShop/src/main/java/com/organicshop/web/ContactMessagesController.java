package com.organicshop.web;

import com.organicshop.service.ContactService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/messages")
public class ContactMessagesController {

    private final ContactService contactService;

    public ContactMessagesController(ContactService contactService) {
        this.contactService = contactService;
    }


    @GetMapping
    public String getMessages(Model model) {
        model.addAttribute("messages", this.contactService.getAllMessages());
        return "contact-messages";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteMessageById(@PathVariable("id") Long messageId) {

        this.contactService.deleteMessageById(messageId);

        return "redirect:/messages";
    }
}
