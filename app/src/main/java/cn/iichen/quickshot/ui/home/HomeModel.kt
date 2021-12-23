package cn.iichen.quickshot.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import cn.iichen.diverseweather.data.remote.doFailure
import cn.iichen.diverseweather.data.remote.doSuccess
import cn.iichen.quickshot.ext.toast
import cn.iichen.quickshot.pojo.*
import cn.iichen.quickshot.pojo.params.RegisterBean
import cn.iichen.quickshot.respository.RepositoryImpl
import kotlinx.coroutines.flow.collectLatest

/**
 *
 * @ProjectName:    QuickShot
 * @Package:        cn.iichen.quickshot.ui.home
 * @ClassName:      HomeModel
 * @Description:     java类作用描述
 * @Author:         作者名 qsdiao
 * @CreateDate:     2021/12/17 14:37
 * @UpdateUser:     更新者：qsdiao
 * @UpdateDate:     2021/12/17 14:37
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


class HomeModel : ViewModel() {
    /*/
        total: 6702,
        per_page: 100,
        current_page: 2,
        last_page: 68,
     */

    private val repository  =  RepositoryImpl()

    // 记录当前页 刷新使用
    var curPage = 1

    private val _currentPage: MutableLiveData<Int> = MutableLiveData()
    val currentPage: LiveData<Int> get() = _currentPage
    private val _lastPage: MutableLiveData<Int> = MutableLiveData()
    val lastPage: LiveData<Int> get() = _lastPage
    private val _videoList: MutableLiveData<List<Data>> = MutableLiveData()
    val videoList: LiveData<List<Data>> get() = _videoList

    fun getNineTvVideo(url:String) = liveData {
        repository.getNineTvVideo(url).collectLatest {
            it.doFailure { throwable ->
                emit(false)
            }
            it.doSuccess { value ->
                emit(true)
                _currentPage.value = value.current_page
                _lastPage.value = value.last_page
                _videoList.value = value.data
            }
        }
    }

    private val _videoJson: MutableLiveData<String> = MutableLiveData()
    val videoJson: LiveData<String> get() = _videoJson


    // 获取视频源
    suspend fun getVideoSource(){
        repository.getVideoSource().collectLatest {
            it.doFailure { throwable ->
            }
            it.doSuccess { value ->
                _videoJson.value = value.data?.url?:""
            }
        }
    }

    fun setVideoUrl(videoUrl: String?) {
        videoUrl?.apply {
            _videoJson.value = this
        }
    }


    private val _userBean: MutableLiveData<UserBean> = MutableLiveData()
    val userBean: LiveData<UserBean> get() = _userBean
    suspend fun doRegister(registerBean: RegisterBean) {
        repository.doRegister(registerBean).collectLatest {
            it.doFailure { throwable ->
            }
            it.doSuccess { value ->
                _userBean.value = value
            }
        }
    }

    suspend fun doLogin(registerBean: RegisterBean) {
        repository.doLogin(registerBean).collectLatest {
            it.doFailure { throwable ->

            }
            it.doSuccess { value ->
                _userBean.value = value
            }
        }
    }

    fun getUserInfo(token: String) = liveData {
        repository.getUserInfo(token).collectLatest {
            it.doFailure { throwable ->
                emit(false)
            }
            it.doSuccess { value ->
                emit(true)
                _userBean.value = value
            }
        }
    }

    private val _activateCodeBean: MutableLiveData<ActivateCodeBean> = MutableLiveData()
    val activateCodeBean: LiveData<ActivateCodeBean> get() = _activateCodeBean
    suspend fun doActiveAccount(userId: String, code: String) {
        repository.doActiveAccount(userId,code).collectLatest {
            it.doFailure { throwable ->
            }
            it.doSuccess { value ->
                _activateCodeBean.value = value
            }
        }
    }


    private val _videoSourceTimeRangeBean: MutableLiveData<VideoSourceTimeRangeBean> = MutableLiveData()
    val videoSourceTimeRangeBean: LiveData<VideoSourceTimeRangeBean> get() = _videoSourceTimeRangeBean
    suspend fun getVideoSourceTimeRange(){
        repository.getVideoSourceTimeRange().collectLatest {
            it.doFailure { throwable ->
            }
            it.doSuccess { value ->
                _videoSourceTimeRangeBean.value = value
            }
        }
    }

    private val _videoSourceBean: MutableLiveData<VideoSourceBean> = MutableLiveData()
    val videoSourceBean: LiveData<VideoSourceBean> get() = _videoSourceBean
    suspend fun getVideoSourceByTime(time: String) {
        repository.getVideoSourceByTime(time).collectLatest {
            it.doFailure { throwable ->
            }
            it.doSuccess { value ->
                _videoSourceBean.value = value
            }
        }
    }


}









