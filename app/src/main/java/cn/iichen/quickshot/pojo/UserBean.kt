package cn.iichen.quickshot.pojo


data class UserBean(
    val code: Int,
    val `data`: Data,
    val msg: String
){
    data class Data(
        val adNumber: Int,
        val admin: Boolean,
        val apply: Boolean,
        val email: String,
        val enable: Boolean,
        val id: Long,
        val level: Int,
        val nick: String,
        val number: Int,
        val photo: Any,
        val secret: String,
        val userId: String,
        val vip: Boolean,
        val token: String
    )
}
