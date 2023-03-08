package togle.plinko.mega.sigma.dominicanos.utils

import android.app.Activity
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import togle.plinko.mega.sigma.dominicanos.BuildConfig
import togle.plinko.mega.sigma.dominicanos.MainActivity

object NetworkUtil {
    object Network {
        private val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl("https://google.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        val service: APIService by lazy {
            retrofit.create(APIService::class.java)
        }
    }

    interface APIService {
        @GET
        suspend fun getData(
            @Url                 url   : String = "${MainActivity.kakayatoDom}/7GaihaebdK",
            @Query(CONST_PADAGE) padage: String = BuildConfig.APPLICATION_ID,
            @Query(CONST_UPID)   upid  : String?,
        ): Response<ResponseBody>

        @GET
        suspend fun getDataByUrl(
            @Url url: String
        ): Response<ResponseBody>
    }

    fun haveNetworkConnection(activity: Activity): Boolean {
        var haveConnectedWifi = false
        var haveConnectedMobile = false
        (activity.getSystemService(AppCompatActivity.CONNECTIVITY_SERVICE) as ConnectivityManager).allNetworkInfo.onEach { networkItem ->
            if (networkItem.typeName.equals("WIFI", ignoreCase = true)) if (networkItem.isConnected) haveConnectedWifi = true
            if (networkItem.typeName.equals("MOBILE", ignoreCase = true)) if (networkItem.isConnected) haveConnectedMobile = true
        }
        return haveConnectedWifi || haveConnectedMobile
    }

}