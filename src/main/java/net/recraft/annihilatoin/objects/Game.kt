package net.recraft.annihilatoin.objects

import net.recraft.annihilatoin.objects.kit.KitType
import net.recraft.annihilatoin.objects.map.Nexus
import net.recraft.annihilatoin.scoreboard.ScoreboardNexusStatus
import net.recraft.annihilatoin.util.GameGenerator
import net.recraft.annihilatoin.util.Util
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*
import kotlin.collections.ArrayList

object Game : KoinComponent {
    val plugin: JavaPlugin by inject()
    val lobby: World = Util.makeWorld("world_lobby").apply {setSpawnLocation(0,2,0)}
    private lateinit var _map: World
    private lateinit var RED: GameTeam
    private lateinit var BLUE: GameTeam
    private lateinit var YELLOW: GameTeam
    private lateinit var GREEN :GameTeam
    private val PLAYERS_KIT_TYPE: MutableMap<UUID, KitType> = HashMap()
    val scoreboard  = Bukkit.getScoreboardManager().newScoreboard
    val map: World get() = _map
    val teams: MutableList<GameTeam>
        get() = listOf(RED, BLUE, GREEN, YELLOW).toMutableList()
    val phase : PhaseController = PhaseController()
    val isStart get() = phase.time != 0

    fun start() {
        val scoreboardAnni = ScoreboardNexusStatus()
        scoreboardAnni.register()
        teams.forEach { it.place() }
        Bukkit.getOnlinePlayers().forEach {
            teleportGameStartLocation(it.player)
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
        RED = generator.getRed()
        BLUE = generator.getBlue()
        YELLOW = generator.getYellow()
        GREEN = generator.getGreen()
    }

    private fun teleportGameStartLocation(player: Player) {
        val team = getTeam(player.uniqueId)
        if (team == null) {
            player.teleport(lobby.spawnLocation)
            println(lobby.spawnLocation)
            return
        }
        player.teleport(team.randomSpawn)
    }

    fun getTeam(teamName: String): GameTeam? {
        for (team in teams) {
            if (team.name == teamName) return team
        }
        return null
    }
    fun getTeam(nexus: Nexus) :GameTeam? {
        for (team in teams) {
            if (team.nexus == nexus) return team
        }
        return null
    }
    fun getTeam(uuid: UUID): GameTeam? {
        for (team in teams) {
            if (uuid in team) return team
        }
        return null
    }
    fun getNexus(block: Block): Nexus? {
        val location : Location = block.location
        for (team in teams) {
            if (team.nexus.location == location) return team.nexus
        }
        return null
    }

    fun getKit(uuid: UUID): KitType {
        val kit = PLAYERS_KIT_TYPE[uuid]
        if (kit == null) {
            PLAYERS_KIT_TYPE[uuid] = KitType.CIVILIAN
        }
        return kit ?: KitType.CIVILIAN
    }

    fun setKit(uuid: UUID, kitType: KitType) {
        PLAYERS_KIT_TYPE[uuid] = kitType
    }
    fun isEnderChest(location: Location):Boolean {
        for (team in teams) {
            if (team.enderChest.location == location) return true
        }
        return false
    }
    fun isWeaponShop(location: Location):Boolean {
        for (team in teams) {
            if (team.shopWeapon.location == location) return true
        }
        return false
    }
    fun isBrewing(location: Location):Boolean {
        for (team in teams) {
            if (team.shopBrewing.location == location) return true
        }
        return false
    }
    fun isFinishStatus():Boolean {
        var i = 0
        for (team in teams.iterator()) {
            if (team.nexus.isAlive()) {
                i+=1
            }
        }
        if (i == 1) return true
        return false
    }
}