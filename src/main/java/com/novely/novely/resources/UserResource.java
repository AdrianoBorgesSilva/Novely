package com.novely.novely.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.novely.novely.domain.User;
import com.novely.novely.dto.LoginResponseDTO;
import com.novely.novely.dto.UserDTO;
import com.novely.novely.dto.UserLoginDTO;
import com.novely.novely.dto.UserSignUpDTO;
import com.novely.novely.dto.UserUpdateDTO;
import com.novely.novely.security.CustomUserDetails;
import com.novely.novely.security.CustomUserDetailsService;
import com.novely.novely.security.JwtUtil;
import com.novely.novely.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserResource {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @GetMapping
    public ResponseEntity<List<UserDTO>> findAll() {
        List<UserDTO> users = userService.findAll().stream().map(UserDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> findById(@PathVariable String userId) {
        User user = userService.findById(userId);
        return ResponseEntity.ok(new UserDTO(user));
    }

    @PostMapping("/register")
    public ResponseEntity<Void> createUser(@RequestBody @Valid UserSignUpDTO userSignUpDTO) {
       
        User user = userService.createUser(userSignUpDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getId()).toUri();
       
        return ResponseEntity.created(uri).build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid UserLoginDTO userLogin) {
        
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLogin.getEmail(), userLogin.getPassword()));
        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(userLogin.getEmail());
        String token = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<Void> update(@RequestBody @Valid UserUpdateDTO userUpdateDTO, @PathVariable String userId, Authentication authentication) {
        
        User user = userService.findByEmail(authentication.getName());
        userService.updateUser(userUpdateDTO, userId, user.getId());

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<Void> delete(@PathVariable String userId, Authentication authentication) {
        userService.deleteUser(authentication.getName(), userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{userId}/favorites/{novelId}")
    public ResponseEntity<Void> addFavorite(@PathVariable String userId, @PathVariable String novelId, Authentication authentication) {
       
        User user = userService.findByEmail(authentication.getName());
        userService.addFavorite(userId, novelId, user.getId());

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}/favorites/{novelId}")
    public ResponseEntity<Void> removeFavorite(@PathVariable String userId, @PathVariable String novelId, Authentication authentication) {
        
        User user = userService.findByEmail(authentication.getName());
        userService.removeFavorite(userId, novelId, user.getId());

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{userId}/super-favorites/{novelId}")
    public ResponseEntity<Void> addSuperFavorite(@PathVariable String userId, @PathVariable String novelId, Authentication authentication) {
        
        User user = userService.findByEmail(authentication.getName());
        userService.addSuperFavorite(userId, novelId, user.getId());

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}/super-favorites/{novelId}")
    public ResponseEntity<Void> removeSuperFavorite(@PathVariable String userId, @PathVariable String novelId, Authentication authentication) {
        
        User user = userService.findByEmail(authentication.getName());
        userService.removeSuperFavorite(userId, novelId, user.getId());
        
        return ResponseEntity.noContent().build();
    }
}