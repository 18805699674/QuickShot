package cn.iichen.quickshot.net

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
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
    private const val BASE_URL_II = "http://100.120.127.154/"

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
            retrofitII = Retrofit.Builder()
                .baseUrl(BASE_URL_II)
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
            retryOnConnectionFailure(true)//错误重连
        }

        return builder.build()
    }

}