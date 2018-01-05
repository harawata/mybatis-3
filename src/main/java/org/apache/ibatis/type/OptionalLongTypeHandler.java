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

package org.apache.ibatis.type;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.OptionalLong;

import org.apache.ibatis.lang.UsesJava8;

@UsesJava8
public class OptionalLongTypeHandler implements TypeHandler<OptionalLong> {

  @Override
  public void setParameter(PreparedStatement ps, int i, OptionalLong parameter, JdbcType jdbcType) throws SQLException {
    if (parameter == null || !parameter.isPresent()) {
      if (jdbcType == null) {
        throw new TypeException("JDBC requires that the JdbcType must be specified for all nullable parameters.");
      }
      try {
        ps.setNull(i, jdbcType.TYPE_CODE);
      } catch (SQLException e) {
        throw new TypeException("Error setting null for parameter #" + i + " with JdbcType " + jdbcType + " . " +
            "Try setting a different JdbcType for this parameter or a different jdbcTypeForNull configuration property. " +
            "Cause: " + e, e);
      }
    } else {
      try {
        ps.setLong(i, parameter.getAsLong());
      } catch (Exception e) {
        throw new TypeException("Error setting non null for parameter #" + i + " with JdbcType " + jdbcType + " . " +
            "Try setting a different JdbcType for this parameter or a different configuration property. " +
            "Cause: " + e, e);
      }
    }
  }

  @Override
  public OptionalLong getResult(ResultSet rs, String columnName) throws SQLException {
    long result = rs.getLong(columnName);
    return rs.wasNull() ? OptionalLong.empty() : OptionalLong.of(result);
  }

  @Override
  public OptionalLong getResult(ResultSet rs, int columnIndex) throws SQLException {
    long result = rs.getLong(columnIndex);
    return rs.wasNull() ? OptionalLong.empty() : OptionalLong.of(result);
  }

  @Override
  public OptionalLong getResult(CallableStatement cs, int columnIndex) throws SQLException {
    long result = cs.getLong(columnIndex);
    return cs.wasNull() ? OptionalLong.empty() : OptionalLong.of(result);
  }

}
