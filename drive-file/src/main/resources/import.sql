delete
from user
where userId = 1;
insert into user (userId, username, telephone, salt, password, available)
values (1, 'admin', 'admin', 'admin', 'df655ad8d3229f3269fad2a8bab59b6c', 1);

delete
from role
where roleId in (1, 2);
INSERT INTO `role` (`roleId`, `available`, `description`, `roleName`, `createTime`, `createUserId`, `modifyTime`,
                    `modifyUserId`)
VALUES (1, 1, '超级管理员', '超级管理员', NULL, NULL, '2021-11-10 20:46:06', NULL);
INSERT INTO `role` (`roleId`, `available`, `description`, `roleName`, `createTime`, `createUserId`, `modifyTime`,
                    `modifyUserId`)
VALUES (2, 1, '普通用户', '普通用户', NULL, NULL, NULL, NULL);

delete
from sysparam
where sysParamId in (1, 2, 3);
insert into sysparam (sysParamId, sysParamKey, sysParamValue, sysParamDesc)
values (1, 'totalStorageSize', '1024', '总存储大小（单位M）');
insert into sysparam (sysParamId, sysParamKey, sysParamValue, sysParamDesc)
values (2, 'initDataFlag', '1', '系统初始化数据标识');
insert into sysparam (sysParamId, sysParamKey, sysParamValue, sysParamDesc)
values (3, 'version', '1.1.2', '当前脚本的版本号');

delete
from filetype
where fileTypeId in (0, 1, 2, 3, 4, 5);
INSERT INTO `filetype` (`fileTypeId`, `fileTypeName`)
VALUES (0, '全部');
INSERT INTO `filetype` (`fileTypeId`, `fileTypeName`)
VALUES (1, '图片');
INSERT INTO `filetype` (`fileTypeId`, `fileTypeName`)
VALUES (2, '文档');
INSERT INTO `filetype` (`fileTypeId`, `fileTypeName`)
VALUES (3, '视频');
INSERT INTO `filetype` (`fileTypeId`, `fileTypeName`)
VALUES (4, '音乐');
INSERT INTO `filetype` (`fileTypeId`, `fileTypeName`)
VALUES (5, '其他');

delete
from fileextend
where 1 = 1;
INSERT INTO `fileextend` (`fileExtendName`)
VALUES ('bmp');
INSERT INTO `fileextend` (`fileExtendName`)
VALUES ('jpg');
INSERT INTO `fileextend` (`fileExtendName`)
VALUES ('png');
INSERT INTO `fileextend` (`fileExtendName`)
VALUES ('tif');
INSERT INTO `fileextend` (`fileExtendName`)
VALUES ('gif');
INSERT INTO `fileextend` (`fileExtendName`)
VALUES ('jpeg');

INSERT INTO `fileextend` (`fileExtendName`)
VALUES ('doc');
INSERT INTO `fileextend` (`fileExtendName`)
VALUES ('docx');
INSERT INTO `fileextend` (`fileExtendName`)
VALUES ('docm');
INSERT INTO `fileextend` (`fileExtendName`)
VALUES ('dot');
INSERT INTO `fileextend` (`fileExtendName`)
VALUES ('dotx');
INSERT INTO `fileextend` (`fileExtendName`)
VALUES ('dotm');
INSERT INTO `fileextend` (`fileExtendName`)
VALUES ('odt');
INSERT INTO `fileextend` (`fileExtendName`)
VALUES ('fodt');
INSERT INTO `fileextend` (`fileExtendName`)
VALUES ('ott');
INSERT INTO `fileextend` (`fileExtendName`)
VALUES ('rtf');
INSERT INTO `fileextend` (`fileExtendName`)
VALUES ('txt');
INSERT INTO `fileextend` (`fileExtendName`)
VALUES ('html');
INSERT INTO `fileextend` (`fileExtendName`)
VALUES ('htm');
INSERT INTO `fileextend` (`fileExtendName`)
VALUES ('mht');
INSERT INTO `fileextend` (`fileExtendName`)
VALUES ('xml');
INSERT INTO `fileextend` (`fileExtendName`)
VALUES ('pdf');
INSERT INTO `fileextend` (`fileExtendName`)
VALUES ('djvu');
INSERT INTO `fileextend` (`fileExtendName`)
VALUES ('fb2');
INSERT INTO `fileextend` (`fileExtendName`)
VALUES ('epub');
INSERT INTO `fileextend` (`fileExtendName`)
VALUES ('xps');

INSERT INTO `fileextend` (`fileExtendName`)
VALUES ('xls');
INSERT INTO `fileextend` (`fileExtendName`)
VALUES ('xlsx');
INSERT INTO `fileextend` (`fileExtendName`)
VALUES ('xlsm');
INSERT INTO `fileextend` (`fileExtendName`)
VALUES ('xlt');
INSERT INTO `fileextend` (`fileExtendName`)
VALUES ('xltx');
INSERT INTO `fileextend` (`fileExtendName`)
VALUES ('xltm');
INSERT INTO `fileextend` (`fileExtendName`)
VALUES ('ods');
INSERT INTO `fileextend` (`fileExtendName`)
VALUES ('fods');
INSERT INTO `fileextend` (`fileExtendName`)
VALUES ('ots');
INSERT INTO `fileextend` (`fileExtendName`)
VALUES ('csv');

INSERT INTO `fileextend` (`fileExtendName`)
VALUES ('pps');
INSERT INTO `fileextend` (`fileExtendName`)
VALUES ('ppsx');
INSERT INTO `fileextend` (`fileExtendName`)
VALUES ('ppsm');
INSERT INTO `fileextend` (`fileExtendName`)
VALUES ('ppt');
INSERT INTO `fileextend` (`fileExtendName`)
VALUES ('pptx');
INSERT INTO `fileextend` (`fileExtendName`)
VALUES ('pptm');
INSERT INTO `fileextend` (`fileExtendName`)
VALUES ('pot');
INSERT INTO `fileextend` (`fileExtendName`)
VALUES ('potx');
INSERT INTO `fileextend` (`fileExtendName`)
VALUES ('potm');
INSERT INTO `fileextend` (`fileExtendName`)
VALUES ('odp');
INSERT INTO `fileextend` (`fileExtendName`)
VALUES ('fodp');
INSERT INTO `fileextend` (`fileExtendName`)
VALUES ('otp');

INSERT INTO `fileextend` (`fileExtendName`)
VALUES ('hlp');
INSERT INTO `fileextend` (`fileExtendName`)
VALUES ('wps');

INSERT INTO `fileextend` (`fileExtendName`)
VALUES ('avi');
INSERT INTO `fileextend` (`fileExtendName`)
VALUES ('mp4');
INSERT INTO `fileextend` (`fileExtendName`)
VALUES ('mpg');
INSERT INTO `fileextend` (`fileExtendName`)
VALUES ('mov');
INSERT INTO `fileextend` (`fileExtendName`)
VALUES ('swf');
INSERT INTO `fileextend` (`fileExtendName`)
VALUES ('wav');
INSERT INTO `fileextend` (`fileExtendName`)
VALUES ('aif');
INSERT INTO `fileextend` (`fileExtendName`)
VALUES ('au');
INSERT INTO `fileextend` (`fileExtendName`)
VALUES ('mp3');
INSERT INTO `fileextend` (`fileExtendName`)
VALUES ('ram');
INSERT INTO `fileextend` (`fileExtendName`)
VALUES ('wma');
INSERT INTO `fileextend` (`fileExtendName`)
VALUES ('mmf');
INSERT INTO `fileextend` (`fileExtendName`)
VALUES ('amr');
INSERT INTO `fileextend` (`fileExtendName`)
VALUES ('aac');
INSERT INTO `fileextend` (`fileExtendName`)
VALUES ('flac');
INSERT INTO `fileextend` (`fileExtendName`)
VALUES ('java');
INSERT INTO `fileextend` (`fileExtendName`)
VALUES ('js');
INSERT INTO `fileextend` (`fileExtendName`)
VALUES ('css');
INSERT INTO `fileextend` (`fileExtendName`)
VALUES ('json');

delete
from fileclassification
where 1 = 1;
INSERT INTO `fileclassification` (`fileClassificationId`, `fileTypeId`, `fileExtendName`)
VALUES (1, 1, 'bmp');
INSERT INTO `fileclassification` (`fileClassificationId`, `fileTypeId`, `fileExtendName`)
VALUES (2, 1, 'jpg');
INSERT INTO `fileclassification` (`fileClassificationId`, `fileTypeId`, `fileExtendName`)
VALUES (3, 1, 'png');
INSERT INTO `fileclassification` (`fileClassificationId`, `fileTypeId`, `fileExtendName`)
VALUES (4, 1, 'tif');
INSERT INTO `fileclassification` (`fileClassificationId`, `fileTypeId`, `fileExtendName`)
VALUES (5, 1, 'gif');
INSERT INTO `fileclassification` (`fileClassificationId`, `fileTypeId`, `fileExtendName`)
VALUES (6, 1, 'jpeg');

INSERT INTO `fileclassification` (`fileClassificationId`, `fileTypeId`, `fileExtendName`)
VALUES (7, 2, 'doc');
INSERT INTO `fileclassification` (`fileClassificationId`, `fileTypeId`, `fileExtendName`)
VALUES (8, 2, 'docx');
INSERT INTO `fileclassification` (`fileClassificationId`, `fileTypeId`, `fileExtendName`)
VALUES (9, 2, 'docm');
INSERT INTO `fileclassification` (`fileClassificationId`, `fileTypeId`, `fileExtendName`)
VALUES (10, 2, 'dot');
INSERT INTO `fileclassification` (`fileClassificationId`, `fileTypeId`, `fileExtendName`)
VALUES (11, 2, 'dotx');
INSERT INTO `fileclassification` (`fileClassificationId`, `fileTypeId`, `fileExtendName`)
VALUES (12, 2, 'dotm');
INSERT INTO `fileclassification` (`fileClassificationId`, `fileTypeId`, `fileExtendName`)
VALUES (13, 2, 'odt');
INSERT INTO `fileclassification` (`fileClassificationId`, `fileTypeId`, `fileExtendName`)
VALUES (14, 2, 'fodt');
INSERT INTO `fileclassification` (`fileClassificationId`, `fileTypeId`, `fileExtendName`)
VALUES (15, 2, 'ott');
INSERT INTO `fileclassification` (`fileClassificationId`, `fileTypeId`, `fileExtendName`)
VALUES (16, 2, 'rtf');
INSERT INTO `fileclassification` (`fileClassificationId`, `fileTypeId`, `fileExtendName`)
VALUES (17, 2, 'txt');
INSERT INTO `fileclassification` (`fileClassificationId`, `fileTypeId`, `fileExtendName`)
VALUES (18, 2, 'html');
INSERT INTO `fileclassification` (`fileClassificationId`, `fileTypeId`, `fileExtendName`)
VALUES (19, 2, 'htm');
INSERT INTO `fileclassification` (`fileClassificationId`, `fileTypeId`, `fileExtendName`)
VALUES (20, 2, 'mht');
INSERT INTO `fileclassification` (`fileClassificationId`, `fileTypeId`, `fileExtendName`)
VALUES (21, 2, 'xml');
INSERT INTO `fileclassification` (`fileClassificationId`, `fileTypeId`, `fileExtendName`)
VALUES (22, 2, 'pdf');
INSERT INTO `fileclassification` (`fileClassificationId`, `fileTypeId`, `fileExtendName`)
VALUES (23, 2, 'djvu');
INSERT INTO `fileclassification` (`fileClassificationId`, `fileTypeId`, `fileExtendName`)
VALUES (24, 2, 'fb2');
INSERT INTO `fileclassification` (`fileClassificationId`, `fileTypeId`, `fileExtendName`)
VALUES (25, 2, 'epub');
INSERT INTO `fileclassification` (`fileClassificationId`, `fileTypeId`, `fileExtendName`)
VALUES (26, 2, 'xps');
INSERT INTO `fileclassification` (`fileClassificationId`, `fileTypeId`, `fileExtendName`)
VALUES (27, 2, 'xls');
INSERT INTO `fileclassification` (`fileClassificationId`, `fileTypeId`, `fileExtendName`)
VALUES (28, 2, 'xlsx');
INSERT INTO `fileclassification` (`fileClassificationId`, `fileTypeId`, `fileExtendName`)
VALUES (29, 2, 'xlsm');
INSERT INTO `fileclassification` (`fileClassificationId`, `fileTypeId`, `fileExtendName`)
VALUES (30, 2, 'xlt');
INSERT INTO `fileclassification` (`fileClassificationId`, `fileTypeId`, `fileExtendName`)
VALUES (31, 2, 'xltx');
INSERT INTO `fileclassification` (`fileClassificationId`, `fileTypeId`, `fileExtendName`)
VALUES (32, 2, 'xltm');
INSERT INTO `fileclassification` (`fileClassificationId`, `fileTypeId`, `fileExtendName`)
VALUES (33, 2, 'ods');
INSERT INTO `fileclassification` (`fileClassificationId`, `fileTypeId`, `fileExtendName`)
VALUES (34, 2, 'fods');
INSERT INTO `fileclassification` (`fileClassificationId`, `fileTypeId`, `fileExtendName`)
VALUES (35, 2, 'ots');
INSERT INTO `fileclassification` (`fileClassificationId`, `fileTypeId`, `fileExtendName`)
VALUES (36, 2, 'csv');
INSERT INTO `fileclassification` (`fileClassificationId`, `fileTypeId`, `fileExtendName`)
VALUES (37, 2, 'pps');
INSERT INTO `fileclassification` (`fileClassificationId`, `fileTypeId`, `fileExtendName`)
VALUES (38, 2, 'ppsx');
INSERT INTO `fileclassification` (`fileClassificationId`, `fileTypeId`, `fileExtendName`)
VALUES (39, 2, 'ppsm');
INSERT INTO `fileclassification` (`fileClassificationId`, `fileTypeId`, `fileExtendName`)
VALUES (40, 2, 'ppt');
INSERT INTO `fileclassification` (`fileClassificationId`, `fileTypeId`, `fileExtendName`)
VALUES (41, 2, 'pptx');
INSERT INTO `fileclassification` (`fileClassificationId`, `fileTypeId`, `fileExtendName`)
VALUES (42, 2, 'pptm');
INSERT INTO `fileclassification` (`fileClassificationId`, `fileTypeId`, `fileExtendName`)
VALUES (43, 2, 'pot');
INSERT INTO `fileclassification` (`fileClassificationId`, `fileTypeId`, `fileExtendName`)
VALUES (44, 2, 'potx');
INSERT INTO `fileclassification` (`fileClassificationId`, `fileTypeId`, `fileExtendName`)
VALUES (45, 2, 'potm');
INSERT INTO `fileclassification` (`fileClassificationId`, `fileTypeId`, `fileExtendName`)
VALUES (46, 2, 'odp');
INSERT INTO `fileclassification` (`fileClassificationId`, `fileTypeId`, `fileExtendName`)
VALUES (47, 2, 'fodp');
INSERT INTO `fileclassification` (`fileClassificationId`, `fileTypeId`, `fileExtendName`)
VALUES (48, 2, 'otp');
INSERT INTO `fileclassification` (`fileClassificationId`, `fileTypeId`, `fileExtendName`)
VALUES (49, 2, 'hlp');
INSERT INTO `fileclassification` (`fileClassificationId`, `fileTypeId`, `fileExtendName`)
VALUES (50, 2, 'wps');

INSERT INTO `fileclassification` (`fileClassificationId`, `fileTypeId`, `fileExtendName`)
VALUES (51, 2, 'java');
INSERT INTO `fileclassification` (`fileClassificationId`, `fileTypeId`, `fileExtendName`)
VALUES (52, 2, 'js');
INSERT INTO `fileclassification` (`fileClassificationId`, `fileTypeId`, `fileExtendName`)
VALUES (53, 2, 'css');
INSERT INTO `fileclassification` (`fileClassificationId`, `fileTypeId`, `fileExtendName`)
VALUES (54, 2, 'json');


INSERT INTO `fileclassification` (`fileClassificationId`, `fileTypeId`, `fileExtendName`)
VALUES (55, 3, 'avi');
INSERT INTO `fileclassification` (`fileClassificationId`, `fileTypeId`, `fileExtendName`)
VALUES (56, 3, 'mp4');
INSERT INTO `fileclassification` (`fileClassificationId`, `fileTypeId`, `fileExtendName`)
VALUES (57, 3, 'mpg');
INSERT INTO `fileclassification` (`fileClassificationId`, `fileTypeId`, `fileExtendName`)
VALUES (58, 3, 'mov');
INSERT INTO `fileclassification` (`fileClassificationId`, `fileTypeId`, `fileExtendName`)
VALUES (59, 3, 'swf');

INSERT INTO `fileclassification` (`fileClassificationId`, `fileTypeId`, `fileExtendName`)
VALUES (60, 4, 'wav');
INSERT INTO `fileclassification` (`fileClassificationId`, `fileTypeId`, `fileExtendName`)
VALUES (61, 4, 'aif');
INSERT INTO `fileclassification` (`fileClassificationId`, `fileTypeId`, `fileExtendName`)
VALUES (62, 4, 'au');
INSERT INTO `fileclassification` (`fileClassificationId`, `fileTypeId`, `fileExtendName`)
VALUES (63, 4, 'mp3');
INSERT INTO `fileclassification` (`fileClassificationId`, `fileTypeId`, `fileExtendName`)
VALUES (64, 4, 'ram');
INSERT INTO `fileclassification` (`fileClassificationId`, `fileTypeId`, `fileExtendName`)
VALUES (65, 4, 'wma');
INSERT INTO `fileclassification` (`fileClassificationId`, `fileTypeId`, `fileExtendName`)
VALUES (66, 4, 'mmf');
INSERT INTO `fileclassification` (`fileClassificationId`, `fileTypeId`, `fileExtendName`)
VALUES (67, 4, 'amr');
INSERT INTO `fileclassification` (`fileClassificationId`, `fileTypeId`, `fileExtendName`)
VALUES (68, 4, 'aac');
INSERT INTO `fileclassification` (`fileClassificationId`, `fileTypeId`, `fileExtendName`)
VALUES (69, 4, 'flac');

INSERT INTO `fileclassification` (`fileClassificationId`, `fileTypeId`, `fileExtendName`)
VALUES (70, 2, 'md');
INSERT INTO `fileclassification` (`fileClassificationId`, `fileTypeId`, `fileExtendName`)
VALUES (71, 2, 'markdown');
