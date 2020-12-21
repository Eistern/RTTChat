package nsu.fit.rttchat

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.wifi.p2p.WifiP2pConfig
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pDeviceList
import android.net.wifi.p2p.WifiP2pManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.*

class P2pBroadcasrReceiver : BroadcastReceiver {
    var manager : WifiP2pManager
    var channel : WifiP2pManager.Channel
    var activity : MainActivity

    var ipGetter = GetIpByArp()

    constructor(manager: WifiP2pManager, channel : WifiP2pManager.Channel, activity: MainActivity) {
        this.manager = manager
        this.channel = channel
        this.activity = activity
    }

    fun renewDevicesInf() {
            if (ActivityCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(activity,
                    listOf(Manifest.permission.ACCESS_FINE_LOCATION).toTypedArray(), 200)
            }
            manager.requestPeers(channel) { peers: WifiP2pDeviceList? ->
                for(device : WifiP2pDevice? in peers?.deviceList!!) {
                    val config = WifiP2pConfig()
                    config.deviceAddress = device?.deviceAddress

                    device?.deviceAddress?.let { ipGetter.getIpByArp(it) }
                }
            }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        renewDevicesInf()

        when (intent?.action) {
            WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION -> {
                when (intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1)) {
                    WifiP2pManager.WIFI_P2P_STATE_ENABLED -> {
                        // It's ok
                    }
                    else -> {
                        Log.e("P2PWatcher", "P2P Unavailable")
                        throw Exception("P2P Unavailable")
                    }
                }
            }

            WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION -> {
                if (ActivityCompat.checkSelfPermission(
                        activity,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                manager.requestPeers(channel) { peers: WifiP2pDeviceList? ->
                    for(device : WifiP2pDevice? in peers?.deviceList!!) {
                        val config = WifiP2pConfig()
                        config.deviceAddress = device?.deviceAddress

                        channel.also { channel ->
                            manager.connect(channel, config, object : WifiP2pManager.ActionListener {
                                override fun onSuccess() {
                                    //success logic

                                }

                                override fun onFailure(reason: Int) {
                                    //failure logic
                                }
                            })
                        }
                    }
                }
            }
        }
    }
}