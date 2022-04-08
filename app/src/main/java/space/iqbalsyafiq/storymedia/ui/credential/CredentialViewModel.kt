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
import space.iqbalsyafiq.storymedia.model.request.RegisterRequest
import space.iqbalsyafiq.storymedia.repository.api.ApiConfig

class CredentialViewModel(application: Application): AndroidViewModel(application) {
    private val service = ApiConfig.getApiService()

    // live data
    private var _registerUser = MutableLiveData<Boolean>()
    val registerUser: LiveData<Boolean> = _registerUser

    fun registerUser(requestBody: RegisterRequest) {
        service.registerUser(requestBody).enqueue(object: Callback<DataResponse> {
            override fun onResponse(call: Call<DataResponse>, response: Response<DataResponse>) {
                _registerUser.value = response.isSuccessful
            }

            override fun onFailure(call: Call<DataResponse>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
                _registerUser.value = false
            }
        })
    }

    companion object {
        private val TAG = CredentialActivity::class.java.simpleName
    }
}