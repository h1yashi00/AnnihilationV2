package net.recraft.annihilatoin.objects

import org.bukkit.Bukkit
import org.bukkit.Sound

class PhaseController {
    private var currentTime: Int = 0
    val time
        get() = currentTime
    val currentPhase
        get() = (currentTime / (60*10)) + 1
    private var phase1Called = false
    private var phase2Called = false
    private var phase3Called = false
    private var phase4Called = false
    private var phase5Called = false
    fun pass() {
        currentTime+= 1
        when (currentPhase) {
            1 -> phase1()
            2 -> phase2()
            3 -> phase3()
            4 -> phase4()
            5 -> phase5()
        }
    }
    private fun phase1() {
        if (phase1Called) return
        phase1Called = true
        Bukkit.broadcastMessage("phase 1!!!!!!!!!!!!!!!!!").times(5)
        Bukkit.getOnlinePlayers().forEach{ it.playSound(it.location, Sound.AMBIENCE_THUNDER, 1F,1F)}
        thunderSound()
    }
    private fun phase2() {
        if (phase2Called) return
        phase2Called = true
        Bukkit.broadcastMessage("phase 2!!!!!!!!!!!!!!!!!").times(5)
        Bukkit.getOnlinePlayers().forEach{ it.playSound(it.location, Sound.AMBIENCE_THUNDER, 1F,1F)}
        thunderSound()
    }
    private fun phase3() {
        if (phase3Called) return
        phase3Called = true
        Bukkit.broadcastMessage("phase 3!!!!!!!!!!!!!!!!!").times(5)
        Bukkit.broadcastMessage("DIAMOND!!!!!!!!!!!").times(5)
        Bukkit.getOnlinePlayers().forEach{ it.playSound(it.location, Sound.AMBIENCE_THUNDER, 1F,1F)}
        thunderSound()
    }
    private fun phase4() {
        if (phase4Called) return
        phase4Called = true
        Bukkit.broadcastMessage("phase 4!!!!!!!!!!!!!!!!!").times(5)
        thunderSound()
    }
    private fun phase5() {
        if (phase5Called) return
        phase5Called = true
        Bukkit.broadcastMessage("phase 5!!!!!!!!!!!!!!!!!").times(5)
        thunderSound()
    }
    private fun thunderSound() {
        Bukkit.getOnlinePlayers().forEach{ it.playSound(it.location, Sound.AMBIENCE_THUNDER, 1F,1F)}
    }
}