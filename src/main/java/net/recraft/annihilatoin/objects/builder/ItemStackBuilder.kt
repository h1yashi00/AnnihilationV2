package net.recraft.annihilatoin.objects.builder

import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.EnchantmentStorageMeta
import org.bukkit.inventory.meta.ItemMeta

open class ItemStackBuilder(private val material: Material) {
    private var title: String?      = null
    private var lore:  MutableList<String> = ArrayList()
    private var amount = 1
    private var enchantments: MutableMap<Enchantment, Int> = HashMap()
    fun title(_title: String): ItemStackBuilder {
        title = _title
        return this
    }
    fun amount(_amount: Int): ItemStackBuilder {
        amount = _amount
        return this
    }
    fun lore(_lores: List<String>): ItemStackBuilder {
        _lores.forEach {
            lore.add(it)
        }
        return this
    }
    fun enchantment(_enchantment: Enchantment, _level: Int = 1): ItemStackBuilder {
        enchantments[_enchantment] = _level
        return this
    }
    open fun build(): ItemStack {
        val item = ItemStack(material, amount)
        val meta = item.itemMeta
        meta.displayName = title
        if (meta is EnchantmentStorageMeta) addStoredEnchantments(meta) else addEnchantments(meta)
        meta.lore = lore
        item.setItemMeta(meta)
        return item
    }
    private fun addEnchantments(meta: ItemMeta) {
        if (enchantments.isNotEmpty()) {
            val keys = enchantments.keys
            for (key in keys) {
                val level = enchantments[key]
                meta.addEnchant(key, level!!, true)
            }
        }
    }

    private fun addStoredEnchantments(meta: EnchantmentStorageMeta) {
        if (enchantments.isNotEmpty()) {
            val keys = enchantments.keys
            for (key in keys) {
                val level = enchantments[key]
                meta.addStoredEnchant(key, level!!, true)
            }
        }
    }
}