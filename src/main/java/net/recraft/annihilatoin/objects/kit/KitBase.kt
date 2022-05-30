package net.recraft.annihilatoin.objects.kit

import net.recraft.annihilatoin.listener.Soulbound
import net.recraft.annihilatoin.objects.GameTeam
import net.recraft.annihilatoin.objects.builder.PlayerInventoryImpl
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory
import org.bukkit.inventory.meta.LeatherArmorMeta

abstract class
KitBase(private val _type: KitType,
        _icon: ItemStack,
        description: List<String>
)
{
    init {
        check(description.isNotEmpty() || description.size < 4)
    }
    private fun setDescription(itemStack: ItemStack, lores: List<String>): ItemStack {
        val meta = itemStack.itemMeta
        meta.displayName = "${ChatColor.GOLD}${_type.name.lowercase().capitalize()}"
        meta.setLore(lores)
        itemStack.setItemMeta(meta)
        return itemStack
    }
    open val icon    = setDescription(_icon, description)
    open val sword   = ItemStack(Material.WOOD_SWORD)
    open val pickaxe = ItemStack(Material.WOOD_PICKAXE)
    open val axe     = ItemStack(Material.WOOD_AXE)
    open val helmet  = ItemStack(Material.LEATHER_HELMET)
    open val chestPlate = ItemStack(Material.LEATHER_CHESTPLATE)
    open val leggings   = ItemStack(Material.LEATHER_LEGGINGS)
    open val boots      = ItemStack(Material.LEATHER_BOOTS)
    fun allItems(team: GameTeam): List<ItemStack> {
        val playerInventory = PlayerInventoryImpl()
        equip(playerInventory, team.color)
        return playerInventory.getItems()
    }
    open fun setInit(player: Player) {}
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