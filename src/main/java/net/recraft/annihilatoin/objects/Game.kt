package net.recraft.annihilatoin.objects

import com.comphenix.protocol.ProtocolLibrary
import net.recraft.annihilatoin.objects.kit.KitGenerator
import net.recraft.annihilatoin.scoreboard.ScoreboardNexusStatus
import net.recraft.annihilatoin.util.GameGenerator
import net.recraft.annihilatoin.util.Util
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*
import kotlin.collections.HashMap


// playerのデータはここで管理している ex..kit, team
object Game : KoinComponent {
    val plugin: JavaPlugin by inject()
    val protocolManager = ProtocolLibrary.getProtocolManager()!!
    val lobby: World = Util.makeWorld("world_lobby").apply {setSpawnLocation(0,2,0)}
    private lateinit var _map: World
    private val playerDatas: MutableMap<UUID, PlayerData> = HashMap()
    fun getPlayerData(uuid: UUID): PlayerData {
        if (!playerDatas.containsKey(uuid))  {
            playerDatas[uuid] = PlayerData()
        }
        return playerDatas[uuid]!!
    }
    val scoreboard  = Bukkit.getScoreboardManager().newScoreboard!!
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

    fun start() {
        ScoreboardNexusStatus().apply {
            register()
        }
        GameTeam.values().forEach { it.objects.place() }
        Bukkit.getOnlinePlayers().forEach {
            val pd = getPlayerData(it.uniqueId)
            val team = pd.team
            if (team == null) {
                it.teleport(lobby.spawnLocation)
                return@forEach
            }
            KitGenerator.get(pd.kitType).equip(it.inventory, team.color)
            it.teleport(team.objects.randomSpawn)
        }
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, object: BukkitRunnable() {
            override fun run() {
                phase.pass()
            }
        }, 0, 20)
    }
    fun end() {
    }
    // Gameを初期化する｡これを呼び出すクラスは
    fun init(generator: GameGenerator) {
        _map = generator.getMap()
        GameTeam.RED.objects    = generator.getRed()
        GameTeam.BLUE.objects   = generator.getBlue()
        GameTeam.YELLOW.objects = generator.getYellow()
        GameTeam.GREEN.objects  = generator.getGreen()
    }
}