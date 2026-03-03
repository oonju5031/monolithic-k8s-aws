package com.example.monolithic.user.controller;

import com.example.monolithic.user.domain.dto.UserRequestDTO;
import com.example.monolithic.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signIn")
    public ResponseEntity<?> signIn(@RequestBody UserRequestDTO request) {
        log.info(">>> UserController signIn: {}", request);

        Map<String, Object> map = userService.signIn(request);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + map.get("access"));
        headers.add("Refresh-Token", (String) map.get("refresh"));
        headers.add("Access-Control-Expose-Headers", "Authorization, Refresh-Token");

        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(headers)
                .body((String)(map.get("access"))) ;
    }

}
