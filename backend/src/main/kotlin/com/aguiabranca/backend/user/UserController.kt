package com.aguiabranca.backend.user

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
open class UserController(private val repository: UserRepository) {

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): UserResponse {
        require(request.email.isNotBlank()) { "Email é obrigatório" }
        require(request.password.isNotBlank()) { "Senha é obrigatória" }
        return repository.login(request.email, request.password)
            ?: throw IllegalArgumentException("Email ou senha incorretos")
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: String): UserResponse =
        repository.findById(id) ?: throw NoSuchElementException("Usuário não encontrado")

    @GetMapping
    fun list(): List<UserResponse> = repository.findAll()
}

