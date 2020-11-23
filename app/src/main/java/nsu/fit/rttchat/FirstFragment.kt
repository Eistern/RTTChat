package nsu.fit.rttchat

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import nsu.fit.rttchat.model.Message

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var editText: EditText? = null

    private var messageAdapter : MessageAdapter? = null

    private var messagesView : ListView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        messageAdapter = context?.let { MessageAdapter(it) }
        messagesView = view.findViewById(R.id.messages_view) as ListView
        messagesView?.adapter = messageAdapter

        editText = view.findViewById<EditText>(R.id.nameInput)

        view.findViewById<FloatingActionButton>(R.id.sendButton).setOnClickListener {
            val text : String = if (editText?.text == null) "" else editText?.text.toString()

            if (text.isNotEmpty()) {
                editText?.text?.clear()

                //Исправить на отправку сообщений из метода-перехватчика сообщений
                val message : Message = Message(text = text, isMe = true)

                activity?.runOnUiThread {
                    messageAdapter?.addMessage(message)
                    messagesView?.setSelection(messagesView!!.count - 1)
                }
            }
        }
    }
}