package com.aguiabranca.inovacao.data.remote.api

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApiService {
    @POST("users/login")
    suspend fun login(@Body body: LoginRequestDto): UserDto

    @GET("users/{id}")
    suspend fun getById(@Path("id") id: String): UserDto

    @GET("users")
    suspend fun getAll(): List<UserDto>
}

interface ProjectApiService {
    @GET("projects")
    suspend fun getAll(): List<ProjectDto>

    @GET("projects/{id}")
    suspend fun getById(@Path("id") id: String): ProjectDto

    @POST("projects")
    suspend fun create(@Body body: CreateProjectRequestDto): ProjectDto

    @PUT("projects/{id}")
    suspend fun update(@Path("id") id: String, @Body body: UpdateProjectRequestDto): ProjectDto

    @DELETE("projects/{id}")
    suspend fun delete(@Path("id") id: String)
}

interface IdeaApiService {
    @GET("ideas")
    suspend fun getAll(@Query("operatorId") operatorId: String? = null): List<IdeaDto>

    @GET("ideas/{id}")
    suspend fun getById(@Path("id") id: String): IdeaDto

    @POST("ideas")
    suspend fun create(@Body body: CreateIdeaRequestDto): IdeaDto

    @PATCH("ideas/{id}/status")
    suspend fun updateStatus(@Path("id") id: String, @Body body: UpdateIdeaStatusRequestDto): IdeaDto

    @PATCH("ideas/{id}/priority")
    suspend fun updatePriority(@Path("id") id: String, @Body body: UpdateIdeaPriorityRequestDto): IdeaDto

    @DELETE("ideas/{id}")
    suspend fun delete(@Path("id") id: String)
}

interface GuidelineApiService {
    @GET("guidelines")
    suspend fun getAll(): List<GuidelineDto>

    @GET("guidelines/{id}")
    suspend fun getById(@Path("id") id: String): GuidelineDto

    @POST("guidelines")
    suspend fun create(@Body body: CreateGuidelineRequestDto): GuidelineDto

    @PUT("guidelines/{id}")
    suspend fun update(@Path("id") id: String, @Body body: UpdateGuidelineRequestDto): GuidelineDto

    @DELETE("guidelines/{id}")
    suspend fun delete(@Path("id") id: String)
}

interface DashboardApiService {
    @GET("dashboard/metrics")
    suspend fun getMetrics(): DashboardMetricsDto
}

