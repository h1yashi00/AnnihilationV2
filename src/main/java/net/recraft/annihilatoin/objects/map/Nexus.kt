package net.recraft.annihilatoin.objects.map

import net.recraft.annihilatoin.database.AnnihilationStatsColumn
import net.recraft.annihilatoin.database.Database
import net.recraft.annihilatoin.objects.Game
import net.recraft.annihilatoin.objects.Game.team
import net.recraft.annihilatoin.objects.GameTeam
import net.recraft.annihilatoin.scoreboard.ScoreboardAnni
import net.recraft.annihilatoin.util.Util
import org.bukkit.*
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import java.util.*


class Nexus (location: Location) : Placeable(location, Material.ENDER_STONE) {
    init {
        location.clone().apply{y-=1}.block.type = Material.BARRIER
    }
    private var _hp = 75
    val hp get() = _hp
    fun damage(player: Player) {
        if (!isAlive()) return
        val damage = if (Game.phase.currentPhase == 5) { if (hp == 1) {1} else {2} } else {1}
        _hp -= damage
        val team = GameTeam.getTeam(this)!!
        val playerTeam = player.team() ?: return
        ScoreboardAnni.breakNexusUpdate(team)
        val msg = "${Util.getColoredPlayersName(player, playerTeam)} is now attacking ${Util.getColoredTeamName(team)} Nexus!!! $hp"
        Bukkit.broadcastMessage(msg)
        attackedSound()
        attackedEffect()
        victimSound()
        object: BukkitRunnable() {
            override fun run() {
                Bukkit.getOnlinePlayers().forEach {
                    if (it.team() == player.team()) {
                        Database.addCount(AnnihilationStatsColumn.NEXUS_DAMAGED, player.uniqueId, damage)
                        Database.addCount(AnnihilationStatsColumn.COIN, player.uniqueId, 3 * damage)
                        player.sendMessage("${ChatColor.GOLD}+${3 * damage} Annihilation Coin")
                    }
                }
            }
        }.runTaskAsynchronously(Game.plugin)
    }
    fun isAlive(): Boolean {
        if (_hp <= 0) return false
        return true
    }
    fun destroyedEvent() {
        location.block.type = Material.BEDROCK
        val team = GameTeam.getTeam(this)!!
        Bukkit.broadcastMessage("${Util.getColoredTeamName(team)}'s Nexus was destroyed!!!")
        var n = 0
        val runnable = object : BukkitRunnable() {
            override fun run() {
                destroyedSound()
                destroyedEffect()
            }
        }
        for (i in 1..5) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(Game.plugin, runnable, i.toLong() + n) //20 Tick (1 Second) delay before run() is called
            n += 3
        }
    }
    private fun attackedEffect() {
        val world = location.world
        for (i in 0..20) {
            world.playEffect(location, Effect.FIREWORKS_SPARK, 10, 12)
        }
        world.playEffect(location, Effect.STEP_SOUND, Material.OBSIDIAN)
    }

    private fun attackedSound() {
        val world = location.world
        val r = Random()
        val pitch = 0.5F + r.nextFloat() * 0.5F
        world.playSound(location, Sound.ANVIL_LAND, 1.0F, pitch)
    }
    private fun victimSound() {
        Bukkit.getOnlinePlayers().forEach {
            val victimTeam = GameTeam.getTeam(this)
            val playerTeam = it.team()
            if (victimTeam != playerTeam) return
            it?.player?.playSound(it.location, Sound.NOTE_PLING, 9.0F, 11.0F);
        }
    }
    private fun destroyedSound() {
        val world = location.world
        val r = Random()
        val pitch = 0.5f + r.nextFloat() * 0.5f
        world.playSound(location, Sound.EXPLODE, 1.0f, pitch)
    }

    private fun destroyedEffect() {
        val world = location.world
        world.playEffect(location, Effect.EXPLOSION_HUGE, 1)
    }
}