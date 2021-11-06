package ro.esolutions.selfregistration.data

import com.example.hackton_android.data.ApiService
import com.example.hackton_android.data.Repository
import com.example.hackton_android.data.RepositoryInterface
import com.squareup.moshi.FromJson
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.lang.reflect.Type
import java.math.BigDecimal

var BASE_URL = "http://192.168.0.103:8080"

val moshi: Moshi = Moshi.Builder()
    .add(BigDecimalAdapter)
    .add(KotlinJsonAdapterFactory())
    .build()

private val loggingInterceptor =
    HttpLoggingInterceptor().apply { this.level = HttpLoggingInterceptor.Level.BODY }

private var okHttpClient = OkHttpClient.Builder()
    .addInterceptor(loggingInterceptor)
    .build()

var retrofitBuilder: Retrofit.Builder = Retrofit.Builder()
    .addConverterFactory(NullOnEmptyConverterFactory())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .client(okHttpClient)

val repositoryInterface: RepositoryInterface = Repository()

var apiService: ApiService =
    retrofitBuilder.baseUrl(BASE_URL).build().create(ApiService::class.java)

object BigDecimalAdapter {
    @FromJson
    fun fromJson(string: String) = BigDecimal(string)

    @ToJson
    fun toJson(value: BigDecimal) = value.toString()
}

class NullOnEmptyConverterFactory : Converter.Factory() {
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, Any> {
        val delegate: Converter<ResponseBody, Any> =
            retrofit.nextResponseBodyConverter(this, type, annotations)
        return Converter<ResponseBody, Any> {
            val contentLength = it.contentLength()
            if (contentLength == 0L) null else delegate.convert(it)
        }
    }
}
