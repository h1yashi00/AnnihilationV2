create database if not exists recraft character set utf8 collate utf8_general_ci;

use recraft

create table if not exists  staff_rank (
     rank_id smallint NOT NULL AUTO_INCREMENT,
     name CHAR(30) NOT NULL,
     PRIMARY KEY (rank_id)
);

insert into staff_rank (name) values
    ('None'),
    ('Owner'),
    ('Mapper'),
    ('Developer'),
    ('Manager'),
    ('YouTuber');

create table if not exists annihilation_stats (
     player_uuid    uuid not null,
     coin           int default 0,
     times_played   int default 0,
     wins           int default 0,
     loses          int default 0,
     boss_kills     int default 0,
     melee_kills    int default 0,
     bow_kills      int default 0,
     kills          int default 0,
     deaths         int default 0,
     nexus_damaged  int default 0,
     blocks_broken  int default 0,
     ores_mined     int default 0,
     blocks_placed  int default 0,
     gapples_consumed int default 0,
     golds_used       int default 0,
     exps_gained     int default 0,
     primary key (player_uuid)
)

create table if not exists playerdata (
    player_uuid  uuid not null,
    current_name varchar(16) not null,
    first_join   timestamp not null,
    last_join    timestamp not null,
    last_logout  timestamp not null,
    rank_id      smallint not null,
    primary key (player_uuid),
    foreign key (rank_id) references staff_rank(rank_id)
);

--
--create table if not exists player_kill_history (
--    game_history_id smallint not null
--    killed_time timestamp not null
--    killer UUID not null
--    victim UUID not null
--    foreign key (game_history_id) references game_history(game_history_id)
--    foreign key (killer) references playerdata(player_uuid)
--    foreign key (victim) references playerdata(player_uuid)
--)
--
--create table if not exists player_nexus_damaged (
--    game_history_id smallint not null
--    killed_time timestamp not null
--    damage smallint not null
--    damager UUID not null
--    victim_team UUID not null
--)
--
--create table if not exists boss_kill_hisotry (
--    game_history_id smallint not null
--    killed_time timestamp not null
--    foreign key (game_history_id) references game_history(game_history_id)
--)
--
--create table if not exists teamColor (
--)
--
--create table if not exists team (
--    game_history_id smallint not null,
--    color_id        smallint not null,
--)
--
--create table if not exists game_history (
--     game_history_id smallint NOT NULL AUTO_INCREMENT,
--     start_time      timestamp not null default current_timestamp,
--     finish_time     timestamp not null default current_timestamp,
--     map             map_id    not null,
--     booster         booster_id not null,
--     win smallint not null;
--)
