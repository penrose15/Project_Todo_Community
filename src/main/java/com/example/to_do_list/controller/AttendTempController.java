package com.example.to_do_list.controller;

import com.example.to_do_list.service.AttendService;
import com.example.to_do_list.service.TeamManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/attend")
@RestController
public class AttendTempController {
    private final AttendService attendService;
    private final TeamManagementService teamManagementService;

    @GetMapping("/")
    public ResponseEntity<Void> saveAllAttend() {
        attendService.saveAll();

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/delete")
    public ResponseEntity<Void> delete() {
        teamManagementService.deleteUser();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
