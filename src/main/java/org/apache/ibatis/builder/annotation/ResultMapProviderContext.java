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

package org.apache.ibatis.builder.annotation;

import java.lang.reflect.Method;

import org.apache.ibatis.builder.ResultMapResolver;
import org.apache.ibatis.session.Configuration;

public class ResultMapProviderContext {
  private final Configuration configuration;
  private final Class<?> mapperType;
  private final Method mapperMethod;
  private final ResultMapResolver resultMapResolver;

  public ResultMapProviderContext(Configuration configuration, Class<?> mapperType, Method mapperMethod, ResultMapResolver resultMapResolver) {
    super();
    this.configuration = configuration;
    this.mapperType = mapperType;
    this.mapperMethod = mapperMethod;
    this.resultMapResolver = resultMapResolver;
  }

  public Class<?> getMapperType() {
    return mapperType;
  }

  public Method getMapperMethod() {
    return mapperMethod;
  }

  public ResultMapResolver getResultMapResolver() {
    return resultMapResolver;
  }

  public Configuration getConfiguration() {
    return configuration;
  }
}
