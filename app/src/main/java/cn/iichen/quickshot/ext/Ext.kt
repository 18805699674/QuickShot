package cn.iichen.quickshot.ext

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import cn.iichen.diverseweather.data.remote.ApiResult
import cn.iichen.diverseweather.data.remote.doFailure
import cn.iichen.diverseweather.data.remote.doSuccess
import cn.iichen.quickshot.R
import cn.iichen.quickshot.pojo.BaseBean
import cn.iichen.quickshot.pojo.UserBean
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
    const val MMKV_KEY_TOKEN:String = "MMKV_KEY_TOKEN"
    // 是否app第一次打开
    const val MMKV_KEY_FIRST_OPEN :String = "MMKV_KEY_FIRST_OPEN"
    // 将当前邮箱与 Code绑定 比如：倒计时就关闭弹窗时，可以直接使用
    const val MMKV_KEY_EMAIL_CODE:String = "MMKV_KEY_EMAIL_CODE"

    // 服务器错误码
    const val SERVICE_ERROR_CODE =  400

    var user: UserBean.Data? = null
}

fun ImageView.setLevel(level:Int){
    this.visibility()
    when(level){
        1 -> {
            this.setImageResource(R.drawable.level_one)
        }
        2 -> {
            this.setImageResource(R.drawable.level_two)
        }
        3 -> {
            this.setImageResource(R.drawable.level_three)
        }
        4 -> {
            this.setImageResource(R.drawable.level_four)
        }
        5 -> {
            this.setImageResource(R.drawable.level_five)
        }
        else ->
            this.setImageResource(R.drawable.level_vip)
    }
}

fun View.click(call:()->Unit){
    setOnClickListener {
        call()
    }
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
    this.observe(owner) {
        if (it != null) {
            // 加载情况处理
//            if(it is Boolean){
//            }
            observer(it)
        }
    }
}


fun <T> Call<T>.doEnqueue(onResponse: (Response<T>)->Unit,onFail:(String?)->Unit){
    this.enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            onResponse(response)
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            onFail(t.message)
        }
    })
}