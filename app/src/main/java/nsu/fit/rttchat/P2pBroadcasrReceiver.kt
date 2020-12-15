package nsu.fit.rttchat

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.wifi.p2p.WifiP2pDeviceList
import android.net.wifi.p2p.WifiP2pManager
import android.util.Log

class P2pBroadcasrReceiver : BroadcastReceiver {
    var manager : WifiP2pManager
    var channel : WifiP2pManager.Channel
    var activity : MainActivity

    constructor(manager: WifiP2pManager, channel : WifiP2pManager.Channel, activity: MainActivity) {
        this.manager = manager
        this.channel = channel
        this.activity = activity
    }

    override fun onReceive(context: Context?, intent: Intent?) {
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
                manager?.requestPeers(channel) { peers: WifiP2pDeviceList? ->
                    // Handle peers list
                }
            }
        }
    }
}