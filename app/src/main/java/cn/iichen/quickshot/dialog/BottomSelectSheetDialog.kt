package cn.iichen.quickshot.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import cn.iichen.quickshot.R
import cn.iichen.quickshot.pojo.BottomSelectSheetBean
import cn.iichen.quickshot.ui.home.HomeModel
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 *
 * @ProjectName:    QuickShot
 * @Package:        cn.iichen.quickshot.dialog
 * @ClassName:      BottomSelectSheetDialog
 * @Description:     java类作用描述
 * @Author:         作者名 qsdiao
 * @CreateDate:     2021/12/25 17:19
 * @UpdateUser:     更新者：qsdiao
 * @UpdateDate:     2021/12/25 17:19
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
enum class BottomSheetType{
    Channel,
    Tag
}

class BottomSelectSheetDialog(
    val dataList: MutableList<BottomSelectSheetBean>,
    val type: BottomSheetType
) : BottomSheetDialogFragment() {
    private val model: HomeModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_bottom_sheet_select, container,false)
        initView(view)
        return view
    }

    private fun initView(view: View?) {
        val titleTv = view?.findViewById<TextView>(R.id.bottom_sheet_select_title)
        val recyclerView = view?.findViewById<RecyclerView>(R.id.bottom_sheet_select_recycle)
        val mAdapter = BottomSelectAdapter()
        mAdapter.addChildClickViewIds(R.id.bottom_sheet_select_recycle_item_tv)
        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            when(view.id){
                R.id.bottom_sheet_select_recycle_item_tv -> {
                    if(type == BottomSheetType.Channel){
                        model.videoJson.value?.let {
                            model.channel(it,dataList[position].id)
                        }
                    }else{
                        model.videoJson.value?.let {
                            model.tag(it,dataList[position].id)
                        }
                    }
                    dismissAllowingStateLoss()
                }
            }
        }
        mAdapter.setNewInstance(dataList)
        recyclerView?.apply {
            adapter = mAdapter
        }
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }
}

class BottomSelectAdapter : BaseQuickAdapter<BottomSelectSheetBean,BaseViewHolder>(R.layout.dialog_bottom_sheet_select_recycle_item){

    override fun convert(holder: BaseViewHolder, item: BottomSelectSheetBean) {
        holder.setText(R.id.bottom_sheet_select_recycle_item_tv,item.desc)
    }

}
















