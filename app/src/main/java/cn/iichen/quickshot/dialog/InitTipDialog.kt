package cn.iichen.quickshot.dialog

import android.view.View
import android.widget.ImageView
import cn.iichen.quickshot.R


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


class InitTipDialog : BaseDialogFragment() {
    override fun handleViewEvent(view: View?) {
        val loginClose = view?.findViewById<ImageView>(R.id.login_close)
        loginClose?.setOnClickListener {
            dismissAllowingStateLoss()
        }
    }

    override fun setDialogLayoutId(): Int  = R.layout.dialog_init_tip_dialog

    override fun initDialogWidth(radio: Float) {
        super.initDialogWidth(0.8f)
    }
}

















