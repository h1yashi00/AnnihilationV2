package net.recraft.annihilatoin.objects.kit

import net.recraft.annihilatoin.objects.Game
import net.recraft.annihilatoin.objects.GameTeam
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory
import org.bukkit.inventory.meta.LeatherArmorMeta




abstract class
KitBase(private val _type: KitType,
        private val _icon: Material)
{
    open val icon    = ItemStack(_icon)
    open val sword   = ItemStack(Material.WOOD_SWORD)
    open val pickaxe = ItemStack(Material.WOOD_AXE)
    open val axe     = ItemStack(Material.WOOD_PICKAXE)
    open val helmet  = ItemStack(Material.LEATHER_HELMET)
    open val chestPlate = ItemStack(Material.LEATHER_CHESTPLATE)
    open val leggings   = ItemStack(Material.LEATHER_LEGGINGS)
    open val boots      = ItemStack(Material.LEATHER_BOOTS)
    fun equip(player: Player, color: Color) {
        setEquipments(player.inventory, color)
        setItems(player.inventory)
    }
    open fun setItems(playerInventory: PlayerInventory) {
        playerInventory.setItem(0, sword)
        playerInventory.setItem(1, pickaxe)
        playerInventory.setItem(2, axe)
    }
    private fun setEquipments(playerInventory: PlayerInventory, color: Color) {
        playerInventory.setHelmet    (helmet)
        playerInventory.setChestplate(chestPlate)
        playerInventory.setLeggings  (leggings)
        playerInventory.setBoots     (boots)
        coloringLeatherEquipments(color, playerInventory)
    }
    private fun coloringLeatherEquipments(color: Color, playerInventory: PlayerInventory) {
        val items = playerInventory.armorContents ?: return
        for (item in items) {
            if (item.itemMeta is LeatherArmorMeta) {
                val meta = item.itemMeta as LeatherArmorMeta
                meta.color = color
                item.itemMeta = meta
            }
        }
    }
}