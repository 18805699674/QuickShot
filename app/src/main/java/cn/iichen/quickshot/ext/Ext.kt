package cn.iichen.quickshot.ext

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import cn.iichen.diverseweather.data.remote.ApiResult
import cn.iichen.diverseweather.data.remote.doFailure
import cn.iichen.diverseweather.data.remote.doSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

/**
 *
 * @ProjectName:    QuickShot
 * @Package:        cn.iichen.quickshot.ext
 * @ClassName:      Ext
 * @Description:     java类作用描述
 * @Author:         作者名 qsdiao
 * @CreateDate:     2021/12/17 14:51
 * @UpdateUser:     更新者：qsdiao
 * @UpdateDate:     2021/12/17 14:51
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
object Ext {
    // 用户登录 Token身份
    var MMKV_KEY_TOKEN:String = "MMKV_KEY_TOKEN"
    // 是否app第一次打开
    var MMKV_KEY_FIRST_OPEN :String = "MMKV_KEY_FIRST_OPEN"
}

fun View.visibility(){
    this.visibility = View.VISIBLE
}

fun View.gone(){
    this.visibility = View.GONE
}

fun String.toast(){
    Toast.makeText(App.context,this,Toast.LENGTH_SHORT).show()
}

fun <T> LiveData<T>.observeNonNull(owner: LifecycleOwner, observer: (T) -> Unit) {
    this.observe(owner, Observer {
        if (it != null) {
            // 加载情况处理
//            if(it is Boolean){
//            }
            observer(it)
        }
    })
}


suspend inline fun <reified T> Flow<ApiResult<T>>.collectLatestII(crossinline doFailure: () -> Unit, crossinline doSuccess: (T) -> Unit) {
    this.collectLatest {
        it.doFailure { throwable ->
            throwable?.message?.toast()
            throwable?.message?.let { it1 -> Log.d("iichen", "请求失败  $it1") }
            doFailure()
        }
        it.doSuccess { value ->
            // 刷新UI
            doSuccess(value)
        }
    }
}