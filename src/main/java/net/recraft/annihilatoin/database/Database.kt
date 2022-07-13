package net.recraft.annihilatoin.database

import net.recraft.annihilatoin.objects.PlayerStats
import org.bukkit.Bukkit
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLDataException
import java.util.*
import kotlin.system.exitProcess
import net.recraft.annihilatoin.database.AnnihilationStatsColumn.*

object Database {
    lateinit var con: Connection
    fun connect(url: String, user: String, password: String) {
        con = DriverManager.getConnection(url, user, password)
    }

    // プレイヤーが接続する以前にBungeeCordのプラグインからplayeruuidが登録されている必要がある
    // プレイヤーが接続する以前にBungeecordのプラグインからannihilaton_statsに登録されている必要がある｡
    fun getRank(uuid: UUID): StaffRank? {
        val myStat = con.prepareStatement("select * from ${RecraftTable.PLAYERDATA} where player_uuid = ?")
        myStat.setString(1, uuid.toString())
        val result = myStat.executeQuery()
        while (result.next()) {
            return StaffRank.get(result.getInt("rank_id"))
        }
        throw SQLDataException("cannot find $uuid data in playerdata")
    }

    fun getPlayerStatus(uuid: UUID): PlayerStats {
        if (!this::con.isInitialized) {
            Bukkit.getLogger().severe("connection is not initialized!!!")
            exitProcess(1)
        }
        val myStat = con.prepareStatement("select * from ${RecraftTable.ANNIHILATION_STATS} where player_uuid = ?")
        myStat.setString(1, uuid.toString())
        val result = myStat.executeQuery()
        while (result.next()) {
            return PlayerStats(
                coins =         result.getInt("$COIN"),
                timesPlayed =   result.getInt("$TIMES_PLAYED"),
                wins =          result.getInt("$WINS"),
                loses =         result.getInt("$LOSES"),
                bossKills =     result.getInt("$BOSS_KILLS"),
                meleeKills =    result.getInt("$MELEE_KILLS"),
                kills =         result.getInt("$KILLS"),
                deaths =        result.getInt("$DEATHS"),
                nexusDamaged =  result.getInt("$NEXUS_DAMAGED"),
                blocksBroken =  result.getInt("$BLOCKS_BROKEN"),
                oresMined =     result.getInt("$ORES_MINED"),
                gapplesConsumed = result.getInt("$GAPPLES_CONSUMED"),
                goldUsed =      result.getInt("$GOLDS_USED"),
                expsGained =    result.getInt("$EXPS_GAINED")
            )
        }
        throw SQLDataException("cannot find $uuid data in ${RecraftTable.ANNIHILATION_STATS}")
    }

    fun incCount(column: AnnihilationStatsColumn, uuid: UUID) {
        val sql = "update ${RecraftTable.ANNIHILATION_STATS} set $column = $column + 1 where ${RecraftTable.ANNIHILATION_STATS}.player_uuid = ?"
        val myStat = con.prepareStatement(sql)
        myStat.setString(1 ,uuid.toString())
        myStat.executeUpdate()
    }
    fun addCount(column: AnnihilationStatsColumn, uuid: UUID, addCount: Int) {
        val sql = "update ${RecraftTable.ANNIHILATION_STATS} set $column = $column + $addCount where ${RecraftTable.ANNIHILATION_STATS}.player_uuid = ?"
        val myStat = con.prepareStatement(sql)
        myStat.setString(1 ,uuid.toString())
        myStat.executeUpdate()
    }

    fun close() {
        con.close()
    }
}