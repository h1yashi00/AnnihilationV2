package net.recraft.annihilatoin.objects.kit

import net.recraft.annihilatoin.listener.Soulbound
import net.recraft.annihilatoin.objects.builder.KitClassIconCreator
import net.recraft.annihilatoin.objects.builder.PlayerInventoryImpl
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory
import org.bukkit.inventory.meta.LeatherArmorMeta

abstract class
KitBase(_type: KitType,
        _icon: Material,
        description: List<String>
)
{
    init {
        check(description.isNotEmpty() || description.size < 4)
    }
    open val icon    = KitClassIconCreator(_icon)
        .title("${ChatColor.AQUA}${_type.name}")
        .lore(description)
        .build()
    open val sword   = ItemStack(Material.WOOD_SWORD)
    open val pickaxe = ItemStack(Material.WOOD_PICKAXE)
    open val axe     = ItemStack(Material.WOOD_AXE)
    open val helmet  = ItemStack(Material.LEATHER_HELMET)
    open val chestPlate = ItemStack(Material.LEATHER_CHESTPLATE)
    open val leggings   = ItemStack(Material.LEATHER_LEGGINGS)
    open val boots      = ItemStack(Material.LEATHER_BOOTS)
    fun allItems(): List<ItemStack> {
        val playerInventory = PlayerInventoryImpl()
        equip(playerInventory, Color.WHITE)
        return playerInventory.getItems()
    }
    fun equip(playerInventory: PlayerInventory, color: Color) {
        setEquipments(playerInventory, color)
        setItems(playerInventory)
        playerInventory.armorContents.forEach {
            Soulbound.set(it)
        }
        playerInventory.forEach {
            Soulbound.set(it ?: return@forEach)
        }
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