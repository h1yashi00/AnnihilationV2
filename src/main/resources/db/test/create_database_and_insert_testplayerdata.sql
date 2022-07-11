-- narikake
insert into playerdata values (
    '14687d5d039949dbb3023cce4f47bc86',
    'Narikake',
    NOW(),
    NOW(),
    NOW(),
    1
)
insert into annihilation_stats (player_uuid) values (
    '14687d5d039949dbb3023cce4f47bc86'
)

---- hankake
--insert into playerdata values (
--    '372dc1e630b34201a5e869f690483bae'
--);
--
--select * from playerdata;
--
---- annihilation kits
--DROP TABLE IF EXISTS annihilatoin_kits;
--create table annihilaton_kits (
--    id smallint NOT NULL AUTO_INCREMENT,
--    name CHAR(30) NOT NULL,
--    PRIMARY KEY (id)
--)
--
--create table annihilatoin_stats (
--    player_uuid  char(32) not null primary key,
--    kills        long not null,
--    bow_kills    unsigned int not null,
--    nexus_damage unsigned int not null,
--)