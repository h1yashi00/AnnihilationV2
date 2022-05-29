package net.recraft.annihilatoin

import net.recraft.annihilatoin.command.*
import net.recraft.annihilatoin.config.ConfigManager
import net.recraft.annihilatoin.config.ConfigMap
import net.recraft.annihilatoin.listener.*
import net.recraft.annihilatoin.listener.menu.InventoryIntercept
import net.recraft.annihilatoin.listener.PlayerLeaveServer
import net.recraft.annihilatoin.listener.map.*
import net.recraft.annihilatoin.listener.PlayerInvisible
import net.recraft.annihilatoin.listener.kit.*
import net.recraft.annihilatoin.listener.special_item.ListenerTransPortItem
import net.recraft.annihilatoin.objects.*
import net.recraft.annihilatoin.objects.menu.KitMenu
import net.recraft.annihilatoin.objects.menu.ShopBrewingMenu
import net.recraft.annihilatoin.objects.menu.ShopWeaponMenu
import net.recraft.annihilatoin.scoreboard.ScoreboardVote
import net.recraft.annihilatoin.scoreboard.scoreboard_team.ScoreboardTeamManager
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import org.koin.core.context.startKoin
import org.koin.dsl.module
import java.io.File

// external functions
fun Player.realTeleport(loc: Location){
    val cloneLoc = loc.clone()
    player.teleport(cloneLoc.apply{x+=0.5; z+=0.5})
}
fun Player.team(): GameTeam? {
    return Game.getPlayerData(player.uniqueId).team
}

fun Player.expLevel(): Int {
    var i = 0
    while(true) {
        when {
            i <= 16 -> {
                Bukkit.broadcastMessage("${player.totalExperience}")
                if (( i*i+(6*i) ) > player.totalExperience) break
            }
            i <= 31 -> {
                if (( ((i*i*2.5) + (-40.5*i) + 360) ) < player.totalExperience) break
            }
            else -> {
                if (( (i*i*4.5) + (-162.5*i) + 2220) < player.totalExperience) break
            }
        }
        i += 1
    }
    return i
}

class Main : JavaPlugin() {
    private val myModule = module {
        single<JavaPlugin> { this@Main }
        single { server }
        single<File> {dataFolder}
    }

    private fun setupKoin() {
        startKoin {
            modules(listOf(myModule))
        }
    }

    private val scoreboardTeamManager = ScoreboardTeamManager()
    private var tp: ListenerTransPortItem? = null
    override fun onEnable() {
        setupKoin()
        tp = ListenerTransPortItem()
        scoreboardTeamManager.enable()
        val configMap = ConfigMap(dataFolder)
        val playerLeaveUnfairAdvantage  = PlayerLeaveUnfairAdvantage()
        ArrayList<Listener>().apply {
            // player game quit
            add( PlayerJoinServer    (playerLeaveUnfairAdvantage) )
            add( PlayerLeaveServer   (playerLeaveUnfairAdvantage) )
            add( ListenerUnfairZombie(playerLeaveUnfairAdvantage) )
            // maps
            add( PlayerAttackNexus()       )
            add( PlayerBreakResourceBlock())
            add( ListenerEnderChest()      )
            add( ListenerEnderFurnace()    )
            add( PlayerPlaceBlock()        )
            // menu
            add( ListenerShop(ShopBrewingMenu(), ShopWeaponMenu()) )
            add( InventoryIntercept(configMap)                     )
            // national events
            add( PlayerAttackEnemyTeam()  )
            add( PlayerRespawn()          )
            add( PlayerInvisible(scoreboardTeamManager))
            add( NetherGate(KitMenu()) )
            add( PlayerOpenEnchantTable() )
            add( ListenerGapple() )
            add( ListenerFlaggedPlayerVoid() )
            add( PlayerDeath() )
            // kits
            add( KitScout() )
            add( Soulbound() )
            add( ListenerAcrobat() )
            add( ListenerSwapper() )
            add( ListenerScorpio() )
            add( ListenerDasher()  )

            // special item
            add (tp!!)

            forEach {
                server.pluginManager.registerEvents(it, this@Main)
            }
        }
        val configManager = ConfigManager().apply {
            loadConfigFile("kits.yaml")
        }
        // VoteManagerにVoteする際に投票させるためのワールド名を追加する
        val worldNames : List<String> = ArrayList<String>().apply {
            add("test")
            add("canyon")
        }

        val voteManager = VoteManager(worldNames)
        Bukkit.getOnlinePlayers().forEach { ScoreboardVote.display(it ?: return@forEach) }
        /* ↑↑↑↑↑↑↑  初期化するために必要なもの   ↑↑↑↑↑↑↑ */
        val debug = true
        // vote初期化
        getCommand("vote").executor = CommandVote(voteManager)
        getCommand("teleport").executor = CommandTeleportSpecificLocation()
        getCommand("team").executor = CommandJoinTeam(scoreboardTeamManager)
        getCommand("anniconfig").executor = CommandAnniConfig()
        getCommand("kit").executor = CommandKit()
        if (debug) {
            val mapName = "world_test"
            val generator = configMap.getTeamGenerator(mapName)
            Game.init(generator)
            Game.start()
            return
        }

        val delayVoting = object: BukkitRunnable() {
            override fun run() {
                val mapName = voteManager.result()
                val generator = configMap.getTeamGenerator(mapName)
                Game.init(generator)
                Game.start()
            }
        }
        val waitPlayerJoin = object: BukkitRunnable() {
            override fun run() {
                if (Bukkit.getOnlinePlayers().isNotEmpty()) {
                    cancel()
                    // プレイヤーがやってくるまで止まっている 一人でも入れば↓が動き出し､2分後にGameが始まる
                    delayVoting.runTaskLater(this@Main, 20 * 60 * 2)
                }
            }

        }
        waitPlayerJoin.runTaskTimerAsynchronously(this,0,1)
    }

    override fun onDisable() {
        scoreboardTeamManager.disable()
        tp!!.disable()
    }
}