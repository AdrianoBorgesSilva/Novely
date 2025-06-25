package com.novely.novely.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
import com.novely.novely.security.AuthenticationService;
import com.novely.novely.security.SecurityConfig;
import com.novely.novely.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
@SecurityRequirement(name = SecurityConfig.SECURITY)
@Tag(name = "Users", description = "Controller responsible for actions related to users")
public class UserResource {

    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final AuthenticationManager authenticationManager;

    public UserResource(UserService userService, AuthenticationService authenticationService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping
    @Operation(summary = "List all users", description = "Returns a list of all users")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Users returned successfully"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<UserDTO>> findAll() {
        List<UserDTO> users = userService.findAll().stream().map(UserDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Get a single user", description = "Returns the user corresponding to the provided ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User returned successfully"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<UserDTO> findById(@PathVariable String userId) {
        User user = userService.findById(userId);
        return ResponseEntity.ok(new UserDTO(user));
    }

    @PostMapping("/auth/signup")
    @Operation(summary = "Create a user", description = "Creates a default user in the database")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid data or validation error"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> createUser(@RequestBody @Valid UserSignUpDTO userSignUpDTO) {
       
        User user = userService.createUser(userSignUpDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getId()).toUri();
       
        return ResponseEntity.created(uri).build();
    }

    @PostMapping("/auth/login")
    @Operation(summary = "User login", description = "User login from email and password")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User logged in, token generated successfully"),
        @ApiResponse(responseCode = "401", description = "Invalid credentials (incorrect email or password)"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid UserLoginDTO userLogin) {
        
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLogin.getEmail(), userLogin.getPassword()));
      
        String token = authenticationService.authenticate(authentication);

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PatchMapping("/{userId}")
    @Operation(summary = "Update user", description = "Partial update of an existing user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User updated successfully"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "401", description = "Unauthenticated (missing or invalid token)"),
        @ApiResponse(responseCode = "403", description = "Forbidden (no permission to access this resource)"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> update(@RequestBody @Valid UserUpdateDTO userUpdateDTO, @PathVariable String userId, Authentication authentication) {
        
        User user = userService.findByEmail(authentication.getName());
        userService.updateUser(userUpdateDTO, userId, user.getId());

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "Delete a user", description = "Soft Delete of an existing user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "User deleted successfully"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "401", description = "Unauthenticated (missing or invalid token)"),
        @ApiResponse(responseCode = "403", description = "Forbidden (no permission to access this resource)"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> delete(@PathVariable String userId, Authentication authentication) {
        
        User user = userService.findByEmail(authentication.getName());
        userService.deleteUser(userId, user.getId());
        
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{userId}/favorites/{novelId}")
    @Operation(summary = "Add favorite", description = "Add a Novel to favorites")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Novel added to favorites successfully"),
        @ApiResponse(responseCode = "404", description = "No users or Novels found"),
        @ApiResponse(responseCode = "401", description = "Unauthenticated (missing or invalid token)"),
        @ApiResponse(responseCode = "403", description = "Forbidden (no permission to access this resource)"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> addFavorite(@PathVariable String userId, @PathVariable String novelId, Authentication authentication) {
       
        User user = userService.findByEmail(authentication.getName());
        userService.addFavorite(userId, novelId, user.getId());

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}/favorites/{novelId}")
    @Operation(summary = "Remove favorite", description = "Remove a Novel from favorites")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Novel successfully removed from favorites"),
        @ApiResponse(responseCode = "404", description = "No users or Novels found"),
        @ApiResponse(responseCode = "401", description = "Unauthenticated (missing or invalid token)"),
        @ApiResponse(responseCode = "403", description = "Forbidden (no permission to access this resource)"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> removeFavorite(@PathVariable String userId, @PathVariable String novelId, Authentication authentication) {
        
        User user = userService.findByEmail(authentication.getName());
        userService.removeFavorite(userId, novelId, user.getId());

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{userId}/super-favorites/{novelId}")
    @Operation(summary = "Add super favorite", description = "Add a Novel to super favorites")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Novel successfully added to super favorites"),
        @ApiResponse(responseCode = "404", description = "No users or Novels found"),
        @ApiResponse(responseCode = "401", description = "Unauthenticated (missing or invalid token)"),
        @ApiResponse(responseCode = "403", description = "Forbidden (no permission to access this resource)"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> addSuperFavorite(@PathVariable String userId, @PathVariable String novelId, Authentication authentication) {
        
        User user = userService.findByEmail(authentication.getName());
        userService.addSuperFavorite(userId, novelId, user.getId());

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}/super-favorites/{novelId}")
    @Operation(summary = "Remove super favorite", description = "Remove a Novel from super favorites")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Novel successfully removed from super favorites"),
        @ApiResponse(responseCode = "404", description = "No users or Novels found"),
        @ApiResponse(responseCode = "401", description = "Unauthenticated (missing or invalid token)"),
        @ApiResponse(responseCode = "403", description = "Forbidden (no permission to access this resource)"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> removeSuperFavorite(@PathVariable String userId, @PathVariable String novelId, Authentication authentication) {
        
        User user = userService.findByEmail(authentication.getName());
        userService.removeSuperFavorite(userId, novelId, user.getId());
        
        return ResponseEntity.noContent().build();
    }
}