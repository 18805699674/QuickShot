package cn.iichen.quickshot.ui.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Process
import android.view.*
import androidx.activity.viewModels
import androidx.core.view.GravityCompat
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import cn.iichen.quickshot.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main_content.*
import cn.iichen.quickshot.R
import cn.iichen.quickshot.adapter.NavContentAdapter
import cn.iichen.quickshot.dialog.*
import cn.iichen.quickshot.encap.HomeKeyWatcher
import cn.iichen.quickshot.encap.NiceVideoPlayer
import cn.iichen.quickshot.encap.NiceVideoPlayerManager
import cn.iichen.quickshot.ext.*
import com.blankj.utilcode.util.SnackbarUtils
import com.tencent.mmkv.MMKV
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

import android.view.MotionEvent
import cn.iichen.quickshot.net.RetrofitClient
import cn.iichen.quickshot.pojo.*
import com.alibaba.fastjson.JSONObject

import com.blankj.utilcode.util.KeyboardUtils
import com.jzxiang.pickerview.TimePickerDialog
import com.jzxiang.pickerview.data.Type
import kotlinx.android.synthetic.main.tx_video_palyer_controller.*
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Delayed
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.util.Log
import android.widget.*


class MainActivity : BaseActivity() {
    private var token: String? = null
    private var dialog: DialogFragment? = null
    private var videoUrl:String? = null
    private val mViewModel: HomeModel by viewModels()

    private var pressedHome = false
    private var mHomeKeyWatcher: HomeKeyWatcher? = null


//    private val ioScope = CoroutineScope(Dispatchers.Main + Job() )
    private val ioScope = MainScope()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // 初始化展开
        drawer.openDrawer(GravityCompat.START)


        mHomeKeyWatcher = HomeKeyWatcher(this)
        mHomeKeyWatcher?.setOnHomePressedListener { pressedHome = true }
        pressedHome = false
        mHomeKeyWatcher?.startWatch()

        val mmkv = MMKV.defaultMMKV()
        token = mmkv.getString(Ext.MMKV_KEY_TOKEN,null)
        val firstOpenApp = mmkv.getBoolean(Ext.MMKV_KEY_FIRST_OPEN,true)
        if(firstOpenApp){
            SnackbarUtils.with(need_login)
                .setMessage("左侧滑查看更多!")
                .setBgColor(Color.parseColor("#19B5FF"))
                .setMessageColor(Color.WHITE)
                .show()
        }
        mmkv.putBoolean(Ext.MMKV_KEY_FIRST_OPEN,false)

        if(token.isNullOrBlank()){
            need_login.visibility()
            dialog = LoginDialog()
            dialog?.show(supportFragmentManager,"Dialog_LoginDialog")
        }else{ // 请求验证 Token 无效 需要和上面一样的逻辑， 有效获取用户信息
            need_login.gone()
            token?.apply {
                mViewModel.getUserInfo(this).observeNonNull(this@MainActivity,{})
            }
        }


        handlerDrawerView()

        handlerRecycleView()

        handlerModelView()

        handlerViewEvent()
    }

    private fun handlerDrawerView() {
//        Glide.with(this)
//            .load(R.drawable.img_default)
//            .transform(GlideCircleTransform(this))
//            .into(drawer_photo);

//        val headerView = nav_view.getHeaderView(0)
//        nav_view.setNavigationItemSelectedListener(this)
        val data = mutableListOf(
            MenuBean(R.drawable.time_fill,"切换源"),
            MenuBean(R.drawable.resource,"分享资源"),
            MenuBean(R.drawable.favorite,"我的收藏"),
            MenuBean(R.drawable.activation,"激活码激活"),
            MenuBean(R.drawable.searchs,"当前源搜索"),
            MenuBean(R.drawable.service,"切换服务器"),
            MenuBean(R.drawable.channel,"指定视频频道"),
            MenuBean(R.drawable.tags,"指定视频标签"),
            // 请求用户信息后 额外添加的  管理员有的权限  不搭建后台 在这里作为后台操作
            MenuBean(R.drawable.loop,"循环播放",true),
            MenuBean(R.drawable.backgroundplay,"后台播放",true),
         )
        val headView = LayoutInflater.from(this).inflate(R.layout.activity_main_drawer_header,nav_view_recycle,false)
//        val footView = LayoutInflater.from(this).inflate(R.layout.activity_main_drawer_foot,nav_view_recycle,false)
        val adapter = NavContentAdapter(data)
        adapter.setHeaderView(headView)
//        adapter.setFooterView(footView)
        nav_view_recycle.adapter = adapter

        // headerView的点击事件
        handlerDrawerHeadViewEvent(headView)
//        handlerDrawerFootViewEvent(footView)
        adapter.addChildClickViewIds(R.id.nav_item_ll)
        adapter.setOnItemChildClickListener { adapter, view, position ->
            when(view.id){
                // 菜单点击
                R.id.nav_item_ll -> {
                    when(position){
//                        切换源
                        0 -> {
                            if(videoSourceTimeRangeLoad==null)
                                ioScope.launch {
                                     mViewModel.getVideoSourceTimeRange()
                                }
                            else{
                                showVideoTimeSelectorDialog()
                            }
                        }
                        //分享资源
                        1 -> {

                        }
                        //我的收藏
                        2 -> {
                            Ext.user?.userId?.let {
                                drawer.closeDrawers()
                                loadingView.show()
                                mViewModel.getFavorite(it)
                            }
                        }
                        //激活码激活
                        3 -> {
                            if(Ext.user==null){
                                dialog = LoginDialog()
                                dialog?.show(supportFragmentManager,"Dialog_LoginDialog")
                            }

                            Ext.user?.apply {
                                if(enable){
                                    "已激活".toast()
                                }else{
                                    dialog = CodeDialog()
                                    dialog?.show(supportFragmentManager,"Dialog_CodeDialog")
                                }
                            }
                        }
                        //当前源搜索
                        4 -> {
                            val curVideoDataList: MutableList<Data> = mViewModel.mAdapter.data
                            dialog = SearchDialog(curVideoDataList)
                            dialog?.show(supportFragmentManager,"Dialog_SearchDialog")
                            drawer.closeDrawers()
                        }
                        5 -> {
                            val curVideoDataList: MutableList<Data> = mViewModel.mAdapter.data
                            dialog = SwitchServiceDialog(curVideoDataList)
                            dialog?.show(supportFragmentManager,"Dialog_SearchDialog")
                            drawer.closeDrawers()
                        }
                        //指定视频频道
                        6 -> {
                            drawer.closeDrawers()
                            if(mViewModel.videoChannelBean.value==null) {
                                ioScope.launch {
                                    loadingView.show()
                                    mViewModel.getVideoChannels()
                                }
                            }else{
                                openBottomChanelSheetSelect(mViewModel.videoChannelBean.value!!.data)
                            }
                        }
                        //指定视频标签
                        7 -> {
                            drawer.closeDrawers()
                            if(mViewModel.videoTagsBean.value==null) {
                                ioScope.launch {
                                    loadingView.show()
                                    mViewModel.getVideoTags()
                                }
                            }else{
                                openBottomTagsSheetSelect(mViewModel.videoTagsBean.value!!.data)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showVideoTimeSelectorDialog() {
        TimePickerDialog.Builder()
            .setCallBack { timePickerView, millseconds ->
                val sdf = SimpleDateFormat("yyyyMMdd")
                val time: String = sdf.format(Date(millseconds)) // 时间戳转换日期
                drawer.closeDrawers()
                loadingView.show()
                ioScope.launch {
                    mViewModel.getVideoSourceByTime(time)
                }
            }
            .setCancelStringId("取消")
            .setSureStringId("确认")
            .setTitleStringId("切换源")
            .setYearText("年")
            .setMonthText("月")
            .setDayText("日")
            .setCyclic(false)
            .setMinMillseconds(videoSourceTimeRangeLoad?.minVideoTime?:System.currentTimeMillis())
            .setMaxMillseconds(videoSourceTimeRangeLoad?.maxVideoTime?:System.currentTimeMillis())
            .setCurrentMillseconds(videoSourceTimeRangeLoad?.maxVideoTime?:System.currentTimeMillis())
            .setThemeColor(Color.parseColor("#FF9D02"))
            .setType(Type.YEAR_MONTH_DAY)
            .setWheelItemTextNormalColor(resources.getColor(R.color.timetimepicker_default_text_color))
            .setWheelItemTextSelectorColor(resources.getColor(R.color.timepicker_toolbar_bg))
            .setWheelItemTextSize(12)
            .build().show(supportFragmentManager,"TimePickerDialog")
    }

    private fun handlerDrawerFootViewEvent(footView: View?) {

    }

    var drawerLevel:ImageView? = null
    var drawerNick:TextView? = null
    var drawerLrLl:LinearLayout? = null
    private fun handlerDrawerHeadViewEvent(headView: View?) {
        headView?.apply {
            val drawerPhoto = findViewById<CircleImageView>(R.id.drawer_photo)
            val drawerLogin = findViewById<TextView>(R.id.drawer_login)
            val drawerRegister = findViewById<TextView>(R.id.drawer_register)

            drawerLevel = findViewById<ImageView>(R.id.drawer_level)
            drawerNick = findViewById<TextView>(R.id.drawer_nick)
            drawerLrLl = findViewById<LinearLayout>(R.id.drawer_lr_ll)


            // 头布局 头像 点击到 个人信息
            drawerPhoto.click {
                "个人信息".toast()
            }
            // 到登录
            drawerLogin.click {
                drawer.closeDrawers()
                dialog = LoginDialog()
                dialog?.show(supportFragmentManager,"Dialog_LoginDialog")
            }
            // 到注册
            drawerRegister.click {
                drawer.closeDrawers()
                dialog = RegisterDialog()
                dialog?.show(supportFragmentManager,"Dialog_RegisterDialog")
            }
        }
    }

    var isLoadMore:Boolean = false
    private fun handlerViewEvent() {
        operate.setOnClickListener {
            recyclerView.smoothScrollToPosition(0)
        }

        smartRefresh.apply {
            setOnRefreshListener {
                try {
                    if(videoUrl==null){
                        requestVideo()
                        finishRefresh(false)
                    }else{
                        mViewModel.curPage = 1
                        isLoadMore = false
                        mViewModel?.getNineTvVideo(mViewModel.paging(videoUrl!!,1)).observeNonNull(this@MainActivity,{
                            loadingView.hide()
                            finishRefresh(it)
                        })
                    }
                }catch (e:Exception){finishRefresh(false)}
            }
            setOnLoadmoreListener {
                try {
                    if(videoUrl==null){
                        requestVideo()
                        finishRefresh(false)
                    }else {
                        mViewModel.curPage += 1
                        loadingView.show()
                        isLoadMore = true
                        mViewModel?.getNineTvVideo(mViewModel.paging(videoUrl!!,mViewModel.curPage)).observeNonNull(this@MainActivity,{
                            loadingView.hide()
                            finishLoadmore(it)
                        })
                    }
                }catch (e:Exception){finishLoadmore(false)}
            }
        }

        need_login.setOnClickListener {
            dialog = LoginDialog()
            dialog?.show(supportFragmentManager,"Dialog_LoginDialog")
        }
    }


    private var videoSourceTimeRangeLoad: VideoSourceTimeRangeBean.Data? = null
    private fun handlerModelView() {
        mViewModel?.apply {
            fail.observeNonNull(this@MainActivity,{
               loadingView.hide()
            })


//              第二步
            currentPage.observeNonNull(this@MainActivity,{
                currentPageNum.text = "$it"
            })
            lastPage.observeNonNull(this@MainActivity,{
                totalPageNum.text = "$it"
            })
            videoList.observeNonNull(this@MainActivity,{
                if(!it.isNullOrEmpty())
                    operate.visibility = View.VISIBLE
                if(isLoadMore)
                    mAdapter.addData(it)
                else{
                    val data = it.toMutableList()
                    mAdapter.setNewInstance(data)
                }
            })

            // 用户信息
            userBean.observeNonNull(this@MainActivity,{
                loadingView.hide()
                if(it.code == Ext.SERVICE_ERROR_CODE){
                    it.msg.toast()
                    dialog = LoginDialog()
                    dialog?.show(supportFragmentManager,"Dialog_LoginDialog")
                }else{
                    dialog?.dismissAllowingStateLoss()
                    Ext.user = it.data
                    drawerLevel?.setLevel(if(it.data.vip) 6 else it.data.level)
                    drawerLrLl?.gone()
                    drawerNick?.visibility()
                    drawerNick?.text = it.data.nick
                    val mmkv:MMKV = MMKV.defaultMMKV()
                    mmkv.putString(Ext.MMKV_KEY_TOKEN,it.data.token)

                    requestVideo()
                }
            })

            videoJson.observeNonNull(this@MainActivity,{
                if(it.isNotEmpty()){
                    videoUrl = it
                    loadingView.show()
                    getNineTvVideo(it).observeNonNull(this@MainActivity,{
                        loadingView.hide()
                    })
                }else{
                    "今日资源未添加".toast()
                    loadingView.hide()
                }
            })

            // 使用激活码
            baseBean.observeNonNull(this@MainActivity,{
                if(it.code == Ext.SERVICE_ERROR_CODE){
                    it.msg.toast()
                }else{
                    dialog?.dismissAllowingStateLoss()
                    it.data.toast()
                    token?.apply {
                        mViewModel.getUserInfo(this).observeNonNull(this@MainActivity,{})
                    }
                }
            })

            // 获取视频源 最小和最大时间 用于显示
            videoSourceTimeRangeBean.observeNonNull(this@MainActivity,{
                if(it.code == Ext.SERVICE_ERROR_CODE){
                    it.msg.toast()
                }else{
                    videoSourceTimeRangeLoad = it.data
                    showVideoTimeSelectorDialog()
                }
            })

            // 获取指定日期的 视频源
            videoSourceBean.observeNonNull(this@MainActivity,{
                if(it.code == Ext.SERVICE_ERROR_CODE){
                    it.msg.toast()
                }else{
                    // 重置 视频主源  即可
                    mViewModel.setVideoUrl(it.data?.url)
                }
            })

            // 所有频道
            videoChannelBean.observeNonNull(this@MainActivity,{ it ->
                loadingView.hide()
                val data = it.data
                if(data.isNotEmpty()){
                    openBottomChanelSheetSelect(data)
                }else{
                    "频道信息为空".toast()
                }
            })

            // 所有标签
            videoTagsBean.observeNonNull(this@MainActivity,{ it ->
                loadingView.hide()
                val data = it.data
                if(data.isNotEmpty()){
                    openBottomTagsSheetSelect(data)
                }else{
                    "标签信息为空".toast()
                }
            })

            favoriteListBean.observeNonNull(this@MainActivity,{ it ->
                loadingView.hide()
                val data = it.data
                val dataList = mutableListOf<Data>()
                data.forEach {
                    dataList.add(Data(
                        comefrom = 0,
                        comefrom_title = it.title,
                        description = "",
                        id = it.videoId,
                        is_bloger = 0,
                        is_vip = 0,
                        panorama = "",
                        preview = "",
                        status = 0,
                        tags = JSONObject.parseArray(it.tags,Tag::class.java),
                        test_video_url = it.videoUrl,
                        thumb = it.thumb,
                        title = it.title,
                        try_second = 0,
                        video_url = it.videoUrl,
                    ))
                }
                if(dataList.isNotEmpty()){
                    "下拉刷新或者上拉推荐吧。".toast()
                    mAdapter.setNewInstance(dataList)
                }else{
                    "没有收藏哦！下拉刷新或者上拉推荐吧。".toast()
                }
            })
        }
    }

    private fun openBottomTagsSheetSelect(data: List<VideoTagsBean.Data>) {
        val adapterData = mutableListOf<BottomSelectSheetBean>()
        data.forEach {
            adapterData.add(BottomSelectSheetBean(it.tagId,it.tagName))
        }
        val dialog = BottomSelectSheetDialog(adapterData,BottomSheetType.Tag)
        dialog?.show(supportFragmentManager,"Dialog_BottomSelectSheetDialog")
    }

    private fun openBottomChanelSheetSelect(data: List<VideoChannelBean.Data>) {
        val adapterData = mutableListOf<BottomSelectSheetBean>()
        data.forEach {
            adapterData.add(BottomSelectSheetBean(it.channelId,it.channelName))
        }
        val dialog = BottomSelectSheetDialog(adapterData,BottomSheetType.Channel)
        dialog?.show(supportFragmentManager,"Dialog_BottomSelectSheetDialog")
    }


    private fun checkUserEnable() :Boolean {
        Ext.user?.apply {
            if(enable){
                return true
            }else{
                dialog = CodeDialog()
                dialog?.show(supportFragmentManager,"Dialog_CodeDialog")
            }
        }
        return false
    }

    private fun requestVideo(){
        val enable = checkUserEnable()
        if(enable){
            loadingView.show()
            need_login.gone()

            ioScope.launch {
                mViewModel.getVideoSource()
            }
        }else{
            need_login.visibility()
            need_login.text = "点击激活"
            need_login.setOnClickListener {
                dialog = CodeDialog()
                dialog?.show(supportFragmentManager,"Dialog_CodeDialog")
            }
        }
    }

    private fun handlerRecycleView() {
        recyclerView.apply {
            adapter = mViewModel.mAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
            // 出发回收机制执行
            setRecyclerListener { holder ->
                val niceVideoPlayer: NiceVideoPlayer = holder.itemView.findViewById(R .id.nice_video_player)
                if (niceVideoPlayer === NiceVideoPlayerManager.instance().currentNiceVideoPlayer) {
                    NiceVideoPlayerManager.instance().releaseNiceVideoPlayer()
                }
            }
        }

    }

    var isExit:Boolean  = false
    override fun onBackPressed() {
        if (NiceVideoPlayerManager.instance().onBackPressd()) {
            return
        }

        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START)
            return
        }else{
            if (isExit) {//第2次返回键
                finish()
            } else {//第一次返回键
                isExit=true;
                recyclerView.postDelayed({
                    isExit = false;
                },1500)
                "再按一次退出!".toast()
                return
            }
        }

        super.onBackPressed()
    }





    override fun onRestart() {
        mHomeKeyWatcher?.startWatch()
        pressedHome = false
        super.onRestart()

        val mmkv = MMKV.defaultMMKV()
        val backgroundPlay = mmkv.getBoolean("backgroundPlay", true)
        if(!backgroundPlay)
            NiceVideoPlayerManager.instance().resumeNiceVideoPlayer()
    }

    override fun onStop() {

        // 在OnStop中是release还是suspend播放器，需要看是不是因为按了Home键
        val mmkv = MMKV.defaultMMKV()
        val backgroundPlay = mmkv.getBoolean("backgroundPlay", true)
        if(!backgroundPlay)
            if (pressedHome) {
                NiceVideoPlayerManager.instance().suspendNiceVideoPlayer()
            } else {
                NiceVideoPlayerManager.instance().releaseNiceVideoPlayer()
            }
        super.onStop()
        mHomeKeyWatcher?.stopWatch()
    }


    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (isShouldHideKeyboard(v, ev)) {
                KeyboardUtils.hideSoftInput(this)
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    // Return whether touch the view.
    private fun isShouldHideKeyboard(v: View?, event: MotionEvent): Boolean {
        if (v is EditText) {
            val l = intArrayOf(0, 0)
            v.getLocationOnScreen(l)
            val left = l[0]
            val top = l[1]
            val bottom = top + v.getHeight()
            val right = left + v.getWidth()
            return !(event.rawX > left && event.rawX < right && event.rawY > top && event.rawY < bottom)
        }
        return false
    }
}