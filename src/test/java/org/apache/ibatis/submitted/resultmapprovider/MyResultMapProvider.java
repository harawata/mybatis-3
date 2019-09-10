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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.builder.ResultMapResolver;
import org.apache.ibatis.builder.annotation.ResultMapProviderContext;
import org.apache.ibatis.mapping.ResultFlag;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.mapping.ResultMapping.Builder;
import org.apache.ibatis.session.Configuration;

public class MyResultMapProvider {

  public void provideResultMap(ResultMapProviderContext context) {
    ResultMapResolver resultMapResolver = context.getResultMapResolver();
    List<ResultMapping> resultMappings = new ArrayList<>();
    Class<?> returnType = context.getMapperMethod().getReturnType();
    if (List.class == returnType) {
      returnType = (Class<?>) ((ParameterizedType) context.getMapperMethod().getGenericReturnType())
          .getActualTypeArguments()[0];
    }
    List<String> constructorFields = new ArrayList<>();
    constructorMapping(context, resultMappings, returnType, constructorFields);
    for (Field field : returnType.getDeclaredFields()) {
      String name = field.getName();
      if (constructorFields.contains(name)) {
        continue;
      }
      MyField myField = field.getAnnotation(MyField.class);
      Class<?> fieldType = field.getType();
      if (myField != null) {
        resultMappings.add(buildResultMapping(context.getConfiguration(), fieldType, name, myField, false));
        continue;
      }
      MyNestedSelect nestedSelect = field.getAnnotation(MyNestedSelect.class);
      if (nestedSelect != null) {
        Builder builder = new ResultMapping.Builder(context.getConfiguration(), name, nestedSelect.column(),
            fieldType);
        builder.nestedQueryId(nestedSelect.select());
        resultMappings.add(builder.build());
        continue;
      }
      MyNestedResult nestedResult = field.getAnnotation(MyNestedResult.class);
      if (nestedResult != null) {
        Builder builder = new ResultMapping.Builder(context.getConfiguration(), name);
        builder.nestedResultMapId(nestedResult.resultMap()).columnPrefix(nestedResult.columnPrefix());
        resultMappings.add(builder.build());
        continue;
      }
    }
    resultMapResolver.resultMappings(resultMappings);
  }

  private void constructorMapping(ResultMapProviderContext context, List<ResultMapping> resultMappings,
      Class<?> returnType, List<String> constructorFields) {
    Constructor<?> constructor = returnType.getConstructors()[0];
    if (constructor.getParameterCount() > 0) {
      for (Parameter param : constructor.getParameters()) {
        MyField anno = param.getAnnotation(MyField.class);
        String name = param.getName();
        resultMappings.add(buildResultMapping(context.getConfiguration(), param.getType(), name, anno, true));
        constructorFields.add(name);
      }
    }
  }

  private ResultMapping buildResultMapping(Configuration configuration, Class<?> javaType, String name,
      MyField anno, boolean isConstructor) {
    String column = anno == null ? "" : anno.column();
    Builder builder = new ResultMapping.Builder(configuration, name, column.isEmpty() ? name : column,
        javaType);
    List<ResultFlag> flags = new ArrayList<>();
    if (isConstructor) {
      flags.add(ResultFlag.CONSTRUCTOR);
    }
    if (anno != null && anno.id()) {
      flags.add(ResultFlag.ID);
    }
    builder.flags(flags);
    return builder.build();
  }

}
