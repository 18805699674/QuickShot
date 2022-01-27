package cn.iichen.quickshot.adapter

import android.annotation.SuppressLint
import android.widget.ImageView
import cn.iichen.quickshot.pojo.MenuBean
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.Switch
import androidx.core.graphics.drawable.toDrawable
import cn.iichen.quickshot.R
import com.blankj.utilcode.util.ImageUtils
import com.chad.library.adapter.base.BaseProviderMultiAdapter
import com.tencent.mmkv.MMKV
import org.jetbrains.annotations.NotNull


/**
 *
 * @ProjectName:    QuickShot
 * @Package:        cn.iichen.quickshot.adapter
 * @ClassName:      NavContentAdapter
 * @Description:     java类作用描述
 * @Author:         作者名 qsdiao
 * @CreateDate:     2021/12/20 16:31
 * @UpdateUser:     更新者：qsdiao
 * @UpdateDate:     2021/12/20 16:31
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


class NavContentAdapter(data:MutableList<MenuBean>) : BaseQuickAdapter<MenuBean, BaseViewHolder>(R.layout.adapter_nav_item_item,data){
    override fun convert(holder: BaseViewHolder, item: MenuBean) {
        val navItemImg = holder.getView<ImageView>(R.id.nav_item_img)
        val switch = holder.getView<Switch>(R.id.nav_item_switch)
//        Glide.with(context).load(item.iconId).into(navItemImg)
        navItemImg.setImageResource(item.iconId)
        if(item.switch){
            holder.setVisible(R.id.nav_item_arrow,false)
            holder.setVisible(R.id.nav_item_switch,true)

            val mmkv = MMKV.defaultMMKV()
            when(item.name){
                "循环播放" -> {
                    switch.isChecked = mmkv.getBoolean("loop", false)
                }
                "后台播放" -> {
                    switch.isChecked = mmkv.getBoolean("backgroundPlay", true)
                }
            }

            switch.setOnCheckedChangeListener { buttonView, isChecked ->
                when(item.name){
                    "循环播放" -> {
                        mmkv.putBoolean("loop", isChecked)
                    }
                    "后台播放" -> {
                        mmkv.putBoolean("backgroundPlay", isChecked)
                    }
                }
            }
        }else{
            holder.setVisible(R.id.nav_item_arrow,true)
            holder.setVisible(R.id.nav_item_switch,false)
        }
        holder.setText(R.id.nav_item_content,item.name)
    }
}


















