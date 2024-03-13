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
