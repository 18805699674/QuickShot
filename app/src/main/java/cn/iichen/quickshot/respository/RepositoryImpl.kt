package cn.iichen.quickshot.respository

import cn.iichen.diverseweather.data.remote.ApiResult
import cn.iichen.quickshot.net.RetrofitClient
import cn.iichen.quickshot.pojo.NineTvBean
import cn.iichen.quickshot.pojo.VideoSourceBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Retrofit
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
                emit(ApiResult.Failure(e.cause))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getVideoSource(): Flow<ApiResult<VideoSourceBean>> {
        return flow {
            try {
                val videoSourceBean = RetrofitClient.serviceII.getVideoSource()
                emit(ApiResult.Success(videoSourceBean))
            }catch (e: Exception){
                emit(ApiResult.Failure(e.cause))
            }
        }.flowOn(Dispatchers.IO)
    }
}







