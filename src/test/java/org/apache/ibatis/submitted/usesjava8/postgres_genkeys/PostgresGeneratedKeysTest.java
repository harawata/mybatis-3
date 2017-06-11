/**
 *    Copyright 2009-2017 the original author or authors.
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
package org.apache.ibatis.submitted.usesjava8.postgres_genkeys;

import static org.junit.Assert.*;

import java.io.Reader;
import java.nio.file.Paths;
import java.sql.Connection;
import java.util.Collections;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import ru.yandex.qatools.embed.postgresql.EmbeddedPostgres;

public class PostgresGeneratedKeysTest {

  private static final EmbeddedPostgres postgres = new EmbeddedPostgres();

  private static SqlSessionFactory sqlSessionFactory;

  @BeforeClass
  public static void setUp() throws Exception {
    //  Launch PostgreSQL server. Download / unarchive if necessary.
    postgres.start(EmbeddedPostgres.cachedRuntimeConfig(Paths.get("target/postgres")), "localhost", 5432, "postgres_genkeys", "postgres", "root", Collections.emptyList());

    Reader reader = Resources.getResourceAsReader("org/apache/ibatis/submitted/usesjava8/postgres_genkeys/MapperConfig.xml");
    sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
    reader.close();

    SqlSession session = sqlSessionFactory.openSession();
    Connection conn = session.getConnection();
    reader = Resources.getResourceAsReader("org/apache/ibatis/submitted/usesjava8/postgres_genkeys/CreateDB.sql");
    ScriptRunner runner = new ScriptRunner(conn);
    runner.setLogWriter(null);
    runner.runScript(reader);
    reader.close();
    session.close();
  }

  @AfterClass
  public static void tearDown() {
    postgres.stop();
  }

  @Test
  public void shouldDefaultKeyPropertyNotCauseException() throws Exception {
    SqlSession sqlSession = sqlSessionFactory.openSession();
    try {
      Mapper mapper = sqlSession.getMapper(Mapper.class);
      int result = mapper.updateUserById(1, "Ethan");
      assertEquals(1, result);
    } finally {
      sqlSession.close();
    }
  }

  @Test
  public void shouldDefaultKeyPropertyNotCauseException2() throws Exception {
    SqlSession sqlSession = sqlSessionFactory.openSession();
    try {
      Mapper mapper = sqlSession.getMapper(Mapper.class);
      int result = mapper.updateSectionById(1, "IMF");
      assertEquals(1, result);
    } finally {
      sqlSession.close();
    }
  }
}
