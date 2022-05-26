package net.recraft.annihilatoin.objects

import net.recraft.annihilatoin.scoreboard.ScoreboardVote
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class VoteManager(mapList: List<String>) {
    private val _candidateMaps: ArrayList<Candidate>
    init {
        _candidateMaps = ArrayList<Candidate>()
        mapList.forEach {
            addCandidate(it)
        }
        ScoreboardVote.registerCandidates(_candidateMaps.clone() as ArrayList<Candidate>)
    }
    companion object {
        var section: Int = 0
    }
    private fun getCandidate(name: String): Candidate {
        return Candidate(name, section)
    }
    data class Candidate(val worldName: String, val section: Int, var amount: Int = 0)
    private val votedPlayers = HashMap<UUID, Candidate>()

    operator fun contains(uuid: UUID): Boolean {
        return votedPlayers.containsKey(uuid)
    }

    fun vote(uuid: UUID, worldName: String) :Boolean {
        val c = candidate(worldName) ?: return false
        votedPlayers[uuid] = c.apply{
            amount += 1
        }
        ScoreboardVote.voteUpdate(c)
        return true
    }

    fun revote(uuid: UUID) {
        votedPlayers[uuid]?.let {
            it.amount -= 1
            ScoreboardVote.voteUpdate(it)
        }
        votedPlayers.remove(uuid)
    }


    fun result() : String {
        var big = 0
        lateinit var resultCandidate: Candidate
        for (candidate in _candidateMaps) {
            if (big <= candidate.amount) {
                big = candidate.amount
                resultCandidate = candidate
            }
        }
        //world_$mapNameという形式でワールドファイルを作成しているのでそれに従ってください
        return "world_${resultCandidate.worldName}"
    }

    private fun candidate(worldName: String): Candidate? {
        for (candidate in _candidateMaps) {
            if (candidate.worldName == worldName) return candidate
        }
        return null
    }

    private fun addCandidate(name: String) {
        _candidateMaps.add(getCandidate(name))
    }
}