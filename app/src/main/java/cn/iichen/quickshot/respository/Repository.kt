package cn.iichen.quickshot.respository

import cn.iichen.diverseweather.data.remote.ApiResult
import cn.iichen.quickshot.pojo.*
import cn.iichen.quickshot.pojo.params.FavoriteBean
import cn.iichen.quickshot.pojo.params.RegisterBean
import kotlinx.coroutines.flow.Flow
import retrofit2.Call

/**
 *
 * @ProjectName:    QuickShot
 * @Package:        cn.iichen.quickshot.respository
 * @ClassName:      Repository
 * @Description:     java类作用描述
 * @Author:         作者名 qsdiao
 * @CreateDate:     2021/12/17 14:26
 * @UpdateUser:     更新者：qsdiao
 * @UpdateDate:     2021/12/17 14:26
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


interface Repository {

    suspend fun getNineTvVideo(url: String) : Flow<ApiResult<NineTvBean>>

    suspend fun getVideoSource() : Flow<ApiResult<VideoSourceBean>>

    suspend fun doRegister(registerBean: RegisterBean): Flow<ApiResult<UserBean>>

    suspend fun doLogin(loginBean: RegisterBean): Flow<ApiResult<UserBean>>

    suspend fun getUserInfo(token: String): Flow<ApiResult<UserBean>>

    suspend fun doActiveAccount(userId:String,code:String): Flow<ApiResult<BaseBean>>

    suspend fun getVideoSourceTimeRange() : Flow<ApiResult<VideoSourceTimeRangeBean>>

    suspend fun getVideoSourceByTime(time: String): Flow<ApiResult<VideoSourceBean>>

    suspend fun getVideoChannels(): Flow<ApiResult<VideoChannelBean>>

    suspend fun getVideoTags(): Flow<ApiResult<VideoTagsBean>>

    suspend fun getFavorite(userId: String): Flow<ApiResult<FavoriteListBean>>
}









