package nsu.fit.rttchat

import android.os.AsyncTask
import java.net.ServerSocket

class SocketBackground : AsyncTask<Void, Void, String>() {
    lateinit var serverSocket : ServerSocket

    override fun doInBackground(vararg params: Void) : String{
        serverSocket = ServerSocket(8888)

        return serverSocket.use {
            val client = serverSocket.accept()

            val inputstream = client.getInputStream()

            var message = StringBuffer()

            val data = ByteArray(100)
            while(inputstream.read(data) != -1) {
                message.append(data)
            }

            //write other
            return  ""
        }
    }


}