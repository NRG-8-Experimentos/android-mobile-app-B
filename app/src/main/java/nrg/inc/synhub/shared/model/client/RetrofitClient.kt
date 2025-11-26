package com.example.synhub.shared.model.client

import com.example.synhub.analytics.model.response.AnalyticsWebService
import com.example.synhub.groups.model.response.GroupWebService
import com.example.synhub.groups.model.response.MembersWebService
import com.example.synhub.requests.model.response.RequestsWebService
import com.example.synhub.invitations.model.response.InvitationsWebService
import com.example.synhub.shared.model.response.LogInWebService
import com.example.synhub.shared.model.response.HomeWebService
import com.example.synhub.shared.model.response.RegisterWebService
import nrg.inc.synhub.tasks.model.response.TasksWebService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    @Volatile
    var token: String? = null

    fun updateToken(newToken: String) {
        token = newToken
    }

    fun resetToken(){
        token = null
    }

    private val client: OkHttpClient
        get() = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val requestBuilder = chain.request().newBuilder()
                val currentToken = token
                if (!currentToken.isNullOrBlank()) {
                    requestBuilder.addHeader("Authorization", "Bearer $currentToken")
                }
                chain.proceed(requestBuilder.build())
            }
            .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8080/api/v1/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val logInWebService: LogInWebService = retrofit.create(LogInWebService::class.java)
    val groupWebService: GroupWebService = retrofit.create(GroupWebService::class.java)
    val homeWebService: HomeWebService = retrofit.create(HomeWebService::class.java)
    val tasksWebService: TasksWebService = retrofit.create(TasksWebService::class.java)
    val membersWebService: MembersWebService = retrofit.create(MembersWebService::class.java)
    val requestsWebService: RequestsWebService = retrofit.create(RequestsWebService::class.java)
    val invitationsWebService: InvitationsWebService = retrofit.create(InvitationsWebService::class.java)
    val registerWebService: RegisterWebService = retrofit.create(RegisterWebService::class.java)
    val analyticsWebService: AnalyticsWebService = retrofit.create(AnalyticsWebService::class.java)
}