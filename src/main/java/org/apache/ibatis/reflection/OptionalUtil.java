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
package org.apache.ibatis.reflection;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Optional;

import org.apache.ibatis.lang.UsesJava8;

/**
 * @author Iwao AVE!
 */
public class OptionalUtil {

  public static boolean isOptional(Type type) {
    if (!Jdk.optionalExists) {
      return false;
    }
    if (type instanceof Class) {
      return (Class<?>) type == Optional.class;
    } else if (type instanceof ParameterizedType) {
      return (Class<?>) ((ParameterizedType) type).getRawType() == Optional.class;
    }
    return false;
  }

  public static boolean isOptionalList(Type type) {
    if (!Jdk.optionalExists) {
      return false;
    }
    if (type instanceof ParameterizedType) {
      ParameterizedType parameterizedType = (ParameterizedType) type;
      Class<?> rawType = (Class<?>) parameterizedType.getRawType();
      if (Collection.class.isAssignableFrom(rawType)) {
        Type[] args = parameterizedType.getActualTypeArguments();
        return args.length == 1 && isOptional(args[0]);
      }
    }
    return false;
  }

  @UsesJava8
  public static Object ofNullable(Object value) {
    return Optional.ofNullable(value);
  }

  @UsesJava8
  public static Object get(Object optional) {
    return ((Optional<?>)optional).get();
  }

  @UsesJava8
  public static Object empty() {
    return Optional.empty();
  }

  public static Class<?> extractArgument(Type optional) {
    ParameterizedType parameterizedType = (ParameterizedType) optional;
    Type argType = parameterizedType.getActualTypeArguments()[0];
    if (argType instanceof ParameterizedType) {
      return (Class<?>) ((ParameterizedType) argType).getRawType();
    } else if (argType instanceof Class) {
      return (Class<?>) argType;
    }
    throw new IllegalArgumentException("Only Class and ParameterizedType are supported as Optional argument.");
  }
}
