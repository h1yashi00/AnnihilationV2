package net.recraft.annihilatoin.database

enum class StaffRank(val rank_id: Int) {
    NONE(1),
    OWNER(2),
    MAPPER(3),
    DEVELOPER(4),
    MANAGER(5),
    YOUTUBER(6);
    companion object {
        fun get(rank_id: Int): StaffRank? {
            values().forEach {
                if (rank_id == it.rank_id)
                    return it
            }
            return null
        }
    }
}