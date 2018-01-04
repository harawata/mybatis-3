--
--    Copyright 2009-2016 the original author or authors.
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

drop table users if exists;
drop table pets if exists;

create table users (
  id int,
  name varchar(20),
  dob date
);

create table pets (
  id int,
  user_id int,
  name varchar(20)
);

insert into users (id, name, dob) values
(1, 'User1', '1970-01-02'),
(2, null, null),
(3, 'User3', null);

insert into pets (id, user_id, name) values
(1, 1, 'Pet1'),
(2, 3, 'PetP'),
(3, 3, 'PetQ');
