package net.recraft.annihilatoin.listener

import net.recraft.annihilatoin.database.AnnihilationStatsColumn
import net.recraft.annihilatoin.database.Database
import org.bukkit.Material.*
import net.recraft.annihilatoin.listener.SuitableTool.Tool.*
import net.recraft.annihilatoin.objects.Game
import org.bukkit.GameMode
import org.bukkit.block.Block
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable

class SuitableTool: Listener {
    enum class Tool {
        PICKAXE,
        SPADE,
        SHEARS,
        SWORD,
        AXE,
        NO_TOOL;
        companion object {
            fun getTool(tool: ItemStack?): Tool? {
                return when {
                    tool == null    -> null
                    tool.type.name.contains("pickaxe", true)  -> PICKAXE
                    tool.type.name.contains("spade", true)    -> SPADE
                    tool.type.name.contains("shears", true)   -> SHEARS
                    tool.type.name.contains("sword", true)    -> SWORD
                    tool.type.name.contains("axe", true)      -> AXE
                    else -> NO_TOOL
                }
            }
        }
    }
    @EventHandler(priority = EventPriority.LOWEST)
    fun onBreak(event: BlockBreakEvent) {
        val player = event.player
        if (player.gameMode == GameMode.CREATIVE) return
        val item = player.itemInHand
        val block = event.block
        val tool = Tool.getTool(item)
        val suitableTool = suitableTool(block) ?: return
        if ((suitableTool == NO_TOOL || suitableTool == tool)) {
            return
        }
        event.isCancelled = true
    }

    private fun suitableTool(block: Block): Tool? {
        return when(block.type) {
            STONE -> PICKAXE
            GRASS -> SPADE
            DIRT  -> SPADE
            COBBLESTONE -> PICKAXE
            SAPLING-> NO_TOOL
            WOOD  -> AXE
            SAND  -> SPADE
            GRAVEL -> SPADE
            GOLD_ORE -> PICKAXE
            IRON_ORE -> PICKAXE
            COAL_ORE -> PICKAXE
            LOG      -> AXE
            LEAVES   -> SWORD
            SPONGE   -> SWORD
            LAPIS_ORE-> PICKAXE
            GLASS    -> NO_TOOL
            LAPIS_BLOCK -> PICKAXE
            DISPENSER   -> PICKAXE
            SANDSTONE   -> PICKAXE
            NOTE_BLOCK  -> AXE
            BED_BLOCK -> NO_TOOL
            POWERED_RAIL -> PICKAXE
            DETECTOR_RAIL -> PICKAXE
            PISTON_STICKY_BASE -> NO_TOOL
            WEB -> SWORD
            LONG_GRASS -> NO_TOOL
            DEAD_BUSH -> NO_TOOL
            PISTON_BASE -> NO_TOOL
            PISTON_EXTENSION -> NO_TOOL
            WOOL -> Tool.SHEARS
            PISTON_MOVING_PIECE -> NO_TOOL
            YELLOW_FLOWER -> NO_TOOL
            RED_ROSE      -> NO_TOOL
            BROWN_MUSHROOM-> NO_TOOL
            RED_MUSHROOM  -> NO_TOOL
            GOLD_BLOCK    -> PICKAXE
            IRON_BLOCK    -> PICKAXE
            DOUBLE_STEP   -> PICKAXE
            STEP          -> PICKAXE
            BRICK         -> PICKAXE
            TNT           -> NO_TOOL
            BOOKSHELF     -> AXE
            MOSSY_COBBLESTONE -> PICKAXE
            OBSIDIAN      -> PICKAXE
            TORCH         -> NO_TOOL
            FIRE          -> NO_TOOL
            MOB_SPAWNER   -> PICKAXE
            WOOD_STAIRS   -> AXE
            CHEST         -> AXE
            REDSTONE_WIRE -> NO_TOOL
            DIAMOND_ORE   -> PICKAXE
            DIAMOND_BLOCK -> PICKAXE
            WORKBENCH     -> AXE
            CROPS         -> NO_TOOL
            SOIL          -> SPADE
            FURNACE       -> PICKAXE
            BURNING_FURNACE->PICKAXE
            SIGN_POST     -> AXE
            WOODEN_DOOR   -> AXE
            LADDER        -> AXE
            RAILS         -> PICKAXE
            COBBLESTONE_STAIRS -> PICKAXE
            WALL_SIGN     -> AXE
            LEVER         -> NO_TOOL
            STONE_PLATE   -> PICKAXE
            IRON_DOOR_BLOCK -> PICKAXE
            WOOD_PLATE -> AXE
            REDSTONE_ORE -> PICKAXE
            GLOWING_REDSTONE_ORE -> PICKAXE
            REDSTONE_TORCH_OFF -> NO_TOOL
            REDSTONE_TORCH_ON  -> NO_TOOL
            STONE_BUTTON       -> NO_TOOL
            SNOW               -> NO_TOOL
            ICE                -> NO_TOOL
            SNOW_BLOCK         -> SPADE
            CACTUS             -> NO_TOOL
            CLAY               -> SPADE
            SUGAR_CANE_BLOCK   -> NO_TOOL
            JUKEBOX            -> AXE
            FENCE              -> AXE
            PUMPKIN            -> SWORD
            NETHERRACK         -> PICKAXE
            SOUL_SAND          -> SPADE
            GLOWSTONE          -> NO_TOOL
            PORTAL             -> NO_TOOL
            JACK_O_LANTERN     -> SWORD
            CAKE_BLOCK         -> NO_TOOL
            DIODE_BLOCK_OFF    -> NO_TOOL
            DIODE_BLOCK_ON     -> NO_TOOL
            STAINED_GLASS      -> NO_TOOL
            TRAP_DOOR          -> AXE
            MONSTER_EGGS       -> NO_TOOL
            SMOOTH_BRICK       -> PICKAXE
            HUGE_MUSHROOM_1    -> NO_TOOL
            HUGE_MUSHROOM_2    -> NO_TOOL
            IRON_FENCE         -> PICKAXE
            THIN_GLASS         -> NO_TOOL
            MELON_BLOCK        -> SWORD
            PUMPKIN_STEM       -> NO_TOOL
            MELON_STEM         -> NO_TOOL
            VINE               -> NO_TOOL
            FENCE_GATE         -> AXE
            BRICK_STAIRS       -> PICKAXE
            SMOOTH_STAIRS      -> PICKAXE
            MYCEL              -> SPADE
            WATER_LILY         -> NO_TOOL
            NETHER_BRICK       -> PICKAXE
            NETHER_FENCE       -> PICKAXE
            NETHER_BRICK_STAIRS-> PICKAXE
            NETHER_WARTS       -> NO_TOOL
            ENCHANTMENT_TABLE  -> PICKAXE
            BREWING_STAND      -> PICKAXE
            CAULDRON           -> PICKAXE
            ENDER_PORTAL       -> NO_TOOL
            ENDER_PORTAL_FRAME -> NO_TOOL
            ENDER_STONE        -> PICKAXE
            DRAGON_EGG         -> NO_TOOL
            REDSTONE_LAMP_OFF  -> NO_TOOL
            REDSTONE_LAMP_ON   -> NO_TOOL
            WOOD_DOUBLE_STEP   -> AXE
            WOOD_STEP          -> AXE
            COCOA              -> NO_TOOL
            SANDSTONE_STAIRS   -> PICKAXE
            EMERALD_ORE        -> PICKAXE
            ENDER_CHEST        -> PICKAXE
            TRIPWIRE_HOOK      -> NO_TOOL
            TRIPWIRE           -> NO_TOOL
            EMERALD_BLOCK      -> PICKAXE
            SPRUCE_WOOD_STAIRS -> AXE
            BIRCH_WOOD_STAIRS  -> AXE
            JUNGLE_WOOD_STAIRS -> AXE
            COMMAND            -> NO_TOOL
            BEACON             -> NO_TOOL
            COBBLE_WALL        -> PICKAXE
            FLOWER_POT         -> NO_TOOL
            CARROT             -> NO_TOOL
            POTATO             -> NO_TOOL
            WOOD_BUTTON        -> NO_TOOL
            SKULL              -> NO_TOOL
            ANVIL              -> PICKAXE
            TRAPPED_CHEST      -> PICKAXE
            GOLD_PLATE         -> PICKAXE
            IRON_PLATE         -> PICKAXE
            REDSTONE_COMPARATOR_OFF -> NO_TOOL
            REDSTONE_COMPARATOR_ON  -> NO_TOOL
            DAYLIGHT_DETECTOR -> NO_TOOL
            REDSTONE_BLOCK    -> PICKAXE
            QUARTZ_ORE        -> PICKAXE
            HOPPER            -> PICKAXE
            QUARTZ_BLOCK      -> PICKAXE
            QUARTZ_STAIRS     -> PICKAXE
            ACTIVATOR_RAIL    -> PICKAXE
            DROPPER           -> PICKAXE
            STAINED_CLAY      -> PICKAXE
            STAINED_GLASS_PANE-> NO_TOOL
            LEAVES_2          -> SWORD
            LOG_2             -> AXE
            ACACIA_STAIRS     -> AXE
            DARK_OAK_STAIRS   -> AXE
            SLIME_BLOCK       -> NO_TOOL
            BARRIER           -> NO_TOOL
            IRON_TRAPDOOR     -> PICKAXE
            PRISMARINE        -> PICKAXE
            SEA_LANTERN       -> NO_TOOL
            HAY_BLOCK         -> NO_TOOL
            CARPET            -> Tool.SHEARS
            HARD_CLAY         -> PICKAXE
            COAL_BLOCK        -> PICKAXE
            PACKED_ICE        -> PICKAXE
            DOUBLE_PLANT      -> NO_TOOL
            STANDING_BANNER   -> NO_TOOL
            WALL_BANNER       -> NO_TOOL
            DAYLIGHT_DETECTOR_INVERTED -> NO_TOOL
            RED_SANDSTONE     -> PICKAXE
            RED_SANDSTONE_STAIRS -> PICKAXE
            DOUBLE_STONE_SLAB2  -> PICKAXE
            STONE_SLAB2        -> PICKAXE
            SPRUCE_FENCE_GATE -> AXE
            BIRCH_FENCE_GATE  -> AXE
            JUNGLE_FENCE_GATE -> AXE
            DARK_OAK_FENCE_GATE -> AXE
            ACACIA_FENCE_GATE -> AXE
            SPRUCE_FENCE  -> AXE
            BIRCH_FENCE -> AXE
            JUNGLE_FENCE -> AXE
            DARK_OAK_FENCE ->AXE
            ACACIA_FENCE -> AXE
            SPRUCE_DOOR -> AXE
            BIRCH_DOOR -> AXE
            JUNGLE_DOOR -> AXE
            ACACIA_DOOR -> AXE
            DARK_OAK_DOOR -> AXE
            else -> NO_TOOL
        }
    }
}