package cn.iichen.quickshot.ui.home

import androidx.lifecycle.*
import cn.iichen.diverseweather.data.remote.doFailure
import cn.iichen.diverseweather.data.remote.doSuccess
import cn.iichen.quickshot.adapter.VideoAdapter
import cn.iichen.quickshot.pojo.*
import cn.iichen.quickshot.pojo.params.RegisterBean
import cn.iichen.quickshot.respository.RepositoryImpl
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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
    val mAdapter by lazy { VideoAdapter() }

    private val repository = RepositoryImpl()

    // 记录当前页 刷新使用
    var curPage = 1

    private val _currentPage: MutableLiveData<Int> = MutableLiveData()
    val currentPage: LiveData<Int> get() = _currentPage
    private val _lastPage: MutableLiveData<Int> = MutableLiveData()
    val lastPage: LiveData<Int> get() = _lastPage
    private val _videoList: MutableLiveData<List<Data>> = MutableLiveData()
    val videoList: LiveData<List<Data>> get() = _videoList

    fun getNineTvVideo(url: String) = liveData {
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

    private val _videoJson: MutableLiveData<String> = MutableLiveData(null)
    val videoJson: LiveData<String> get() = _videoJson


    private val _fail: MutableLiveData<Boolean> = MutableLiveData(false)
    val fail: LiveData<Boolean> get() = _fail

    // 获取视频源
    suspend fun getVideoSource() {
        repository.getVideoSource().collectLatest {
            it.doFailure { throwable ->
                _fail.value = !_fail.value!!
            }
            it.doSuccess { value ->
                _videoJson.value = value.data?.url ?: ""
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
                _fail.value = !_fail.value!!
            }
            it.doSuccess { value ->
                _userBean.value = value
            }
        }
    }

    suspend fun doLogin(registerBean: RegisterBean) {
        repository.doLogin(registerBean).collectLatest {
            it.doFailure { throwable ->
                _fail.value = !_fail.value!!
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

    private val _baseBean: MutableLiveData<BaseBean> = MutableLiveData()
    val baseBean: LiveData<BaseBean> get() = _baseBean
    suspend fun doActiveAccount(userId: String, code: String) {
        repository.doActiveAccount(userId, code).collectLatest {
            it.doFailure { throwable ->
                _fail.value = !_fail.value!!
            }
            it.doSuccess { value ->
                _baseBean.value = value
            }
        }
    }


    private val _videoSourceTimeRangeBean: MutableLiveData<VideoSourceTimeRangeBean> =
        MutableLiveData()
    val videoSourceTimeRangeBean: LiveData<VideoSourceTimeRangeBean> get() = _videoSourceTimeRangeBean
    suspend fun getVideoSourceTimeRange() {
        repository.getVideoSourceTimeRange().collectLatest {
            it.doFailure { throwable ->
                _fail.value = !_fail.value!!
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
                _fail.value = !_fail.value!!
            }
            it.doSuccess { value ->
                _videoSourceBean.value = value
            }
        }
    }


    private val _videoChannelBean: MutableLiveData<VideoChannelBean> = MutableLiveData()
    val videoChannelBean: LiveData<VideoChannelBean> get() = _videoChannelBean
    suspend fun getVideoChannels() {
        repository.getVideoChannels().collectLatest {
            it.doFailure { throwable ->
                _fail.value = !_fail.value!!
            }
            it.doSuccess { value ->
                _videoChannelBean.value = value
            }
        }
    }

    private val _videoTagsBean: MutableLiveData<VideoTagsBean> = MutableLiveData()
    val videoTagsBean: LiveData<VideoTagsBean> get() = _videoTagsBean
    suspend fun getVideoTags() {
        repository.getVideoTags().collectLatest {
            it.doFailure { throwable ->
                _fail.value = !_fail.value!!
            }
            it.doSuccess { value ->
                _videoTagsBean.value = value
            }
        }
    }

    private val _favoriteListBean: MutableLiveData<FavoriteListBean> = MutableLiveData()
    val favoriteListBean: LiveData<FavoriteListBean> get() = _favoriteListBean
    fun getFavorite(userId: String) {
        viewModelScope.launch {
            repository.getFavorite(userId).collectLatest {
                it.doFailure { throwable ->
                    _fail.value = !_fail.value!!
                }
                it.doSuccess { value ->
                    _favoriteListBean.value = value
                }
            }
        }
    }
//    ---------------------------------------------------------------------------------------
//    ---------------------------------------------------------------------------------------

    fun paging(source: String, curPage: Int): String {
        val lastPointIndex = source.lastIndexOf(".")
        val prefix = source.substring(0, lastPointIndex - 1)
        return "$prefix$curPage.json"
    }


    var hvTagChannel:Boolean = false
    var originVideoUrl:String = ""
    fun channel(source: String, id: Int): String {
        var mSource = source
        if(source.isNotEmpty()){
            if(hvTagChannel){
                mSource = originVideoUrl
            }else{
                originVideoUrl = mSource
                hvTagChannel = true
            }
            val ketIndex = mSource.lastIndexOf("-")
            val prefix = mSource.substring(0, ketIndex)
            val suffix = mSource.substring(ketIndex + 1)
            setVideoUrl("$prefix$id$suffix")
            return "$prefix$id$suffix"
        }
        return mSource
    }

    fun tag(source: String, id: Int): String {
        var mSource = source
        if(source.isNotEmpty()){
            if(hvTagChannel){
                mSource = originVideoUrl
            }else{
                originVideoUrl = mSource
                hvTagChannel = true
            }
            val ketIndex = mSource.indexOf("-")
            val prefix = mSource.substring(0, ketIndex)
            val suffix = mSource.substring(ketIndex + 1)
            setVideoUrl("$prefix$id$suffix")
            return "$prefix$id$suffix"
        }
        return mSource
    }
}









