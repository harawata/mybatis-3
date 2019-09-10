--
--    Copyright 2009-2019 the original author or authors.
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

drop table author if exists;
drop table blog if exists;
drop table post if exists;

create table author (
  id int,
  name varchar(20),
  date_of_birth date
);

insert into author (id, name, date_of_birth) values
(1, 'Mary', '2000-12-31'),
(2, 'Paul', '1999-01-01');

create table blog (
  id int,
  title varchar(20),
  author_id int
);

insert into blog (id, title, author_id) values
(1, 'Blog1', 1),
(2, 'Blog2', 2);

create table post (
  id int,
  body varchar(64),
  blog_id int
);

insert into post (id, body, blog_id) values
(1, 'Post1', 1),
(2, 'Post2', 1);
