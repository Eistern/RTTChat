package nsu.fit.rttchat

import java.io.BufferedReader
import java.io.File
import java.io.FileReader

class GetIpByArp {
    fun getIpByArp(mac : String) {
        val br = BufferedReader(FileReader(File("/proc/net/arp")))

        var line: String
        while (br.readLine().also({ line = it }) != null) {
            print("")
        }

        br.close()
    }
}