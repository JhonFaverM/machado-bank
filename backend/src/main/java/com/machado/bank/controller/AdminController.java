package com.machado.bank.controller;

import com.machado.bank.dto.CreateUserRequest;
import com.machado.bank.model.Role;
import com.machado.bank.model.User;
import com.machado.bank.service.IUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/admin/users")
public class AdminController {

    private final IUserService userService;

    public AdminController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest request) {

        User user = userService.createUser(
                request.userName(),
                request.password(),
                request.role()
        );

        return ResponseEntity.ok(user);
    }

    //Buscar usuario por Name
    @GetMapping("/{userName}")
    public ResponseEntity<User> getByUserName(@PathVariable String userName) {
        return userService.findByUserName(userName)
                .map(user -> ResponseEntity.ok(user)) // Si existe, retorna 200 OK con el usuario
                .orElse(ResponseEntity.notFound().build()); // Si no existe, retorna 404 Not Found
    }


    @PatchMapping("/{id}/disable")
    public ResponseEntity<?> disableUser(@PathVariable Long id) {

        userService.disableUser(id);

        return ResponseEntity.ok().body(
                Map.of(
                        "message", "Usuario deshabilitado correctamente",
                        "userId", id
                )
        );
    }

    @PatchMapping("/{id}/enable")
    public ResponseEntity<Map<String, Object>> enableUser(@PathVariable Long id) {

        userService.enableUser(id);

        return ResponseEntity.ok(Map.of(
                "message", "Usuario habilitado correctamente",
                "userId", id
        ));
    }
}
