package nsu.fit.rttchat.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.wifi.aware.*
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import java.net.ServerSocket

class WifiAwareService(private var context: Context) : Service() {
    private var AWARE_FILE_SHARE_SERVICE_NAME: String = "RTT_CHAT_AWARE_SERVICE"
    private lateinit var wifiSession : WifiAwareSession

    private var messageId = 0

    val androidId = 1

    fun createRoom() {
        val config: PublishConfig = PublishConfig.Builder()
            .setServiceName(AWARE_FILE_SHARE_SERVICE_NAME)
            .build()
        wifiSession.publish(config, object : DiscoverySessionCallback() {
            private lateinit var session: DiscoverySession

            override fun onPublishStarted(session: PublishDiscoverySession) {
                this.session = session
                Log.d("onPublishStarted", "onPublishStarted$androidId")
            }

            override fun onMessageReceived(peerHandle: PeerHandle, message: ByteArray) {
                val socket = ServerSocket(0)
                val port = socket.localPort

                val connMgr = getSystemService(ConnectivityManager::class.java) as ConnectivityManager

                Log.d("onMessageReceived", "onMessageReceived$androidId")
            }
        }, null)
    }

    fun joinRoom() {
        val config: SubscribeConfig = SubscribeConfig.Builder()
            .setServiceName(AWARE_FILE_SHARE_SERVICE_NAME)
            .build()

        wifiSession.subscribe(config, object : DiscoverySessionCallback() {
            private lateinit var peerHandle : PeerHandle

            override fun onServiceDiscovered(
                peerHandle: PeerHandle,
                serviceSpecificInfo: ByteArray,
                matchFilter: List<ByteArray>
            ) {
                this.peerHandle = peerHandle
                Log.d("onServiceDiscovered", "onServiceDiscovered$androidId")
            }

            override fun onSubscribeStarted(session: SubscribeDiscoverySession) {
                session.sendMessage(peerHandle, messageId++, "Hello".toByteArray())
                Log.d("onSubscribeStarted", "onSubscribeStarted$androidId")
            }
        }, null)
    }

    override fun onCreate() {
        if (!context.packageManager.hasSystemFeature(PackageManager.FEATURE_WIFI_AWARE)) {
            Log.e("WifiAwareService", "No Wifi Aware avalible")
            throw Exception("No Wifi Aware")
        }
        val wifiAwareManager = getSystemService(WifiAwareManager::class.java) as WifiAwareManager
        wifiAwareManager.attach(SessionFiller(this), object : Handler(Looper.myLooper()!!) {})

    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onDestroy() {
        wifiSession.close()
    }

    class SessionFiller(private val service: WifiAwareService) : AttachCallback() {
        override fun onAttached(session: WifiAwareSession?) {
            service.wifiSession = session!!
        }
    }
}