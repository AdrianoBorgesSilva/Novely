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
@Tag(name = "Usuários", description = "Controller responsável pelas ações relacionadas aos usuários")
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
    @Operation(summary = "Listar todos os usuários", description = "Retorna uma lista contendo todos os usuários")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuários retornados com sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<List<UserDTO>> findAll() {
        List<UserDTO> users = userService.findAll().stream().map(UserDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Retornar um único usuário", description = "Retorna o usuário correspondente ao ID fornecido")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuário retornado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<UserDTO> findById(@PathVariable String userId) {
        User user = userService.findById(userId);
        return ResponseEntity.ok(new UserDTO(user));
    }

    @PostMapping("/auth/signup")
    @Operation(summary = "Criar um usuário", description = "Cria um usuário padrão no banco de dados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou erro de validação"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<Void> createUser(@RequestBody @Valid UserSignUpDTO userSignUpDTO) {
       
        User user = userService.createUser(userSignUpDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getId()).toUri();
       
        return ResponseEntity.created(uri).build();
    }

    @PostMapping("/auth/login")
    @Operation(summary = "Login de usuário", description = "Login de um usuário a partir de email e senha")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuário logado, token gerado com sucesso"),
        @ApiResponse(responseCode = "401", description = "Credenciais inválidas (email ou senha incorretos)"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid UserLoginDTO userLogin) {
        
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLogin.getEmail(), userLogin.getPassword()));
      
        String token = authenticationService.authenticate(authentication);

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PatchMapping("/{userId}")
    @Operation(summary = "Atualizar usuário", description = "Atualização parcial de um usuário existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
        @ApiResponse(responseCode = "401", description = "Não autenticado (token ausente ou inválido)"),
        @ApiResponse(responseCode = "403", description = "Acesso proibido (sem permissão para acessar este recurso)"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<Void> update(@RequestBody @Valid UserUpdateDTO userUpdateDTO, @PathVariable String userId, Authentication authentication) {
        
        User user = userService.findByEmail(authentication.getName());
        userService.updateUser(userUpdateDTO, userId, user.getId());

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "Deletar um usuário", description = "Soft Delete de um usuário existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Usuário deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
        @ApiResponse(responseCode = "401", description = "Não autenticado (token ausente ou inválido)"),
        @ApiResponse(responseCode = "403", description = "Acesso proibido (sem permissão para acessar este recurso)"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<Void> delete(@PathVariable String userId, Authentication authentication) {
        
        User user = userService.findByEmail(authentication.getName());
        userService.deleteUser(userId, user.getId());
        
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{userId}/favorites/{novelId}")
    @Operation(summary = "Adicionar favorito", description = "Adicionar uma Novel aos favoritos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Novel adicionada aos favoritos com sucesso"),
        @ApiResponse(responseCode = "404", description = "Nenhum usuário ou Novel encontrado"),
        @ApiResponse(responseCode = "401", description = "Não autenticado (token ausente ou inválido)"),
        @ApiResponse(responseCode = "403", description = "Acesso proibido (sem permissão para acessar este recurso)"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<Void> addFavorite(@PathVariable String userId, @PathVariable String novelId, Authentication authentication) {
       
        User user = userService.findByEmail(authentication.getName());
        userService.addFavorite(userId, novelId, user.getId());

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}/favorites/{novelId}")
    @Operation(summary = "Remover favorito", description = "Retirar uma Novel dos favoritos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Novel retirada dos favoritos com sucesso"),
        @ApiResponse(responseCode = "404", description = "Nenhum usuário ou Novel encontrado"),
        @ApiResponse(responseCode = "401", description = "Não autenticado (token ausente ou inválido)"),
        @ApiResponse(responseCode = "403", description = "Acesso proibido (sem permissão para acessar este recurso)"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<Void> removeFavorite(@PathVariable String userId, @PathVariable String novelId, Authentication authentication) {
        
        User user = userService.findByEmail(authentication.getName());
        userService.removeFavorite(userId, novelId, user.getId());

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{userId}/super-favorites/{novelId}")
    @Operation(summary = "Adicionar superfavorito", description = "Adicionar uma Novel aos superfavoritos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Novel adicionada aos superfavoritos com sucesso"),
        @ApiResponse(responseCode = "404", description = "Nenhum usuário ou Novel encontrado"),
        @ApiResponse(responseCode = "401", description = "Não autenticado (token ausente ou inválido)"),
        @ApiResponse(responseCode = "403", description = "Acesso proibido (sem permissão para acessar este recurso)"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<Void> addSuperFavorite(@PathVariable String userId, @PathVariable String novelId, Authentication authentication) {
        
        User user = userService.findByEmail(authentication.getName());
        userService.addSuperFavorite(userId, novelId, user.getId());

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}/super-favorites/{novelId}")
    @Operation(summary = "Remover superfavorito", description = "Retirar uma Novel dos superfavoritos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Novel retirada dos superfavoritos com sucesso"),
        @ApiResponse(responseCode = "404", description = "Nenhum usuário ou Novel encontrado"),
        @ApiResponse(responseCode = "401", description = "Não autenticado (token ausente ou inválido)"),
        @ApiResponse(responseCode = "403", description = "Acesso proibido (sem permissão para acessar este recurso)"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<Void> removeSuperFavorite(@PathVariable String userId, @PathVariable String novelId, Authentication authentication) {
        
        User user = userService.findByEmail(authentication.getName());
        userService.removeSuperFavorite(userId, novelId, user.getId());
        
        return ResponseEntity.noContent().build();
    }
}