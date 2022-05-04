package net.recraft.annihilatoin

import net.recraft.annihilatoin.command.CommandAnniConfig
import net.recraft.annihilatoin.command.CommandJoinTeam
import net.recraft.annihilatoin.command.CommandTeleportSpecificLocation
import net.recraft.annihilatoin.command.CommandVote
import net.recraft.annihilatoin.config.ConfigManager
import net.recraft.annihilatoin.config.ConfigMap
import net.recraft.annihilatoin.listener.*
import net.recraft.annihilatoin.listener.menu.InventoryIntercept
import net.recraft.annihilatoin.objects.*
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

    override fun onEnable() {
        setupKoin()
        val configMap = ConfigMap(dataFolder)
        ArrayList<Listener>().apply {
            add(PlayerAttackEnemyTeam())
            add(PlayerAttackNexus())
            add(PlayerBreakResourceBlock())
            add(ListenerEnderChest())
            add(ListenerEnderFurnace())
            add(InventoryIntercept(configMap))
            forEach {
                server.pluginManager.registerEvents(it, this@Main)
            }
        }
        val configManager = ConfigManager().apply {
            loadConfigFile("kits.yaml")
            loadConfigFile("shops.yaml")
        }
        // VoteManagerにVoteする際に投票させるためのワールド名を追加する
        val worldNames : List<String> = ArrayList<String>().apply {
            add("test")
            add("canyon")
        }
        /* ↑↑↑↑↑↑↑  初期化するために必要なもの   ↑↑↑↑↑↑↑ */
        val debug = true
        val voteManager = VoteManager(worldNames)
        getCommand("vote").executor = CommandVote(voteManager)
        getCommand("teleport").executor = CommandTeleportSpecificLocation()
        getCommand("team").executor = CommandJoinTeam()
        getCommand("anniconfig").executor = CommandAnniConfig()
        if (debug) {
            val mapName = "world_test"
            val generator = configMap.getTeamGenerator(mapName)
            Game.init(generator)
            Game.start()
            return
        }
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, object : BukkitRunnable() {
            override fun run() {
                if (Bukkit.getOnlinePlayers().isNotEmpty()) {
                    cancel()
                    // プレイヤーがやってくるまで止まっている 一人でも入れば↓が動き出し､2分後にGameが始まる
                    Bukkit.getScheduler().scheduleSyncDelayedTask(this@Main, object : BukkitRunnable() {
                        override fun run() {
                            val mapName = voteManager.result()
                            val generator = configMap.getTeamGenerator(mapName)
                            Game.init(generator)
                            Game.start()
                        }
                    }, 20 * 60 * 2)
                }
            }
        }, 1, 1)
    }
}