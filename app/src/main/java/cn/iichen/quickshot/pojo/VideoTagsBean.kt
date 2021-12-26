package cn.iichen.quickshot.pojo

data class VideoTagsBean(
    val code: Int,
    val `data`: List<Data>,
    val msg: String
){
    data class Data(
        val id: Int,
        val tagId: Int,
        val tagName: String
    )
}