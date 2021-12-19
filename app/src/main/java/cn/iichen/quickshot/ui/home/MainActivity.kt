package cn.iichen.quickshot.ui.home

import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import cn.iichen.quickshot.adapter.VideoAdapter
import cn.iichen.quickshot.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main_content.*
import cn.iichen.quickshot.R
import cn.iichen.quickshot.dialog.ForgetDialog
import cn.iichen.quickshot.dialog.InitTipDialog
import cn.iichen.quickshot.dialog.LoginDialog
import cn.iichen.quickshot.dialog.RegisterDialog
import cn.iichen.quickshot.encap.HomeKeyWatcher
import cn.iichen.quickshot.encap.NiceVideoPlayer
import cn.iichen.quickshot.encap.NiceVideoPlayerManager
import cn.iichen.quickshot.ext.*
import cn.iichen.quickshot.utils.mail.MailSender
import com.blankj.utilcode.util.SnackbarUtils
import com.google.android.material.navigation.NavigationView
import com.tencent.mmkv.MMKV
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main_drawer.*
import java.lang.Exception


class MainActivity : BaseActivity(),NavigationView.OnNavigationItemSelectedListener {
    private var videoUrl:String? = null
    private val mViewModel: HomeModel by viewModels()

    private val mAdapter by lazy { VideoAdapter() }

    private var pressedHome = false
    private var mHomeKeyWatcher: HomeKeyWatcher? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mHomeKeyWatcher = HomeKeyWatcher(this)
        mHomeKeyWatcher?.setOnHomePressedListener { pressedHome = true }
        pressedHome = false
        mHomeKeyWatcher?.startWatch()

        val mmkv = MMKV.defaultMMKV()
        val token = mmkv.getString(Ext.MMKV_KEY_TOKEN,null)
        val firstOpenApp = mmkv.getBoolean(Ext.MMKV_KEY_FIRST_OPEN,true)
        if(firstOpenApp){
            SnackbarUtils.with(need_login)
                .setMessage("左侧滑查看更多!")
                .setBgColor(Color.parseColor("#19B5FF"))
                .setMessageColor(Color.WHITE)
                .show()
        }
        mmkv.putBoolean(Ext.MMKV_KEY_FIRST_OPEN,true)

        if(token.isNullOrBlank()){
            need_login.visibility()
            val dialog = LoginDialog()
            dialog.show(supportFragmentManager,"Dialog_LoginDialog")
        }else{ // 请求验证 Token 无效 需要和上面一样的逻辑， 有效获取用户信息
            need_login.gone()
        }


        handlerDrawerView()

        handlerRecycleView()

//        handlerModelView()

        handlerViewEvent()
    }

    private fun handlerDrawerView() {
//        Glide.with(this)
//            .load(R.drawable.img_default)
//            .transform(GlideCircleTransform(this))
//            .into(drawer_photo);

        val headerView = nav_view.getHeaderView(0)
        nav_view.setNavigationItemSelectedListener(this)
    }

    var isLoadMore:Boolean = false
    private fun handlerViewEvent() {
        operate.setOnClickListener {
            recyclerView.smoothScrollToPosition(0)
        }

        smartRefresh.apply {
            setOnRefreshListener {
                try {
                    videoUrl?.apply {
                        var prefix = videoUrl!!.split(".")[0]
                        prefix = prefix.substring(0,prefix.length-1)
                        mViewModel.curPage = 1
                        isLoadMore = false
                        mViewModel?.getNineTvVideo("$prefix${mViewModel.curPage}.json").observeNonNull(this@MainActivity,{
                            loadingView.hide()
                            finishRefresh(it)
                        })
                    }
                }catch (e:Exception){}
            }
            setOnLoadmoreListener {
                try {
                    videoUrl?.apply {
                        var prefix = videoUrl!!.split(".")[0]
                        prefix = prefix.substring(0,prefix.length-1)
                        mViewModel.curPage += 1
                        loadingView.show()
                        isLoadMore = true
                        mViewModel?.getNineTvVideo("$prefix${mViewModel.curPage}.json").observeNonNull(this@MainActivity,{
                            loadingView.hide()
                            finishLoadmore(it)
                        })
                    }
                }catch (e:Exception){}
            }
        }

        need_login.setOnClickListener {
            val dialog = LoginDialog()
            dialog.show(supportFragmentManager,"Dialog_LoginDialog")
        }
    }


    private fun handlerModelView() {
        mViewModel?.apply {

            // 获取视频源
            loadingView.show()
            getVideoSource().observeNonNull(this@MainActivity,{
                // 获取失败 设置默认
                if(!it)
                    videoUrl = "https://nnp35.com/upload_json_live/20211219/videolist_20211219_14_2_-_-_100_1.json"
            })
//             第一步
            // 视频源 Json获取
            videoJson.observeNonNull(this@MainActivity,{
                videoUrl = it
                getNineTvVideo(it).observeNonNull(this@MainActivity,{
                    loadingView.hide()
                })
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
                    mAdapter.setList(it)
                else
                    mAdapter.addData(it)
            })
        }
    }

    private fun handlerRecycleView() {
        recyclerView.apply {
            adapter = mAdapter
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

    override fun onBackPressed() {
        if (NiceVideoPlayerManager.instance().onBackPressd()) {
            return
        }

        super.onBackPressed()
    }


    var isExit:Boolean  = false
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        //2s内按2次返回键退出
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isExit) {//第2次返回键
                finish()
            } else {//第一次返回键
                isExit=true;
                recyclerView.postDelayed({
                    isExit = false;
                },1500)
                "再按一次退出!".toast()
                return  true
            }
        }
        return super.onKeyDown(keyCode, event)
    }


    override fun onRestart() {
        mHomeKeyWatcher?.startWatch()
        pressedHome = false
        super.onRestart()
        NiceVideoPlayerManager.instance().resumeNiceVideoPlayer()
    }

    override fun onStop() {

        // 在OnStop中是release还是suspend播放器，需要看是不是因为按了Home键
        if (pressedHome) {
            NiceVideoPlayerManager.instance().suspendNiceVideoPlayer()
        } else {
            NiceVideoPlayerManager.instance().releaseNiceVideoPlayer()
        }
        super.onStop()
        mHomeKeyWatcher?.stopWatch()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
             R.id.switching_time -> {
                "switching_time".toast()
                 val dialog = LoginDialog()
                 dialog.show(supportFragmentManager,"Dialog_CodeActiveDialog")
             }
             R.id.share_resources -> {
                "share_resources".toast()
                 val dialog = RegisterDialog()
                 dialog.show(supportFragmentManager,"Dialog_CodeActiveDialog")
             }
             R.id.video_integral -> {
                "video_integral".toast()
                 val dialog = ForgetDialog()
                 dialog.show(supportFragmentManager,"Dialog_CodeActiveDialog")
             }
              R.id.code_activation -> {
                "code_activation".toast()
                  val dialog = InitTipDialog()
                  dialog.show(supportFragmentManager,"Dialog_CodeActiveDialog")
             }
            R.id.ad_free_viewing -> {
                "ad_free_viewing".toast()
                MailSender.send("1826522282@qq.com","123456")
             }
        }
        //设置菜单项选中
        item.isCheckable = false
        //关闭Drawer
        drawer.closeDrawers()
        return true
    }
}