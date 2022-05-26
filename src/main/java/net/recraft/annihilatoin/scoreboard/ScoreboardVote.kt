package net.recraft.annihilatoin.scoreboard

import net.recraft.annihilatoin.objects.VoteManager
import net.recraft.annihilatoin.util.ScoreboardUtil
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Objective

object ScoreboardVote: ScoreboardUtil() {
    private lateinit var candidates: ArrayList<VoteManager.Candidate>
    fun registerCandidates(_candidates: ArrayList<VoteManager.Candidate>) {
        candidates = _candidates
    }

    fun createObject(objective: Objective) {
        objective.apply {
            displayName = "${ChatColor.YELLOW}${ChatColor.BOLD}VoteMap"
            displaySlot = DisplaySlot.SIDEBAR
            candidates.forEach { getScore(candidateFormat(it)).score = it.section}
        }
    }
    fun voteUpdate(candidate: VoteManager.Candidate) {
        Bukkit.getOnlinePlayers().forEach {
            replaceScore(it, candidate.section, candidateFormat(candidate))
        }
    }
    private fun candidateFormat(candidate: VoteManager.Candidate): String {
        return "${candidate.worldName} ${ChatColor.AQUA}${candidate.amount}"
    }
}