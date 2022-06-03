package net.recraft.annihilatoin.objects

import net.recraft.annihilatoin.scoreboard.ScoreboardAnni
import org.bukkit.Bukkit
import org.bukkit.Sound


class PhaseController {
    private var currentTime: Int = 0
    val time
        get() = currentTime
    val nextPhaseTime
        get() = formatTime()
    val currentPhase
        get() = currentPhase()
    private fun currentPhase(): Int {
        val phase = (currentTime / (60*10)) + 1
        if (5 <= phase) return 5
        return phase
    }
    private var phase1Called = false
    private var phase2Called = false
    private var phase3Called = false
    private var phase4Called = false
    private var phase5Called = false
    fun pass() {
        ScoreboardAnni.nextPhaseTimeUpdate()
        currentTime+= 1
        when (currentPhase) {
            1 -> phase1()
            2 -> phase2()
            3 -> phase3()
            4 -> phase4()
            5 -> phase5()
        }
    }
    private fun formatTime(): String {
        val phaseNum = 600 * currentPhase
        val minute = ((phaseNum - time) / 60)
        val secound = (phaseNum - time) - (minute*60)
        val sec = if (secound < 10) "0$secound" else secound
        val min = if (minute  < 10) "0$minute" else minute
        if (currentPhase == 5) {
            return "Final Phase"
        }
        return "$min:$sec"
    }

    private fun phase1() {
        if (phase1Called) return
        phase1Called = true
        ScoreboardAnni.currentPhaseUpdate()
        Bukkit.broadcastMessage("phase 1!!!!!!!!!!!!!!!!!").times(5)
        Bukkit.getOnlinePlayers().forEach{ it.playSound(it.location, Sound.AMBIENCE_THUNDER, 1F,1F)}
        thunderSound()
    }
    private fun phase2() {
        if (phase2Called) return
        phase2Called = true
        ScoreboardAnni.currentPhaseUpdate()
        Bukkit.broadcastMessage("phase 2!!!!!!!!!!!!!!!!!").times(5)
        Bukkit.getOnlinePlayers().forEach{ it.playSound(it.location, Sound.AMBIENCE_THUNDER, 1F,1F)}
        thunderSound()
    }
    private fun phase3() {
        if (phase3Called) return
        phase3Called = true
        ScoreboardAnni.currentPhaseUpdate()
        Bukkit.broadcastMessage("phase 3!!!!!!!!!!!!!!!!!").times(5)
        Bukkit.broadcastMessage("DIAMOND!!!!!!!!!!!").times(5)
        Bukkit.getOnlinePlayers().forEach{ it.playSound(it.location, Sound.AMBIENCE_THUNDER, 1F,1F)}
        thunderSound()
    }
    private fun phase4() {
        if (phase4Called) return
        phase4Called = true
        ScoreboardAnni.currentPhaseUpdate()
        Bukkit.broadcastMessage("phase 4!!!!!!!!!!!!!!!!!").times(5)
        thunderSound()
    }
    private fun phase5() {
        if (phase5Called) return
        phase5Called = true
        ScoreboardAnni.currentPhaseUpdate()
        Bukkit.broadcastMessage("phase 5!!!!!!!!!!!!!!!!!").times(5)
        thunderSound()
    }
    private fun thunderSound() {
        Bukkit.getOnlinePlayers().forEach{ it.playSound(it.location, Sound.AMBIENCE_THUNDER, 1F,1F)}
    }
}