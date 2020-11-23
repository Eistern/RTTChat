package nsu.fit.rttchat.service

import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.net.wifi.aware.*
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log

class WifiAwareService : Service() {
    private var AWARE_FILE_SHARE_SERVICE_NAME: String = "RTT_CHAT_AWARE_SERVICE"
    private lateinit var wifiSession : WifiAwareSession

    fun createRoom() {
        val config: PublishConfig = PublishConfig.Builder()
            .setServiceName(AWARE_FILE_SHARE_SERVICE_NAME)
            .build()
        wifiSession.publish(config, object : DiscoverySessionCallback() {
            override fun onPublishStarted(session: PublishDiscoverySession) {

            }

            override fun onMessageReceived(peerHandle: PeerHandle, message: ByteArray) {

            }
        }, null)
    }

    fun joinRoom() {
        val config: SubscribeConfig = SubscribeConfig.Builder()
            .setServiceName(AWARE_FILE_SHARE_SERVICE_NAME)
            .build()
        wifiSession.subscribe(config, object : DiscoverySessionCallback() {

            override fun onSubscribeStarted(session: SubscribeDiscoverySession) {

            }

            override fun onServiceDiscovered(
                peerHandle: PeerHandle,
                serviceSpecificInfo: ByteArray,
                matchFilter: List<ByteArray>
            ) {

            }
        }, null)
    }

    override fun onCreate() {
        if (!applicationContext.packageManager.hasSystemFeature(PackageManager.FEATURE_WIFI_AWARE)) {
            Log.e("WifiAwareService", "No Wifi Aware avalible")
            throw Exception("No Wifi Aware")
        }
        val wifiAwareManager = getSystemService(WifiAwareManager::class.java) as WifiAwareManager
        wifiAwareManager.attach(SessionFiller(this), object : Handler(Looper.myLooper()!!) {
        })

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