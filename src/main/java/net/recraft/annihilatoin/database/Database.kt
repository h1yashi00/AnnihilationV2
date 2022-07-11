package net.recraft.annihilatoin.database

import net.recraft.annihilatoin.objects.PlayerData
import net.recraft.annihilatoin.objects.TotalStats
import org.bukkit.Bukkit
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLDataException
import java.util.*
import kotlin.system.exitProcess

object Database {
    lateinit var con: Connection
    fun connect(url: String, user: String, password: String) {
        con = DriverManager.getConnection(url, user, password)
    }

    // プレイヤーが接続する以前にBungeeCordのプラグインからplayeruuidが登録されている必要がある
    // プレイヤーが接続する以前にBungeecordのプラグインからannihilaton_statsに登録されている必要がある｡
    fun getPlayerStatus(rawUUID: UUID): PlayerData {
        if (!this::con.isInitialized) {
            Bukkit.getLogger().severe("connection is not initialized!!!")
            exitProcess(1)
        }
        val uuid = StringBuilder()
        rawUUID.toString().split("-").forEach { uuid.append(it) }
        val myStat = con.prepareStatement("select * from annihilaton_stats where player_uuid = ?")
        myStat.setString(1, uuid.toString())
        val result = myStat.executeQuery()
        while (result.next()) {
            val totalStats      = TotalStats(
                coins = result.getInt("annihilaton_coin"),
                timesPlayed = result.getInt("times_played"),
                wins = result.getInt("wins"),
                loses = result.getInt("loses"),
                bossKills = result.getInt("boss_kills"),
                meleeKills = result.getInt("melee_kills"),
                kills = result.getInt("kills"),
                deaths = result.getInt("deaths"),
                nexusDamaged = result.getInt("nexus_damaged"),
                blocksBroken = result.getInt("blocks_broken"),
                oresMined = result.getInt("ores_mined"),
                gapplesConsumed = result.getInt("gapples_consumed"),
                goldUsed = result.getInt("golds_used"),
                expsGained = result.getInt("exps_gained")
            )
            return PlayerData(rawUUID, totalStats)
        }
        throw SQLDataException("cannot find $uuid data in playerdata")
    }

    fun close() {
        con.close()
    }
}