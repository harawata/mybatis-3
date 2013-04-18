/*
 *    Copyright 2009-2013 The MyBatis Team
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
package org.apache.ibatis.submitted.joined_vs_multirs;

import static org.junit.Assert.*;

import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class JoinedVsMultiRsTest {
  private static SqlSessionFactory sqlSessionFactory;

  @BeforeClass
  public static void initDatabase() throws Exception {
    Connection conn = null;

    try {
      Class.forName("org.hsqldb.jdbcDriver");
      conn = DriverManager.getConnection("jdbc:hsqldb:mem:joinedvsmultirs", "sa", "");

      Reader reader = Resources.getResourceAsReader("org/apache/ibatis/submitted/joined_vs_multirs/CreateDB.sql");

      ScriptRunner runner = new ScriptRunner(conn);
      runner.setDelimiter("go");
      runner.setLogWriter(null);
      runner.setErrorLogWriter(null);
      runner.runScript(reader);
      conn.commit();
      reader.close();

      reader = Resources.getResourceAsReader("org/apache/ibatis/submitted/joined_vs_multirs/MapperConfig.xml");
      sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
      reader.close();
    } finally {
      if (conn != null) {
        conn.close();
      }
    }
  }

  @Test
  public void testGetMultiRsSp() throws SQLException {
    SqlSession sqlSession = sqlSessionFactory.openSession();
    try {
      List<Parent> parents = sqlSession.selectList("getMultiRsSp", 100);
      assertResults(parents);
    } finally {
      sqlSession.close();
    }
  }

  @Ignore
  @Test
  public void testGetJoinedSp() throws SQLException {
    SqlSession sqlSession = sqlSessionFactory.openSession();
    try {
      List<Parent> parents = sqlSession.selectList("getJoinedSp", 100);
      assertResults(parents);
    } finally {
      sqlSession.close();
    }
  }

  @Ignore
  @Test
  public void testGetJoined() throws SQLException {
    SqlSession sqlSession = sqlSessionFactory.openSession();
    try {
      List<Parent> parents = sqlSession.selectList("getJoined", 100);
      assertResults(parents);
    } finally {
      sqlSession.close();
    }
  }

  @Test
  public void testNestedSelect() throws SQLException {
    SqlSession sqlSession = sqlSessionFactory.openSession();
    try {
      List<Parent> parents = sqlSession.selectList("getNestedSelect", 100);
      assertResults(parents);
    } finally {
      sqlSession.close();
    }
  }

  protected void assertResults(List<Parent> parents) {
    assertEquals(9, parents.size());
    assertEquals("Parent 1", parents.get(0).getName());
    assertEquals(20, parents.get(0).getChildren1().size());
    assertEquals(Integer.valueOf(1), parents.get(0).getChildren1().get(0).getId());
    assertEquals(20, parents.get(0).getChildren2().size());
    assertEquals(Integer.valueOf(1), parents.get(0).getChildren2().get(0).getId());
    assertEquals(Integer.valueOf(21), parents.get(1).getChildren1().get(0).getId());
    assertEquals(Integer.valueOf(41), parents.get(2).getChildren3().get(0).getId());
    assertEquals(Integer.valueOf(200), parents.get(8).getChildren3().get(19).getId());
  }
}
