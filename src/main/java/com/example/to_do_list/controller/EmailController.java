package com.example.to_do_list.controller;

import com.example.to_do_list.common.email.EmailSender;
import com.example.to_do_list.dto.auth.AuthCodeDto;
import com.example.to_do_list.dto.email.EmailDTO;
import com.example.to_do_list.dto.email.EmailMessageDto;
import com.example.to_do_list.service.EmailService;
import com.example.to_do_list.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/email")
@RequiredArgsConstructor
@RestController
public class EmailController {
    private final EmailSender emailSender;
    private final UsersService usersService;
    private final EmailService emailService;

    @PostMapping("/password")
    public ResponseEntity sendPassword(@RequestBody EmailDTO request) throws InterruptedException {
        String email = request.getEmail();

        usersService.findByEmail(email);
        String tmpPassword = emailService.getTmpPassword();
        EmailMessageDto dto = emailService.Message(email, tmpPassword);
        usersService.changePassword(email, tmpPassword);

        emailSender.sendEmail(dto.getTo(), dto.getSubject(), dto.getText());

        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/authcode")
    public ResponseEntity mailConfirm(@RequestBody EmailDTO emailDTO) throws InterruptedException {
        String authCode = emailService.getTmpPassword();
        EmailMessageDto dto = emailService.Message(emailDTO.getEmail(), authCode);

        emailSender.sendEmail(dto.getTo(), dto.getSubject(), dto.getText());
        emailService.saveAuthCode(emailDTO.getEmail(), authCode);

        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/check-authcode")
    public ResponseEntity verifyAuthCode(@RequestBody AuthCodeDto dto) {
        String authcode = dto.getAuthCode();
        boolean verified = emailService.verifyAuthCode(dto.getEmail(), authcode);

        return new ResponseEntity(verified, HttpStatus.OK);
    }

}
