/**
 *    Copyright 2009-2020 the original author or authors.
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

package org.apache.ibatis.executor;

import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.cache.TransactionalCacheManager;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.junit.jupiter.api.Test;

public class CachingExecutorTest {
  @Test
  void shouldSkipCacheLookupAfterFlushingCache() throws Exception {
    // gh-1960
    Executor delegate = mock(Executor.class);
    Configuration configuration = new Configuration();
    CachingExecutor cachingExecutor = new CachingExecutor(delegate);
    TransactionalCacheManager transactionalCacheManager = mock(TransactionalCacheManager.class);
    Field field = CachingExecutor.class.getDeclaredField("tcm");
    field.setAccessible(true);
    field.set(cachingExecutor, transactionalCacheManager);
    Cache cache = mock(Cache.class);
    CacheKey cacheKey = new CacheKey();
    BoundSql boundSql = mock(BoundSql.class);
    List<Object> queryResult = new ArrayList<>();
    MappedStatement mappedStatement = new MappedStatement.Builder(configuration, "id",
        new StaticSqlSource(configuration, "sql"), null).useCache(true).cache(cache).flushCacheRequired(true).build();
    when(delegate.query(mappedStatement, null, null, null, cacheKey, boundSql)).thenReturn(queryResult);
    cachingExecutor.query(mappedStatement, null, null, null, cacheKey, boundSql);
    verify(transactionalCacheManager, times(1)).putObject(cache, cacheKey, queryResult);
    verify(transactionalCacheManager, times(0)).getObject(cache, cacheKey);
  }
}
