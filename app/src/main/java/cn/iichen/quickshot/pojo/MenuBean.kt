package cn.iichen.quickshot.pojo

/**
 *
 * @ProjectName:    QuickShot
 * @Package:        cn.iichen.quickshot.pojo
 * @ClassName:      MenuBean
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


data class MenuBean(
    val iconId : Int,
    val name : String,
    val switch : Boolean = false
)