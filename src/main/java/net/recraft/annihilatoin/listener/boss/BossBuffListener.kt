package net.recraft.annihilatoin.listener.boss

import net.recraft.annihilatoin.isSame
import net.recraft.annihilatoin.objects.builder.ItemStackBuilder
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object BossBuffListener: Listener {
    @EventHandler
    fun onDamageEntity(event: EntityDamageByEntityEvent) {
        val player = if (event.entity is Player) {event.entity as Player} else {return}
        if (player.inventory.chestplate.isSame(chestplate)) {
            player.addPotionEffect(PotionEffect(PotionEffectType.REGENERATION, 20 * 9,0))
        }
        if (player.inventory.leggings.isSimilar(leggings)) {
            player.addPotionEffect(PotionEffect(PotionEffectType.SPEED, 20 * 9, 0))
        }
    }
    @EventHandler
    fun onDamage(event: EntityDamageEvent) {
        val player = if (event.entity is Player) {event.entity as Player} else {return}
        if (event.cause == EntityDamageEvent.DamageCause.FIRE_TICK) {
            if (player.inventory.helmet.isSame(helmet)) {
                event.isCancelled = true
                if (player.fireTicks > 0) {
                    player.setFireTicks(0)
                }
            }
        }
        if (event.cause == EntityDamageEvent.DamageCause.FALL) {
            if (player.inventory.boots.isSame(boots)) event.isCancelled = true
        }
    }
    @EventHandler
    fun rightClickBossBuff(event: PlayerInteractEvent) {
        val player = event.player
        if (!player.itemInHand.isSame(itemBossBuff)) return
        player.openInventory(itemBossBuffInv)
    }

    @EventHandler
    fun clickMenu(event: InventoryClickEvent) {
        val inv = event.inventory
        if (inv.title != itemBossBuffInv.title) return
        event.isCancelled = true
        val item = event.currentItem
        if (!allItem().contains(item)) return
        val player = event.whoClicked
        player.closeInventory()
        player.inventory.remove(itemBossBuff)
        player.inventory.addItem(item)
    }

    operator fun invoke(): BossBuffListener {
        return this
    }

    val itemBossBuff
        get() = ItemStackBuilder(Material.NETHER_STAR).title("${ChatColor.GOLD}ボスバフ").lore(listOf("右クリックで見れます")).build()
    val itemBossBuffInv = Bukkit.createInventory(null, (9*6), "${ChatColor.DARK_GRAY}${ChatColor.BOLD}ボスバフ").apply {
        allItem().forEach{addItem(it)}
    }
    private fun allItem(): List<ItemStack> {
        return listOf(
            str,
            reg,
            gapple,
            inv,
            bow,
            helmet,
            chestplate,
            leggings,
            boots
        )
    }
    // str
    val str
        get () = ItemStackBuilder(Material.POTION).damage(16393).build()
    // regen
    val reg
        get() = ItemStackBuilder(Material.POTION).damage(16417).build()
    // inv
    val inv
        get() = ItemStackBuilder(Material.POTION).damage(16462).build()
    // gapple (lower)
    val gapple
        get() = ItemStackBuilder(Material.GOLDEN_APPLE).amount(3).damage(0).build()
    val bow
        get() = ItemStackBuilder(Material.BOW)
            .enchantment(Enchantment.ARROW_DAMAGE, 5)
            .enchantment(Enchantment.ARROW_INFINITE)
            .enchantment(Enchantment.ARROW_FIRE )
            .build()
    val helmet
        get() = ItemStackBuilder(Material.CHAINMAIL_HELMET)
            .title("ボスバフ装備")
            .lore(listOf("火を取り除く"))
            .build()
    val chestplate
        get() = ItemStackBuilder(Material.CHAINMAIL_CHESTPLATE)
            .title("ボスバフ装備")
            .lore(listOf("ダメージを受けると9秒間再生Ⅰが付与される"))
            .build()
    val leggings
        get() = (ItemStackBuilder(Material.CHAINMAIL_LEGGINGS)
            .title("ボスバフ装備")
            .lore(listOf("ダメージを受けると9秒間スピードⅠが付与される"))
            .build())
    val boots
        get() = ItemStackBuilder(Material.CHAINMAIL_BOOTS)
            .title("ボスバフ装備")
            .lore(listOf("落下ダメージがなくなる"))
            .build()
}