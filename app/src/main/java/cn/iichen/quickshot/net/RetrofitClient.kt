package cn.iichen.quickshot.net

import android.util.Log
import cn.iichen.quickshot.ext.App
import cn.iichen.quickshot.ext.Ext
import com.tencent.mmkv.MMKV
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 *
 * @ProjectName:    QuickShot
 * @Package:        cn.iichen.quickshot.net
 * @ClassName:      RetrofitClient
 * @Description:     java类作用描述
 * @Author:         作者名 qsdiao
 * @CreateDate:     2021/12/17 14:31
 * @UpdateUser:     更新者：qsdiao
 * @UpdateDate:     2021/12/17 14:31
 * @UpdateRemark:   更新说明：Fuck code, go to hell, serious people who maintain it：
 * @Version:        更新说明: 1.0
┏┓　　　┏┓
┏┛┻━━━┛┻┓
┃　　　　　　　┃
┃　　　━　　　┃
┃　┳┛　┗┳　┃
┃　　　　　　　┃
┃　　　┻　　　┃
┃　　　　　　　┃
┗━┓　　　┏━┛
┃　　　┃   神兽保佑
┃　　　┃   代码无BUG！
┃　　　┗━━━┓
┃　　　　　　　┣┓
┃　　　　　　　┏┛
┗┓┓┏━┳┓┏┛
┃┫┫　┃┫┫
┗┻┛　┗┻┛
 */

object RetrofitClient {
    private const val BASE_URL_THIRD = "https://nnp35.com/"

    private val DefaultDomainService = "620cdf50"

    private var retrofitThird: Retrofit? = null
    private var retrofitII: Retrofit? = null

    val serviceThird: Api by lazy {
        getRetrofitThird().create(Api::class.java)
    }

    val serviceII: Api by lazy {
        getRetrofitII().create(Api::class.java)
    }

    private fun getRetrofitThird(): Retrofit {
        if (retrofitThird == null) {
            retrofitThird = Retrofit.Builder()
                .baseUrl(BASE_URL_THIRD)
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                //.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        }
        return retrofitThird!!
    }

    private fun getRetrofitII(): Retrofit {
        if (retrofitII == null) {
            val mmkv = MMKV.defaultMMKV()
            var service =  mmkv.getString("domain",DefaultDomainService)
            service = "http://${service}.cpolar.io/"
            Log.d("iichen","############ $service")
            retrofitII = Retrofit.Builder()
                .baseUrl(service)
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                //.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        }
        return retrofitII!!
    }



    private fun getOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient().newBuilder()

        builder.run {
            connectTimeout(60, TimeUnit.SECONDS)
            readTimeout(60, TimeUnit.SECONDS)
            writeTimeout(60, TimeUnit.SECONDS)
            addInterceptor { chain ->
                val request: Request = chain.request()
                    .newBuilder()
                    .addHeader("Authorization", Ext.user?.token?:"")
//                    .addHeader(
//                        "User-Agent",
//                        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36"
//                    )
                    .build()
                chain.proceed(request)
            }
            retryOnConnectionFailure(true)//错误重连
        }

        return builder.build()
    }

}