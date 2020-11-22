package nsu.fit.rttchat.model

import kotlin.properties.Delegates

public data class Message(
    var text : String,
    //var memberData : MemderData,
    var isMe : Boolean) {
}