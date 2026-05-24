package com.aguiabranca.backend.project

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/projects")
open class ProjectController(private val repository: ProjectRepository) {

    @GetMapping
    fun list(): List<ProjectResponse> = repository.findAll()

    @GetMapping("/{id}")
    fun getById(@PathVariable id: String): ProjectResponse =
        repository.findById(id) ?: throw NoSuchElementException("Projeto não encontrado")

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody request: CreateProjectRequest): ProjectResponse {
        require(request.title.isNotBlank()) { "Título é obrigatório" }
        require(request.objective.isNotBlank()) { "Objetivo é obrigatório" }
        return repository.create(request)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: String, @RequestBody request: UpdateProjectRequest): ProjectResponse {
        require(request.title.isNotBlank()) { "Título é obrigatório" }
        require(request.objective.isNotBlank()) { "Objetivo é obrigatório" }
        return repository.update(id, request)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: String) {
        repository.delete(id)
    }
}

