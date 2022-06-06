package net.recraft.annihilatoin.listener.map

import net.minecraft.server.v1_8_R3.EntityHuman
import net.minecraft.server.v1_8_R3.TileEntityFurnace
import net.recraft.annihilatoin.objects.Game
import net.recraft.annihilatoin.objects.GameTeam
import net.recraft.annihilatoin.objects.builder.ItemStackBuilder
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventoryFurnace
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

class ListenerEnderFurnace : Listener, KoinComponent {
    private val plugin: JavaPlugin by inject()
    private val menu = Menu()
    init {
        Bukkit.getScheduler().runTaskTimer(plugin, {
            menu.furnaces.values.forEach { arrayOfVirtualFurnaces ->
                arrayOfVirtualFurnaces.forEach {
                    try {
                        it.c()
                    } catch (e: Exception) {
                    }
                }
            }
        }, 0L, 1L)
    }
    @EventHandler
    fun playerOpenEnderFurnace(event: PlayerInteractEvent) {
        if (event.action != Action.RIGHT_CLICK_BLOCK) return
        if (!GameTeam.isEnderFurnace(event.clickedBlock.location)) return
        event.isCancelled = true
        val player = event.player
        menu.openMenu(player)
        // エンダー釜戸メニュ
    }
    @EventHandler
    fun onInventoryIntercept(event: InventoryClickEvent) {
        if (event.inventory.name != menu.title) return
        val item = event.currentItem
        val player = event.whoClicked as Player
        if (!menu.furnaceItems.contains(item)) return
        event.isCancelled = true
        menu.furnaceItems.forEach { if (it == item) {menu.openFurnace(player, it)} }
    }
    private class Menu {
        val title = "EnderFurnace Menu"
        val furnaceItems = arrayOf (
            ItemStackBuilder(Material.FURNACE).title("1").build(),
            ItemStackBuilder(Material.FURNACE).title("2").build(),
            ItemStackBuilder(Material.FURNACE).title("3").build(),
            ItemStackBuilder(Material.FURNACE).title("4").build()
        )
        fun openMenu(player: Player) {
            val inv = Bukkit.createInventory(null, 9, title)
            inv.addItem(furnaceItems[0])
            inv.addItem(furnaceItems[1])
            inv.addItem(furnaceItems[2])
            inv.addItem(furnaceItems[3])
            player.openInventory(inv)
        }
        val furnaces: MutableMap<UUID, Array<VirtualFurnace>> = HashMap()
        fun openFurnace(player: Player, item: ItemStack) {
            val handler: EntityHuman = (player as CraftPlayer).handle
            val uuid = player.uniqueId
            if (!furnaces.containsKey(uuid)) {
                furnaces[uuid] = arrayOf(
                    VirtualFurnace(handler),
                    VirtualFurnace(handler),
                    VirtualFurnace(handler),
                    VirtualFurnace(handler),
                )
            }
            val arrayFurnace = furnaces[uuid]!!
            furnaceItems.forEachIndexed { index, itemStack ->
                if (itemStack == item) {
                    val virtualFurnace = arrayFurnace[index]
                    val inv = CraftInventoryFurnace(virtualFurnace)
                    player.openInventory(inv)
                }
            }
        }
    }

    @EventHandler
    fun onPlayerBreakEnderFurnace(event: BlockBreakEvent) {
        if (!GameTeam.isEnderFurnace(event.block.location))  return
        event.isCancelled = true
    }

    private class VirtualFurnace(entity: EntityHuman) : TileEntityFurnace() {
        init {
            world = entity.world
            super.a("Ender Furnace")
        }

        override fun a(itemstack: net.minecraft.server.v1_8_R3.ItemStack?): Int {
             return if (Game.phase.currentPhase == 5) {10} else {200/4}
        }

        override fun getOwner(): InventoryHolder {
            return InventoryHolder {
                CraftInventoryFurnace(VirtualFurnace@this)
            }
        }
    }
}