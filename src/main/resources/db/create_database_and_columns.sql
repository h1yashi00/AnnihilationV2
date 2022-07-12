create database if not exists recraft character set utf8 collate utf8_general_ci;

use recraft

CREATE TABLE if not exists  staff_rank (
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


create table if not exists playerdata (
    player_uuid  char(32) not null,
    current_name varchar(13) not null,
    first_join   timestamp not null,
    last_join    timestamp not null,
    last_logout  timestamp not null,
    rank_id      smallint not null,
    primary key (player_uuid),
    foreign key (rank_id) references staff_rank(rank_id),
    foreign key (annihilatoin_stats) annihilaton_stats(annihilaton_stats_id)
);

create table if not exists annihilation_stats (
     player_uuid  char(32) not null,
     coin           int default 0,
     times_played   int default 0,
     wins           int default 0,
     loses          int default 0,
     boss_kills     int default 0,
     melee_kills    int default 0,
     kills          int default 0,
     deaths         int default 0,
     nexus_damaged  int default 0,
     blocks_broken  int default 0,
     ores_mined      int default 0,
     gapples_consumed int default 0,
     golds_used       int default 0,
     exps_gained     int default 0,
     primary KEY (player_uuid)
)