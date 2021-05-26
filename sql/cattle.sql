/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80022
 Source Host           : localhost:3306
 Source Schema         : cattle

 Target Server Type    : MySQL
 Target Server Version : 80022
 File Encoding         : 65001

 Date: 26/05/2021 14:10:03
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for cattle_job_info
-- ----------------------------
DROP TABLE IF EXISTS `cattle_job_info`;
CREATE TABLE `cattle_job_info`  (
  `job_id` int(0) NOT NULL AUTO_INCREMENT,
  `job_name` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `relate_id` int(0) NULL DEFAULT NULL,
  `script_type` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `start_time` datetime(0) NULL DEFAULT NULL,
  `end_time` datetime(0) NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`job_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cattle_job_info
-- ----------------------------
INSERT INTO `cattle_job_info` VALUES (1, '测试', 1, 'kettle', '2021-05-08 15:31:46', NULL, '2021-05-13 17:15:22');
INSERT INTO `cattle_job_info` VALUES (2, '杭州楼盘', 3, 'spider', NULL, NULL, '2021-05-13 17:15:24');
INSERT INTO `cattle_job_info` VALUES (3, '电影首发站', 1, 'spider', '2021-05-14 14:14:48', NULL, '2021-05-14 06:14:50');

-- ----------------------------
-- Table structure for cattle_ktr_field
-- ----------------------------
DROP TABLE IF EXISTS `cattle_ktr_field`;
CREATE TABLE `cattle_ktr_field`  (
  `field_id` int(0) NOT NULL AUTO_INCREMENT,
  `field_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `field_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字段类型：String、Number',
  `length` int(0) NULL DEFAULT NULL COMMENT '字段长度',
  `precision` int(0) NULL DEFAULT NULL COMMENT '精度，只有在type为number时才有用',
  `default_value` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '默认值',
  `comment` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `step_id` int(0) NULL DEFAULT NULL,
  `deleted` tinyint(1) NULL DEFAULT 0,
  `update_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`field_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '步骤字段' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cattle_ktr_field
-- ----------------------------
INSERT INTO `cattle_ktr_field` VALUES (1, 'name', 'String', 20, NULL, NULL, NULL, 1, 0, NULL);
INSERT INTO `cattle_ktr_field` VALUES (2, 'age', 'String', 50, NULL, NULL, NULL, 1, 0, NULL);
INSERT INTO `cattle_ktr_field` VALUES (3, 'sex', 'Number', 15, 2, NULL, '在统孵化器数量(个)', 1, 0, NULL);
INSERT INTO `cattle_ktr_field` VALUES (24, 'ceshi', 'String', 12, NULL, 'e', '', NULL, 0, NULL);
INSERT INTO `cattle_ktr_field` VALUES (25, '1', 'String', 1, 0, '', '', 14, 1, NULL);
INSERT INTO `cattle_ktr_field` VALUES (26, '2', 'String', 1, 0, '1', '1', 14, 0, NULL);
INSERT INTO `cattle_ktr_field` VALUES (27, 'ce', 'String', 122, 0, '1', '1', 1, 1, NULL);
INSERT INTO `cattle_ktr_field` VALUES (28, 'c', 'String', 1, 0, '123', '1', 1, 1, NULL);

-- ----------------------------
-- Table structure for cattle_ktr_info
-- ----------------------------
DROP TABLE IF EXISTS `cattle_ktr_info`;
CREATE TABLE `cattle_ktr_info`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `ktr_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '脚本名',
  `script_file` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用于kettle脚本文件地址 kettle执行需要有个默认的  以 .ktr结尾的文件作为模板',
  `table_name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `process_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `deleted` tinyint(1) NULL DEFAULT 0,
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  `update_time` datetime(0) NULL DEFAULT NULL,
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cattle_ktr_info
-- ----------------------------
INSERT INTO `cattle_ktr_info` VALUES (1, '测试脚本', 'D:\\code\\new work space from git\\Cattle\\doc\\科技企业孵化\\excel导入基础脚本.ktr', 'test_tableYearTemplate_kettle_test', 'edit', 0, '2021-05-11 07:52:02', NULL, NULL);
INSERT INTO `cattle_ktr_info` VALUES (6, '测试', 'D:\\临时文件\\temp\\\\1.ktr', '测试', 'normal', 1, '2021-05-12 08:25:03', NULL, NULL);
INSERT INTO `cattle_ktr_info` VALUES (7, '1231', NULL, '123', 'normal', 1, '2021-05-12 08:15:11', NULL, NULL);
INSERT INTO `cattle_ktr_info` VALUES (8, '测试', 'D:\\临时文件\\temp\\\\1.ktr', 'c', 'normal', 1, '2021-05-12 08:15:09', NULL, NULL);
INSERT INTO `cattle_ktr_info` VALUES (9, '123', 'D:\\临时文件\\temp\\\\1.ktr', '132', 'edit', 1, '2021-05-12 08:15:06', NULL, NULL);
INSERT INTO `cattle_ktr_info` VALUES (10, '测试', 'D:\\临时文件\\temp多excel导入测试.ktr', '测试', 'normal', 1, '2021-05-11 09:18:02', NULL, NULL);
INSERT INTO `cattle_ktr_info` VALUES (11, '测试2', NULL, '测试2', 'edit', 0, '2021-05-12 08:53:27', NULL, NULL);
INSERT INTO `cattle_ktr_info` VALUES (12, '1', NULL, '1', 'normal', 1, '2021-05-25 08:18:24', NULL, NULL);

-- ----------------------------
-- Table structure for cattle_ktr_step
-- ----------------------------
DROP TABLE IF EXISTS `cattle_ktr_step`;
CREATE TABLE `cattle_ktr_step`  (
  `step_id` int(0) NOT NULL AUTO_INCREMENT,
  `step_name` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `ktr_info_id` int(0) NULL DEFAULT NULL COMMENT 'cattle_ktr_info',
  `step_type` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '步骤类型：excel导入、字段选择、自定义字段',
  `next_step_id` int(0) NULL DEFAULT NULL COMMENT '下一步步骤ID',
  `sheet_name` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'excel   sheetName',
  `start_row` int(0) NULL DEFAULT NULL COMMENT 'excel  开始行',
  `start_col` int(0) NULL DEFAULT NULL COMMENT 'excel 开始列',
  `file_list` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'excel步骤保存文件，逗号分割',
  `input_db_id` int(0) NULL DEFAULT NULL,
  `output_db_id` int(0) NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  `update_time` datetime(0) NULL DEFAULT NULL,
  `deleted` tinyint(1) NULL DEFAULT 0,
  PRIMARY KEY (`step_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cattle_ktr_step
-- ----------------------------
INSERT INTO `cattle_ktr_step` VALUES (1, 'excel导入', 1, 'excelImport', 2, 'Sheet1', 12, 0, 'D:\\code\\new work space from git\\Cattle\\doc\\科技企业孵化\\1.xlsx', NULL, NULL, '2021-05-13 02:56:41', NULL, 0);
INSERT INTO `cattle_ktr_step` VALUES (3, '新增常量', 1, 'constant', 0, '', NULL, NULL, NULL, NULL, NULL, '2021-05-12 08:24:12', NULL, 1);
INSERT INTO `cattle_ktr_step` VALUES (5, '测试', 1, 'excelImport', NULL, NULL, 12, 12, NULL, NULL, NULL, '2021-05-12 09:35:23', NULL, 1);
INSERT INTO `cattle_ktr_step` VALUES (14, 'excel', 11, 'excelImport', NULL, '12', 123, 123, NULL, NULL, NULL, '2021-05-13 02:07:52', NULL, 0);

-- ----------------------------
-- Table structure for cattle_run_log
-- ----------------------------
DROP TABLE IF EXISTS `cattle_run_log`;
CREATE TABLE `cattle_run_log`  (
  `batch_id` bigint(0) NOT NULL,
  `job_id` int(0) NULL DEFAULT NULL,
  `job_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `job_status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'job状态',
  `count` int(0) NULL DEFAULT NULL COMMENT '成功条数',
  `error_count` int(0) NULL DEFAULT NULL COMMENT '错误数',
  `error_text` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '错误日志',
  `warn_count` int(0) NULL DEFAULT NULL COMMENT '警告数',
  `warn_text` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '警告日志',
  `start_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  `end_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`batch_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cattle_run_log
-- ----------------------------
INSERT INTO `cattle_run_log` VALUES (1397395075641774080, 2, '杭州楼盘', 'finish', 0, 0, NULL, 0, '', '2021-05-26 03:35:45', '2021-05-26 11:35:46');
INSERT INTO `cattle_run_log` VALUES (1397396710497587200, 2, '杭州楼盘', 'finish', 533, 0, NULL, 0, '', '2021-05-26 03:39:38', '2021-05-26 11:39:39');
INSERT INTO `cattle_run_log` VALUES (1397396750377029632, 2, '杭州楼盘', 'finish', 498, 0, NULL, 0, '', '2021-05-26 03:44:51', '2021-05-26 11:44:52');
INSERT INTO `cattle_run_log` VALUES (1397398201664933888, 2, '杭州楼盘', 'finish', 20, 0, NULL, 0, '', '2021-05-26 04:25:48', '2021-05-26 12:25:48');
INSERT INTO `cattle_run_log` VALUES (1397408556638146560, 2, '杭州楼盘', 'finish', 671, 0, NULL, 0, '', '2021-05-26 04:27:19', '2021-05-26 12:27:19');
INSERT INTO `cattle_run_log` VALUES (1397408883152130048, 2, '杭州楼盘', 'running', NULL, NULL, NULL, NULL, NULL, '2021-05-26 04:34:31', NULL);
INSERT INTO `cattle_run_log` VALUES (1397413775174406144, 2, '杭州楼盘', 'finish', 294, 0, NULL, 22, 'com.cattle.component.spider.process.PageTargetProcessexecutor warn Exception:java.lang.RuntimeException --- message:当前页面：https://cs.newhouse.fang.com/house/s/b931 , 字段信息数量无法对应，请检查！com.cattle.component.spider.process.PageTargetProcessexecutor warn Exception:java.lang.RuntimeException --- message:当前页面：https://cs.newhouse.fang.com/house/s/b918 , 字段信息数量无法对应，请检查！com.cattle.component.spider.process.PageTargetProcessexecutor warn Exception:java.lang.RuntimeException --- message:当前页面：https://cs.newhouse.fang.com/house/s/b928 , 字段信息数量无法对应，请检查！com.cattle.component.spider.process.PageTargetProcessexecutor warn Exception:java.lang.RuntimeException --- message:当前页面：https://cs.newhouse.fang.com/house/s/b921 , 字段信息数量无法对应，请检查！com.cattle.component.spider.process.PageTargetProcessexecutor warn Exception:java.lang.RuntimeException --- message:当前页面：https://cs.newhouse.fang.com/house/s/b924 , 字段信息数量无法对应，请检查！com.cattle.component.spider.process.PageTargetProcessexecutor warn Exception:java.lang.RuntimeException --- message:当前页面：https://cs.newhouse.fang.com/house/s/b934 , 字段信息数量无法对应，请检查！com.cattle.component.spider.process.PageTargetProcessexecutor warn Exception:java.lang.RuntimeException --- message:当前页面：https://cs.newhouse.fang.com/house/s/b919 , 字段信息数量无法对应，请检查！com.cattle.component.spider.process.PageTargetProcessexecutor warn Exception:java.lang.RuntimeException --- message:当前页面：https://cs.newhouse.fang.com/house/s/b94 , 字段信息数量无法对应，请检查！com.cattle.component.spider.process.PageTargetProcessexecutor warn Exception:java.lang.RuntimeException --- message:当前页面：https://cs.newhouse.fang.com/house/s/b914 , 字段信息数量无法对应，请检查！com.cattle.component.spider.process.PageTargetProcessexecutor warn Exception:java.lang.RuntimeException --- message:当前页面：https://cs.newhouse.fang.com/house/s/b34 , 字段信息数量无法对应，请检查！com.cattle.component.spider.process.PageTargetProcessexecutor warn Exception:java.lang.RuntimeException --- message:当前页面：https://cs.newhouse.fang.com/house/s/b933 , 字段信息数量无法对应，请检查！com.cattle.component.spider.process.PageTargetProcessexecutor warn Exception:java.lang.RuntimeException --- message:当前页面：https://cs.newhouse.fang.com/house/s/b913 , 字段信息数量无法对应，请检查！com.cattle.component.spider.process.PageTargetProcessexecutor warn Exception:java.lang.RuntimeException --- message:当前页面：https://cs.newhouse.fang.com/house/s/b926 , 字段信息数量无法对应，请检查！com.cattle.component.spider.process.PageTargetProcessexecutor warn Exception:java.lang.RuntimeException --- message:当前页面：https://cs.newhouse.fang.com/house/s/b923 , 字段信息数量无法对应，请检查！com.cattle.component.spider.process.PageTargetProcessexecutor warn Exception:java.lang.RuntimeException --- message:当前页面：https://cs.newhouse.fang.com/house/s/b4267 , 字段信息数量无法对应，请检查！com.cattle.component.spider.process.PageTargetProcessexecutor warn Exception:java.lang.RuntimeException --- message:当前页面：https://cs.newhouse.fang.com/house/s/b920 , 字段信息数量无法对应，请检查！com.cattle.component.spider.process.PageTargetProcessexecutor warn Exception:java.lang.RuntimeException --- message:当前页面：https://cs.newhouse.fang.com/house/s/b917 , 字段信息数量无法对应，请检查！com.cattle.component.spider.process.PageTargetProcessexecutor warn Exception:java.lang.RuntimeException --- message:当前页面：https://cs.newhouse.fang.com/house/s/b927 , 字段信息数量无法对应，请检查！com.cattle.component.spider.process.PageTargetProcessexecutor warn Exception:java.lang.RuntimeException --- message:当前页面：https://cs.newhouse.fang.com/house/s/b932 , 字段信息数量无法对应，请检查！com.cattle.component.spider.process.PageTargetProcessexecutor warn Exception:java.lang.RuntimeException --- message:当前页面：https://cs.newhouse.fang.com/house/s/b925 , 字段信息数量无法对应，请检查！com.cattle.component.spider.process.PageTargetProcessexecutor warn Exception:java.lang.RuntimeException --- message:当前页面：https://cs.newhouse.fang.com/house/s/b912 , 字段信息数量无法对应，请检查！com.cattle.component.spider.process.PageTargetProcessexecutor warn Exception:java.lang.RuntimeException --- message:当前页面：https://cs.newhouse.fang.com/house/s/b922 , 字段信息数量无法对应，请检查！', '2021-05-26 04:47:09', '2021-05-26 12:47:09');
INSERT INTO `cattle_run_log` VALUES (1397413869370085376, 2, '杭州楼盘', 'finish', 0, 0, NULL, 47, 'com.cattle.component.spider.process.PageTargetProcessexecutor warn Exception:java.lang.RuntimeException --- message:当前页面：https://cs.newhouse.fang.com/house/s/b4346 , 字段信息数量无法对应，请检查！com.cattle.component.spider.process.PageTargetProcessexecutor warn Exception:java.lang.RuntimeException --- message:当前页面：https://cs.newhouse.fang.com/house/s/b28000 , 字段信息数量无法对应，请检查！com.cattle.component.spider.process.PageTargetProcessexecutor warn Exception:java.lang.RuntimeException --- message:当前页面：https://cs.newhouse.fang.com/house/s/b931 , 字段信息数量无法对应，请检查！com.cattle.component.spider.process.PageTargetProcessexecutor warn Exception:java.lang.RuntimeException --- message:当前页面：https://cs.newhouse.fang.com/house/s/b911 , 字段信息数量无法对应，请检查！com.cattle.component.spider.process.PageTargetProcessexecutor warn Exception:java.lang.RuntimeException --- message:当前页面：https://cs.newhouse.fang.com/house/s/b921 , 字段信息数量无法对应，请检查！com.cattle.component.spider.process.PageTargetProcessexecutor warn Exception:java.lang.RuntimeException --- message:当前页面：https://cs.newhouse.fang.com/house/s/b924 , 字段信息数量无法对应，请检查！com.cattle.component.spider.process.PageTargetProcessexecutor warn Exception:java.lang.RuntimeException --- message:当前页面：https://cs.newhouse.fang.com/house/s/b210000 , 字段信息数量无法对应，请检查！com.cattle.component.spider.process.PageTargetProcessexecutor warn Exception:java.lang.RuntimeException --- message:当前页面：https://cs.newhouse.fang.com/house/s/b934 , 字段信息数量无法对应，请检查！com.cattle.component.spider.process.PageTargetProcessexecutor warn Exception:java.lang.RuntimeException --- message:当前页面：https://cs.newhouse.fang.com/house/s/b919 , 字段信息数量无法对应，请检查！com.cattle.component.spider.process.PageTargetProcessexecutor warn Exception:java.lang.RuntimeException --- message:当前页面：https://cs.newhouse.fang.com/house/s/b94 , 字段信息数量无法对应，请检查！com.cattle.component.spider.process.PageTargetProcessexecutor warn Exception:java.lang.RuntimeException --- message:当前页面：https://cs.newhouse.fang.com/house/s/b914 , 字段信息数量无法对应，请检查！com.cattle.component.spider.process.PageTargetProcessexecutor warn Exception:java.lang.RuntimeException --- message:当前页面：https://cs.newhouse.fang.com/house/s/b4227 , 字段信息数量无法对应，请检查！com.cattle.component.spider.process.PageTargetProcessexecutor warn Exception:java.lang.RuntimeException --- message:当前页面：https://cs.newhouse.fang.com/house/s/b99 , 字段信息数量无法对应，请检查！com.cattle.component.spider.process.PageTargetProcessexecutor warn Exception:java.lang.RuntimeException --- message:当前页面：https://cs.newhouse.fang.com/house/s/b916 , 字段信息数量无法对应，请检查！com.cattle.component.spider.process.PageTargetProcessexecutor warn Exception:java.lang.RuntimeException --- message:当前页面：https://cs.newhouse.fang.com/house/s/b1 , 字段信息数量无法对应，请检查！com.cattle.component.spider.process.PageTargetProcessexecutor warn Exception:java.lang.RuntimeException --- message:当前页面：https://cs.newhouse.fang.com/house/s/b91 , 字段信息数量无法对应，请检查！com.cattle.component.spider.process.PageTargetProcessexecutor warn Exception:java.lang.RuntimeException --- message:当前页面：https://cs.newhouse.fang.com/house/s/b26000 , 字段信息数量无法对应，请检查！com.cattle.component.spider.process.PageTargetProcessexecutor warn Exception:java.lang.RuntimeException --- message:当前页面：https://cs.newhouse.fang.com/house/s/b4267 , 字段信息数量无法对应，请检查！com.cattle.component.spider.process.PageTargetProcessexecutor warn Exception:java.lang.RuntimeException --- message:当前页面：https://cs.newhouse.fang.com/house/s/b98 , 字段信息数量无法对应，请检查！com.cattle.component.spider.process.PageTargetProcessexecutor warn Exception:java.lang.RuntimeException --- message:当前页面：https://cs.newhouse.fang.com/house/s/b932 , 字段信息数量无法对应，请检查！com.cattle.component.spider.process.PageTargetProcessexecutor warn Exception:java.lang.RuntimeException --- message:当前页面：https://cs.newhouse.fang.com/house/s/b925 , 字段信息数量无法对应，请检查！com.cattle.component.spider.process.PageTargetProcessexecutor warn Exception:java.lang.RuntimeException --- message:当前页面：https://cs.newhouse.fang.com/house/s/b912 , 字段信息数量无法对应，请检查！com.cattle.component.spider.process.PageTargetProcessexecutor warn Exception:java.lang.RuntimeException --- message:当前页面：https://cs.newhouse.fang.com/house/s/b915 , 字段信息数量无法对应，请检查！com.cattle.component.spider.process.PageTargetProcessexecutor warn Exception:java.lang.RuntimeException --- message:当前页面：https://cs.newhouse.fang.com/house/s/b922 , 字段信息数量无法对应，请检查！com.cattle.component.spider.process.PageTargetProcessexecutor warn Exception:java.lang.RuntimeException --- message:当前页面：https://cs.newhouse.fang.com/house/s/b95 , 字段信息数量无法对应，请检查！com.cattle.component.spider.process.PageTargetProcessexecutor warn Exception:java.lang.RuntimeException --- message:当前页面：https://cs.newhouse.fang.com/house/s/b4226 , 字段信息数量无法对应，请检查！com.cattle.component.spider.process.PageTargetProcessexecutor warn Exception:java.lang.RuntimeException --- message:当前页面：https://cs.newhouse.fang.com/house/s/b918 , 字段信息数量无法对应，请检查！com.cattle.component.spider.process.PageTargetProcessexecutor warn Exception:java.lang.RuntimeException --- message:当前页面：https://cs.newhouse.fang.com/house/s/b928 , 字段信息数量无法对应，请检查！com.cattle.component.spider.process.PageTargetProcessexecutor warn Exception:java.lang.RuntimeException --- message:当前页面：https://cs.newhouse.fang.com/house/s/b92 , 字段信息数量无法对应，请检查！com.cattle.component.spider.process.PageTargetProcessexecutor warn Exception:java.lang.RuntimeException --- message:当前页面：https://cs.newhouse.fang.com/house/s/b4264 , 字段信息数量无法对应，请检查！com.cattle.component.spider.process.PageTargetProcessexecutor warn Exception:java.lang.RuntimeException --- message:当前页面：https://cs.newhouse.fang.com/house/s/b214000 , 字段信息数量无法对应，请检查！com.cattle.component.spider.process.PageTargetProcessexecutor warn Exception:java.lang.RuntimeException --- message:当前页面：https://cs.newhouse.fang.com/house/s/b930 , 字段信息数量无法对应，请检查！com.cattle.component.spider.process.PageTargetProcessexecutor warn Exception:java.lang.RuntimeException --- message:当前页面：https://cs.newhouse.fang.com/house/s/b34 , 字段信息数量无法对应，请检查！com.cattle.component.spider.process.PageTargetProcessexecutor warn Exception:java.lang.RuntimeException --- message:当前页面：https://cs.newhouse.fang.com/house/s/b933 , 字段信息数量无法对应，请检查！com.cattle.component.spider.process.PageTargetProcessexecutor warn Exception:java.lang.RuntimeException --- message:当前页面：https://cs.newhouse.fang.com/house/s/b4266 , 字段信息数量无法对应，请检查！com.cattle.component.spider.process.PageTargetProcessexecutor warn Exception:java.lang.RuntimeException --- message:当前页面：https://cs.newhouse.fang.com/house/s/b212000 , 字段信息数量无法对应，请检查！com.cattle.component.spider.process.PageTargetProcessexecutor warn Exception:java.lang.RuntimeException --- message:当前页面：https://cs.newhouse.fang.com/house/s/b913 , 字段信息数量无法对应，请检查！com.cattle.component.spider.process.PageTargetProcessexecutor warn Exception:java.lang.RuntimeException --- message:当前页面：https://cs.newhouse.fang.com/house/s/b926 , 字段信息数量无法对应，请检查！com.cattle.component.spider.process.PageTargetProcessexecutor warn Exception:java.lang.RuntimeException --- message:当前页面：https://cs.newhouse.fang.com/house/s/b923 , 字段信息数量无法对应，请检查！com.cattle.component.spider.process.PageTargetProcessexecutor warn Exception:java.lang.RuntimeException --- message:当前页面：https://cs.newhouse.fang.com/house/s/b97 , 字段信息数量无法对应，请检查！com.cattle.component.spider.process.PageTargetProcessexecutor warn Exception:java.lang.RuntimeException --- message:当前页面：https://cs.newhouse.fang.com/house/s/b93 , 字段信息数量无法对应，请检查！com.cattle.component.spider.process.PageTargetProcessexecutor warn Exception:java.lang.RuntimeException --- message:当前页面：https://cs.newhouse.fang.com/house/s/b96 , 字段信息数量无法对应，请检查！com.cattle.component.spider.process.PageTargetProcessexecutor warn Exception:java.lang.RuntimeException --- message:当前页面：https://cs.newhouse.fang.com/house/s/b920 , 字段信息数量无法对应，请检查！com.cattle.component.spider.process.PageTargetProcessexecutor warn Exception:java.lang.RuntimeException --- message:当前页面：https://cs.newhouse.fang.com/house/s/b910 , 字段信息数量无法对应，请检查！com.cattle.component.spider.process.PageTargetProcessexecutor warn Exception:java.lang.RuntimeException --- message:当前页面：https://cs.newhouse.fang.com/house/s/b917 , 字段信息数量无法对应，请检查！com.cattle.component.spider.process.PageTargetProcessexecutor warn Exception:java.lang.RuntimeException --- message:当前页面：https://cs.newhouse.fang.com/house/s/b927 , 字段信息数量无法对应，请检查！com.cattle.component.spider.process.PageTargetProcessexecutor warn Exception:java.lang.RuntimeException --- message:当前页面：https://cs.newhouse.fang.com/house/s/b4265 , 字段信息数量无法对应，请检查！', '2021-05-26 05:05:46', '2021-05-26 13:05:46');
INSERT INTO `cattle_run_log` VALUES (1397418571277144064, 2, '杭州楼盘', 'running', NULL, NULL, NULL, NULL, NULL, '2021-05-26 05:06:46', NULL);
INSERT INTO `cattle_run_log` VALUES (1397419321772347392, 2, '杭州楼盘', 'running', NULL, NULL, NULL, NULL, NULL, '2021-05-26 05:10:17', NULL);
INSERT INTO `cattle_run_log` VALUES (1397420091804618752, 2, '杭州楼盘', 'finish', 656, 0, NULL, 1, 'com.cattle.component.spider.process.PageTargetProcessexecutor warn Exception:java.lang.RuntimeException --- message:当前页面：https://cs.newhouse.fang.com/house/s/b934 , 字段信息数量无法对应，请检查！', '2021-05-26 05:13:13', '2021-05-26 13:13:14');
INSERT INTO `cattle_run_log` VALUES (1397420136771751936, 2, '杭州楼盘', 'finish', 659, 0, NULL, 1, 'com.cattle.component.spider.process.PageTargetProcessexecutor warn Exception:java.lang.RuntimeException --- message:当前页面：https://cs.newhouse.fang.com/house/s/b934 , 字段信息数量无法对应，请检查！', '2021-05-26 05:14:40', '2021-05-26 13:14:41');

-- ----------------------------
-- Table structure for cattle_spider_info
-- ----------------------------
DROP TABLE IF EXISTS `cattle_spider_info`;
CREATE TABLE `cattle_spider_info`  (
  `spider_id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `spider_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '爬虫名',
  `table_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '保存的表名',
  `list_regex` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '列表页正则表达式',
  `entry_url` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '入口页',
  `content_xpath` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '正文页xpath',
  `fields_json` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '列表页字段规则json字符串',
  `content_fields_json` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '正文页规则json字符串',
  `thread_num` int(0) NULL DEFAULT NULL COMMENT '线程数',
  `sleep_time` int(0) NULL DEFAULT NULL COMMENT '每个页面处理完后的睡眠时间 单位秒',
  `retry_times` int(0) NULL DEFAULT NULL COMMENT '页面下载失败重试次数',
  `retry_sleep_time` int(0) NULL DEFAULT NULL COMMENT '重试睡眠时间 单位秒',
  `cycle_retry_times` int(0) NULL DEFAULT NULL COMMENT '页面爬取失败后放回队列的次数',
  `time_out` int(0) NULL DEFAULT NULL COMMENT '下载页面超时时间 单位秒',
  `x_path_selection` int(0) NULL DEFAULT NULL COMMENT 'xpath 解析选型 0 - htmlCleaner，1 - xSoup',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  `modify_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  `deleted` tinyint(1) NULL DEFAULT 0,
  `scan_url_type` tinyint(1) NULL DEFAULT NULL COMMENT 'url 扫描类型   0 - 全量，1 - 增量',
  PRIMARY KEY (`spider_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cattle_spider_info
-- ----------------------------
INSERT INTO `cattle_spider_info` VALUES (1, '电影首发站', 'spider_movie_info', 'https://www\\.wuhaozhan\\.net/movie/list/\\?p=\\d+', 'https://www.wuhaozhan.net/movie/list/?p=1', '//h1/a/@href', '[{\"index\":1,\"key\":\"title\",\"value\":\"//div[@Class=\'pure-g\']/div/div/div/div[2]/h1/a/text()\"},{\"index\":2,\"key\":\"rate\",\"value\":\"//div[@Class=\'pure-g\']/div/div/div/div[3]//a/span/text()\"},{\"key\":\"url\",\"value\":\"//div[@Class=\'pure-g\']/div/div/div/div[2]/h1/a/@href\"}]', '[{\"key\":\"contentName\",\"value\":\"//h1/text()\"}]', 3, 2, 1, 2, 3, 5, 0, '2021-05-24 08:30:58', '2021-05-24 08:30:58', 0, 1);
INSERT INTO `cattle_spider_info` VALUES (2, '豆瓣top250', 'douban_top_320', 'https://movie\\.douban\\.com/top250\\?start=\\d+&filter=', 'https://movie.douban.com/top250?start=0&filter=', '//*[@class=\'article\']//div[@class=\'hd\']/a/@href', '[{\"index\":0,\"key\":\"title\",\"value\":\"//*[@id=\\\"content\\\"]/div/div[1]/ol/li/div/div[2]/div[1]/a/span[1]/text()\"},{\"index\":1,\"key\":\"rate\",\"value\":\"//*[@id=\\\"content\\\"]/div/div[1]/ol/li/div/div[2]/div[2]/div/span[2]/text()\"}]', '[{\"index\":0,\"key\":\"taptap\",\"value\":\"//*[@id=\'content\']/h1/span[1]/text()\"}]', 1, 5, 0, 1, 0, 5, 1, '2021-03-09 10:16:19', '2021-03-09 10:16:19', 0, 1);
INSERT INTO `cattle_spider_info` VALUES (3, '长沙楼盘', 'cs_lp', 'https://cs\\.newhouse\\.fang\\.com/house/s/b\\d+', 'https://cs.newhouse.fang.com/house/s/b91', '', '[{\"index\":0,\"key\":\"name\",\"value\":\"//div[@class=\'nlc_details\']//div[@class=\'nlcd_name\']//a/text()\"},{\"index\":1,\"key\":\"address\",\"value\":\"//div[@class=\'address\']//a/text()\"},{\"index\":2,\"key\":\"price\",\"value\":\"//div[@class=\'nhouse_price\']/*[1]/text()\"},{\"key\":\"url\",\"value\":\"//div[@class=\'nlcd_name\']//a/@href\"}]', '[]', 4, 1, 2, 1, 2, 5, 0, '2021-05-26 05:11:12', '2021-05-26 05:11:12', 0, 1);
INSERT INTO `cattle_spider_info` VALUES (4, '1', '1', '1', '1', '', '[]', '[]', 1, 1, 1, 1, 1, 1, NULL, '2021-05-24 07:48:05', '2021-05-24 07:48:05', 1, NULL);

SET FOREIGN_KEY_CHECKS = 1;
