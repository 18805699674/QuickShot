package cn.iichen.quickshot.dialog

import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import cn.iichen.quickshot.R
import cn.iichen.quickshot.ext.click
import com.google.android.material.textfield.TextInputLayout

import cn.iichen.quickshot.ext.Ext
import cn.iichen.quickshot.ext.toast
import cn.iichen.quickshot.pojo.EmailCodeBean
import cn.iichen.quickshot.pojo.params.RegisterBean
import cn.iichen.quickshot.ui.home.HomeModel
import cn.iichen.quickshot.utils.mail.MailSender
import cn.iichen.quickshot.widget.loading.LoadingButton
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.RegexUtils
import com.tencent.mmkv.MMKV
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


class RegisterDialog : BaseDialogFragment() {
    private var registerBtn: LoadingButton? = null
    private var code: String = "000000"
    private var countDownTimer: CountDownTimer? = null
    private var codeBtn: TextView? = null
    private var emailS:String? = null

    private val model: HomeModel by activityViewModels()
    private val mainScope = CoroutineScope(Dispatchers.Main + Job() )


    private var registerUser:EmailCodeBean? = null

    override fun handleViewEvent(view: View?) {
        val registerClose = view?.findViewById<ImageView>(R.id.register_close)
        val registerUserName = view?.findViewById<TextInputLayout>(R.id.register_username)
        val registerPasswd = view?.findViewById<TextInputLayout>(R.id.register_password)
        val registerCode = view?.findViewById<TextInputLayout>(R.id.register_code)
        val email = view?.findViewById<TextInputLayout>(R.id.register_email)
        codeBtn = view?.findViewById<TextView>(R.id.register_code_btn)
        val toLoginBtn = view?.findViewById<TextView>(R.id.login_two_register)
        registerBtn = view?.findViewById<LoadingButton>(R.id.register_btn)
        registerClose?.click {
            dismissAllowingStateLoss()
        }
        codeBtn?.click {
            emailS = email?.editText?.text.toString()
            if(RegexUtils.isEmail(emailS)){
                startCountDown(emailS!!)
            }else{
                "邮箱格式不正确！".toast()
            }
        }
        toLoginBtn?.click {
            dismissAllowingStateLoss()
            val dialog = LoginDialog()
            activity?.supportFragmentManager?.let { dialog.show(it,"Dialog_LoginDialog") }
        }

        registerBtn?.click {
            val userName = registerUserName?.editText?.text.toString()
            val password = registerPasswd?.editText?.text.toString()
            val emailCode = registerCode?.editText?.text.toString()
            emailS = email?.editText?.text.toString()


            if(userName.isEmpty() || userName.length>10){
                "检查您的用户名！".toast()
                return@click
            }
            if(password.length<6 || password.length>15){
                "检查您的密码！".toast()
                return@click
            }

            if(!RegexUtils.isEmail(emailS)){
                "邮箱格式不正确！".toast()
                return@click
            }

            if(emailCode.length != 6){
                "验证码格式不正确！".toast()
                return@click
            }

            if(countDownTimer==null){   // 不点击发送验证码 获取之前的
                val mmkv = MMKV.defaultMMKV()
                val emailCodeJson = mmkv.getString(Ext.MMKV_KEY_EMAIL_CODE,"")
                val emailCodeBean = GsonUtils.fromJson(emailCodeJson,EmailCodeBean::class.java)
                emailCodeBean?.apply {
                    if(emailS == emailCodeBean.email && emailCode == emailCodeBean.code)
                        handleRegisterRequest(RegisterBean(userName,password, emailS!!))
                    else
                        "邮箱验证码失效".toast()
                }
            }else{
                countDownTimer?.apply {
                    if(codeIsValid && code == emailCode){
                        registerUser?.apply {
                            if(this.email != emailS){
                                "当前验证码对应的邮箱不一致！".toast()
                            }else
                                handleRegisterRequest(RegisterBean(userName,password, emailS!!))
                        }
                    }else{
                        "邮箱验证码失效".toast()
                    }
                }
            }
        }

    }

    private fun handleRegisterRequest(registerBean: RegisterBean) {
        if(view!=null){
            registerBtn?.start()
            mainScope.launch {
                val login =  async {model.doRegister(registerBean)}
                login.await()
                if(model.userBean.value!=null){
                    registerBtn?.complete(true)
                }else{
                    registerBtn?.complete(false)
                }
                registerBtn?.cancel()
            }
        }
    }

    override fun setDialogLayoutId(): Int  = R.layout.dialog_register

    var codeIsValid:Boolean = false
    private fun startCountDown(email:String) {
        // 将当前邮箱与 Code绑定 比如：倒计时就关闭弹窗时，可以直接使用

        codeBtn?.isEnabled = false
        codeIsValid = true
        codeBtn?.setBackgroundResource(R.drawable.bg_text_67ff7000_5_radius)
        code = ((Math.random()*9+1)*100000).toInt().toString()
        MailSender.send(email, code)
        registerUser = EmailCodeBean(email,code)
        countDownTimer = object : CountDownTimer(1000*60*5L, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                activity?.apply {
                    if (!this.isFinishing) {
                        codeBtn?.text =  "${millisUntilFinished/1000}s"
                    }
                }
            }

            /**
             * 倒计时结束后调用的
             */
            override fun onFinish() {
                codeBtn?.setBackgroundResource(R.drawable.bg_text_ffff7000_5_radius)
                codeBtn?.isEnabled = true
                codeIsValid = false
            }
        }
        countDownTimer?.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.apply {
            cancel()
            val mmkv = MMKV.defaultMMKV()
            mmkv.putString(Ext.MMKV_KEY_EMAIL_CODE, GsonUtils.toJson(EmailCodeBean(emailS!!,code)))
        }
        countDownTimer = null
    }
}

















