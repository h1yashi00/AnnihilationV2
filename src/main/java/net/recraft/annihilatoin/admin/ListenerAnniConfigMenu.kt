package net.recraft.annihilatoin.admin

import net.recraft.annihilatoin.admin.AnniConfigMenu.Menu.Companion.isSpecialWool
import net.recraft.annihilatoin.config.ConfigMap
import net.recraft.annihilatoin.objects.Game
import net.recraft.annihilatoin.objects.GameTeam
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.inventory.InventoryClickEvent


class ListenerAnniConfigMenu(private val configMap: ConfigMap): Listener, CommandExecutor {
    private val menu = AnniConfigMenu();

    // anniConfigに反応する
    override fun onCommand(sender: CommandSender?, p1: Command?, p2: String?, p3: Array<out String>?): Boolean {
        val player = if (sender is Player) { sender as Player} else {return false}
        // 色ようもを表示
        menu.showMenu(player)
        return true
    }

    // 色羊毛をクリックしときの動作｡
    @EventHandler
    fun onInventoryListener(event: InventoryClickEvent) {
        if (!event.whoClicked.isOp) return
        if (event.clickedInventory?.title != menu.title) return
        val player = if (event.whoClicked is Player) {Bukkit.getPlayer(event.whoClicked.name)} else { return }
        val colorWool = if (isSpecialWool(event.currentItem)) {event.currentItem} else return
        ConfigMap.TeamObjective.giveSpecificItems(colorWool, player)
        event.isCancelled = true
    }

    // 破壊したブロックの位置を
    // mapConfigファイルに上書きして登録する｡
    @EventHandler
    fun onPlaceBlock(event: BlockBreakEvent) {
        val player = if (event.player.isOp) { event.player } else { return }
        val slotItem = event.player.inventory.getItem(0) ?: return
        val specialWool = if (isSpecialWool(slotItem)) { slotItem } else { return }
        val team = when (specialWool) {
            AnniConfigMenu.Menu.RED     .item  ->GameTeam.getTeam("red")  ?: return
            AnniConfigMenu.Menu.BLUE    .item  ->GameTeam.getTeam("blue")  ?: return
            AnniConfigMenu.Menu.YELLOW  .item  ->GameTeam.getTeam("yellow")?: return
            AnniConfigMenu.Menu.GREEN   .item  ->GameTeam.getTeam("green") ?: return
            else -> return
        }
        val holdingItem = player.itemInHand
        event.isCancelled = true
        val world = event.player.world
        val location = event.block.location
        player.sendMessage("aaaaaa")
        val objective = ConfigMap.TeamObjective.getObject(holdingItem) ?: return
        configMap.writeLocation(world, objective, team, location)
        event.player.sendMessage("保存しました")
    }

    // anniconfigMenuを所有しているときのインベントリ操作を禁止する
    // protectBaseのアイテムをクリックしたときの動作も含まれる
    @EventHandler
    fun onInventoryClickEvent(event: InventoryClickEvent) {
        if (!event.whoClicked.isOp) return
        val item = event.currentItem
        if (isSpecialWool(item ?: return)) {
            event.isCancelled = true
            return
        }

        // protectbaseだったとき登録する保護する範囲を登録する
        if (item != ConfigMap.TeamObjective.PROTECT_BASE.item) return
        val player = if (event.whoClicked is Player) {event.whoClicked as Player} else {return}
        val slotItem = player.inventory.getItem(0) ?: return
        val specialWool = if (isSpecialWool(slotItem)) { slotItem } else { return }
        val team = when (specialWool) {
            AnniConfigMenu.Menu.RED.    item   ->GameTeam.getTeam("red")  ?: return
            AnniConfigMenu.Menu.BLUE.   item   ->GameTeam.getTeam("blue")  ?: return
            AnniConfigMenu.Menu.YELLOW. item   ->GameTeam.getTeam("yellow")?: return
            AnniConfigMenu.Menu.GREEN.  item   ->GameTeam.getTeam("green") ?: return
            else -> return
        }
        val selection = Game.getWorldEdit()?.getSelection(player)
        if (selection == null) {
            player.sendMessage("${ChatColor.RED}範囲を選択してください")
            return
        }
        configMap.writePlayerSelection(player,team, selection.minimumPoint, selection.maximumPoint)
        player.sendMessage("保存しました｡")
    }
}
