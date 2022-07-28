-- ----------------------------
-- Table structure for device_dept_rel
-- ----------------------------
DROP TABLE IF EXISTS `device_dept_rel`;
CREATE TABLE `device_dept_rel`  (
                                    `dept_id` int(10) UNSIGNED NOT NULL COMMENT '主键，部门id',
                                    `dept_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '部门名称',
                                    `dept_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备id',
                                    `create_time` datetime(0) NOT NULL COMMENT '创建时间',
                                    PRIMARY KEY (`dept_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of device_dept_rel
-- ----------------------------
INSERT INTO `device_dept_rel` VALUES (53000, '电信易通', '电信易通', '2022-07-11 17:46:57');
INSERT INTO `device_dept_rel` VALUES (53222, '你瞅啥', '电信易通>你瞅啥', '2022-07-11 17:50:19');

-- ----------------------------
-- Table structure for device_info
-- ----------------------------
DROP TABLE IF EXISTS `device_info`;
CREATE TABLE `device_info`  (
                                `device_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
                                `device_mid` int(11) NOT NULL COMMENT '设备型号ID',
                                `user_id` int(11) NOT NULL COMMENT '所属用户ID',
                                `user_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '所属用户名',
                                `mode_type` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备模式',
                                `dept_id` int(11) NOT NULL COMMENT '所属机构ID',
                                `phone` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '手机号码',
                                `pin` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '手机PIN码',
                                `imei` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '绑定设备（IMEI）',
                                `imsi` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户识别码',
                                `meid` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '设备识别码',
                                `register` tinyint(1) NOT NULL DEFAULT 1 COMMENT '注册状态：0-未注册；1-已注册；2-撤销注册',
                                `rule` tinyint(1) NOT NULL DEFAULT 1 COMMENT '策略状态：0-指定策略；1-动态策略',
                                `creator` int(11) NOT NULL COMMENT '添加人',
                                `system` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '无' COMMENT '系统版本号',
                                `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态：0-注销；1-正常；2-禁用 3-注销中',
                                `regist_type` tinyint(1) NOT NULL DEFAULT 0 COMMENT '开通方式 0-服务端开通，1-客户端注册',
                                `updated_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
                                `created_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录时间',
                                PRIMARY KEY (`device_id`) USING BTREE,
                                INDEX `normal_index`(`imei`) USING BTREE,
                                INDEX `idx_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 185 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of device_info
-- ----------------------------
INSERT INTO `device_info` VALUES (182, 6, 33142, 'BUG', 'life', 53000, '15788889999', '000000', '314543654655765', '314543654655765', '00000000000000', 1, 1, 1, '无', 1, 0, '2022-07-19 10:36:07', '2022-07-11 17:46:56');
INSERT INTO `device_info` VALUES (183, 6, 33141, 'adasdasd', 'office', 53222, '13500000000', '123456', '000039485642710', '000039485642710', '000039485642710', 1, 1, 1, '无', 1, 0, '2022-07-12 13:29:51', '2022-07-11 17:50:19');
INSERT INTO `device_info` VALUES (184, 6, 33155, '产品', 'life', 53000, '18010101111', '000000', '868463050079251', '868463050079251', '00000000000000', 1, 1, 1, '无', 1, 0, '2022-07-20 14:13:32', '2022-07-20 14:13:32');

-- ----------------------------
-- Table structure for device_model
-- ----------------------------
DROP TABLE IF EXISTS `device_model`;
CREATE TABLE `device_model`  (
                                 `device_mid` int(11) NOT NULL AUTO_INCREMENT COMMENT '设备型号ID',
                                 `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '终端名称',
                                 `terminal_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '终端型号',
                                 `company` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备制造商',
                                 `cpu_speed` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '处理器速度',
                                 `cpu_model` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '处理器型号',
                                 `cpu_core` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '处理器核心数',
                                 `bluetooth` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '蓝牙模块版本',
                                 `wifi` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Wi-Fi模块版本',
                                 `ram` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '运行存储',
                                 `rom` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '机身存储',
                                 `status` tinyint(1) NOT NULL COMMENT '状态:0-已删除,1-正常',
                                 `updated_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
                                 `created_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录时间',
                                 PRIMARY KEY (`device_mid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 34 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '设备型号表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of device_model
-- ----------------------------
INSERT INTO `device_model` VALUES (6, 'A31 PRO', 'ZTE A2022H', 'ZTE', '', '', '', '', '', '', '', 1, '2022-04-09 13:48:11', '2022-04-09 13:48:11');
INSERT INTO `device_model` VALUES (7, '荣耀magic4 至臻', '荣耀magic4 至臻', '华为', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 1, '2022-07-20 14:35:23', '2022-07-20 14:35:23');
INSERT INTO `device_model` VALUES (8, '荣耀magic Pro', '荣耀magic Pro', '华为', '', '', '', '', '', '', '', 1, '2022-04-09 13:48:11', '2022-04-09 13:48:11');

-- ----------------------------
-- Table structure for cont_app_audit
-- ----------------------------
DROP TABLE IF EXISTS `cont_app_audit`;
CREATE TABLE `cont_app_audit`  (
                                   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
                                   `user_id` int(11) NOT NULL COMMENT '人员id',
                                   `dept_ids` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '部门Id',
                                   `create_time` datetime(0) NOT NULL COMMENT '创建时间',
                                   `update_time` datetime(0) NOT NULL COMMENT '更新时间',
                                   PRIMARY KEY (`id`) USING BTREE,
                                   INDEX `idx_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '审核权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for cont_audit_info
-- ----------------------------
DROP TABLE IF EXISTS `cont_audit_info`;
CREATE TABLE `cont_audit_info`  (
                                    `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
                                    `video_id` int(11) NOT NULL COMMENT '短视频ID',
                                    `publisher` int(11) NOT NULL COMMENT '视频发布者id',
                                    `user_id` int(11) NOT NULL COMMENT '审核人ID',
                                    `dept_id` int(11) NULL DEFAULT NULL COMMENT '审核人所属部门',
                                    `audit_status` tinyint(1) NOT NULL COMMENT '审核状态 1-审核通过、2-审核驳回',
                                    `reject_reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '驳回原因',
                                    `audit_time` datetime(0) NULL DEFAULT NULL COMMENT '审核时间',
                                    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '审核记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for cont_category
-- ----------------------------
DROP TABLE IF EXISTS `cont_category`;
CREATE TABLE `cont_category`  (
                                  `cate_id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键，分类id',
                                  `cate_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '专栏名称',
                                  `sort` tinyint(3) UNSIGNED NOT NULL DEFAULT 1 COMMENT '排序，从小到大',
                                  `user_id` int(11) NOT NULL COMMENT '添加人员id',
                                  `deleted` tinyint(3) NOT NULL DEFAULT 1 COMMENT '删除标识 1正常 2 删除',
                                  `create_time` datetime(0) NOT NULL COMMENT '添加时间',
                                  `update_time` datetime(0) NOT NULL COMMENT '更新时间',
                                  PRIMARY KEY (`cate_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '视频栏目表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for cont_online_feedback
-- ----------------------------
DROP TABLE IF EXISTS `cont_online_feedback`;
CREATE TABLE `cont_online_feedback`  (
                                         `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
                                         `feedback_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '反馈内容',
                                         `user_id` int(11) NOT NULL COMMENT '反馈者ID',
                                         `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '回复状态 1-未回复，2-已回复，默认1',
                                         `create_time` datetime(0) NOT NULL COMMENT '创建时间',
                                         `update_time` datetime(0) NOT NULL COMMENT '修改时间',
                                         PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '在线反馈表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for cont_online_feedback_file
-- ----------------------------
DROP TABLE IF EXISTS `cont_online_feedback_file`;
CREATE TABLE `cont_online_feedback_file`  (
                                              `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
                                              `feedback_id` int(11) NOT NULL COMMENT '在线反馈ID',
                                              `file_url` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件url',
                                              `create_time` datetime(0) NOT NULL COMMENT '创建时间',
                                              `update_time` datetime(0) NOT NULL COMMENT '修改时间',
                                              PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '在线反馈表图片' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for cont_online_feedback_reply
-- ----------------------------
DROP TABLE IF EXISTS `cont_online_feedback_reply`;
CREATE TABLE `cont_online_feedback_reply`  (
                                               `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
                                               `feedback_id` int(11) NOT NULL COMMENT '在线反馈ID',
                                               `reply_content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '回复内容',
                                               `creator` int(11) NOT NULL COMMENT '回复人员',
                                               `create_time` datetime(0) NOT NULL COMMENT '回复时间',
                                               PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '反馈回附表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for cont_video
-- ----------------------------
DROP TABLE IF EXISTS `cont_video`;
CREATE TABLE `cont_video`  (
                               `video_id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键，视频id',
                               `cate_id` int(11) UNSIGNED NOT NULL COMMENT '分类id',
                               `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '视频标题',
                               `introduction` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '简介',
                               `video_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '视频的云端地址',
                               `video_md5` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '视频md5',
                               `picture_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '图片的云端地址',
                               `publisher` int(11) NOT NULL COMMENT '发布者',
                               `dept_id` int(11) UNSIGNED NOT NULL COMMENT '视频所属部门id',
                               `audit_status` tinyint(3) NOT NULL DEFAULT 0 COMMENT '审核状态 0-待审核、1-通过、2-驳回',
                               `commented` tinyint(3) NOT NULL DEFAULT 1 COMMENT '是否可被评论  1 默认 可以 2 不可',
                               `visualed` tinyint(3) NOT NULL DEFAULT 1 COMMENT '是否可见评论  1 默认 可以  2 不可',
                               `like_count` int(11) NOT NULL DEFAULT 0 COMMENT '喜欢数',
                               `collect_count` int(11) NOT NULL DEFAULT 0 COMMENT '收藏数',
                               `comment_count` int(11) NOT NULL DEFAULT 0 COMMENT '评论数',
                               `skim_count` int(11) NOT NULL DEFAULT 0 COMMENT '浏览数',
                               `total_count` int(11) NULL DEFAULT 0 COMMENT '总数量数',
                               `skim_time` bigint(20) NOT NULL DEFAULT 0 COMMENT '浏览时长  ',
                               `top_status` tinyint(3) NOT NULL DEFAULT 1 COMMENT '置顶状态  默认1 不置顶 2置顶',
                               `top_time` date NULL DEFAULT NULL COMMENT '置顶结束时间',
                               `deleted` tinyint(3) NOT NULL DEFAULT 1 COMMENT '删除标识 1正常 2 删除',
                               `create_time` datetime(0) NOT NULL COMMENT '添加时间',
                               `update_time` datetime(0) NOT NULL COMMENT '更新时间',
                               PRIMARY KEY (`video_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 73 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '短视频表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for cont_video_collect
-- ----------------------------
DROP TABLE IF EXISTS `cont_video_collect`;
CREATE TABLE `cont_video_collect`  (
                                       `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
                                       `video_id` int(11) UNSIGNED NOT NULL COMMENT '短视频ID',
                                       `publisher` int(11) NOT NULL COMMENT '发布者ID',
                                       `collector` int(11) NOT NULL COMMENT '收藏者ID',
                                       `create_time` datetime(0) NOT NULL COMMENT '添加时间',
                                       `update_time` datetime(0) NOT NULL COMMENT '更新时间',
                                       PRIMARY KEY (`id`) USING BTREE,
                                       UNIQUE INDEX `uni_key`(`video_id`, `collector`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '收藏信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for cont_video_comment
-- ----------------------------
DROP TABLE IF EXISTS `cont_video_comment`;
CREATE TABLE `cont_video_comment`  (
                                       `comment_id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
                                       `video_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '短视频ID',
                                       `publisher` int(11) NOT NULL DEFAULT 0 COMMENT '视频发布者id',
                                       `cid` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '被评论ID',
                                       `comment_path` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '评论路径',
                                       `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '评论内容',
                                       `commentator` int(11) NOT NULL COMMENT '评论者ID',
                                       `replier` int(11) NOT NULL DEFAULT 0 COMMENT '回复人员',
                                       `like_count` int(11) NOT NULL DEFAULT 0 COMMENT '获赞数',
                                       `comment_type` tinyint(3) NOT NULL COMMENT '评论对象：0-短视频、1-评论',
                                       `deleted` tinyint(3) NOT NULL DEFAULT 1 COMMENT '删除标识 1正常 2 删除',
                                       `create_time` datetime(0) NOT NULL COMMENT '添加时间',
                                       `update_time` datetime(0) NOT NULL COMMENT '更新时间',
                                       PRIMARY KEY (`comment_id`) USING BTREE,
                                       INDEX `idx_video_id`(`video_id`) USING BTREE,
                                       INDEX `idx_cid_replier`(`cid`, `replier`) USING BTREE,
                                       INDEX `idx_commentator`(`commentator`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 41 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '评论表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for cont_video_forward
-- ----------------------------
DROP TABLE IF EXISTS `cont_video_forward`;
CREATE TABLE `cont_video_forward`  (
                                       `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
                                       `video_id` int(11) NOT NULL COMMENT '短视频id',
                                       `user_id` int(11) NOT NULL COMMENT '转发人员id',
                                       `dept_id` int(11) NOT NULL COMMENT '转发人员部门',
                                       `create_time` datetime(0) NOT NULL COMMENT '创建时间',
                                       PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '转发表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for cont_video_like
-- ----------------------------
DROP TABLE IF EXISTS `cont_video_like`;
CREATE TABLE `cont_video_like`  (
                                    `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
                                    `content_id` int(11) NOT NULL COMMENT '短视频或评论ID',
                                    `publisher` int(11) NOT NULL DEFAULT 0 COMMENT '发布者',
                                    `point` int(11) NOT NULL COMMENT '喜欢者',
                                    `point_type` tinyint(3) NOT NULL COMMENT '点赞对象：0-短视频、1-评论',
                                    `create_time` datetime(0) NOT NULL COMMENT '添加时间',
                                    `update_time` datetime(0) NOT NULL COMMENT '更新时间',
                                    PRIMARY KEY (`id`) USING BTREE,
                                    INDEX `idx_content_id`(`content_id`, `point_type`) USING BTREE,
                                    INDEX `idx_nor_pont`(`point`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '喜欢信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for cont_video_trace
-- ----------------------------
DROP TABLE IF EXISTS `cont_video_trace`;
CREATE TABLE `cont_video_trace`  (
                                     `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
                                     `video_id` int(11) NOT NULL COMMENT '短视频ID',
                                     `user_id` int(11) NOT NULL COMMENT '浏览者ID',
                                     `duration` int(64) NOT NULL DEFAULT 0 COMMENT '浏览时长 单位：秒',
                                     `create_time` datetime(0) NOT NULL COMMENT '创建时间',
                                     `update_time` datetime(0) NOT NULL COMMENT '修改时间',
                                     PRIMARY KEY (`id`) USING BTREE,
                                     INDEX `idx_video_id`(`video_id`) USING BTREE COMMENT '视频id索引',
                                     INDEX `idx_user_id`(`user_id`) USING BTREE COMMENT '用户id索引'
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '足迹表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for error_log
-- ----------------------------
DROP TABLE IF EXISTS `error_log`;
CREATE TABLE `error_log`  (
                              `error_id` int(11) NOT NULL AUTO_INCREMENT,
                              `error_result` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '请求参数',
                              `error_info` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '错误信息',
                              `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                              `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
                              PRIMARY KEY (`error_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '调用错误日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for service_error_log
-- ----------------------------
DROP TABLE IF EXISTS `service_error_log`;
CREATE TABLE `service_error_log`  (
                                      `error_id` int(11) NOT NULL AUTO_INCREMENT,
                                      `error_result` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '请求参数',
                                      `error_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '请求路径',
                                      `error_info` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '错误信息',
                                      `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                      `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
                                      PRIMARY KEY (`error_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 106 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '服务内部错误日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for stat_video_scan
-- ----------------------------
DROP TABLE IF EXISTS `stat_video_scan`;
CREATE TABLE `stat_video_scan`  (
                                    `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
                                    `video_id` int(11) NOT NULL COMMENT '视频id',
                                    `dept_id` int(11) NOT NULL COMMENT '部门id',
                                    `skim_user_count` int(11) NOT NULL DEFAULT 0 COMMENT '浏览用户数量',
                                    `skim_rate` float NOT NULL DEFAULT 0 COMMENT '浏览率',
                                    `create_time` datetime(0) NOT NULL COMMENT '创建时间',
                                    PRIMARY KEY (`id`) USING BTREE,
                                    INDEX `idx_video_id`(`video_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '视频部门浏览率统计表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict`  (
                             `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
                             `key_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                             `value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                             `creator` int(11) NULL DEFAULT NULL COMMENT '创建人',
                             `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                             `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
                             PRIMARY KEY (`id`) USING BTREE,
                             UNIQUE INDEX `uniq_idx`(`key_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统参数表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_sensitive_word
-- ----------------------------
DROP TABLE IF EXISTS `sys_sensitive_word`;
CREATE TABLE `sys_sensitive_word`  (
                                       `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
                                       `sensitive_word` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '敏感词',
                                       `creator` int(11) NOT NULL COMMENT '创建人',
                                       `create_time` datetime(0) NOT NULL COMMENT '创建时间',
                                       `update_time` datetime(0) NOT NULL COMMENT '修改时间',
                                       PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '敏感词表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for third_error_log
-- ----------------------------
DROP TABLE IF EXISTS `third_error_log`;
CREATE TABLE `third_error_log`  (
                                    `error_id` int(11) NOT NULL AUTO_INCREMENT,
                                    `error_result` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '请求参数',
                                    `third_type` tinyint(2) NOT NULL COMMENT '1-用户中心 2-青牛云',
                                    `error_info` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '错误信息',
                                    `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                    `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
                                    PRIMARY KEY (`error_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 27 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '第三方调用错误日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for uc_dept
-- ----------------------------
DROP TABLE IF EXISTS `uc_dept`;
CREATE TABLE `uc_dept`  (
                            `dept_id` int(11) UNSIGNED NOT NULL COMMENT '主键，部门id',
                            `dept_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '部门名称',
                            `parent_id` int(11) NOT NULL DEFAULT 0 COMMENT '上级部门ID',
                            `dept_level` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '部门层级',
                            `dept_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '部门全路径',
                            `dept_order` int(4) NULL DEFAULT 0 COMMENT '部门排序',
                            `status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '部门状态 0 启用 1停用 2删除',
                            `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
                            PRIMARY KEY (`dept_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '部门表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for uc_user
-- ----------------------------
DROP TABLE IF EXISTS `uc_user`;
CREATE TABLE `uc_user`  (
                            `user_id` int(10) UNSIGNED NOT NULL COMMENT '主键，用户id',
                            `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户姓名',
                            `nick_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '昵称',
                            `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像',
                            `autograph` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '签名',
                            `sex` varchar(4) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '性别,男女',
                            `telephone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
                            `id_card` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '身份证号',
                            `user_no` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '工号',
                            `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '职务',
                            `like_count` int(11) NOT NULL DEFAULT 0 COMMENT '喜欢数（点赞数）',
                            `collect_count` int(11) NOT NULL DEFAULT 0 COMMENT '收藏数',
                            `video_count` int(11) NOT NULL DEFAULT 0 COMMENT '作品数',
                            `audit_auth` tinyint(3) NOT NULL DEFAULT 1 COMMENT '审核本部门视频的权限  1 默认有 2 没有',
                            `limit_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '受限类型（黑名单）默认 0-不受限、1-不能发视频\r\n2-不能发评论',
                            `limit_time` date NULL DEFAULT NULL COMMENT '受限结束时间',
                            `status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '用户状态,0启用，1停用 2 删除',
                            `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
                            PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '人员信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for uc_user_dept
-- ----------------------------
DROP TABLE IF EXISTS `uc_user_dept`;
CREATE TABLE `uc_user_dept`  (
                                 `id` int(8) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                 `user_id` int(8) NOT NULL COMMENT '用户ID',
                                 `dept_id` int(8) NOT NULL COMMENT '部门ID',
                                 `status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '用户状态 0 启用 1 停用 2 删除',
                                 `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '添加时间',
                                 PRIMARY KEY (`id`) USING BTREE,
                                 UNIQUE INDEX `uni_key`(`user_id`, `dept_id`, `status`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 418 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '人员部门关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Triggers structure for table cont_video
-- ----------------------------
DROP TRIGGER IF EXISTS `calculate`;
delimiter ;;
CREATE TRIGGER `calculate` BEFORE UPDATE ON `cont_video` FOR EACH ROW BEGIN
    set new.total_count = (new.like_count + new.collect_count+ new.comment_count);
END
;;
delimiter ;