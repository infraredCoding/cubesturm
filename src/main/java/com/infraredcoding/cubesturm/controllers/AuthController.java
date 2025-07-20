package com.infraredcoding.cubesturm.controllers;

import com.infraredcoding.cubesturm.domain.dto.auth.LoginRequestDTO;
import com.infraredcoding.cubesturm.domain.dto.auth.LoginResponseDTO;
import com.infraredcoding.cubesturm.domain.dto.auth.RegisterRequestDTO;
import com.infraredcoding.cubesturm.domain.models.core.SturmUser;
import com.infraredcoding.cubesturm.infrastructure.utils.JwtUtils;
import com.infraredcoding.cubesturm.repositories.UserRepository;
import com.infraredcoding.cubesturm.infrastructure.services.SturmUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1")
@Service
@RequiredArgsConstructor
public class AuthController {
//    private final CoreService coreService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final SturmUserDetailsService sturmUserDetailsService;
    private final JwtUtils jwtUtils;

    @PostMapping("auth/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO registerRequestDTO){
        if (userRepository.findByUsername(registerRequestDTO.getUsername()).isPresent()) {
            return new ResponseEntity<>("Username already exist", HttpStatus.CONFLICT);
        }

        SturmUser user = new SturmUser();
        user.setUsername(registerRequestDTO.getUsername());
        user.setName(registerRequestDTO.getName());
        user.setEmail(registerRequestDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));

        userRepository.save(user);
        return new ResponseEntity<>("User created", HttpStatus.CREATED);
    }

    @PostMapping("auth/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequestDTO){
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword())
            );

            String token = jwtUtils.generateToken(loginRequestDTO.getUsername());

            return new ResponseEntity<>(new LoginResponseDTO(token, loginRequestDTO.getUsername()), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED);
        }
    }
}
