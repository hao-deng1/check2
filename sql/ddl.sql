# 第一次执行
create database find_lost;

use find_lost;

# 用户表
create table user
(
    id           bigint auto_increment comment 'id' primary key,
    username     varchar(256)       null comment '用户昵称',
    userAccount  varchar(256)       null comment '账号',
    avatarUrl    varchar(1024)      null comment '用户头像',
    gender       tinyint            null comment '性别',
    userPassword varchar(512)       not null comment '密码',
    phone        varchar(128)       null comment '电话',
    email        varchar(512)       null comment '邮箱',
    userStatus   int      default 0 not null comment '状态 0 - 正常',
    createTime   datetime default CURRENT_TIMESTAMP comment '创建时间',
    updateTime   datetime default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
    isDelete     tinyint  default 0 not null comment '是否删除',
    userRole     int      default 0 not null comment '用户角色 0 - 普通用户 1 - 管理员'
) ENGINE=INNODB DEFAULT CHARSET=utf8 COLLATE=utf8_bin comment '用户';
# 用户
create table user
(
    id     int auto_increment comment 'id' primary key,
    userAccount  varchar(256)       null comment '账号',
    userPassword    varchar(256)       null comment '密码',
    username    varchar(256)       null comment '用户名',
    gender       tinyint            null comment '性别',
    phone    varchar(256)       null comment '联系电话',
    email       varchar(256)       null comment '邮箱',
    avatarUrl   varchar(1024)      null comment '头像',
    createTime   datetime default CURRENT_TIMESTAMP comment '创建时间',
    userStatus   int      default 0 not null comment '状态 0 - 正常',
    Integral    int null comment '积分',
    temporary   int null comment '暂存积分',
    isDelete    tinyint  default 0 not null comment '是否删除',
    userRole     int      default 0 not null comment '用户角色 0 - 普通用户 1 - 管理员'
) ENGINE=INNODB DEFAULT CHARSET=utf8 COLLATE=utf8_bin  comment '用户表';
# 招领物品表
DROP TABLE IF EXISTS `pick`;
create table pick
(
    userId     int  comment 'userId',
    id     bigint(20)  auto_increment comment 'id' primary key,
    pickName   varchar(45)       null comment '失物名称',
    pickDate   datetime default CURRENT_TIMESTAMP comment '捡拾时间',
    pickPlace  varchar(128)       null comment '捡拾地点',
    contact     varchar(45)       null comment '联系电话',
    reportTime   datetime default CURRENT_TIMESTAMP comment '上报时间',
    description varchar(512)     null comment '物品描述',
    isDelete    tinyint  default 0 not null comment '是否删除',
    img  varchar(512) null comment '物品图片',
    status char(1) DEFAULT '0' COMMENT '订单状态（0进行 1完成）'
)ENGINE=INNODB DEFAULT CHARSET=utf8 COLLATE=utf8_bin comment '招领物品表';
DROP TABLE IF EXISTS `lost`;

# 丢失物品表
create table lost
(
    userId     int  comment 'userId',
    id     bigint(20)  auto_increment comment 'id' primary key,
    lostName   varchar(45)       null comment '报失名称',
    lostDate   datetime default CURRENT_TIMESTAMP comment '丢失时间',
    contact     varchar(45)       null comment '联系电话',
    reportTime   datetime default CURRENT_TIMESTAMP comment '报失时间',
    description varchar(512)     null comment '物品描述',
    isDelete    tinyint  default 0 not null comment '是否删除',
    img  varchar(512) null comment '物品图片',
    business double default 0 null comment '赏金',
    status char(1) DEFAULT '0' COMMENT '订单状态（0进行 1完成）',
    urgent double COMMENT '紧急程度'
)ENGINE=INNODB DEFAULT CHARSET=utf8 COLLATE=utf8_bin comment '丢失物品表';

DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
                            `id` bigint(20) NOT NULL AUTO_INCREMENT,
                            `menuName` varchar(64) NOT NULL DEFAULT 'NULL' COMMENT '菜单名',
                            `path` varchar(200) DEFAULT NULL COMMENT '路由地址',
                            `component` varchar(255) DEFAULT NULL COMMENT '组件路径',
                            `visible` char(1) DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
                            `status` char(1) DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
                            `perms` varchar(100) DEFAULT NULL COMMENT '权限标识',
                            `parentId` bigint(20) COMMENT '父节点',
                            `createBy` bigint(20) DEFAULT NULL,
                            `createTime` datetime DEFAULT NULL,
                            `updateBy` bigint(20) DEFAULT NULL,
                            `updateTime` datetime DEFAULT NULL,
                            `isDelete` int(11) DEFAULT '0' COMMENT '是否删除（0未删除 1已删除）',
                            `remark` varchar(500) DEFAULT NULL COMMENT '备注',
                            PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='菜单表';

/*Table structure for table `sys_role` */

DROP TABLE IF EXISTS `sys_role`;

CREATE TABLE `sys_role` (
                            `id` bigint(20) NOT NULL AUTO_INCREMENT,
                            `name` varchar(128) DEFAULT NULL,
                            `role_key` varchar(100) DEFAULT NULL COMMENT '角色权限字符串',
                            `status` char(1) DEFAULT '0' COMMENT '角色状态（0正常 1停用）',
                            `del_flag` int(1) DEFAULT '0' COMMENT 'del_flag',
                            `create_by` bigint(200) DEFAULT NULL,
                            `create_time` datetime DEFAULT NULL,
                            `update_by` bigint(200) DEFAULT NULL,
                            `update_time` datetime DEFAULT NULL,
                            `remark` varchar(500) DEFAULT NULL COMMENT '备注',
                            PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

/*Table structure for table `sys_role_menu` */

DROP TABLE IF EXISTS `sys_role_menu`;

CREATE TABLE `sys_role_menu` (
                                 `id` bigint(20) NOT NULL AUTO_INCREMENT primary key,
                                 `role_id` bigint(200) NOT NULL COMMENT '角色ID',
                                 `menu_id` bigint(200) NOT NULL DEFAULT '0' COMMENT '菜单id'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `tag`;
-- auto-generated definition
create table tag
(
    id       bigint auto_increment
        primary key,
    tagName  varchar(512)  not null,
    isDelete int default 0 null,
    parentId       bigint
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='标签表';

-- 登录日志
create table sys_log_login
(
    id                   bigint NOT NULL COMMENT 'id',
    operation            tinyint unsigned COMMENT '用户操作   0：用户登录   1：用户退出',
    status               tinyint unsigned NOT NULL COMMENT '状态  0：失败    1：成功    2：账号已锁定',
    userAgent           varchar(500) COMMENT '用户代理',
    ip                   varchar(32) COMMENT '操作IP',
    creatorName         varchar(50) COMMENT '用户名',
    creator              bigint COMMENT '创建者',
    createDate          varchar(50) COMMENT '创建时间',
    primary key (id),
    key idx_status (status),
    key idx_create_date (createDate)
)ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COMMENT='登录日志';

-- 操作日志
create table sys_log_operation
(
    id                   bigint NOT NULL COMMENT 'id',
    operation            varchar(50) COMMENT '用户操作',
    requestUri          varchar(200) COMMENT '请求URI',
    requestMethod       varchar(20) COMMENT '请求方式',
    requestParams       text COMMENT '请求参数',
    requestTime         int unsigned NOT NULL COMMENT '请求时长(毫秒)',
    userAgent           varchar(500) COMMENT '用户代理',
    ip                   varchar(32) COMMENT '操作IP',
    status               tinyint unsigned NOT NULL COMMENT '状态  0：失败   1：成功',
    creatorName         varchar(50) COMMENT '用户名',
    creator              bigint COMMENT '创建者',
    createDate          varchar(50) COMMENT '创建时间',
    primary key (id),
    key idx_create_date (createDate)
)ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COMMENT='操作日志';

-- 异常日志
create table sys_log_error
(
    id                   bigint NOT NULL COMMENT 'id',
    requestUri          varchar(200) COMMENT '请求URI',
    requestMethod       varchar(20) COMMENT '请求方式',
    requestParams       text COMMENT '请求参数',
    userAgent           varchar(500) COMMENT '用户代理',
    ip                   varchar(32) COMMENT '操作IP',
    errorInfo           text COMMENT '异常信息',
    creator              bigint COMMENT '创建者',
    createDate          varchar(50) COMMENT '创建时间',
    primary key (id),
    key idx_create_date (createDate)
)ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COMMENT='异常日志';


DROP TABLE IF EXISTS `sys_user_role`;

CREATE TABLE `sys_user_role` (
                                 `id` bigint(20) NOT NULL AUTO_INCREMENT primary key,
                                 `user_id` bigint(200) NOT NULL  COMMENT '用户id',
                                 `role_id` bigint(200) NOT NULL DEFAULT '0' COMMENT '角色id'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



DROP TABLE IF EXISTS `tag_rel`;

CREATE TABLE `tag_rel` (
                                 `id` bigint(20) NOT NULL AUTO_INCREMENT primary key,
                                 `type` char (1) NOT NULL  COMMENT '0为lost，1为pick',
                                 `thingId`bigint NOT NULL  COMMENT '物品id',
                                 `tagId` bigint NOT NULL  COMMENT '标签id'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
SELECT
    DISTINCT m.`perms`
FROM
    sys_user_role ur
        LEFT JOIN `sys_role` r ON ur.roleId = r.`id`
        LEFT JOIN `sys_role_menu` rm ON ur.roleId = rm.`role_id`
        LEFT JOIN `sys_menu` m ON m.`id` = rm.`menu_id`
WHERE
        user_id = 1
  AND r.`status` = 0
  AND m.`status` = 0

