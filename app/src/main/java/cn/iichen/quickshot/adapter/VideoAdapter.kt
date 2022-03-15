package cn.iichen.quickshot.adapter

import android.util.Log
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import cn.iichen.quickshot.R
import cn.iichen.quickshot.encap.NiceVideoPlayer
import cn.iichen.quickshot.encap.TxVideoPlayerController
import cn.iichen.quickshot.ext.Ext
import cn.iichen.quickshot.ext.observeNonNull
import cn.iichen.quickshot.pojo.Data
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

import cn.iichen.quickshot.ext.toast
import cn.iichen.quickshot.pojo.params.FavoriteBean
import cn.iichen.quickshot.ui.home.HomeModel


/**
 *
 * @ProjectName:    QuickShot
 * @Package:        cn.iichen.quickshot.adapter
 * @ClassName:      VideoAdapter
 * @Description:     java类作用描述
 * @Author:         作者名 qsdiao
 * @CreateDate:     2021/12/17 14:02
 * @UpdateUser:     更新者：qsdiao
 * @UpdateDate:     2021/12/17 14:02
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


class VideoAdapter() : BaseQuickAdapter<Data, BaseViewHolder>(R.layout.adapter_nine_tv_item){

    /*
    id: 28705,
    title: "#短视频【国产】这样的美乳白丝妹子操起来就是爽",
    status: 1,
    thumb: "https://imges.zgcbzs.com/77xxx/files/202111/24/1637690262_2iKnhfTb70.jpg",
    preview: "https://imges.zgcbzs.com/91tv/91tv/rXGZpiQWJiHcNiaaVESlBWhOHeQHQB/rXGZpiQWJiHcNiaaVESlBWhOHeQHQB.webp",
    panorama: "https://img.rrt77.com/91tv/91tv/rXGZpiQWJiHcNiaaVESlBWhOHeQHQB/rXGZpiQWJiHcNiaaVESlBWhOHeQHQB_longPreview.jpg",
    description: "",
    video_url: "https://tvcdns.hhw95.com/91tv/91tv/rXGZpiQWJiHcNiaaVESlBWhOHeQHQB/hls/1/index.m3u8",
    comefrom: 59,
    tags: [
    {
    id: 47,
    name: "美少女"
    },
    {
    id: 62,
    name: "国产"
    },
    {
    id: 97,
    name: "自拍"
    },
    {
    id: 99,
    name: "热门推荐"
    }
    ],
    test_video_url: "/91tv/91tv/rXGZpiQWJiHcNiaaVESlBWhOHeQHQB/hls/preview/index.m3u8",
    try_second: 180,
    is_bloger: 0,
    is_vip: 1,
    comefrom_title: "91TV短视频"
     */
    // 记录当前播放的 索引
    override fun convert(holder: BaseViewHolder, item: Data) {
        val controller:TxVideoPlayerController = TxVideoPlayerController(context)
        val mediaPlayer = holder.getView<NiceVideoPlayer>(R.id.nice_video_player)
        // 将列表中的每个视频设置为默认16:9的比例
        val params: ViewGroup.LayoutParams = mediaPlayer.layoutParams
        params.width = holder.itemView.resources.displayMetrics.widthPixels // 宽度为屏幕宽度

        params.height = (params.width * 9f / 16f).toInt() // 高度为宽度的9/16

        mediaPlayer.layoutParams = params
        mediaPlayer.setController(controller)
        controller.setTitle(item.title)
        controller.setTags(item.tags)
        controller.setVideoBean(item)
        Log.d("iichen","###########${item.thumb.replace("tvimg.sjtyks.com","img.rrt77.com")}")
        Glide.with(context)
            .load(item.thumb.replace("tvimg.sjtyks.com","img.rrt77.com"))
            .placeholder(R.drawable.img_default)
            .crossFade()
            .into(controller.imageView())
        mediaPlayer.setUp(item.video_url, null)
    }

}
/*
homeModel.doFavorite(FavoriteBean(
                userId = Ext.user?.userId?:"",
                videoId = data[index].id,
                title = data[index].title,
                thumb = data[index].thumb,
                tags = data[index].tags,
                videoUrl = data[index].video_url
            ))
 */













