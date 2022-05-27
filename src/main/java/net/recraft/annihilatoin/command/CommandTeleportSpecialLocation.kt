package net.recraft.annihilatoin.command

import net.recraft.annihilatoin.config.ConfigMap
import net.recraft.annihilatoin.objects.GameTeam
import net.recraft.annihilatoin.realTeleport
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
        val team: GameTeam = GameTeam.getTeam(args?.get(0)?.toLowerCase()) ?: return false
        val teleportLocation = getTeleportLocation(team, args?.get(1)?.toLowerCase()) ?: return false
        player.sendMessage(teleportLocation.toString())
        player.realTeleport(teleportLocation)
        return true
    }

    private fun getTeleportLocation(team: GameTeam, location: String?): Location? {
        return when (location) {
            ConfigMap.shopWeapon    -> team.objects.shopWeapon.location
            ConfigMap.shopBrewing   -> team.objects.shopBrewing.location
            ConfigMap.nexus         -> team.objects.nexus.location
            ConfigMap.spawn1        -> team.objects.spawn1.location
            ConfigMap.spawn2        -> team.objects.spawn2.location
            ConfigMap.spawn3        -> team.objects.spawn3.location
            ConfigMap.enderChest    -> team.objects.enderChest.location
            ConfigMap.enderFurnace  -> team.objects.enderFurnace.location
            else -> null
        }
    }
}
