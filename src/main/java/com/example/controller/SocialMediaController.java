package com.example.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */


 @RestController
 @RequestMapping("/")
public class SocialMediaController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private MessageService messageService;

    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody Account account) {
        if (account.getUsername() == null || account.getUsername().trim().isEmpty()
            || account.getPassword() == null || account.getPassword().length() < 4) {

                return ResponseEntity.badRequest().build();

            }

            if (accountService.findByUsername(account.getUsername()).isPresent()) {

                return ResponseEntity.status(409).build();
            }

            Account savedAccount = accountService.register(account);
            return ResponseEntity.ok(savedAccount);
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody Account loginRequest) {

        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        Optional<Account> PotentialAccount = accountService.login(username, password);

        if (PotentialAccount.isPresent()) {

            return ResponseEntity.ok(PotentialAccount.get());

        } else {

            return ResponseEntity.status(401).build();

        }
    }

    @PostMapping("messages")
    public ResponseEntity<?> createMessage(@RequestBody Message message) {

        if (message.getMessageText() == null || message.getMessageText().trim().isEmpty()
            || message.getMessageText().length() > 255) {
                return ResponseEntity.badRequest().build();
            }

            Optional<Account> poster = accountService.findAccountById(message.getPostedBy());
            if (poster.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            Message created = messageService.createMessage(message);
            return ResponseEntity.ok(created);
    }

    @GetMapping("messages")
    public List<Message> getAllMessages() {
        return messageService.getAllMessages();
    }

    @GetMapping("messages/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable Integer messageId) {
        Optional<Message> potetionaMessage = messageService.getMessageById(messageId);
        return potetionaMessage.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.ok().build());
    }

    @GetMapping("accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getMessagesByAccount(@PathVariable Integer accountId) {
        List<Message> messages = messageService.getMessagesByAccountId(accountId);
        return ResponseEntity.ok(messages);
    }

    @DeleteMapping("messages/{messageId}")
    public ResponseEntity<?> deleteMessage(@PathVariable Integer messageId) {
        Optional<Message> potentialMessage = messageService.getMessageById(messageId);
        if (potentialMessage.isPresent()) {
            messageService.deleteMessage(messageId);
            return ResponseEntity.ok(1);
        } else {
            return ResponseEntity.ok().build();
        }
    }

    @PatchMapping("messages/{messageId}")
    public ResponseEntity<?> updatMessageText(@PathVariable Integer messageId, @RequestBody Message body) {
        Optional<Message> potentialMessage = messageService.getMessageById(messageId);
        if(potentialMessage.isPresent()) {
            Message existing = potentialMessage.get();

            String newMessage = body.getMessageText();
            if (newMessage == null || newMessage.trim().isEmpty() || newMessage.length() > 255) {
                return ResponseEntity.badRequest().build();
            }

            existing.setMessageText(newMessage);
            messageService.updatMessage(existing);
            return ResponseEntity.ok(1);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }


}
