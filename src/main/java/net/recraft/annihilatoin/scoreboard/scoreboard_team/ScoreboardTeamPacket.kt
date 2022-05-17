package net.recraft.annihilatoin.scoreboard.scoreboard_team

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer
import net.recraft.annihilatoin.objects.Game
import org.bukkit.entity.Player
import java.util.*

class ScoreboardTeamPacket(_teamName: String) {
    companion object {
        enum class MODE(val value: Int) {
            CREATE_TEAM   (0),
            REMOVE_TEAM   (1),
            UPDATE_TEAM   (2),
            ADD_PLAYER    (3),
            REMOVE_PLAYER (4);
            companion object {
                fun getMode(mode: String): MODE? {
                    return when (mode.lowercase()) {
                        "create" -> {
                            CREATE_TEAM
                        }
                        "remove_team" -> {
                            REMOVE_PLAYER
                        }
                        "update" -> {
                            UPDATE_TEAM
                        }
                        "add" -> {
                            ADD_PLAYER
                        }
                        "remove_player" -> {
                            REMOVE_PLAYER
                        }
                        else -> {
                            null
                        }
                    }
                }
            }
        }
        enum class NameTagInvisibility {
            ALWAYS,
            HIDE_FOR_OTHER_TEAMS,
            HIDE_FOR_OWN_TEAMS,
            NEVER;
        }
        enum class FriendlyFire{
            TRUE,
            FALSE,
            FRIENDLY_INVISIBLE
        }
    }

    private val teamName: String
    private var prefix: String = ""
    private var suffix: String = ""
    private var nameTagVisibility = "always" // hideForOtherTeams hideForOwnTeam never
    private var friendlyFire: Int = 0 // 0 off 1 on 3 friendly invisible
    private val color: String = ""
    private val players: MutableMap<UUID, String> = HashMap()

    init {
        require( _teamName.length <= 16) { "team name must be less than 16" }
        teamName = _teamName
    }
    private fun makePacket(): PacketContainer {
        return PacketContainer(PacketType.Play.Server.SCOREBOARD_TEAM)
    }
    // プレイヤーが接続してきた際にcreatePacketを送ってあげる必要性がある
    fun joinPacket(bound: Player) {
        val packetFirst = makePacket()
        packetFirst.apply {
            integers.write(0, -1)
            integers.write(1, MODE.CREATE_TEAM.value)
            integers.write(2, friendlyFire)
            strings.write(0, teamName)
            strings.write(1, teamName)
            strings.write(2, prefix)
            strings.write(3,suffix)
            strings.write(4,nameTagVisibility)
            getSpecificModifier(MutableCollection::class.java).write(0, mutableListOf(""))
        }
        Game.protocolManager.sendServerPacket(bound, packetFirst)
        val packetSecond = makePacket().apply {
            integers.write(0, -1)
            strings.write(0, teamName)
            strings.write(1, teamName)
            integers.write(1, MODE.REMOVE_TEAM.value)
        }
        Game.protocolManager.sendServerPacket(bound, packetSecond)
        val packetThird = makePacket().apply {
            integers.write(0, -1)
            integers.write(1, MODE.CREATE_TEAM.value)
            integers.write(2, friendlyFire)
            strings.write(0, teamName)
            strings.write(1, teamName)
            strings.write(2, prefix)
            strings.write(3,suffix)
            strings.write(4,nameTagVisibility)
            getSpecificModifier(MutableCollection::class.java).write(0, players.values)
        }
        Game.protocolManager.sendServerPacket(bound, packetThird)
    }
    // InvisiblePlayerは誰も所属していないパケットを送信する必要性がある
    fun fakeJoinPacket(bound: Player) {
        val packetFirst = makePacket()
        packetFirst.apply {
            integers.write(0, -1)
            integers.write(1, MODE.CREATE_TEAM.value)
            integers.write(2, friendlyFire)
            strings.write(0, teamName)
            strings.write(1, teamName)
            strings.write(2, prefix)
            strings.write(3,suffix)
            strings.write(4,nameTagVisibility)
            getSpecificModifier(MutableCollection::class.java).write(0, mutableListOf(""))
        }
        Game.protocolManager.sendServerPacket(bound, packetFirst)
        val packetSecond = makePacket().apply {
            integers.write(0, -1)
            strings.write(0, teamName)
            strings.write(1, teamName)
            integers.write(1, MODE.REMOVE_TEAM.value)
        }
        Game.protocolManager.sendServerPacket(bound, packetSecond)
        val packetThird = makePacket().apply {
            integers.write(0, -1)
            integers.write(1, MODE.CREATE_TEAM.value)
            integers.write(2, friendlyFire)
            strings.write(0, teamName)
            strings.write(1, teamName)
            strings.write(2, prefix)
            strings.write(3,suffix)
            strings.write(4,nameTagVisibility)
            getSpecificModifier(MutableCollection::class.java).write(0, mutableListOf(""))
        }
        Game.protocolManager.sendServerPacket(bound, packetThird)
    }

    //when need friendly fire on you have to implement damage listener
    fun createTeam(
        _prefix: String = "",
        _suffix: String = "",
        _friendlyFire: FriendlyFire? = null,
        _nameTagVisibility: NameTagInvisibility = NameTagInvisibility.ALWAYS,
        bound: List<Player>
    ) {
        prefix = _prefix
        suffix = _suffix
        nameTagVisibility = when(_nameTagVisibility) {
            NameTagInvisibility.ALWAYS -> "always"
            NameTagInvisibility.HIDE_FOR_OTHER_TEAMS -> "hideForOtherTeam"
            NameTagInvisibility.HIDE_FOR_OWN_TEAMS   -> "hideForOwnTeam"
            NameTagInvisibility.NEVER -> "never"
        }
        friendlyFire = when(_friendlyFire) {
            FriendlyFire.FALSE -> 0
            FriendlyFire.TRUE  -> 1
            FriendlyFire.FRIENDLY_INVISIBLE -> 3
            else -> {0}
        }
        val packet = makePacket()
        packet.apply {
            integers.write(0, -1); // 不明 なぜか-1なのでそれに従う
            integers.write(1, MODE.CREATE_TEAM.value) // mode
            integers.write(2,friendlyFire)
            strings.write(0, teamName) //
            strings.write(1, teamName) // display name
            strings.write(2, prefix)
            strings.write(3,suffix)
            strings.write(4,nameTagVisibility)
            getSpecificModifier(MutableCollection::class.java).write(0,players.values)
        }
        bound.forEach {
            Game.protocolManager.sendServerPacket(it, packet)
        }
    }

    fun removeTeam(bound: List<Player>) {
        val packet = makePacket()
        packet.apply {
            integers.write(0, -1)
            integers.write(1,MODE.REMOVE_TEAM.value)
            strings.write(0, teamName) //
        }
        players.clear()
        bound.forEach {
            Game.protocolManager.sendServerPacket(it, packet)
        }
    }

    fun updateTeam(bound: List<Player>) {
        val packet = makePacket()
        packet.apply {
            integers.write(0, -1); // 不明 なぜか-1なのでそれに従う
            strings.write(0, teamName) //
            integers.write(1, MODE.UPDATE_TEAM.value) // mode
            integers.write(2, friendlyFire)
            strings.write(1, teamName)
            strings.write(2, prefix)
            strings.write(3, suffix)
            strings.write(4, nameTagVisibility)
            getSpecificModifier(MutableCollection::class.java).write(0, players.values)
        }
        bound.forEach {
            Game.protocolManager.sendServerPacket(it, packet)
        }
    }

    fun addPlayer(player: Player, bound: List<Player>) {
        val uuid = player.uniqueId
        players[uuid] = player.name
        val packet = makePacket()
        packet.apply {
            integers.write(0,-1)
            integers.write(1, MODE.ADD_PLAYER.value)
            strings.write(0, teamName) //
            getSpecificModifier(MutableCollection::class.java).write(0, listOf(player.name).toMutableList())
        }
        bound.forEach {
            Game.protocolManager.sendServerPacket(it, packet)
        }
    }

    fun removePlayer(player: Player, bound: List<Player>) {
        val uuid = player.uniqueId
        players.remove(uuid)
        val packet = makePacket()
        packet.apply {
            integers.write(0,-1)
            integers.write(1, MODE.REMOVE_PLAYER.value)
            strings.write(0, teamName) //
            getSpecificModifier(MutableCollection::class.java).write(0, listOf(player.name).toMutableList())
        }
        bound.forEach {
            Game.protocolManager.sendServerPacket(it, packet)
        }
    }

    fun fakeRemovePlayer(player: Player, bound: List<Player>) {
        val packet = makePacket()
        packet.apply {
            integers.write(0,-1)
            integers.write(1, MODE.REMOVE_PLAYER.value)
            strings.write(0, teamName) //
            getSpecificModifier(MutableCollection::class.java).write(0, listOf(player.name).toMutableList())
        }
        bound.forEach {
            Game.protocolManager.sendServerPacket(it, packet)
        }
    }

    fun fakeAddPlayer(player: Player, bound: List<Player>) {
        val packet = makePacket()
        packet.apply {
            integers.write(0,-1)
            integers.write(1, MODE.ADD_PLAYER.value)
            strings.write(0, teamName) //
            getSpecificModifier(MutableCollection::class.java).write(0, listOf(player.name).toMutableList())
        }
        bound.forEach {
            Game.protocolManager.sendServerPacket(it, packet)
        }
    }
}
