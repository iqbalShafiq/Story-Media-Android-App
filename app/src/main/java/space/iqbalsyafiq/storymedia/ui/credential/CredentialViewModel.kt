package space.iqbalsyafiq.storymedia.ui.credential

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import space.iqbalsyafiq.storymedia.model.DataResponse
import space.iqbalsyafiq.storymedia.model.request.LoginRequest
import space.iqbalsyafiq.storymedia.model.request.RegisterRequest
import space.iqbalsyafiq.storymedia.repository.api.ApiConfig

class CredentialViewModel(application: Application) : AndroidViewModel(application) {
    private val service = ApiConfig.getApiService()

    // live data
    private var _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> = _loadingState

    private var _registerUserStatus = MutableLiveData<Boolean>()
    val registerUserStatus: LiveData<Boolean> = _registerUserStatus

    private var _duplicateEmailStatus = MutableLiveData<Boolean>()
    val duplicateEmailStatus: LiveData<Boolean> = _duplicateEmailStatus

    private var _loginUserStatus = MutableLiveData<Boolean>()
    val loginUserStatus: LiveData<Boolean> = _loginUserStatus

    fun registerUser(requestBody: RegisterRequest) {
        _loadingState.value = true

        service.registerUser(requestBody).enqueue(object : Callback<DataResponse> {
            override fun onResponse(call: Call<DataResponse>, response: Response<DataResponse>) {
                Log.d(TAG, "onResponse: ${response.code()}")

                _loadingState.value = false
                when {
                    response.isSuccessful -> {
                        _registerUserStatus.value = true
                    }
                    response.code() == 400 -> {
                        _duplicateEmailStatus.value = true
                    }
                    else -> {
                        _registerUserStatus.value = false
                    }
                }
            }

            override fun onFailure(call: Call<DataResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
                _loadingState.value = false
                _registerUserStatus.value = false
            }
        })
    }

    fun loginUser(requestBody: LoginRequest) {
        service.loginUser(requestBody).enqueue(object : Callback<DataResponse> {
            override fun onResponse(call: Call<DataResponse>, response: Response<DataResponse>) {
                Log.d(TAG, "onResponse: ${response.code()}")

                _loadingState.value = false
                when {
                    response.isSuccessful -> {
                        _loginUserStatus.value = true
                    }
                    else -> {
                        _loginUserStatus.value = false
                    }
                }
            }

            override fun onFailure(call: Call<DataResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
                _loadingState.value = false
                _loginUserStatus.value = false
            }
        })
    }

    companion object {
        private val TAG = CredentialActivity::class.java.simpleName
    }
}