package cn.iichen.quickshot.net

import cn.iichen.quickshot.pojo.NineTvBean
import cn.iichen.quickshot.pojo.VideoSourceBean
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

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
}








