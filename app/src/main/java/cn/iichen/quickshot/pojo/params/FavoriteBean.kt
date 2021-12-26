package cn.iichen.quickshot.pojo.params;

import cn.iichen.quickshot.pojo.Tag

data class FavoriteBean (

    val userId:String,

    val videoId:Long,

    val  title:String,

    val  thumb:String,

    val  tags:List<Tag>,

    val  videoUrl:String
)
