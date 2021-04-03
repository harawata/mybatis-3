/**
 *    Copyright 2009-2021 the original author or authors.
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
package org.apache.ibatis.submitted.flushstatementsinterceptor;

import static org.junit.jupiter.api.Assertions.*;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.BaseDataTest;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class FlushStatementsInterceptorTest {

  private static SqlSessionFactory sqlSessionFactory;

  @BeforeAll
  static void setUp() throws Exception {
    try (Reader reader = Resources
        .getResourceAsReader("org/apache/ibatis/submitted/flushstatementsinterceptor/mybatis-config.xml")) {
      sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
    }
    Configuration configuration = sqlSessionFactory.getConfiguration();
    BaseDataTest.runScript(configuration.getEnvironment().getDataSource(),
        "org/apache/ibatis/submitted/flushstatementsinterceptor/CreateDB.sql");
  }

  @Test
  void shouldInterceptFlushStatements() {
    Configuration configuration = sqlSessionFactory.getConfiguration();
    TestInterceptor interceptor = new TestInterceptor();
    configuration.addInterceptor(interceptor);
    try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH)) {
      Mapper mapper = sqlSession.getMapper(Mapper.class);
      mapper.updateUser(new User(1, "Jen"));
      mapper.updateUser(new User(2, "John"));
      mapper.updateUser(new User(3, "Pete"));
      sqlSession.commit();
    }
    List<int[]> updatedCounts = interceptor.getUpdatedCounts();
    assertEquals(1, updatedCounts.size());
    int[] countArray = updatedCounts.get(0);
    assertEquals(1, countArray[0]);
    assertEquals(1, countArray[1]);
    assertEquals(0, countArray[2]);
  }

  @Intercepts({ @Signature(type = Executor.class, method = "flushStatements", args = {}) })
  public static class TestInterceptor implements Interceptor {
    private List<int[]> updatedCounts = new ArrayList<>();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
      @SuppressWarnings("unchecked")
      List<BatchResult> batchResults = (List<BatchResult>) invocation.proceed();
      for (BatchResult batchResult : batchResults) {
        updatedCounts.add(batchResult.getUpdateCounts());
      }
      return batchResults;
    }

    public List<int[]> getUpdatedCounts() {
      return updatedCounts;
    }
  }
}
