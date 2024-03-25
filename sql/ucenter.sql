drop table if exists `user`;
create table `user` (
  `id` bigint not null comment 'id',
  `mobile` varchar(11) comment '手机号',
  `uname` varchar(32) comment '用户名',
  `password` varchar(32) comment '密码',
  `gmt_create` datetime(3) comment '新增时间',
  `gmt_modified` datetime(3) comment '修改时间',
  primary key (`id`),
  unique key `mobile_unique` (`mobile`)
) engine=innodb default charset=utf8mb4 comment='会员';


drop table if exists `passenger`;
create table `passenger` (
  `id` bigint not null comment 'id',
  `user_id` bigint not null comment '会员id',
  `name` varchar(20) not null comment '姓名',
  `id_card` varchar(18) not null comment '身份证',
  `type` char(1) not null comment '旅客类型|枚举[PassengerTypeEnum]',
  `gmt_create` datetime(3) comment '新增时间',
  `gmt_modified` datetime(3) comment '修改时间',
  primary key (`id`),
  index `use_id_index` (`user_id`)
) engine=innodb default charset=utf8mb4 comment='乘车人';


drop table if exists `ticket`;
create table `ticket` (
  `id` bigint not null comment 'id',
  `user_id` bigint not null comment '会员id',
  `passenger_id` bigint not null comment '乘客id',
  `passenger_name` varchar(20) comment '乘客姓名',
  `train_date` date not null comment '日期',
  `train_code` varchar(20) not null comment '车次编号',
  `carriage_index` int not null comment '箱序',
  `seat_row` char(2) not null comment '排号|01, 02',
  `seat_col` char(1) not null comment '列号|枚举[SeatColEnum]',
  `start_station` varchar(20) not null comment '出发站',
  `start_time` time not null comment '出发时间',
  `end_station` varchar(20) not null comment '到达站',
  `end_time` time not null comment '到站时间',
  `seat_type` char(1) not null comment '座位类型|枚举[SeatTypeEnum]',
  `gmt_create` datetime(3) comment '新增时间',
  `gmt_modified` datetime(3) comment '修改时间',
  primary key (`id`),
  index `user_id_index` (`user_id`)
) engine=innodb default charset=utf8mb4 comment='车票';

drop table if exists `undo_log`;
CREATE TABLE IF NOT EXISTS `undo_log`
(
    `branch_id`     BIGINT       NOT NULL COMMENT 'branch transaction id',
    `xid`           VARCHAR(128) NOT NULL COMMENT 'global transaction id',
    `context`       VARCHAR(128) NOT NULL COMMENT 'undo_log context,such as serialization',
    `rollback_info` LONGBLOB     NOT NULL COMMENT 'rollback info',
    `log_status`    INT(11)      NOT NULL COMMENT '0:normal status,1:defense status',
    `log_created`   DATETIME(6)  NOT NULL COMMENT 'create datetime',
    `log_modified`  DATETIME(6)  NOT NULL COMMENT 'modify datetime',
    UNIQUE KEY `ux_undo_log` (`xid`, `branch_id`)
    ) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COMMENT ='AT transaction mode undo table';
ALTER TABLE `undo_log` ADD INDEX `ix_log_created` (`log_created`);
