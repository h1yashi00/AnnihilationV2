package net.recraft.annihilatoin.objects

import net.recraft.annihilatoin.objects.kit.Kit
import net.recraft.annihilatoin.objects.map.Nexus
import net.recraft.annihilatoin.scleduler.PhaseController
import net.recraft.annihilatoin.util.GameGenerator
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*
import kotlin.collections.ArrayList

object Game : KoinComponent {
    val plugin: JavaPlugin by inject()
    private lateinit var _lobby: World
    private lateinit var _map: World
    private lateinit var RED: GameTeam
    private lateinit var BLUE: GameTeam
    private lateinit var YELLOW: GameTeam
    private lateinit var GREEN :GameTeam
    private val playersKit: MutableMap<UUID, Kit> = HashMap()
    val candidateMaps: MutableList<VoteManager.Candidate> = ArrayList()
    val scoreboard  = Bukkit.getScoreboardManager().newScoreboard
    val lobby get() = _lobby
    val map: World get() = _map
    val teams: MutableList<GameTeam>
        get() = listOf(RED, BLUE, GREEN, YELLOW).toMutableList()
    val phase : PhaseController = PhaseController()

    fun start() {
        teams.forEach { it.place() }
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
        _lobby = generator.getLobby()
        _map = generator.getMap()
        RED = generator.getRed()
        BLUE = generator.getBlue()
        YELLOW = generator.getYellow()
        GREEN = generator.getGreen()
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

    fun getKit(uuid: UUID): Kit {
        val kit = playersKit[uuid]
        if (kit == null) {
            playersKit[uuid] = Kit.CIVILIAN
        }
        return kit ?: Kit.CIVILIAN
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