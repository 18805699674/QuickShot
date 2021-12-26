package cn.iichen.quickshot.dialog

import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.activityViewModels
import cn.iichen.quickshot.R
import cn.iichen.quickshot.ext.click
import cn.iichen.quickshot.ext.toast
import cn.iichen.quickshot.pojo.Data
import cn.iichen.quickshot.ui.home.HomeModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

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


class SearchDialog(private val curVideoDataList: MutableList<Data>) : BaseDialogFragment(){
    private val model: HomeModel by activityViewModels()

    override fun handleViewEvent(view: View?) {
        val searchEv = view?.findViewById<EditText>(R.id.dialog_search_key)
        val searchActive = view?.findViewById<Button>(R.id.dialog_search_search)

        searchActive?.click {
            val search = searchEv?.text?.toString()
            if(search.isNullOrEmpty())
                "关键词不能为空！".toast()
            else{
                val filterListData = curVideoDataList.filter {
                    it.title.contains(search,ignoreCase = true)
                }
                if(filterListData.isNotEmpty()){
                    model.mAdapter.setNewInstance(filterListData.toMutableList());
                }
                dismissAllowingStateLoss()
            }
        }
    }

    override fun setDialogLayoutId() = R.layout.dialog_search

    override fun setCanceledOnTouchOutside(canceled: Boolean) {
        super.setCanceledOnTouchOutside(true)
    }
}