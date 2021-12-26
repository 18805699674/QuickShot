package cn.iichen.quickshot.pojo

data class VideoSourceTimeRangeBean(
    val code: Int,
    val `data`: Data,
    val msg: String
){
    data class Data(
        val maxVideoTime: Long,
        val minVideoTime: Long
    )
}