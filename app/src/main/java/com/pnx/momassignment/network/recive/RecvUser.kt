package com.pnx.momassignment.network.recive

import com.pnx.momassignment.room.model.UserItem

class RecvUser:RecvBase() {
    var incomplete_results: Boolean = false
    var items: ArrayList<UserItem> = arrayListOf()
    var total_count: Int = 0

//    class Item: Serializable {
//        var avatar_url: String = ""
//        var events_url: String = ""
//        var followers_url: String = ""
//        var following_url: String = ""
//        var gists_url: String = ""
//        var gravatar_id: String = ""
//        var html_url: String = ""
//        var id: Int = 0
//        var login: String = ""
//        var node_id: String = ""
//        var organizations_url: String = ""
//        var received_events_url: String = ""
//        var repos_url: String = ""
//        var score: Int = 0
//        var site_admin: Boolean = true
//        var starred_url: String = ""
//        var subscriptions_url: String = ""
//        var type: String = ""
//        var url: String = ""
//    }
}