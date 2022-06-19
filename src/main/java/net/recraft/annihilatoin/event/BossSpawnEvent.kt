package net.recraft.annihilatoin.event

import net.recraft.annihilatoin.objects.boss.WitherBoss
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class BossSpawnEvent(val boss: WitherBoss) : Event() {
    companion object {
        private val HANDLERS = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList {
            return HANDLERS
        }
    }

    override fun getHandlers(): HandlerList {
        return HANDLERS
    }
}