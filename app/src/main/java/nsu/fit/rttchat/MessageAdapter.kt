package nsu.fit.rttchat

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import nsu.fit.rttchat.model.Message


class MessageAdapter(context : Context) : BaseAdapter() {
    var messages: MutableList<Message> = mutableListOf()
    var context : Context = context

    fun addMessage(message : Message) {
        messages.add(message)
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val holder = MessageViewHolder()
        val messageInflater : LayoutInflater =
            context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val message = messages[position]

        var view = convertView

        if (message.isMe) { // this message was sent by us so let's create a basic chat bubble on the right
            view = messageInflater.inflate(R.layout.my_message, null)
            holder.messageBody = view!!.findViewById(R.id.message_body) as TextView
            view.tag = holder
            holder.messageBody!!.text = message.text
        } else { // this message was sent by someone else so let's create an advanced chat bubble on the left
            /*
            convertView = messageInflater.inflate(R.layout.their_message, null)
            holder.avatar = convertView!!.findViewById(R.id.avatar) as View
            holder.name = convertView!!.findViewById(R.id.name) as TextView
            holder.messageBody = convertView!!.findViewById(R.id.message_body) as TextView
            convertView!!.tag = holder
            holder.name.setText(message.getMemberData().getName())
            holder.messageBody.setText(message.getText())
            holder.avatar.getBackground()
                .setColor(Color.parseColor(message.getMemberData().getColor()))
             */
        }

        return view!!
    }

    override fun getItem(position: Int): Any {
        return messages[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return messages.size
    }
}


internal class MessageViewHolder {
    var avatar: View? = null
    var name: TextView? = null
    var messageBody: TextView? = null
}