package net.recraft.annihilatoin.database

import org.junit.Test
import java.util.*

internal class DatabaseTest {
    private val url = "jdbc:mysql://localhost/recraft?useSSL=false"
    private val user = "root"
    private val password = "narikakeisgod"

    // Narikakeのデータがすでに保存されている状態でテストする必要がある
    @Test
    fun getPlayerStatsTest() {
        Database.connect(url, user, password)
        val uuid = UUID.fromString("14687d5d-0399-49db-b302-3cce4f47bc86")
        val playerStats = Database.getPlayerStatus(uuid)
        println(playerStats.totalStats)
    }
}