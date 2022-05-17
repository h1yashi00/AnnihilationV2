package net.recraft.annihilatoin

import net.recraft.annihilatoin.command.*
import net.recraft.annihilatoin.config.ConfigManager
import net.recraft.annihilatoin.config.ConfigMap
import net.recraft.annihilatoin.listener.*
import net.recraft.annihilatoin.listener.menu.InventoryIntercept
import net.recraft.annihilatoin.listener.PlayerLeaveServer
import net.recraft.annihilatoin.listener.kit.KitScout
import net.recraft.annihilatoin.listener.map.*
import net.recraft.annihilatoin.listener.PlayerInvisible
import net.recraft.annihilatoin.objects.*
import net.recraft.annihilatoin.objects.menu.ShopBrewingMenu
import net.recraft.annihilatoin.objects.menu.ShopWeaponMenu
import net.recraft.annihilatoin.scoreboard.ScoreboardVote
import net.recraft.annihilatoin.scoreboard.scoreboard_team.ScoreboardTeamManager
import org.bukkit.Bukkit
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import org.koin.core.context.startKoin
import org.koin.dsl.module
import java.io.File

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
    override fun onEnable() {
        setupKoin()
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
            // kits
            add( KitScout() )
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

        Bukkit.getOnlinePlayers().forEach {it.scoreboard = Game.scoreboard }
        /* ↑↑↑↑↑↑↑  初期化するために必要なもの   ↑↑↑↑↑↑↑ */
        val debug = true
        // vote初期化
        val voteManager = VoteManager(worldNames)
        val scoreboardVote = ScoreboardVote(voteManager)
        getCommand("vote").executor = CommandVote(voteManager)
        getCommand("teleport").executor = CommandTeleportSpecificLocation()
        getCommand("team").executor = CommandJoinTeam(scoreboardTeamManager)
        getCommand("anniconfig").executor = CommandAnniConfig()
        getCommand("kit").executor = CommandKit()
        scoreboardVote.register()
        if (debug) {
            scoreboardVote.clear()
            val mapName = "world_test"
            val generator = configMap.getTeamGenerator(mapName)
            Game.init(generator)
            Game.start()
            return
        }

        val delayVoting = object: BukkitRunnable() {
            override fun run() {
                scoreboardVote.clear()
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
    }
}