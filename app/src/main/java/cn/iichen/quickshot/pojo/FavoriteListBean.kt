package cn.iichen.quickshot.pojo

data class FavoriteListBean(
    val code: Int,
    val `data`: List<Data>,
    val msg: String
){
    data class Data(
        val id: Long,
        val tags: String,
        val thumb: String,
        val title: String,
        val userId: String,
        val videoId: Long,
        val videoUrl: String
    )
}