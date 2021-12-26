package cn.iichen.quickshot.pojo

data class VideoChannelBean(
    val code: Int,
    val `data`: List<Data>,
    val msg: String
){
    data class Data(
        val channelId: Int,
        val channelName: String,
        val id: Any
    )
}