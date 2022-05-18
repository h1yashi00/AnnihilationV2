package net.recraft.annihilatoin.objects.builder

import org.bukkit.Material
import org.bukkit.entity.HumanEntity
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory
import java.util.HashMap

class PlayerInventoryImpl: PlayerInventory {
    private val armor = Array(4) {ItemStack(Material.AIR)}
    private val items = ArrayList<ItemStack>()
    fun getItems(): ArrayList<ItemStack> {
        val clonedItems = ArrayList<ItemStack>()
        armor.forEach {
            clonedItems.add(it)
        }
        items.forEach {
            clonedItems.add(it)
        }
        return clonedItems

    }
    override fun iterator(): MutableListIterator<ItemStack> {
        val mutableList = ArrayList<ItemStack>()
        items.forEach { mutableList.add(it) }
        return mutableList.listIterator()
    }

    override fun iterator(p0: Int): MutableListIterator<ItemStack> {
        TODO("Not yet implemented")
    }

    override fun getSize(): Int {
        TODO("Not yet implemented")
    }

    override fun getMaxStackSize(): Int {
        TODO("Not yet implemented")
    }

    override fun setMaxStackSize(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun getName(): String {
        TODO("Not yet implemented")
    }

    override fun getItem(p0: Int): ItemStack {
        TODO("Not yet implemented")
    }

    override fun setItem(p0: Int, p1: ItemStack) {
        items.add(p1)
    }

    override fun addItem(vararg p0: ItemStack?): HashMap<Int, ItemStack> {
        TODO("Not yet implemented")
    }

    override fun removeItem(vararg p0: ItemStack?): HashMap<Int, ItemStack> {
        TODO("Not yet implemented")
    }

    override fun getContents(): Array<ItemStack> {
        TODO("Not yet implemented")
    }

    override fun setContents(p0: Array<out ItemStack>?) {
        TODO("Not yet implemented")
    }

    override fun contains(p0: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun contains(p0: Material?): Boolean {
        TODO("Not yet implemented")
    }

    override fun contains(p0: ItemStack?): Boolean {
        TODO("Not yet implemented")
    }

    override fun contains(p0: Int, p1: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun contains(p0: Material?, p1: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun contains(p0: ItemStack?, p1: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun containsAtLeast(p0: ItemStack?, p1: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun all(p0: Int): HashMap<Int, out ItemStack> {
        TODO("Not yet implemented")
    }

    override fun all(p0: Material?): HashMap<Int, out ItemStack> {
        TODO("Not yet implemented")
    }

    override fun all(p0: ItemStack?): HashMap<Int, out ItemStack> {
        TODO("Not yet implemented")
    }

    override fun first(p0: Int): Int {
        TODO("Not yet implemented")
    }

    override fun first(p0: Material?): Int {
        TODO("Not yet implemented")
    }

    override fun first(p0: ItemStack?): Int {
        TODO("Not yet implemented")
    }

    override fun firstEmpty(): Int {
        TODO("Not yet implemented")
    }

    override fun remove(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun remove(p0: Material?) {
        TODO("Not yet implemented")
    }

    override fun remove(p0: ItemStack?) {
        TODO("Not yet implemented")
    }

    override fun clear(p0: Int, p1: Int): Int {
        TODO("Not yet implemented")
    }

    override fun clear(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun clear() {
        TODO("Not yet implemented")
    }

    override fun getViewers(): MutableList<HumanEntity> {
        TODO("Not yet implemented")
    }

    override fun getTitle(): String {
        TODO("Not yet implemented")
    }

    override fun getType(): InventoryType {
        TODO("Not yet implemented")
    }

    override fun getHolder(): HumanEntity {
        TODO("Not yet implemented")
    }

    override fun getArmorContents(): Array<ItemStack> {
        return armor
    }

    override fun getHelmet(): ItemStack {
        TODO("Not yet implemented")
    }

    override fun getChestplate(): ItemStack {
        TODO("Not yet implemented")
    }

    override fun getLeggings(): ItemStack {
        TODO("Not yet implemented")
    }

    override fun getBoots(): ItemStack {
        TODO("Not yet implemented")
    }

    override fun setArmorContents(p0: Array<out ItemStack>?) {
        TODO("Not yet implemented")
    }

    override fun setHelmet(helmet: ItemStack) {
        armor[0] = helmet
    }

    override fun setChestplate(chestplate: ItemStack) {
        armor[1] = chestplate
    }

    override fun setLeggings(leggings: ItemStack) {
        armor[2] = leggings
    }

    override fun setBoots(boots: ItemStack) {
        armor[3] = boots
    }

    override fun getItemInHand(): ItemStack {
        TODO("Not yet implemented")
    }

    override fun setItemInHand(p0: ItemStack?) {
        TODO("Not yet implemented")
    }

    override fun getHeldItemSlot(): Int {
        TODO("Not yet implemented")
    }

    override fun setHeldItemSlot(p0: Int) {
        TODO("Not yet implemented")
    }
}