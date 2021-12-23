package cn.iichen.quickshot.pojo

data class VideoSourceBean(
    val code: Int,
    val `data`: Data?,
    val msg: String
){
    data class Data(
        val date: String,
        val id: Long,
        val url: String
    )
}
