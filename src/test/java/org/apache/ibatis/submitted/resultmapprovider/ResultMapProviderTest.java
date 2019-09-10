/**
 *    Copyright 2009-2019 the original author or authors.
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
package org.apache.ibatis.submitted.resultmapprovider;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.Reader;
import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.BaseDataTest;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ResultMapProviderTest {

  private static SqlSessionFactory sqlSessionFactory;

  @BeforeAll
  static void setUp() throws Exception {
    // create an SqlSessionFactory
    try (Reader reader = Resources
        .getResourceAsReader("org/apache/ibatis/submitted/resultmapprovider/mybatis-config.xml")) {
      sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
    }

    // populate in-memory database
    BaseDataTest.runScript(sqlSessionFactory.getConfiguration().getEnvironment().getDataSource(),
        "org/apache/ibatis/submitted/resultmapprovider/CreateDB.sql");
  }

  @Test
  void shouldGetAuthor() {
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      Mapper mapper = sqlSession.getMapper(Mapper.class);
      AnnotatedAuthor author = mapper.selectAuthorById(1);
      assertEquals(Long.valueOf(1), author.getId());
      assertEquals("Mary", author.getName());
      assertEquals(LocalDate.of(2000, 12, 31), author.getDateOfBirth());
    }
  }

  @Test
  void shouldGetBlog() {
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      Mapper mapper = sqlSession.getMapper(Mapper.class);
      AnnotatedBlog blog = mapper.selectBlogById(1);
      assertEquals(Long.valueOf(1), blog.getId());
      assertEquals("Blog1", blog.getTitle());
      assertEquals(LocalDate.of(2000, 12, 31), blog.getAuthor().getDateOfBirth());
      assertEquals(2, blog.getPosts().size());
    }
  }

}
