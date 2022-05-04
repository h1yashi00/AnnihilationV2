package net.recraft.annihilatoin.command

import net.recraft.annihilatoin.config.ConfigMap
import net.recraft.annihilatoin.objects.Game
import net.recraft.annihilatoin.objects.GameTeam
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CommandTeleportSpecificLocation : CommandExecutor {
    override fun onCommand(sender: CommandSender?, command: Command?, label: String?, args: Array<out String>?): Boolean {
        val player = if (sender is Player) { sender.player ?: return false } else return false
        if (!player.isOp) return false
        if (args?.size != 2) return false
        val team: GameTeam = Game.getTeam(args?.get(0)?.toLowerCase() ?: return false) ?: return false
        val teleportLocation = getTeleportLocation(team, args?.get(1)?.toLowerCase()) ?: return false
        player.sendMessage(teleportLocation.toString())
        player.teleport(teleportLocation)
        return true
    }

    private fun getTeleportLocation(team: GameTeam, location: String?): Location? {
        return when (location) {
            ConfigMap.shopWeapon    -> team.shopWeapon.location
            ConfigMap.shopArmor     -> team.shopArmor.location
            ConfigMap.nexus         -> team.nexus.location
            ConfigMap.spawn1        -> team.spawn1.location
            ConfigMap.spawn2        -> team.spawn2.location
            ConfigMap.spawn3        -> team.spawn3.location
            ConfigMap.enderChest    -> team.enderChest.location
            ConfigMap.enderFurnace  -> team.enderFurnace.location
            else -> null
        }
    }
}
