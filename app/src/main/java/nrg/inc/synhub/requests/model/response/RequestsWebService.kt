package com.example.synhub.requests.model.response

import com.example.synhub.requests.application.dto.CreateRequest
import com.example.synhub.requests.application.dto.RequestResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface RequestsWebService {
    // Get requests by task ID
    @GET("tasks/{taskId}/requests")
    suspend fun getRequestsByTaskId(
        @Path("taskId") taskId: Long
    ): Response<List<RequestResponse>>

    // Get a request by ID
    @GET("tasks/{taskId}/requests/{requestId}")
    suspend fun getRequestById(
        @Path("taskId") taskId: Long,
        @Path("requestId") requestId: Long
    ): Response<RequestResponse>

    // Update request status
    @PUT("tasks/{taskId}/requests/{requestId}/status/{status}")
    suspend fun updateRequestStatus(
        @Path("taskId") taskId: Long?,
        @Path("requestId") requestId: Long,
        @Path("status") status: String
    ): Response<RequestResponse>

    // Delete request by task ID
    @DELETE("tasks/{taskId}/requests/{requestId}")
    suspend fun deleteRequest(
        @Path("taskId") taskId: Long,
        @Path("requestId") requestId: Long
    ): Response<Unit>

    // Get all requests from a group
    @GET("leader/group/requests")
    suspend fun getGroupRequests(): Response<List<RequestResponse>>

    // Create a new request
    @POST("tasks/{taskId}/requests")
    suspend fun createRequest(
        @Path("taskId") taskId: Long,
        @Body request: CreateRequest
    ): Response<RequestResponse>
}