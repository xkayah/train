drop table if exists `station`;
create table `station` (
  `id` bigint not null comment 'id',
  `name` varchar(20) not null comment '站名',
  `name_pinyin` varchar(32) not null comment '站名拼音',
  `name_py` varchar(32) not null comment '站名拼音首字母',
  `gmt_create` datetime(3) comment '新增时间',
  `gmt_modified` datetime(3) comment '修改时间',
  primary key (`id`),
  unique key `name_unique` (`name`)
) engine=innodb default charset=utf8mb4 comment='车站';
