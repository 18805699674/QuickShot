package cn.iichen.quickshot.dialog

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import cn.iichen.quickshot.R
import cn.iichen.quickshot.ext.App
import cn.iichen.quickshot.ext.click
import cn.iichen.quickshot.ext.toast
import cn.iichen.quickshot.pojo.Data
import cn.iichen.quickshot.ui.home.HomeModel
import cn.iichen.quickshot.ui.home.MainActivity
import com.blankj.utilcode.util.RegexUtils
import com.tencent.mmkv.MMKV

/**
 *
 * @ProjectName:    QuickShot
 * @Package:        cn.iichen.quickshot.dialog
 * @ClassName:      searchDialog
 * @Description:     java类作用描述
 * @Author:         作者名 qsdiao
 * @CreateDate:     2021/12/22 20:24
 * @UpdateUser:     更新者：qsdiao
 * @UpdateDate:     2021/12/22 20:24
 * @UpdateRemark:   更新说明：Fuck search, go to hell, serious people who maintain it：
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


class SwitchServiceDialog(private val curVideoDataList: MutableList<Data>) : BaseDialogFragment(){
    private val model: HomeModel by activityViewModels()

    override fun handleViewEvent(view: View?) {
        val searchEv = view?.findViewById<EditText>(R.id.dialog_search_key)
        val searchTv = view?.findViewById<TextView>(R.id.dialog_search_title)
        val searchActive = view?.findViewById<Button>(R.id.dialog_search_search)

        searchTv?.text = "输入主机名"
        searchActive?.text = "立即切换"

        searchActive?.click {
            val search = searchEv?.text?.toString()
            if(search.isNullOrEmpty())
                "主机名不能为空！".toast()
           else{
                val mmkv = MMKV.defaultMMKV()
                mmkv.putString("domain",search)
                dismissAllowingStateLoss()
                restartApp()
            }
        }
    }

    fun restartApp(){
        val intent = Intent(activity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        activity?.startActivity(intent)
        activity?.finish()
        Runtime.getRuntime().exit(0)
    }

    override fun setDialogLayoutId() = R.layout.dialog_search

    override fun setCanceledOnTouchOutside(canceled: Boolean) {
        super.setCanceledOnTouchOutside(true)
    }
}