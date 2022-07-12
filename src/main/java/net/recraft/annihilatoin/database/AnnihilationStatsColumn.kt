package net.recraft.annihilatoin.database

enum class AnnihilationStatsColumn {
    COIN,
    TIMES_PLAYED,
    WINS,
    LOSES,
    BOSS_KILLS,
    MELEE_KILLS,
    KILLS,
    DEATHS,
    NEXUS_DAMAGED,
    BLOCKS_BROKEN,
    ORES_MINED,
    GAPPLES_CONSUMED,
    GOLDS_USED,
    EXPS_GAINED;

    override fun toString(): String {
        return name.toLowerCase()
    }
}