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

import static org.junit.Assert.*;

import java.io.Reader;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.BeforeClass;
import org.junit.Test;

public class OptionalTest {
  private static SqlSessionFactory sqlSessionFactory;

  @BeforeClass
  public static void setUp() throws Exception {
    // create an SqlSessionFactory
    Reader reader = Resources.getResourceAsReader("org/apache/ibatis/submitted/usesjava8/optional/mybatis-config.xml");
    sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
    reader.close();

    // populate in-memory database
    SqlSession session = sqlSessionFactory.openSession();
    Connection conn = session.getConnection();
    reader = Resources.getResourceAsReader("org/apache/ibatis/submitted/usesjava8/optional/CreateDB.sql");
    ScriptRunner runner = new ScriptRunner(conn);
    runner.setLogWriter(null);
    runner.runScript(reader);
    reader.close();
    session.close();
  }

  @Test
  public void shouldSetOptionalToProperty() {
    SqlSession sqlSession = sqlSessionFactory.openSession();
    try {
      Mapper mapper = sqlSession.getMapper(Mapper.class);
      User user = mapper.getBeanWithOptionalProperties(1);
      assertEquals("User1", user.getName().get());
      assertNotNull(user.getDob().get());
      assertEquals(10, user.getRank().getAsInt());
    } finally {
      sqlSession.close();
    }
  }

  @Test
  public void shouldHandleGenericOptional() {
    SqlSession sqlSession = sqlSessionFactory.openSession();
    try {
      Mapper mapper = sqlSession.getMapper(Mapper.class);
      Optional<GenericUser<String, LocalDate>> user = mapper.getGenericUserAutoMapping(1);
      assertEquals("User1", user.get().getName().get());
      assertNotNull(user.get().getDob().get());
    } finally {
      sqlSession.close();
    }
  }

  @Test
  public void shouldSetEmptyToPropertyIfColumnIsNull() {
    SqlSession sqlSession = sqlSessionFactory.openSession();
    try {
      Mapper mapper = sqlSession.getMapper(Mapper.class);
      User user = mapper.getBeanWithOptionalProperties(2);
      assertFalse(user.getName().isPresent());
      assertFalse(user.getDob().isPresent());
      assertFalse(user.getRank().isPresent());
    } finally {
      sqlSession.close();
    }
  }

  @Test
  public void shouldOptionalConstructorArgWork() {
    SqlSession sqlSession = sqlSessionFactory.openSession();
    try {
      Mapper mapper = sqlSession.getMapper(Mapper.class);
      UserWithConstructor user = mapper.getUserWithConstructor(1);
      assertEquals("User1", user.getName().get());
      assertEquals(10, user.getRank().getAsInt());
    } finally {
      sqlSession.close();
    }
  }

  @Test
  public void shouldEmptyOptionalConstructorArgWork() {
    SqlSession sqlSession = sqlSessionFactory.openSession();
    try {
      Mapper mapper = sqlSession.getMapper(Mapper.class);
      UserWithConstructor user = mapper.getUserWithConstructor(2);
      assertFalse(user.getName().isPresent());
      assertFalse(user.getRank().isPresent());
    } finally {
      sqlSession.close();
    }
  }

  @Test
  public void shouldOptionalConstructorArgWithoutMappingWork() {
    SqlSession sqlSession = sqlSessionFactory.openSession();
    try {
      Mapper mapper = sqlSession.getMapper(Mapper.class);
      UserWithConstructor user = mapper.getUserWithConstructorWithoutMapping(1);
      assertEquals("User1", user.getName().get());
    } finally {
      sqlSession.close();
    }
  }

  @Test
  public void shouldGetUserWithConstructorXml() {
    SqlSession sqlSession = sqlSessionFactory.openSession();
    try {
      Mapper mapper = sqlSession.getMapper(Mapper.class);
      UserWithConstructor user = mapper.getUserWithConstructorXml(1);
      assertEquals("User1", user.getName().get());
      assertTrue(user.getDob().isPresent());
    } finally {
      sqlSession.close();
    }
  }

  @Test
  public void shouldGetUserWithConstructorXml2() {
    SqlSession sqlSession = sqlSessionFactory.openSession();
    try {
      Mapper mapper = sqlSession.getMapper(Mapper.class);
      UserWithConstructor user = mapper.getUserWithConstructorXml2(1);
      assertEquals("User1", user.getName().get());
      assertTrue(user.getDob().isPresent());
    } finally {
      sqlSession.close();
    }
  }

  @Test
  public void shouldReturnOptionalSimpleType() {
    SqlSession sqlSession = sqlSessionFactory.openSession();
    try {
      Mapper mapper = sqlSession.getMapper(Mapper.class);
      Optional<String> name = mapper.getUserName(1);
      assertEquals("User1", name.get());
    } finally {
      sqlSession.close();
    }
  }

  @Test
  public void shouldReturnEmptyWhenTheColumnIsNull() {
    SqlSession sqlSession = sqlSessionFactory.openSession();
    try {
      Mapper mapper = sqlSession.getMapper(Mapper.class);
      Optional<String> name = mapper.getUserName(2);
      assertFalse(name.isPresent());
    } finally {
      sqlSession.close();
    }
  }

  @Test
  public void shouldReturnEmptyWhenThereIsNoMatchingRow_SimpleType() {
    SqlSession sqlSession = sqlSessionFactory.openSession();
    try {
      Mapper mapper = sqlSession.getMapper(Mapper.class);
      Optional<String> name = mapper.getUserName(9999);
      assertFalse(name.isPresent());
    } finally {
      sqlSession.close();
    }
  }

  @Test
  public void shouldReturnOptionalBean_Anno() {
    SqlSession sqlSession = sqlSessionFactory.openSession();
    try {
      Mapper mapper = sqlSession.getMapper(Mapper.class);
      Optional<User> user = mapper.getOptionalUser(1);
      assertEquals("User1", user.get().getName().get());
      assertTrue(user.get().getDob().isPresent());
    } finally {
      sqlSession.close();
    }
  }

  @Test
  public void shouldReturnOptionalBean_Xml() {
    SqlSession sqlSession = sqlSessionFactory.openSession();
    try {
      Mapper mapper = sqlSession.getMapper(Mapper.class);
      Optional<User> user = mapper.getOptionalUserXml(1);
      assertEquals("User1", user.get().getName().get());
      assertTrue(user.get().getDob().isPresent());
    } finally {
      sqlSession.close();
    }
  }

  @Test
  public void shouldReturnOptionalBeans() {
    SqlSession sqlSession = sqlSessionFactory.openSession();
    try {
      Mapper mapper = sqlSession.getMapper(Mapper.class);
      List<Optional<User>> users = mapper.getOptionalUsers();
      assertEquals(3, users.size());
      assertEquals("User1", users.get(0).get().getName().get());
    } finally {
      sqlSession.close();
    }
  }

  @Test
  public void shouldReturnEmptyWhenThereIsNoMatchingRow_Bean() {
    SqlSession sqlSession = sqlSessionFactory.openSession();
    try {
      Mapper mapper = sqlSession.getMapper(Mapper.class);
      Optional<User> user = mapper.getOptionalUser(9999);
      assertFalse(user.isPresent());
    } finally {
      sqlSession.close();
    }
  }

  @Test
  public void shouldReturnEmptyWhenAllColumnsAreNull() {
    SqlSession sqlSession = sqlSessionFactory.openSession();
    try {
      Mapper mapper = sqlSession.getMapper(Mapper.class);
      Optional<User> user = mapper.getOptionalNameAndDob(2);
      assertFalse(user.isPresent());
    } finally {
      sqlSession.close();
    }
  }

  @Test
  public void shouldReturnBeanWithOptionalAssociation_NestedQuery() {
    SqlSession sqlSession = sqlSessionFactory.openSession();
    try {
      Mapper mapper = sqlSession.getMapper(Mapper.class);
      Optional<User> user = mapper.getBeanWithOptionalAssociationAnno(1);
      assertEquals("Pet1", user.get().getPet().get().getName().get());
    } finally {
      sqlSession.close();
    }
  }

  @Test
  public void shouldReturnBeanWithEmptyAssociation_NestedQuery() {
    SqlSession sqlSession = sqlSessionFactory.openSession();
    try {
      Mapper mapper = sqlSession.getMapper(Mapper.class);
      Optional<User> user = mapper.getBeanWithOptionalAssociationAnno(2);
      assertTrue(user.isPresent());
      assertFalse(user.get().getPet().isPresent());
    } finally {
      sqlSession.close();
    }
  }

  @Test
  public void shouldReturnBeanWithOptionalAssociation_NestedResult() {
    SqlSession sqlSession = sqlSessionFactory.openSession();
    try {
      Mapper mapper = sqlSession.getMapper(Mapper.class);
      Optional<User> user = mapper.getBeanWithOptionalAssociationXml(1);
      assertEquals("Pet1", user.get().getPet().get().getName().get());
    } finally {
      sqlSession.close();
    }
  }

  @Test
  public void shouldReturnBeanWithOptionalInlineXml_NestedResult() {
    SqlSession sqlSession = sqlSessionFactory.openSession();
    try {
      Mapper mapper = sqlSession.getMapper(Mapper.class);
      Optional<User> user = mapper.getBeanWithOptionalInlineXml(1);
      assertEquals("Pet1", user.get().getPet().get().getName().get());
    } finally {
      sqlSession.close();
    }
  }

  @Test
  public void shouldReturnBeanWithEmptyAssociation_NestedResult() {
    SqlSession sqlSession = sqlSessionFactory.openSession();
    try {
      Mapper mapper = sqlSession.getMapper(Mapper.class);
      Optional<User> user = mapper.getBeanWithOptionalAssociationXml(2);
      assertFalse(user.get().getPet().isPresent());
    } finally {
      sqlSession.close();
    }
  }

  @Test
  public void shouldReturnBeanWithOptionalCollection_NestedResult() {
    SqlSession sqlSession = sqlSessionFactory.openSession();
    try {
      Mapper mapper = sqlSession.getMapper(Mapper.class);
      Optional<User> user = mapper.getBeanWithOptionalCollectionXml(3);
      assertTrue(user.get().getPets().get(0).get().getName().isPresent());
    } finally {
      sqlSession.close();
    }
  }

  @Test
  public void shouldReturnOptionalInt() {
    SqlSession sqlSession = sqlSessionFactory.openSession();
    try {
      Mapper mapper = sqlSession.getMapper(Mapper.class);
      OptionalInt rank = mapper.getOptionalRank(1);
      assertEquals(10, rank.getAsInt());
    } finally {
      sqlSession.close();
    }
  }

  @Test
  public void shouldReturnEmptyOptionalInt() {
    SqlSession sqlSession = sqlSessionFactory.openSession();
    try {
      Mapper mapper = sqlSession.getMapper(Mapper.class);
      OptionalInt rank = mapper.getOptionalRank(2);
      assertFalse(rank.isPresent());
    } finally {
      sqlSession.close();
    }
  }

  @Test
  public void shouldLoadLazyOptionalAssociation() {
    SqlSession sqlSession = sqlSessionFactory.openSession();
    try {
      Mapper mapper = sqlSession.getMapper(Mapper.class);
      User user = mapper.getUserWithLazyPet(1);
      assertEquals("User1", user.getName().get());
      assertTrue(user.getDob().isPresent());
      assertTrue(user.getPet().isPresent());
    } finally {
      sqlSession.close();
    }
  }

  @Test
  public void shouldLoadLazyOptionalCollection() {
    SqlSession sqlSession = sqlSessionFactory.openSession();
    try {
      Mapper mapper = sqlSession.getMapper(Mapper.class);
      User user = mapper.getUserWithLazyPets(3);
      assertEquals("User3", user.getName().get());
      assertEquals(2, user.getPets().size());
      assertTrue(user.getPets().get(0).isPresent());
      assertTrue(user.getPets().get(1).isPresent());
    } finally {
      sqlSession.close();
    }
  }

  @Test
  public void shouldInsertAUser() {
    SqlSession sqlSession = sqlSessionFactory.openSession();
    try {
      Mapper mapper = sqlSession.getMapper(Mapper.class);
      User user = new User();
      user.setId(100);
      user.setName(Optional.of("User100"));
      user.setRank(OptionalInt.of(99));
      mapper.insertUser(user);

      User result = mapper.getBeanWithOptionalProperties(100);
      assertEquals("User100", result.getName().get());
      assertEquals(99, result.getRank().getAsInt());
    } finally {
      sqlSession.close();
    }
  }
}
