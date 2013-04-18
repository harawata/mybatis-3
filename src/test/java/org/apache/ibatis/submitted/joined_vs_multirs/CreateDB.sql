--
--    Copyright 2009-2013 The MyBatis Team
--
--    Licensed under the Apache License, Version 2.0 (the "License");
--    you may not use this file except in compliance with the License.
--    You may obtain a copy of the License at
--
--       http://www.apache.org/licenses/LICENSE-2.0
--
--    Unless required by applicable law or agreed to in writing, software
--    distributed under the License is distributed on an "AS IS" BASIS,
--    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
--    See the License for the specific language governing permissions and
--    limitations under the License.
--

drop procedure getmultirs if exists
go

drop procedure getjoined if exists
go

create table parent (
  id integer,
  name varchar(20),
  team integer,
  primary key(id)
)
go

create table child (
  id integer,
  parent_id integer,
  f1 varchar(20),
  f2 varchar(20),
  f3 varchar(20),
  f4 varchar(20),
  f5 varchar(20),
  primary key(id)
)
go

create procedure getjoined(in teamId integer)
reads sql data
dynamic result sets 1
BEGIN ATOMIC
  declare cur1 cursor for select
    parent.id as parent_id,
    parent.name as parent_name,
    team,
    c1.id as c1_id,
    c1.parent_id as c1_parent_id,
    c1.f1 as c1_f1,
    c1.f2 as c1_f2,
    c1.f3 as c1_f3,
    c1.f4 as c1_f4,
    c1.f5 as c1_f5,
    c2.id as c2_id,
    c2.parent_id as c2_parent_id,
    c2.f1 as c2_f1,
    c2.f2 as c2_f2,
    c2.f3 as c2_f3,
    c2.f4 as c2_f4,
    c2.f5 as c2_f5,
    c3.id as c3_id,
    c3.parent_id as c3_parent_id,
    c3.f1 as c3_f1,
    c3.f2 as c3_f2,
    c3.f3 as c3_f3,
    c3.f4 as c3_f4,
    c3.f5 as c3_f5,
    c4.id as c4_id,
    c4.parent_id as c4_parent_id,
    c4.f1 as c4_f1,
    c4.f2 as c4_f2,
    c4.f3 as c4_f3,
    c4.f4 as c4_f4,
    c4.f5 as c4_f5,
    c5.id as c5_id,
    c5.parent_id as c5_parent_id,
    c5.f1 as c5_f1,
    c5.f2 as c5_f2,
    c5.f3 as c5_f3,
    c5.f4 as c5_f4,
    c5.f5 as c5_f5,
    c6.id as c6_id,
    c6.parent_id as c6_parent_id,
    c6.f1 as c6_f1,
    c6.f2 as c6_f2,
    c6.f3 as c6_f3,
    c6.f4 as c6_f4,
    c6.f5 as c6_f5
    from parent
    left join child c1 on c1.parent_id = parent.id
    left join child c2 on c2.parent_id = parent.id
    left join child c3 on c3.parent_id = parent.id
    left join child c4 on c4.parent_id = parent.id
    left join child c5 on c5.parent_id = parent.id
    left join child c6 on c6.parent_id = parent.id
    where parent.team = teamId
    order by parent.id, c1.id, c2.id, c3.id, c4.id, c5.id, c6.id
    for read only;
  open cur1;
END
go

create procedure getmultirs(in teamId integer)
reads sql data
dynamic result sets 7
BEGIN ATOMIC
  declare cur1 cursor for select * from parent where team = teamId order by id for read only;
  declare cur2 cursor for select * from child
    where parent_id in (select id from parent where team = teamId) order by id for read only;
  declare cur3 cursor for select * from child
    where parent_id in (select id from parent where team = teamId) order by id for read only;
  declare cur4 cursor for select * from child
    where parent_id in (select id from parent where team = teamId) order by id for read only;
  declare cur5 cursor for select * from child
    where parent_id in (select id from parent where team = teamId) order by id for read only;
  declare cur6 cursor for select * from child
    where parent_id in (select id from parent where team = teamId) order by id for read only;
  declare cur7 cursor for select * from child
    where parent_id in (select id from parent where team = teamId) order by id for read only;
  open cur1;
  open cur2;
  open cur3;
  open cur4;
  open cur5;
  open cur6;
  open cur7;
END
go

insert into parent (id, name, team) values
(1, 'Parent 1', 100),
(2, 'Parent 2', 100),
(3, 'Parent 3', 100),
(4, 'Parent 4', 100),
(5, 'Parent 5', 100),
(6, 'Parent 6', 100),
(7, 'Parent 7', 200),
(8, 'Parent 8', 100),
(9, 'Parent 9', 100),
(10, 'Parent 10', 100)
go

insert into child (id, parent_id, f1, f2, f3, f4, f5) values 
(1, 1, 'The', 'quick', 'brown', 'fox', 'jumps'),
(2, 1, 'The', 'quick', 'brown', 'fox', 'jumps'),
(3, 1, 'The', 'quick', 'brown', 'fox', 'jumps'),
(4, 1, 'The', 'quick', 'brown', 'fox', 'jumps'),
(5, 1, 'The', 'quick', 'brown', 'fox', 'jumps'),
(6, 1, 'The', 'quick', 'brown', 'fox', 'jumps'),
(7, 1, 'The', 'quick', 'brown', 'fox', 'jumps'),
(8, 1, 'The', 'quick', 'brown', 'fox', 'jumps'),
(9, 1, 'The', 'quick', 'brown', 'fox', 'jumps'),
(10, 1, 'The', 'quick', 'brown', 'fox', 'jumps'),
(11, 1, 'The', 'quick', 'brown', 'fox', 'jumps'),
(12, 1, 'The', 'quick', 'brown', 'fox', 'jumps'),
(13, 1, 'The', 'quick', 'brown', 'fox', 'jumps'),
(14, 1, 'The', 'quick', 'brown', 'fox', 'jumps'),
(15, 1, 'The', 'quick', 'brown', 'fox', 'jumps'),
(16, 1, 'The', 'quick', 'brown', 'fox', 'jumps'),
(17, 1, 'The', 'quick', 'brown', 'fox', 'jumps'),
(18, 1, 'The', 'quick', 'brown', 'fox', 'jumps'),
(19, 1, 'The', 'quick', 'brown', 'fox', 'jumps'),
(20, 1, 'The', 'quick', 'brown', 'fox', 'jumps'),
(21, 2, 'The', 'quick', 'brown', 'fox', 'jumps'),
(22, 2, 'The', 'quick', 'brown', 'fox', 'jumps'),
(23, 2, 'The', 'quick', 'brown', 'fox', 'jumps'),
(24, 2, 'The', 'quick', 'brown', 'fox', 'jumps'),
(25, 2, 'The', 'quick', 'brown', 'fox', 'jumps'),
(26, 2, 'The', 'quick', 'brown', 'fox', 'jumps'),
(27, 2, 'The', 'quick', 'brown', 'fox', 'jumps'),
(28, 2, 'The', 'quick', 'brown', 'fox', 'jumps'),
(29, 2, 'The', 'quick', 'brown', 'fox', 'jumps'),
(30, 2, 'The', 'quick', 'brown', 'fox', 'jumps'),
(31, 2, 'The', 'quick', 'brown', 'fox', 'jumps'),
(32, 2, 'The', 'quick', 'brown', 'fox', 'jumps'),
(33, 2, 'The', 'quick', 'brown', 'fox', 'jumps'),
(34, 2, 'The', 'quick', 'brown', 'fox', 'jumps'),
(35, 2, 'The', 'quick', 'brown', 'fox', 'jumps'),
(36, 2, 'The', 'quick', 'brown', 'fox', 'jumps'),
(37, 2, 'The', 'quick', 'brown', 'fox', 'jumps'),
(38, 2, 'The', 'quick', 'brown', 'fox', 'jumps'),
(39, 2, 'The', 'quick', 'brown', 'fox', 'jumps'),
(40, 2, 'The', 'quick', 'brown', 'fox', 'jumps'),
(41, 3, 'The', 'quick', 'brown', 'fox', 'jumps'),
(42, 3, 'The', 'quick', 'brown', 'fox', 'jumps'),
(43, 3, 'The', 'quick', 'brown', 'fox', 'jumps'),
(44, 3, 'The', 'quick', 'brown', 'fox', 'jumps'),
(45, 3, 'The', 'quick', 'brown', 'fox', 'jumps'),
(46, 3, 'The', 'quick', 'brown', 'fox', 'jumps'),
(47, 3, 'The', 'quick', 'brown', 'fox', 'jumps'),
(48, 3, 'The', 'quick', 'brown', 'fox', 'jumps'),
(49, 3, 'The', 'quick', 'brown', 'fox', 'jumps'),
(50, 3, 'The', 'quick', 'brown', 'fox', 'jumps'),
(51, 3, 'The', 'quick', 'brown', 'fox', 'jumps'),
(52, 3, 'The', 'quick', 'brown', 'fox', 'jumps'),
(53, 3, 'The', 'quick', 'brown', 'fox', 'jumps'),
(54, 3, 'The', 'quick', 'brown', 'fox', 'jumps'),
(55, 3, 'The', 'quick', 'brown', 'fox', 'jumps'),
(56, 3, 'The', 'quick', 'brown', 'fox', 'jumps'),
(57, 3, 'The', 'quick', 'brown', 'fox', 'jumps'),
(58, 3, 'The', 'quick', 'brown', 'fox', 'jumps'),
(59, 3, 'The', 'quick', 'brown', 'fox', 'jumps'),
(60, 3, 'The', 'quick', 'brown', 'fox', 'jumps'),
(61, 4, 'The', 'quick', 'brown', 'fox', 'jumps'),
(62, 4, 'The', 'quick', 'brown', 'fox', 'jumps'),
(63, 4, 'The', 'quick', 'brown', 'fox', 'jumps'),
(64, 4, 'The', 'quick', 'brown', 'fox', 'jumps'),
(65, 4, 'The', 'quick', 'brown', 'fox', 'jumps'),
(66, 4, 'The', 'quick', 'brown', 'fox', 'jumps'),
(67, 4, 'The', 'quick', 'brown', 'fox', 'jumps'),
(68, 4, 'The', 'quick', 'brown', 'fox', 'jumps'),
(69, 4, 'The', 'quick', 'brown', 'fox', 'jumps'),
(70, 4, 'The', 'quick', 'brown', 'fox', 'jumps'),
(71, 4, 'The', 'quick', 'brown', 'fox', 'jumps'),
(72, 4, 'The', 'quick', 'brown', 'fox', 'jumps'),
(73, 4, 'The', 'quick', 'brown', 'fox', 'jumps'),
(74, 4, 'The', 'quick', 'brown', 'fox', 'jumps'),
(75, 4, 'The', 'quick', 'brown', 'fox', 'jumps'),
(76, 4, 'The', 'quick', 'brown', 'fox', 'jumps'),
(77, 4, 'The', 'quick', 'brown', 'fox', 'jumps'),
(78, 4, 'The', 'quick', 'brown', 'fox', 'jumps'),
(79, 4, 'The', 'quick', 'brown', 'fox', 'jumps'),
(80, 4, 'The', 'quick', 'brown', 'fox', 'jumps'),
(81, 5, 'The', 'quick', 'brown', 'fox', 'jumps'),
(82, 5, 'The', 'quick', 'brown', 'fox', 'jumps'),
(83, 5, 'The', 'quick', 'brown', 'fox', 'jumps'),
(84, 5, 'The', 'quick', 'brown', 'fox', 'jumps'),
(85, 5, 'The', 'quick', 'brown', 'fox', 'jumps'),
(86, 5, 'The', 'quick', 'brown', 'fox', 'jumps'),
(87, 5, 'The', 'quick', 'brown', 'fox', 'jumps'),
(88, 5, 'The', 'quick', 'brown', 'fox', 'jumps'),
(89, 5, 'The', 'quick', 'brown', 'fox', 'jumps'),
(90, 5, 'The', 'quick', 'brown', 'fox', 'jumps'),
(91, 5, 'The', 'quick', 'brown', 'fox', 'jumps'),
(92, 5, 'The', 'quick', 'brown', 'fox', 'jumps'),
(93, 5, 'The', 'quick', 'brown', 'fox', 'jumps'),
(94, 5, 'The', 'quick', 'brown', 'fox', 'jumps'),
(95, 5, 'The', 'quick', 'brown', 'fox', 'jumps'),
(96, 5, 'The', 'quick', 'brown', 'fox', 'jumps'),
(97, 5, 'The', 'quick', 'brown', 'fox', 'jumps'),
(98, 5, 'The', 'quick', 'brown', 'fox', 'jumps'),
(99, 5, 'The', 'quick', 'brown', 'fox', 'jumps'),
(100, 5, 'The', 'quick', 'brown', 'fox', 'jumps'),
(101, 6, 'The', 'quick', 'brown', 'fox', 'jumps'),
(102, 6, 'The', 'quick', 'brown', 'fox', 'jumps'),
(103, 6, 'The', 'quick', 'brown', 'fox', 'jumps'),
(104, 6, 'The', 'quick', 'brown', 'fox', 'jumps'),
(105, 6, 'The', 'quick', 'brown', 'fox', 'jumps'),
(106, 6, 'The', 'quick', 'brown', 'fox', 'jumps'),
(107, 6, 'The', 'quick', 'brown', 'fox', 'jumps'),
(108, 6, 'The', 'quick', 'brown', 'fox', 'jumps'),
(109, 6, 'The', 'quick', 'brown', 'fox', 'jumps'),
(110, 6, 'The', 'quick', 'brown', 'fox', 'jumps'),
(111, 6, 'The', 'quick', 'brown', 'fox', 'jumps'),
(112, 6, 'The', 'quick', 'brown', 'fox', 'jumps'),
(113, 6, 'The', 'quick', 'brown', 'fox', 'jumps'),
(114, 6, 'The', 'quick', 'brown', 'fox', 'jumps'),
(115, 6, 'The', 'quick', 'brown', 'fox', 'jumps'),
(116, 6, 'The', 'quick', 'brown', 'fox', 'jumps'),
(117, 6, 'The', 'quick', 'brown', 'fox', 'jumps'),
(118, 6, 'The', 'quick', 'brown', 'fox', 'jumps'),
(119, 6, 'The', 'quick', 'brown', 'fox', 'jumps'),
(120, 6, 'The', 'quick', 'brown', 'fox', 'jumps'),
(121, 7, 'The', 'quick', 'brown', 'fox', 'jumps'),
(122, 7, 'The', 'quick', 'brown', 'fox', 'jumps'),
(123, 7, 'The', 'quick', 'brown', 'fox', 'jumps'),
(124, 7, 'The', 'quick', 'brown', 'fox', 'jumps'),
(125, 7, 'The', 'quick', 'brown', 'fox', 'jumps'),
(126, 7, 'The', 'quick', 'brown', 'fox', 'jumps'),
(127, 7, 'The', 'quick', 'brown', 'fox', 'jumps'),
(128, 7, 'The', 'quick', 'brown', 'fox', 'jumps'),
(129, 7, 'The', 'quick', 'brown', 'fox', 'jumps'),
(130, 7, 'The', 'quick', 'brown', 'fox', 'jumps'),
(131, 7, 'The', 'quick', 'brown', 'fox', 'jumps'),
(132, 7, 'The', 'quick', 'brown', 'fox', 'jumps'),
(133, 7, 'The', 'quick', 'brown', 'fox', 'jumps'),
(134, 7, 'The', 'quick', 'brown', 'fox', 'jumps'),
(135, 7, 'The', 'quick', 'brown', 'fox', 'jumps'),
(136, 7, 'The', 'quick', 'brown', 'fox', 'jumps'),
(137, 7, 'The', 'quick', 'brown', 'fox', 'jumps'),
(138, 7, 'The', 'quick', 'brown', 'fox', 'jumps'),
(139, 7, 'The', 'quick', 'brown', 'fox', 'jumps'),
(140, 7, 'The', 'quick', 'brown', 'fox', 'jumps'),
(141, 8, 'The', 'quick', 'brown', 'fox', 'jumps'),
(142, 8, 'The', 'quick', 'brown', 'fox', 'jumps'),
(143, 8, 'The', 'quick', 'brown', 'fox', 'jumps'),
(144, 8, 'The', 'quick', 'brown', 'fox', 'jumps'),
(145, 8, 'The', 'quick', 'brown', 'fox', 'jumps'),
(146, 8, 'The', 'quick', 'brown', 'fox', 'jumps'),
(147, 8, 'The', 'quick', 'brown', 'fox', 'jumps'),
(148, 8, 'The', 'quick', 'brown', 'fox', 'jumps'),
(149, 8, 'The', 'quick', 'brown', 'fox', 'jumps'),
(150, 8, 'The', 'quick', 'brown', 'fox', 'jumps'),
(151, 8, 'The', 'quick', 'brown', 'fox', 'jumps'),
(152, 8, 'The', 'quick', 'brown', 'fox', 'jumps'),
(153, 8, 'The', 'quick', 'brown', 'fox', 'jumps'),
(154, 8, 'The', 'quick', 'brown', 'fox', 'jumps'),
(155, 8, 'The', 'quick', 'brown', 'fox', 'jumps'),
(156, 8, 'The', 'quick', 'brown', 'fox', 'jumps'),
(157, 8, 'The', 'quick', 'brown', 'fox', 'jumps'),
(158, 8, 'The', 'quick', 'brown', 'fox', 'jumps'),
(159, 8, 'The', 'quick', 'brown', 'fox', 'jumps'),
(160, 8, 'The', 'quick', 'brown', 'fox', 'jumps'),
(161, 9, 'The', 'quick', 'brown', 'fox', 'jumps'),
(162, 9, 'The', 'quick', 'brown', 'fox', 'jumps'),
(163, 9, 'The', 'quick', 'brown', 'fox', 'jumps'),
(164, 9, 'The', 'quick', 'brown', 'fox', 'jumps'),
(165, 9, 'The', 'quick', 'brown', 'fox', 'jumps'),
(166, 9, 'The', 'quick', 'brown', 'fox', 'jumps'),
(167, 9, 'The', 'quick', 'brown', 'fox', 'jumps'),
(168, 9, 'The', 'quick', 'brown', 'fox', 'jumps'),
(169, 9, 'The', 'quick', 'brown', 'fox', 'jumps'),
(170, 9, 'The', 'quick', 'brown', 'fox', 'jumps'),
(171, 9, 'The', 'quick', 'brown', 'fox', 'jumps'),
(172, 9, 'The', 'quick', 'brown', 'fox', 'jumps'),
(173, 9, 'The', 'quick', 'brown', 'fox', 'jumps'),
(174, 9, 'The', 'quick', 'brown', 'fox', 'jumps'),
(175, 9, 'The', 'quick', 'brown', 'fox', 'jumps'),
(176, 9, 'The', 'quick', 'brown', 'fox', 'jumps'),
(177, 9, 'The', 'quick', 'brown', 'fox', 'jumps'),
(178, 9, 'The', 'quick', 'brown', 'fox', 'jumps'),
(179, 9, 'The', 'quick', 'brown', 'fox', 'jumps'),
(180, 9, 'The', 'quick', 'brown', 'fox', 'jumps'),
(181, 10, 'The', 'quick', 'brown', 'fox', 'jumps'),
(182, 10, 'The', 'quick', 'brown', 'fox', 'jumps'),
(183, 10, 'The', 'quick', 'brown', 'fox', 'jumps'),
(184, 10, 'The', 'quick', 'brown', 'fox', 'jumps'),
(185, 10, 'The', 'quick', 'brown', 'fox', 'jumps'),
(186, 10, 'The', 'quick', 'brown', 'fox', 'jumps'),
(187, 10, 'The', 'quick', 'brown', 'fox', 'jumps'),
(188, 10, 'The', 'quick', 'brown', 'fox', 'jumps'),
(189, 10, 'The', 'quick', 'brown', 'fox', 'jumps'),
(190, 10, 'The', 'quick', 'brown', 'fox', 'jumps'),
(191, 10, 'The', 'quick', 'brown', 'fox', 'jumps'),
(192, 10, 'The', 'quick', 'brown', 'fox', 'jumps'),
(193, 10, 'The', 'quick', 'brown', 'fox', 'jumps'),
(194, 10, 'The', 'quick', 'brown', 'fox', 'jumps'),
(195, 10, 'The', 'quick', 'brown', 'fox', 'jumps'),
(196, 10, 'The', 'quick', 'brown', 'fox', 'jumps'),
(197, 10, 'The', 'quick', 'brown', 'fox', 'jumps'),
(198, 10, 'The', 'quick', 'brown', 'fox', 'jumps'),
(199, 10, 'The', 'quick', 'brown', 'fox', 'jumps'),
(200, 10, 'The', 'quick', 'brown', 'fox', 'jumps')
go
