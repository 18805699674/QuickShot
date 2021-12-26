package cn.iichen.quickshot.respository

import android.util.Log
import cn.iichen.diverseweather.data.remote.ApiResult
import cn.iichen.quickshot.ext.doEnqueue
import cn.iichen.quickshot.net.RetrofitClient
import cn.iichen.quickshot.pojo.*
import cn.iichen.quickshot.pojo.params.FavoriteBean
import cn.iichen.quickshot.pojo.params.RegisterBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

/**
 *
 * @ProjectName:    QuickShot
 * @Package:        cn.iichen.quickshot.respository
 * @ClassName:      RepositoryImpl
 * @Description:     java类作用描述
 * @Author:         作者名 qsdiao
 * @CreateDate:     2021/12/17 14:35
 * @UpdateUser:     更新者：qsdiao
 * @UpdateDate:     2021/12/17 14:35
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


class RepositoryImpl : Repository {
    override suspend fun getNineTvVideo(url: String): Flow<ApiResult<NineTvBean>> {
        return flow {
            try {
                val nineTvBean = RetrofitClient.serviceThird.getNineTvVideo(url)
                emit(ApiResult.Success(nineTvBean))
            }catch (e: Exception){
                emit(ApiResult.Failure(e.message))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getVideoSource(): Flow<ApiResult<VideoSourceBean>> {
        return flow {
            try {
                val videoSourceBean = RetrofitClient.serviceII.getVideoSource()
                emit(ApiResult.Success(videoSourceBean))
            }catch (e: Exception){
                emit(ApiResult.Failure(e.message))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun doRegister(registerBean: RegisterBean): Flow<ApiResult<UserBean>> {
        return flow {
            try {
                val userBean = RetrofitClient.serviceII.doRegister(registerBean)
                emit(ApiResult.Success(userBean))
            }catch (e: Exception){
                emit(ApiResult.Failure(e.message))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun doLogin(loginBean: RegisterBean): Flow<ApiResult<UserBean>> {
        return flow {
            try {
                val userBean = RetrofitClient.serviceII.doLogin(loginBean)
                emit(ApiResult.Success(userBean))
            }catch (e: Exception){
                emit(ApiResult.Failure(e.message))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getUserInfo(token: String): Flow<ApiResult<UserBean>> {
        return flow {
            try {
                val userBean = RetrofitClient.serviceII.getUserInfo(token)
                emit(ApiResult.Success(userBean))
            }catch (e: Exception){
                emit(ApiResult.Failure(e.message))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun doActiveAccount(userId:String,code:String): Flow<ApiResult<BaseBean>>{
        return flow {
            try {
                val userBean = RetrofitClient.serviceII.doActiveAccount(code,userId)
                emit(ApiResult.Success(userBean))
            }catch (e: Exception){
                emit(ApiResult.Failure(e.message))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getVideoSourceTimeRange(): Flow<ApiResult<VideoSourceTimeRangeBean>> {
        return flow {
            try {
                val userBean = RetrofitClient.serviceII.getVideoSourceTimeRange()
                emit(ApiResult.Success(userBean))
            }catch (e: Exception){
                emit(ApiResult.Failure(e.message))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getVideoSourceByTime(time: String): Flow<ApiResult<VideoSourceBean>> {
        return flow {
            try {
                val userBean = RetrofitClient.serviceII.getVideoSourceByTime(time)
                emit(ApiResult.Success(userBean))
            }catch (e: Exception){
                emit(ApiResult.Failure(e.message))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getVideoChannels(): Flow<ApiResult<VideoChannelBean>> {
        return flow {
            try {
                val videoChannelBean = RetrofitClient.serviceII.getVideoChannels()
                emit(ApiResult.Success(videoChannelBean))
            }catch (e: Exception){
                emit(ApiResult.Failure(e.message))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getVideoTags(): Flow<ApiResult<VideoTagsBean>> {
        return flow {
            try {
                val videoTagsBean = RetrofitClient.serviceII.getVideoTags()
                emit(ApiResult.Success(videoTagsBean))
            }catch (e: Exception){
                emit(ApiResult.Failure(e.message))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getFavorite(userId: String): Flow<ApiResult<FavoriteListBean>> {
        return flow {
            try {
                val favoriteListBean = RetrofitClient.serviceII.getFavorite(userId)
                emit(ApiResult.Success(favoriteListBean))
            }catch (e: Exception){
                emit(ApiResult.Failure(e.message))
            }
        }.flowOn(Dispatchers.IO)
    }
}







