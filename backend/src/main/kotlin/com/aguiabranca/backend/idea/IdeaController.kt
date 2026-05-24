package com.aguiabranca.backend.idea

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/ideas")
open class IdeaController(private val repository: IdeaRepository) {

    @GetMapping
    fun list(@RequestParam(required = false) operatorId: String?): List<IdeaResponse> =
        if (operatorId.isNullOrBlank()) repository.findAll() else repository.findByOperator(operatorId)

    @GetMapping("/{id}")
    fun getById(@PathVariable id: String): IdeaResponse =
        repository.findById(id) ?: throw NoSuchElementException("Ideia não encontrada")

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody request: CreateIdeaRequest): IdeaResponse {
        require(request.title.isNotBlank()) { "Título é obrigatório" }
        require(request.description.isNotBlank()) { "Descrição é obrigatória" }
        return repository.create(request)
    }

    @PatchMapping("/{id}/status")
    fun updateStatus(@PathVariable id: String, @RequestBody request: UpdateIdeaStatusRequest): IdeaResponse {
        require(request.status.isNotBlank()) { "Status é obrigatório" }
        return repository.updateStatus(id, request)
    }

    @PatchMapping("/{id}/priority")
    fun updatePriority(@PathVariable id: String, @RequestBody request: UpdateIdeaPriorityRequest): IdeaResponse {
        require(request.priority.isNotBlank()) { "Prioridade é obrigatória" }
        return repository.updatePriority(id, request)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: String) {
        repository.delete(id)
    }
}

