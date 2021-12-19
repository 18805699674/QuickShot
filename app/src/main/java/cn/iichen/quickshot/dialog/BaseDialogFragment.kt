package cn.iichen.quickshot.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment

/**
 *
 * @ProjectName:    QuickShot
 * @Package:        cn.iichen.quickshot.dialog
 * @ClassName:      BaseDialogFragment
 * @Description:     java类作用描述
 * @Author:         作者名 qsdiao
 * @CreateDate:     2021/12/19 19:07
 * @UpdateUser:     更新者：qsdiao
 * @UpdateDate:     2021/12/19 19:07
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


abstract class BaseDialogFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setDimAmount(0.4f)
        dialog?.setCanceledOnTouchOutside(false)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    open fun setDimAmount(dimAmount: Float){
        dialog?.window?.setDimAmount(dimAmount)
    }

    open fun setCanceledOnTouchOutside(canceled:Boolean){
        dialog?.setCanceledOnTouchOutside(canceled)
    }

    override fun onStart() {
        super.onStart()
        initDialogWidth(1f)
    }

    open fun initDialogWidth(radio:Float) {
        if(radio!=1f)
             dialog?.apply {
                val dm = DisplayMetrics()
                requireActivity().windowManager.defaultDisplay.getMetrics(dm)
                window?.setLayout(
                    (dm.widthPixels * radio).toInt(),
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        val inflater = activity?.layoutInflater
        val view: View? = inflater?.inflate(setDialogLayoutId(), null)
        handleViewEvent(view)
        builder.setView(view)
        return builder.create()
    }

    abstract fun handleViewEvent(view: View?)

    abstract fun setDialogLayoutId(): Int

}













