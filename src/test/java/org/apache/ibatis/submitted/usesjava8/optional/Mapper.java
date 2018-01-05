/**
 *    Copyright 2009-2018 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.submitted.usesjava8.optional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import org.apache.ibatis.annotations.Arg;
import org.apache.ibatis.annotations.ConstructorArgs;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

public interface Mapper {
  @Select("select * from users where id = #{id}")
  User getBeanWithOptionalProperties(Integer id);

  Optional<User> getBeanWithOptionalCollectionXml(Integer id);

  Optional<User> getBeanWithOptionalAssociationXml(Integer id);

  Optional<User> getBeanWithOptionalInlineXml(Integer id);

  @Select("select * from users where id = #{id}")
  Optional<GenericUser<String, LocalDate>> getGenericUserAutoMapping(Integer id);

  @Results({ @Result(property = "id", column = "id", id = true), @Result(property = "name", column = "name"), @Result(property = "dob", column = "dob"), @Result(property = "pet", column = "id", one = @One(select = "getPetByUserId")) })
  @Select("select * from users where id = #{id}")
  Optional<User> getBeanWithOptionalAssociationAnno(Integer id);

  @ConstructorArgs({ @Arg(id = true, column = "id", javaType = Integer.class),
      @Arg(column = "name", javaType = String.class), @Arg(column = "dob", javaType = LocalDate.class),
      @Arg(column = "rank", javaType = OptionalInt.class) })
  @Select("select id, name, dob, rank from users where id = #{id}")
  UserWithConstructor getUserWithConstructor(Integer id);

  @Select("select id, name from users where id = #{id}")
  UserWithConstructor getUserWithConstructorWithoutMapping(Integer id);

  UserWithConstructor getUserWithConstructorXml(Integer id);

  UserWithConstructor getUserWithConstructorXml2(Integer id);

  @Select("select name from users where id = #{id}")
  Optional<String> getUserName(Integer id);

  @Select("select * from users where id = #{id}")
  Optional<User> getOptionalUser(Integer id);

  @Select("select * from users")
  List<Optional<User>> getOptionalUsers();

  @Select("select name, dob from users where id = #{id}")
  Optional<User> getOptionalNameAndDob(Integer id);

  Optional<User> getOptionalUserXml(Integer id);

  @Select("select rank from users where id = #{id}")
  OptionalInt getOptionalRank(Integer id);

  @Insert("insert into users (id, name, rank) values(#{id}, #{name}, #{rank})")
  void insertUser(User user);
}
