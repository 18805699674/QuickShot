package cn.iichen.quickshot.dialog

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import cn.iichen.quickshot.R
import cn.iichen.quickshot.ext.*
import cn.iichen.quickshot.pojo.params.RegisterBean
import cn.iichen.quickshot.ui.home.HomeModel
import cn.iichen.quickshot.widget.loading.DrawableTextView
import cn.iichen.quickshot.widget.loading.LoadingButton
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.*


/**
 *
 * @ProjectName:    QuickShot
 * @Package:        cn.iichen.quickshot.dialog
 * @ClassName:      CodeActiveDialog
 * @Description:     java类作用描述
 * @Author:         作者名 qsdiao
 * @CreateDate:     2021/12/17 22:02
 * @UpdateUser:     更新者：qsdiao
 * @UpdateDate:     2021/12/17 22:02
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


class LoginDialog : BaseDialogFragment() {
    private val model: HomeModel by activityViewModels()
    private val mainScope = CoroutineScope(Dispatchers.Main + Job() )



    override fun handleViewEvent(view: View?) {
        val loginClose = view?.findViewById<ImageView>(R.id.login_close)
        loginClose?.setOnClickListener {
            dismissAllowingStateLoss()
        }


        val loginEmail = view?.findViewById<TextInputLayout>(R.id.login_username)
        val loginPasswd = view?.findViewById<TextInputLayout>(R.id.login_password)


        val loginDo = view?.findViewById<LoadingButton>(R.id.login_do)
        val login2Register = view?.findViewById<TextView>(R.id.login_two_register)
        val forgetPasswd = view?.findViewById<TextView>(R.id.login_forget_passwd)

        loginDo?.click {
            val email = loginEmail?.editText?.text.toString()
            val passwd = loginPasswd?.editText?.text.toString()
            if(email.isBlank() || passwd.isBlank()){
                "邮箱或密码不能为空".toast()
                return@click
            }

            loginDo.start()
            mainScope.launch {
                val login =  async { model.doLogin(RegisterBean("", passwd, email)) }
                login.await()
                if(model.userBean.value!=null){
                    loginDo.complete(true)
                }else{
                    loginDo.complete(false)
                }
                loginDo.cancel()
            }
        }
        login2Register?.setOnClickListener {
            dismissAllowingStateLoss()
            val dialog = RegisterDialog()
            activity?.supportFragmentManager?.let { dialog.show(it,"Dialog_RegisterDialog") }
        }
        forgetPasswd?.setOnClickListener {
            "找回密码".toast()
        }

    }

    override fun setDialogLayoutId(): Int  = R.layout.dialog_login
}

















