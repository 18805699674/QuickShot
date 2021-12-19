package cn.iichen.quickshot.utils

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.os.Message
import java.lang.ref.WeakReference

/**
 *
 * @ProjectName:    QuickShot
 * @Package:        cn.iichen.quickshot.utils
 * @ClassName:      IIHandler
 * @Description:     java类作用描述
 * @Author:         作者名 qsdiao
 * @CreateDate:     2021/12/16 22:27
 * @UpdateUser:     更新者：qsdiao
 * @UpdateDate:     2021/12/16 22:27
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


class IIHandler(var activity: Activity?,var callback:HandlerMessage) : Handler(Looper.getMainLooper()) {
    private val mActivity: WeakReference<Activity> = WeakReference(activity)

    override fun handleMessage(msg: Message) {
        super.handleMessage(msg)
        activity?.apply {
            callback?.handleMessage(msg)
        }
    }

    interface HandlerMessage{
        fun handleMessage(msg: Message)
    }
}
