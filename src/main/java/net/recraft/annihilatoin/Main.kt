package net.recraft.annihilatoin

import net.minecraft.server.v1_8_R3.EnumDirection
import net.recraft.annihilatoin.command.*
import net.recraft.annihilatoin.config.ConfigManager
import net.recraft.annihilatoin.config.ConfigMap
import net.recraft.annihilatoin.listener.*
import net.recraft.annihilatoin.listener.kit.*
import net.recraft.annihilatoin.listener.map.*
import net.recraft.annihilatoin.listener.menu.InventoryIntercept
import net.recraft.annihilatoin.listener.special_item.ListenerLanchPad
import net.recraft.annihilatoin.listener.special_item.ListenerTransPortItem
import net.recraft.annihilatoin.listener.troll.PlayerOpeningWorkingBench
import net.recraft.annihilatoin.objects.Game
import net.recraft.annihilatoin.objects.GameTeam
import net.recraft.annihilatoin.objects.PlayerLeaveUnfairAdvantage
import net.recraft.annihilatoin.objects.VoteManager
import net.recraft.annihilatoin.objects.kit.KitType
import net.recraft.annihilatoin.objects.menu.KitMenu
import net.recraft.annihilatoin.objects.menu.ShopBrewingMenu
import net.recraft.annihilatoin.objects.menu.ShopWeaponMenu
import net.recraft.annihilatoin.scoreboard.ScoreboardVote
import net.recraft.annihilatoin.scoreboard.scoreboard_team.ScoreboardTeamManager
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import org.koin.core.context.startKoin
import org.koin.dsl.module
import java.io.File


// external functions
fun Player.realTeleport(loc: Location){
    val cloneLoc = loc.clone()
    teleport(cloneLoc.apply{x+=0.5; z+=0.5})
}
// hideは非同期でやると動作しないので注意しよう｡
fun Player.hide() {
    Bukkit.getOnlinePlayers().forEach {
        if (it == null) return@forEach
        if (it.uniqueId == uniqueId) return@forEach
        it.hidePlayer(this)
    }
}
// showは非同期でやる動作しない
fun Player.show() {
    Bukkit.getOnlinePlayers().forEach {
        if (it == null) return@forEach
        if (it.uniqueId == player.uniqueId) return@forEach
        it.showPlayer(player)
    }
}
fun Player.direction(): EnumDirection {
    return (player as CraftPlayer).handle.direction
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

//    private val scoreboardTeamManager = ScoreboardTeamManager()
    private var tp: ListenerTransPortItem? = null
    override fun onEnable() {
        setupKoin()
        tp = ListenerTransPortItem()
        ScoreboardTeamManager.enable()
        val configMap = ConfigMap(dataFolder)
        val playerLeaveUnfairAdvantage  = PlayerLeaveUnfairAdvantage()
        ArrayList<Listener>().apply {
            // unfair cancel events
            // player game quit
            add( PlayerJoinServer    (playerLeaveUnfairAdvantage) )
            add( PlayerLeaveServer   (playerLeaveUnfairAdvantage) )
            add( ListenerUnfairZombie(playerLeaveUnfairAdvantage) )
            // playerBreakWorkingBench
            add( PlayerOpeningWorkingBench() )
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
            add( SuitableTool() )
            add( PlayerAttackEnemyTeam()  )
            add( PlayerRespawn()          )
            add( PlayerInvisible())
            add( NetherGate(KitMenu()) )
            add( PlayerOpenEnchantTable() )
            add( ListenerGapple() )
            add( ListenerFlaggedPlayerVoid() )
            add( PlayerDeath() )
            // kits
            add( ListenerMiner())
            add( ListenerLumberjack() )
            add( KitScout() )
            add( SoulBound() )
            add( ListenerAcrobat() )
            add( ListenerSwapper() )
            add( ListenerScorpio() )
            add( ListenerDasher()  )
            add( ListenerSpy()     )
            add( ListenerDefender())

            // special item
            add (tp!!)
            add (ListenerLanchPad())

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
        getCommand("team").executor = CommandJoinTeam()
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
            var currentWaitTime = VoteManager.waitTime
            // いい秒毎実行される
            override fun run() {
                currentWaitTime -= 1
                ScoreboardVote.timeUpdate(currentWaitTime)
                // currentWaitTime が 0になるまで実行されない
                if (currentWaitTime >= 0) return
                val mapName = voteManager.result()
                val generator = configMap.getTeamGenerator(mapName)
                Game.init(generator)
                Game.start()
                cancel()
            }
        }
        val waitPlayerJoin = object: BukkitRunnable() {
            override fun run() {
                if (Bukkit.getOnlinePlayers().isNotEmpty()) {
                    cancel()
                    delayVoting.runTaskTimer(this@Main, 20, 20)
                }
            }

        }
        waitPlayerJoin.runTaskTimerAsynchronously(this,0,1)
    }

    override fun onDisable() {
        ScoreboardTeamManager.disable()
        tp!!.disable()
    }
}