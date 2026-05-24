package com.aguiabranca.backend.guideline

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
@RequestMapping("/api/guidelines")
open class GuidelineController(private val repository: GuidelineRepository) {

    @GetMapping
    fun list(): List<GuidelineResponse> = repository.findAll()

    @GetMapping("/{id}")
    fun getById(@PathVariable id: String): GuidelineResponse =
        repository.findById(id) ?: throw NoSuchElementException("Diretriz não encontrada")

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody request: CreateGuidelineRequest): GuidelineResponse {
        require(request.title.isNotBlank()) { "Título é obrigatório" }
        require(request.description.isNotBlank()) { "Descrição é obrigatória" }
        require(request.authorId.isNotBlank()) { "Autor é obrigatório" }
        return repository.create(request)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: String, @RequestBody request: UpdateGuidelineRequest): GuidelineResponse {
        require(request.title.isNotBlank()) { "Título é obrigatório" }
        require(request.description.isNotBlank()) { "Descrição é obrigatória" }
        return repository.update(id, request)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: String) {
        repository.delete(id)
    }
}

