-- ----------------------------
-- Table structure for user_test
-- ----------------------------
DROP TABLE IF EXISTS `user_test`;
CREATE TABLE `user_test`  (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `age` int(3) NULL DEFAULT NULL,
  `grade` int(5) NULL DEFAULT NULL,
  `sex` int(5) NULL DEFAULT NULL,
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1483344759452152714 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;