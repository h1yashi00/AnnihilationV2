package net.recraft.annihilatoin.objects

import com.comphenix.protocol.ProtocolLibrary
import com.sk89q.worldedit.bukkit.WorldEditPlugin
import net.recraft.annihilatoin.database.AnnihilationStatsColumn
import net.recraft.annihilatoin.database.Database
import net.recraft.annihilatoin.objects.kit.KitType
import net.recraft.annihilatoin.objects.map.MapObject
import net.recraft.annihilatoin.scoreboard.ScoreboardAnni
import net.recraft.annihilatoin.util.GameGenerator
import net.recraft.annihilatoin.util.Util
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


// playerのデータはここで管理している ex..kit, team
object Game {
    fun Player.setTeam(team: GameTeam) {
        getPlayerData(uniqueId).team = team
    }
    fun Player.team(): GameTeam? {
        return getPlayerData(uniqueId).team
    }

    fun Player.setKitType(type: KitType) {
        getPlayerData(uniqueId).kitType = type
    }

    fun Player.kitType(): KitType {
        return getPlayerData(uniqueId).kitType
    }

    fun Player.setInvisible(boolean: Boolean) {
        getPlayerData(uniqueId).invisible = boolean
    }

    fun Player.invisible(): Boolean {
        return getPlayerData(uniqueId).invisible
    }

    fun Player.voidCancel(): Boolean {
        return getPlayerData(uniqueId).voidCancel
    }

    fun Player.setVoidCancel(boolean: Boolean) {
        getPlayerData(uniqueId).voidCancel = boolean
    }

    fun getWorldEdit(): WorldEditPlugin? {
        val p = plugin.server.pluginManager.getPlugin("WorldEdit")
        if (p is WorldEditPlugin) return p as WorldEditPlugin
        return null
    }

    lateinit var plugin: JavaPlugin
    private lateinit var _map: World
    lateinit var mapObject: MapObject
    val protocolManager = ProtocolLibrary.getProtocolManager()!!
    val lobby: World = Util.makeWorld("world_lobby").apply {setSpawnLocation(0,2,0)}
    private val playerDatas: MutableMap<UUID, PlayerData> = HashMap()

    private fun getPlayerData(uuid: UUID): PlayerData {
        if (!playerDatas.containsKey(uuid)) {
            playerDatas[uuid] = PlayerData(uuid)
        }
        return playerDatas[uuid]!!
    }
    private fun getTeamTotal(): List<Pair<GameTeam, Int>> {
        var redCount    = 0
        var blueCount   = 0
        var yellowCount = 0
        var greenCount  = 0
        Bukkit.getOnlinePlayers().forEach {
            when(it.team()) {
                GameTeam.RED    -> redCount +=1
                GameTeam.BLUE   -> blueCount +=1
                GameTeam.YELLOW -> yellowCount +=1
                GameTeam.GREEN  -> greenCount +=1
            }
        }
        return listOf(
            Pair(GameTeam.RED, redCount),
            Pair(GameTeam.BLUE, blueCount),
            Pair(GameTeam.YELLOW, yellowCount),
            Pair(GameTeam.GREEN, greenCount)
        )
    }
    val map: World get() = _map
    val phase : PhaseController = PhaseController()
    val isStart get() = phase.time != 0
    fun getNonPlayers(team: GameTeam): ArrayList<Player> {
        val playersKey = playerDatas.keys.filter { getPlayerData(it).team != team && getPlayerData(it).team != null}
        val nonTeamPlayers = ArrayList<Player>()
        playersKey.forEach {
            nonTeamPlayers.add(Bukkit.getPlayer(it) ?: return@forEach)
        }
        return nonTeamPlayers
    }

    fun getTeamPlayers(team: GameTeam): ArrayList<UUID> {
        val uuids = arrayListOf<UUID>()
        playerDatas.forEach {
            if (it.value.team == team) {
                uuids.add(it.key)
            }
        }
        return uuids
    }

    fun start() {
        phase.pass()
        GameTeam.values().forEach { it.objects.place() }
        Bukkit.getOnlinePlayers().forEach {
            it.setTeam(randomTeamJoin())
            ScoreboardAnni.display(it)
            it.health = 0.0
        }
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, object: BukkitRunnable() {
            override fun run() {
                phase.pass()
            }
        }, 0, 1)
    }
    private fun randomTeamJoin(): GameTeam {
        return getLowestTeam()
    }
    private fun getLowestTeam(): GameTeam {
        var lowestTeamCount = 9999
        var lowestTeam: GameTeam = GameTeam.RED // 仮
        getTeamTotal().forEach {
            val team = it.first
            val count = it.second
            if ( count > lowestTeamCount) {return@forEach}
            lowestTeamCount = count
            lowestTeam = team
        }
        return lowestTeam
    }
    fun end() {
        object: BukkitRunnable() {
            override fun run() {
                playerDatas.keys.forEach {
                    Database.incCount(AnnihilationStatsColumn.TIMES_PLAYED, it)
                }
            }
        }.runTaskAsynchronously(Game.plugin)
    }
    // Gameを初期化する｡これを呼び出すクラスは
    fun init(generator: GameGenerator) {
        _map = generator.getMap()
        GameTeam.RED.objects    = generator.getRed()
        GameTeam.BLUE.objects   = generator.getBlue()
        GameTeam.YELLOW.objects = generator.getYellow()
        GameTeam.GREEN.objects  = generator.getGreen()
        mapObject = generator.getMapObject()
    }
}