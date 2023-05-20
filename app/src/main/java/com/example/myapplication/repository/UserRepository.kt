package com.example.myapplication.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.api.UserAPI
import com.example.myapplication.models.UserRequest
import com.example.myapplication.models.UserResponse
import com.example.myapplication.utils.Constant.TAG
import com.example.myapplication.utils.NetworkResult
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(private val userAPI: UserAPI) {


    private val _userResponseLiveData = MutableLiveData<NetworkResult<UserResponse>>()
    val userResponseLiveData: LiveData<NetworkResult<UserResponse>>
        get() = _userResponseLiveData

    suspend fun registerUser(userRequest: UserRequest) {
        try {
            _userResponseLiveData.postValue(NetworkResult.Loading())
            val response = userAPI.signUp(userRequest)
            handleResponse(response)
        } catch (e: Exception) {
            Log.d(TAG, e.message.toString())
        }
    }

    suspend fun loginUser(userRequest: UserRequest) {
        try {
            _userResponseLiveData.postValue(NetworkResult.Loading())
            val response = userAPI.signIn(userRequest)
            handleResponse(response)
        } catch (e: Exception) {
            Log.d(TAG, e.message.toString())
        }
    }

    private fun handleResponse(response: Response<UserResponse>) {
        if (response.isSuccessful && response.body() != null) {
            _userResponseLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            Log.d(TAG, response.errorBody().toString())
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _userResponseLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _userResponseLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }
    }
}