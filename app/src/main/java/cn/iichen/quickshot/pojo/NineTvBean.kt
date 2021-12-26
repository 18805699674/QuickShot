package cn.iichen.quickshot.pojo

data class NineTvBean(
    val current_page: Int,
    val `data`: List<Data>,
    val last_page: Int,
    val per_page: Int,
    val total: Int
)

data class Data(
    val comefrom: Int,
    val comefrom_title: String,
    val description: String,
    val id: Long,
    val is_bloger: Int,
    val is_vip: Int,
    val panorama: String,
    val preview: String,
    val status: Int,
    val tags: List<Tag>,
    val test_video_url: String,
    val thumb: String,
    val title: String,
    val try_second: Int,
    val video_url: String,

//    @Transient
//    var  controller: TxVideoPlayerController?
)

data class Tag(
    val id: Int,
    val name: String
)