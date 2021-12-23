package cn.iichen.quickshot.dialog

import android.content.DialogInterface
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.activityViewModels
import cn.iichen.quickshot.R
import cn.iichen.quickshot.ext.Ext
import cn.iichen.quickshot.ext.click
import cn.iichen.quickshot.ext.toast
import cn.iichen.quickshot.ui.home.HomeModel
import com.blankj.utilcode.util.KeyboardUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 *
 * @ProjectName:    QuickShot
 * @Package:        cn.iichen.quickshot.dialog
 * @ClassName:      CodeDialog
 * @Description:     java类作用描述
 * @Author:         作者名 qsdiao
 * @CreateDate:     2021/12/22 20:24
 * @UpdateUser:     更新者：qsdiao
 * @UpdateDate:     2021/12/22 20:24
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


class CodeDialog : BaseDialogFragment(){
    private var codeEv: EditText? = null
    private val model: HomeModel by activityViewModels()

    private val mainScope = CoroutineScope(Dispatchers.Main + Job() )


    override fun handleViewEvent(view: View?) {
        codeEv = view?.findViewById<EditText>(R.id.dialog_code_code)
        val codeActive = view?.findViewById<Button>(R.id.dialog_code_activation)

        codeActive?.click {
            val code = codeEv?.text?.toString()
            if(code?.length != 32)
                "激活码不正确".toast()
            else{
                mainScope.launch {
                    model.doActiveAccount(Ext.user?.userId?:"",code)
                }
            }
        }
    }

    override fun setDialogLayoutId() = R.layout.dialog_code

    override fun setCanceledOnTouchOutside(canceled: Boolean) {
        super.setCanceledOnTouchOutside(true)
    }
}