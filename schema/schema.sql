USE cc;

DROP TABLE IF EXISTS `c`;
CREATE TABLE `c` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CHANGE_SET_ID` bigint(20) NOT NULL,
  `TYPE` varchar(1) DEFAULT NULL,
  `PATH` varchar(128) DEFAULT NULL,
  `VALUE` longtext,
  `CREATED_AT` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `cs`;
CREATE TABLE `cs` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `STATE` varchar(32) DEFAULT NULL,
  `SEQ` int(11) DEFAULT NULL,
  `OWNER` varchar(32) DEFAULT NULL,
  `CREATED_AT` datetime DEFAULT NULL,
  `UPDATED_AT` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

--
-- published 4,5,6,8,11
--
insert into cs (seq, owner, state) values (1,    'user-a', 'published');
insert into cs (seq, owner, state) values (2,    'user-b', 'published');
insert into cs (seq, owner, state) values (3,    'user-c', 'published');
insert into cs (seq, owner, state) values (null, 'user-a', 'editing');
insert into cs (seq, owner, state) values (4,    'user-b', 'published');
insert into cs (seq, owner, state) values (null, 'user-b', 'editing');
insert into cs (seq, owner, state) values (null, 'user-b', 'staged');
insert into cs (seq, owner, state) values (5,    'user-c', 'published');

insert into c (change_set_id, type, path, value) values (4,  'C', '/root/cache1', 'cache1-v1');
insert into c (change_set_id, type, path, value) values (7,  'U', '/root/cache1', 'cache1-v2');
insert into c (change_set_id, type, path, value) values (5,  'C', '/root/cache2', 'cache2-v1');
insert into c (change_set_id, type, path, value) values (8,  'U', '/root/cache2', 'cache2-v2');
insert into c (change_set_id, type, path, value) values (9,  'U', '/root/cache2', 'cache2-v3');
insert into c (change_set_id, type, path, value) values (10, 'U', '/root/cache2', 'cache2-v4');
insert into c (change_set_id, type, path, value) values (6,  'C', '/root/cache3', 'cache3-v1');
insert into c (change_set_id, type, path, value) values (4,  'C', '/root/service1', 'service1-v1');
insert into c (change_set_id, type, path, value) values (7,  'U', '/root/service1', 'service1-v2');
insert into c (change_set_id, type, path, value) values (7,  'C', '/root/service1-1', 'service1-1-v1');
insert into c (change_set_id, type, path, value) values (5,  'C', '/root/service2', 'service2-v1');
insert into c (change_set_id, type, path, value) values (8,  'U', '/root/service2', 'service2-v2');
insert into c (change_set_id, type, path, value) values (9,  'U', '/root/service2', 'service2-v3');
insert into c (change_set_id, type, path, value) values (10, 'D', '/root/service2', 'deleted');
insert into c (change_set_id, type, path, value) values (8,  'C', '/root/service2-0', 'service2-0-v1');
insert into c (change_set_id, type, path, value) values (9,  'C', '/root/service2-1', 'service2-1-v1');
insert into c (change_set_id, type, path, value) values (7,  'U', '/root/service2-1', 'service2-1-v2');
insert into c (change_set_id, type, path, value) values (10, 'C', '/root/service2-2', 'service2-2-v1');
insert into c (change_set_id, type, path, value) values (6,  'C', '/root/service3', 'service3-v1');
insert into c (change_set_id, type, path, value) values (11, 'D', '/root/service3', 'deleted');

