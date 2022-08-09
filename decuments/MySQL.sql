-- iqnoon.t_user definition


CREATE TABLE `iqnoon`.`t_user` (
  `user_id` varchar(16) NOT NULL COMMENT '用户ID',
  `user_name` varchar(20) NOT NULL COMMENT '用户名称',
  `password` varchar(20) NOT NULL COMMENT '密码',
  `telephone` varchar(11) NOT NULL COMMENT '电话号码',
  `age` tinyint DEFAULT NULL COMMENT '用户年龄',
  `gender` char(8) DEFAULT NULL COMMENT '用户性别：1-男，0-女',
  `user_type` char(10) NOT NULL DEFAULT 'COMMON' COMMENT '用户类型：COMMON-普通用户，TEMPORARY-临时用户',
  `we_chat` varchar(28) DEFAULT NULL COMMENT '微信号',
  `id_card_number` varchar(19) DEFAULT NULL COMMENT '身份证号',
  `birthday` date DEFAULT NULL COMMENT '出生年月日',
  `email` varchar(64) DEFAULT NULL COMMENT '邮箱',
  `user_status` char(1) NOT NULL DEFAULT '1' COMMENT '用户状态（0-禁用，1-启用）',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_by` varchar(16) NOT NULL COMMENT '创建人ID',
  `update_time` datetime DEFAULT NULL COMMENT '最近更新时间',
  `update_by` varchar(16) DEFAULT NULL COMMENT '最近更新人ID',
  `delete_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记（0-未删除，1-删除）',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='用户基础信息表';

-- iqnoon.t_role definition

CREATE TABLE `iqnoon`.`t_role` (
  `role_id` varchar(16) NOT NULL COMMENT '角色ID',
  `role_name` varchar(20) NOT NULL COMMENT '角色名称',
  `parent_id` varchar(20) DEFAULT NULL COMMENT '上一级角色ID',
  `description` varchar(64) DEFAULT NULL COMMENT '说明描述',
  `role_type` char(10) NOT NULL DEFAULT 'COMMON' COMMENT '角色类型：COMMON-普通角色,TEMPORARY-临时角色,VIRTUAL-虚拟角色',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_by` varchar(16) NOT NULL COMMENT '创建人ID',
  `update_time` datetime DEFAULT NULL COMMENT '最近更新时间',
  `expiry_time` datetime DEFAULT NULL COMMENT '失效时间(临时角色有值)',
  `update_by` varchar(16) DEFAULT NULL COMMENT '最近更新人ID',
  `delete_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记（0-未删除，1-删除）',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='角色基础信息表';


-- iqnoon.t_permission definition

CREATE TABLE `iqnoon`.`t_permission` (
  `permission_id` varchar(16) NOT NULL COMMENT '权限ID',
  `permission_name` varchar(20) NOT NULL COMMENT '权限名称',
  `parent_id` varchar(20) DEFAULT NULL COMMENT '上一级权限ID',
  `description` varchar(64) DEFAULT NULL COMMENT '说明描述',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_by` varchar(16) NOT NULL COMMENT '创建人ID',
  `update_time` datetime DEFAULT NULL COMMENT '最近更新时间',
  `update_by` varchar(16) DEFAULT NULL COMMENT '最近更新人ID',
  `delete_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记（0-未删除，1-删除）',
  PRIMARY KEY (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='权限基础信息表';


CREATE TABLE `iqnoon`.`t_address` (
  `address_id` varchar(16) NOT NULL COMMENT '地址ID',
  `address_name` varchar(16) NOT NULL COMMENT '地址名称',
  `address_type` char(10) NOT NULL COMMENT '地址类型',
  `parent_id` varchar(20) DEFAULT NULL COMMENT '上一级地址ID',
  `description` varchar(64) DEFAULT NULL COMMENT '说明描述',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_by` varchar(16) NOT NULL COMMENT '创建人ID',
  `update_time` datetime DEFAULT NULL COMMENT '最近更新时间',
  `update_by` varchar(16) DEFAULT NULL COMMENT '最近更新人ID',
  `delete_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记（0-未删除，1-删除）',
  PRIMARY KEY (`address_id`),
  KEY `idx_parent_id` (`parent_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='基础地址信息表';