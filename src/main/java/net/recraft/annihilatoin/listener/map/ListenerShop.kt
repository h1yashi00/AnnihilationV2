package net.recraft.annihilatoin.listener.map

import net.recraft.annihilatoin.objects.Game
import net.recraft.annihilatoin.objects.GameTeam
import net.recraft.annihilatoin.objects.builder.Priceable
import net.recraft.annihilatoin.objects.menu.ShopBrewingMenu
import net.recraft.annihilatoin.objects.menu.ShopWeaponMenu
import net.recraft.annihilatoin.util.Util
import org.bukkit.Color
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import java.util.*

class ListenerShop(private val shopBrewingMenu: ShopBrewingMenu, private val shopWeaponMenu: ShopWeaponMenu): Listener {
    @EventHandler
    fun onSignBreak(event: BlockBreakEvent) {
        val location = event.block.location
        if (GameTeam.isShopBrewing(location) || GameTeam.isShopBrewing(location))
            event.isCancelled = true
    }

    @EventHandler
    fun onSignClick(event: PlayerInteractEvent) {
        val type = event.clickedBlock?.type ?: return
        val location = if (type == Material.SIGN || type == Material.WALL_SIGN) { event.clickedBlock.location } else { return }
        if (GameTeam.isShopBrewing(location)) {
            event.player.openInventory(shopBrewingMenu.createInventory())
        }
        if (GameTeam.isShopWeapon(location)) {
            event.player.openInventory(shopWeaponMenu.createInventory())
        }
    }
    @EventHandler
    fun onBuyShopItem(event: InventoryClickEvent) {
        val inventory = event.inventory
        val shopItem = if (inventory.title == shopBrewingMenu.title || inventory.title == shopWeaponMenu.title) { event.currentItem ?: return } else { return }
        if (Util.isAir(shopItem)) return
        event.isCancelled = true
        val price = Priceable.getPrice(shopItem)
        val player = event.whoClicked
        val playerInventory = player.inventory
        if (player.gameMode == GameMode.CREATIVE) playerInventory.addItem(shopItem)
        if (playerInventory.contains(Material.GOLD_INGOT, price)) { playerInventory.removeItem(ItemStack(Material.GOLD_INGOT, price)) } else { return }
        val addItem = if (shopItem.type == Material.WOOL) {colorWool(shopItem, player.uniqueId)} else {shopItem}
        playerInventory.addItem(addItem)
    }
    private fun colorWool(whiteWool: ItemStack, uuid: UUID): ItemStack {
        val color = Game.getPlayerData(uuid).team?.color ?: Color.WHITE
        val amount = whiteWool.amount
        return when(color) {
            Color.RED    -> ItemStack(Material.WOOL, amount, 14)
            Color.BLUE   -> ItemStack(Material.WOOL, amount, 11)
            Color.YELLOW -> ItemStack(Material.WOOL, amount, 4)
            Color.GREEN  -> ItemStack(Material.WOOL, amount, 13)
            else -> whiteWool
        }
    }
}