package net.recraft.annihilatoin.objects

import net.recraft.annihilatoin.util.Util
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.inventory.ItemStack
import java.util.*
import kotlin.collections.ArrayList

enum class ResourceBlocks(val type: Material, val item: Material, val exp: Int = 8, val restore: Int = 8, val setCobble: Boolean = false) {
    DIAMOND(Material.DIAMOND_ORE, Material.DIAMOND, 12, 30, true),
    GOLD(Material.GOLD_ORE, Material.GOLD_ORE, 12, 20, true),
    IRON(Material.IRON_ORE, Material.IRON_ORE, 12, 20, true),
    REDSTONE(Material.REDSTONE_ORE, Material.REDSTONE, 12, 20, true),
    LAPIS(Material.LAPIS_ORE, Material.INK_SACK, 12, 20, true),
    EMERALD(Material.EMERALD_ORE, Material.EMERALD, 12, 40, true),
    COAL(Material.COAL_ORE, Material.COAL, 12, 10, true),
    MELON(Material.MELON_BLOCK, Material.MELON, 12, 8, false),
    GRAVEL(Material.GRAVEL, Material.GRAVEL, 12, 8, true),
    OAK(Material.LOG, Material.LOG),
    SPRUCE(Material.LOG, Material.LOG),
    BIRCH(Material.LOG, Material.LOG),
    JUNGLE(Material.LOG, Material.LOG),
    ACACIA(Material.LOG_2, Material.LOG_2),
    DARK_OAK(Material.LOG_2, Material.LOG_2);

    // LOGs setting
    private fun randomAmount(): Int = Random().nextInt(3) + 1;
    private fun gravelRandomItem(): Material {
        val gravelItems = ArrayList<Material>()
        gravelItems[0] = Material.FLINT;
        gravelItems[1] = Material.FEATHER;
        gravelItems[2] = Material.STRING;
        val randomItem = Random().nextInt(gravelItems.size);
        return gravelItems[randomItem];
    }

    fun getItemStack(): ItemStack {
        return when (this) {
            LAPIS -> ItemStack(item, 1, 4);
            OAK -> ItemStack(item, 1, 0);
            SPRUCE -> ItemStack(item, 1, 1);
            BIRCH -> ItemStack(item, 1, 2);
            JUNGLE -> ItemStack(item, 1, 3);
            ACACIA -> ItemStack(item, 1, 0);
            DARK_OAK -> ItemStack(item, 1, 1);
            MELON -> ItemStack(item, randomAmount() + 2);
            GRAVEL -> ItemStack(gravelRandomItem(), randomAmount());
            else -> ItemStack(item, 1);
        }
    }
    companion object {
        fun getStatus(block: Block):ResourceBlocks? {
            for (resource in values()) {
                if (Util.isLog(block.type)) {
                    return getResourceLog(block)
                }
                if (resource.type == block.type) {
                    return resource
                }
            }
            if (block.type == Material.GLOWING_REDSTONE_ORE) return REDSTONE // レッドストーン採掘する際に光ってしまうのでそれの処理
            return null
        }
        // 原木はMaterial.Log Log_2で管理されている
        private fun getResourceLog(log: Block): ResourceBlocks? {
            when (log.type) {
                Material.LOG -> {
                    when (log.data % 4) {
                        0 -> return OAK
                        1 -> return SPRUCE
                        2 -> return BIRCH
                        3 -> return JUNGLE
                        else -> Util.fatal("Is Log?")
                    }
                }
                Material.LOG_2 -> {
                    when (log.data % 4) {
                        0 -> return ACACIA
                        1 -> return DARK_OAK
                        else -> Util.fatal("IS LOG_2?")
                    }
                }
                else -> {
                    Util.fatal("IS not LOG or LOG_2")
                }
            }
            return null
        }
        fun isResourceBlocks(type: Material): Boolean {
            values().forEach {
                if (it.type == type) return true
            }
            return false
        }
    }
}