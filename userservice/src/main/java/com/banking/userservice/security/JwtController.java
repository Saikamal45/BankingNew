package com.banking.userservice.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JwtController {

    @Autowired
    private JwtImplementation jwtServiceImplementation;

    @PostMapping("/login")
    public JwtResponse generateToken(@RequestBody JwtRequest jwtRequest) throws Exception {

        return jwtServiceImplementation.createJwtToken(jwtRequest);
    }
}

