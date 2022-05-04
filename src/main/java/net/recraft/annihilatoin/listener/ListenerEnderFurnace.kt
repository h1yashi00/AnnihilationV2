package net.recraft.annihilatoin.listener

import net.minecraft.server.v1_8_R3.EntityHuman
import net.minecraft.server.v1_8_R3.TileEntityFurnace
import net.recraft.annihilatoin.Main
import net.recraft.annihilatoin.objects.map.EnderFurnace
import org.bukkit.Bukkit
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventoryFurnace
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.InventoryHolder
import org.bukkit.plugin.java.JavaPlugin
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

class ListenerEnderFurnace : Listener, KoinComponent {
    private val furnaces: MutableMap<UUID, VirtualFurnace> = HashMap()
    private val plugin: JavaPlugin by inject()
    init {
        println(plugin.server.name)
    }
    init {
        Bukkit.getScheduler().runTaskTimer(plugin, {
            for (f in furnaces.values) {
                try {
                    f.c()
                } catch (e: Exception) {
                }
            }
        }, 0L, 1L)
    }
    @EventHandler
    fun playerOpenEnderFurnace(event: PlayerInteractEvent) {
        if (event.action != Action.RIGHT_CLICK_BLOCK) return
        if (!EnderFurnace.isIn(event.clickedBlock.location)) return
        event.isCancelled = true
        val player = event.player
        val uuid   = event.player.uniqueId
        val handler: EntityHuman = (player as CraftPlayer).handle
        if (!furnaces.containsKey(uuid)) { furnaces[uuid] = VirtualFurnace(handler)}
        val virtualFurnace = furnaces[uuid]
        val inv = CraftInventoryFurnace(virtualFurnace)
        player.openInventory(inv)
    }

    @EventHandler
    fun onPlayerBreakEnderFurnace(event: BlockBreakEvent) {
        if (!EnderFurnace.isIn(event.block.location))  return
        event.isCancelled = true
    }
    private inner class VirtualFurnace(entity: EntityHuman) : TileEntityFurnace() {
        init {
            world = entity.world
            super.a("Ender Furnace")
        }

        override fun getOwner(): InventoryHolder {
            return InventoryHolder {
                val craftInventoryFurnace = CraftInventoryFurnace(VirtualFurnace@this)
                craftInventoryFurnace
            }
        }
    }
}