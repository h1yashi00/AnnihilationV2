package net.recraft.annihilatoin.objects

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
    }
    data class Candidate(val worldName: String, var amount: Int = 0)
    private val votedPlayers = HashMap<UUID, Candidate>()
    val candidateMaps: List<Candidate> get() = _candidateMaps.clone() as List<Candidate>

    operator fun contains(uuid: UUID): Boolean {
        return votedPlayers.containsKey(uuid)
    }

    fun vote(uuid: UUID, worldName: String) :Boolean {
        val c = get(worldName) ?: return false
        votedPlayers[uuid] = c
        return true
    }

    fun revote(uuid: UUID) {
        votedPlayers.remove(uuid)
    }


    fun result() : String {
        var big = 0
        lateinit var resultCandidate: Candidate
        for (candidate in _candidateMaps) {
            if (big < candidate.amount) {
                big = candidate.amount
                resultCandidate = candidate
            }
        }
        //world_$mapNameという形式でワールドファイルを作成しているのでそれに従ってください
        return "world_${resultCandidate.worldName}"
    }

    private fun get(worldName: String): Candidate? {
        for (candidate in _candidateMaps) {
            if (candidate.worldName == worldName) return candidate
        }
        return null
    }

    private fun addCandidate(name: String) {
        _candidateMaps.add(Candidate(name))
    }
}