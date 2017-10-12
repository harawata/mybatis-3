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
package org.apache.ibatis.submitted.keygen;

import static com.googlecode.catchexception.apis.BDDCatchException.*;
import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.Assert.*;

import java.io.Reader;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author liuzh
 */
public class Jdbc3KeyGeneratorTest {

  private static SqlSessionFactory sqlSessionFactory;

  @BeforeClass
  public static void setUp() throws Exception {
    // create an SqlSessionFactory
    Reader reader = Resources.getResourceAsReader("org/apache/ibatis/submitted/keygen/MapperConfig.xml");
    sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
    reader.close();

    // populate in-memory database
    SqlSession session = sqlSessionFactory.openSession();
    Connection conn = session.getConnection();
    reader = Resources.getResourceAsReader("org/apache/ibatis/submitted/keygen/CreateDB.sql");
    ScriptRunner runner = new ScriptRunner(conn);
    runner.setLogWriter(null);
    runner.runScript(reader);
    conn.close();
    reader.close();
    session.close();
  }

  @Test
  public void shouldSetGeneratedIdsOnList() throws Exception {
    SqlSession sqlSession = sqlSessionFactory.openSession();
    try {
      CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
      List<Country> countries = new ArrayList<Country>();
      countries.add(new Country("China", "CN"));
      countries.add(new Country("United Kiongdom", "GB"));
      countries.add(new Country("United States of America", "US"));
      mapper.insertList(countries);
      for (Country country : countries) {
        assertNotNull(country.getId());
      }
    } finally {
      sqlSession.rollback();
      sqlSession.close();
    }
  }

  @Test
  public void shouldSetGeneratedIdsOnNamedList() throws Exception {
    SqlSession sqlSession = sqlSessionFactory.openSession();
    try {
      CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
      List<Country> countries = new ArrayList<Country>();
      countries.add(new Country("China", "CN"));
      countries.add(new Country("United Kiongdom", "GB"));
      countries.add(new Country("United States of America", "US"));
      mapper.insertNamedList(countries);
      for (Country country : countries) {
        assertNotNull(country.getId());
      }
    } finally {
      sqlSession.rollback();
      sqlSession.close();
    }
  }

  @Test
  public void shouldSetGeneratedIdsOnCollection() throws Exception {
    SqlSession sqlSession = sqlSessionFactory.openSession();
    try {
      CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
      Set<Country> countries = new HashSet<Country>();
      countries.add(new Country("China", "CN"));
      countries.add(new Country("United Kiongdom", "GB"));
      mapper.insertSet(countries);
      for (Country country : countries) {
        assertNotNull(country.getId());
      }
    } finally {
      sqlSession.rollback();
      sqlSession.close();
    }
  }

  @Test
  public void shouldSetGeneratedIdsOnNamedCollection() throws Exception {
    SqlSession sqlSession = sqlSessionFactory.openSession();
    try {
      CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
      Set<Country> countries = new HashSet<Country>();
      countries.add(new Country("China", "CN"));
      countries.add(new Country("United Kiongdom", "GB"));
      mapper.insertNamedSet(countries);
      for (Country country : countries) {
        assertNotNull(country.getId());
      }
    } finally {
      sqlSession.rollback();
      sqlSession.close();
    }
  }

  @Test
  public void shouldSetGeneratedIdsOnArray() throws Exception {
    SqlSession sqlSession = sqlSessionFactory.openSession();
    try {
      CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
      Country[] countries = new Country[2];
      countries[0] = new Country("China", "CN");
      countries[1] = new Country("United Kiongdom", "GB");
      mapper.insertArray(countries);
      for (Country country : countries) {
        assertNotNull(country.getId());
      }
    } finally {
      sqlSession.rollback();
      sqlSession.close();
    }
  }

  @Test
  public void shouldSetGeneratedIdsOnNamedArray() throws Exception {
    SqlSession sqlSession = sqlSessionFactory.openSession();
    try {
      CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
      Country[] countries = new Country[2];
      countries[0] = new Country("China", "CN");
      countries[1] = new Country("United Kiongdom", "GB");
      mapper.insertNamedArray(countries);
      for (Country country : countries) {
        assertNotNull(country.getId());
      }
    } finally {
      sqlSession.rollback();
      sqlSession.close();
    }
  }

  @Test
  public void shouldSetGeneratedIdOnObject_MultiParams() throws Exception {
    SqlSession sqlSession = sqlSessionFactory.openSession();
    try {
      CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
      Country country = new Country("China", "CN");
      mapper.insertCountryAndSomeId(country, Integer.valueOf(1));
      assertNotNull(country.getId());
    } finally {
      sqlSession.rollback();
      sqlSession.close();
    }
  }

  @Test
  public void shouldSetGeneratedIdsOnList_MultiParams() throws Exception {
    SqlSession sqlSession = sqlSessionFactory.openSession();
    try {
      CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
      List<Country> countries = new ArrayList<Country>();
      countries.add(new Country("China", "CN"));
      countries.add(new Country("United Kiongdom", "GB"));
      mapper.insertListAndSomeId(countries, Integer.valueOf(1));
      for (Country country : countries) {
        assertNotNull(country.getId());
      }
    } finally {
      sqlSession.rollback();
      sqlSession.close();
    }
  }

  @Test
  public void shouldSetGeneratedIdsOnNamedList_MultiParams() throws Exception {
    // Not supported currently.
    SqlSession sqlSession = sqlSessionFactory.openSession();
    try {
      CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
      List<Country> countries = new ArrayList<Country>();
      countries.add(new Country("China", "CN"));
      countries.add(new Country("United Kiongdom", "GB"));
      when(mapper).insertNamedListAndSomeId(countries, Integer.valueOf(1));
      then(caughtException()).isInstanceOf(PersistenceException.class).hasMessageContaining(
          "### Error updating database.  Cause: org.apache.ibatis.executor.ExecutorException: Error getting generated key or setting result to parameter object. Cause: java.lang.UnsupportedOperationException");
    } finally {
      sqlSession.rollback();
      sqlSession.close();
    }
  }

  @Ignore("#782 was reverted. See #902.")
  @Test
  public void shouldErrorUndefineProperty() {
    SqlSession sqlSession = sqlSessionFactory.openSession();
    try {
      CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);

      when(mapper).insertUndefineKeyProperty(new Country("China", "CN"));
      then(caughtException()).isInstanceOf(PersistenceException.class).hasMessageContaining(
          "### Error updating database.  Cause: org.apache.ibatis.executor.ExecutorException: Error getting generated key or setting result to parameter object. Cause: org.apache.ibatis.executor.ExecutorException: No setter found for the keyProperty 'country_id' in org.apache.ibatis.submitted.keygen.Country.");
    } finally {
      sqlSession.rollback();
      sqlSession.close();
    }
  }
}
