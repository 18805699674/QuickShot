package cn.iichen.quickshot.net

import cn.iichen.quickshot.pojo.*
import cn.iichen.quickshot.pojo.params.FavoriteBean
import cn.iichen.quickshot.pojo.params.RegisterBean
import retrofit2.Call
import retrofit2.http.*

/**
 *
 * @ProjectName:    QuickShot
 * @Package:        cn.iichen.quickshot.net
 * @ClassName:      Api
 * @Description:     java类作用描述
 * @Author:         作者名 qsdiao
 * @CreateDate:     2021/12/17 14:23
 * @UpdateUser:     更新者：qsdiao
 * @UpdateDate:     2021/12/17 14:23
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


interface Api {

    // 获取51
    @GET
    suspend fun getNineTvVideo(@Url url:String) : NineTvBean

    @GET("video/source")
    suspend fun getVideoSource() : VideoSourceBean

    @POST("register")
    suspend fun doRegister(@Body registerBean: RegisterBean): UserBean

    @POST("login")
    suspend fun doLogin(@Body loginBean: RegisterBean): UserBean

    @GET("user/{token}")
    suspend fun getUserInfo(@Path(value = "token") token:String): UserBean

    @GET("code/use/{code}/{userId}")
    suspend fun doActiveAccount(@Path(value = "code") code: String,@Path(value = "userId") userId: String): BaseBean

    @GET("video/source/range")
    suspend fun getVideoSourceTimeRange() : VideoSourceTimeRangeBean

    @GET("video/source/{time}")
    suspend fun getVideoSourceByTime(@Path(value = "time") time:String): VideoSourceBean

    @GET("channel/get")
    suspend fun getVideoChannels() : VideoChannelBean

    @GET("tags/get")
    suspend fun getVideoTags() : VideoTagsBean



    @POST("favorite/add")
    fun doFavorite(@Body favoriteBean: FavoriteBean): Call<BaseBean>
    @GET("favorite/get/{userId}")
    suspend fun getFavorite(@Path(value = "userId") userId:String): FavoriteListBean
}








