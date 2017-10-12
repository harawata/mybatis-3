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
package org.apache.ibatis.executor.keygen;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.binding.BindingException;
import org.apache.ibatis.binding.MapperMethod.ParamMap;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.defaults.DefaultSqlSession.StrictMap;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;

/**
 * @author Clinton Begin
 * @author Kazuki Shimizu
 */
public class Jdbc3KeyGenerator implements KeyGenerator {

  /**
   * A shared instance.
   * @since 3.4.3
   */
  public static final Jdbc3KeyGenerator INSTANCE = new Jdbc3KeyGenerator();

  @Override
  public void processBefore(Executor executor, MappedStatement ms, Statement stmt, Object parameter) {
    // do nothing
  }

  @Override
  public void processAfter(Executor executor, MappedStatement ms, Statement stmt, Object parameter) {
    processBatch(ms, stmt, getParameters(parameter));
  }

  public void processBatch(MappedStatement ms, Statement stmt, Collection<?> parameters) {
    ResultSet rs = null;
    try {
      rs = stmt.getGeneratedKeys();
      final Configuration configuration = ms.getConfiguration();
      final TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
      final String[] keyProperties = ms.getKeyProperties();
      final ResultSetMetaData rsmd = rs.getMetaData();
      TypeHandler<?>[] typeHandlers = null;
      if (keyProperties != null && rsmd.getColumnCount() >= keyProperties.length) {
        for (Object parameter : parameters) {
          // there should be one row for each statement (also one for each parameter)
          if (!rs.next()) {
            break;
          }
          final MetaObject metaParam = configuration.newMetaObject(parameter);
          if (typeHandlers == null) {
            typeHandlers = getTypeHandlers(typeHandlerRegistry, metaParam, keyProperties, rsmd);
          }
          populateKeys(rs, metaParam, keyProperties, typeHandlers);
        }
      }
    } catch (Exception e) {
      throw new ExecutorException("Error getting generated key or setting result to parameter object. Cause: " + e, e);
    } finally {
      if (rs != null) {
        try {
          rs.close();
        } catch (Exception e) {
          // ignore
        }
      }
    }
  }

  private Collection<?> getParameters(Object parameter) {
    if (parameter instanceof ParamMap || parameter instanceof StrictMap) {
      final Map<?, ?> paramMap = (Map<?, ?>) parameter;
      Object soleParam = null;
      for (Object paramValue : paramMap.values()) {
        if (soleParam == paramValue) {
          continue;
        } else if (soleParam != null) {
          soleParam = null;
          break;
        }
        soleParam = paramValue;
      }
      if (soleParam != null) {
        if (soleParam instanceof Collection) {
          return (Collection<?>)soleParam;
        } else if (soleParam instanceof Object[]){
          return Arrays.asList((Object[])soleParam);
        } else {
          return Arrays.asList(soleParam);
        }
      }
      // There are multiple parameters.
      // Look for a parameter with special name.
      if (paramMap.containsKey("collection")) {
        Object param = paramMap.get("collection");
        if (param != null && param instanceof Collection) {
          return (Collection<?>) param;
        }
      }
      if (paramMap.containsKey("list")) {
        Object param = paramMap.get("list");
        if (param != null && param instanceof List) {
          return (Collection<?>) param;
        }
      }
      if (paramMap.containsKey("array")) {
        Object param = paramMap.get("array");
        if (param != null && param instanceof Object[]) {
          return Arrays.asList((Object[])param);
        }
      }
    }
    return Arrays.asList(parameter);
  }

  private TypeHandler<?>[] getTypeHandlers(TypeHandlerRegistry typeHandlerRegistry, MetaObject metaParam, String[] keyProperties, ResultSetMetaData rsmd) throws SQLException {
    TypeHandler<?>[] typeHandlers = new TypeHandler<?>[keyProperties.length];
    for (int i = 0; i < keyProperties.length; i++) {
      if (metaParam.hasSetter(keyProperties[i])) {
        TypeHandler<?> th;
        try {
          Class<?> keyPropertyType = metaParam.getSetterType(keyProperties[i]);
          th = typeHandlerRegistry.getTypeHandler(keyPropertyType, JdbcType.forCode(rsmd.getColumnType(i + 1)));
        } catch (BindingException e) {
          th = null;
        }
        typeHandlers[i] = th;
      }
    }
    return typeHandlers;
  }

  private void populateKeys(ResultSet rs, MetaObject metaParam, String[] keyProperties, TypeHandler<?>[] typeHandlers) throws SQLException {
    for (int i = 0; i < keyProperties.length; i++) {
      String property = keyProperties[i];
      TypeHandler<?> th = typeHandlers[i];
      if (th != null) {
        Object value = th.getResult(rs, i + 1);
        metaParam.setValue(property, value);
      }
    }
  }

}
