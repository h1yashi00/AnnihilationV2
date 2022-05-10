package net.recraft.annihilatoin.objects

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.entity.Zombie
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory
import java.util.*

data class QuitPlayer(val zombie: Zombie, private val inventory: PlayerInventory, private val armorContents: List<ItemStack>) {
    private var zombieKilled = false
    private var survived     = false

    fun zombieKilled() {
        dropInventoryItem()
        zombieKilled = true
    }
    fun respawn(player: Player, team: GameTeam) {
        // ゾンビが殺されていたときチームにスポーン
        if (zombieKilled) {
            player.health = 0.0
            // player.teleport(team.randomSpawn)
            return
        }
        // ゾンビが殺されなかった場合
        inventory.forEach { player.inventory.addItem(it ?: return@forEach) }
        player.inventory.armorContents = armorContents.toTypedArray() // setHelmet methodを使うと何故か正常に動かない｡
        // 一分以内に戻ってくるとZombieがremoveされていないのでzombieの場所に戻ってくる
        val loc = if (survived) { team.objects.randomSpawn } else { zombie.location }
        player.teleport(loc)
        zombie.remove()
    }
    fun survived() {
        survived = true
        zombie.remove()
    }
    private fun dropInventoryItem() {
        val location = zombie.location
        val dropArmor = armorContents.filterNot { it.type == Material.AIR }
        inventory.forEach { location.world.dropItem(location, it ?: return@forEach) }
        dropArmor.forEach { location.world.dropItem(location, it) }
    }
}

class PlayerLeaveUnfairAdvantage {
    private val quitPlayers = HashMap<UUID, QuitPlayer>()
    fun add(uuid: UUID, location: Location, customName: String, hp: Double, inventory: PlayerInventory) {
        val zombie = spawnZombie(location, customName, hp, inventory.armorContents.toList())
        quitPlayers[uuid] = QuitPlayer(zombie, inventory, inventory.armorContents.toList())
    }
    fun respawn(player: Player, team: GameTeam) {
        val uuid = player.uniqueId
        val quitPlayer = quitPlayers[uuid]
        // playerがleaveに居なかった場合自分のチームに戻る
        if (quitPlayer == null) {
            player.teleport(team.objects.randomSpawn)
            return
        }
        // playerのゾンビが殺されていた場合
        quitPlayer.respawn(player, team)
        quitPlayers.remove(uuid)
    }
    operator fun get(uuid: UUID): QuitPlayer? {
        return quitPlayers[uuid]
    }
    operator fun get(zombie: Zombie): QuitPlayer? {
        val uuidKeys= quitPlayers.keys
        for (uuid in uuidKeys) {
            if (quitPlayers[uuid]?.zombie == zombie) return quitPlayers[uuid]
        }
        return null
    }
    operator fun contains(uuid: UUID): Boolean {
        return quitPlayers.contains(uuid)
    }
    private fun spawnZombie(spawnLocation: Location, customName: String, health: Double, armorContents: List<ItemStack>): Zombie {
        val mob = spawnLocation.world.spawnEntity(spawnLocation, EntityType.ZOMBIE)!!
        val zombie = mob as Zombie
        zombie.equipment.apply {
            setHelmet       (armorContents[3])
            setChestplate   (armorContents[2])
            setLeggings     (armorContents[1])
            setBoots        (armorContents[0])
        }
        zombie.apply {
            setCustomName(customName)
            isCustomNameVisible = true
            isBaby = false
            isVillager = true
            setHealth(health)
        }
        return zombie
    }
}