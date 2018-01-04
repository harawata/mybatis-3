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

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

import org.apache.ibatis.io.Resources;

/**
 * @author Iwao AVE!
 */
public class OptionalUtil {

  private static final String OPTIONAL = "java.util.Optional";
  private static Class<?> OPTIONAL_CLASS = null;
  private static Method OF_NULLABLE = null;
  private static Method GET = null;
  private static Method EMPTY = null;

  static {
    try {
      OPTIONAL_CLASS = Resources.classForName(OPTIONAL);
      OF_NULLABLE = OPTIONAL_CLASS.getMethod("ofNullable", Object.class);
      GET = OPTIONAL_CLASS.getMethod("get");
      EMPTY = OPTIONAL_CLASS.getMethod("empty");
    } catch (Exception e) {
      // ignore
    }

  }

  public static boolean isOptional(Type type) {
    if (OPTIONAL_CLASS == null) {
      return false;
    }
    if (type instanceof Class) {
      return (Class<?>) type == OPTIONAL_CLASS;
    } else if (type instanceof ParameterizedType) {
      return (Class<?>) ((ParameterizedType) type).getRawType() == OPTIONAL_CLASS;
    }
    return false;
  }

  public static boolean isOptionalList(Type type) {
    if (OPTIONAL_CLASS == null) {
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

  public static Object ofNullable(Object value) {
    if (OPTIONAL_CLASS == null) {
      return null;
    }
    try {
      return OF_NULLABLE.invoke(null, value);
    } catch (Exception e) {
      return null;
    }
  }

  public static Object get(Object optional) {
    if (OPTIONAL_CLASS == null) {
      return null;
    }
    try {
      return GET.invoke(optional, (Object[]) null);
    } catch (Exception e) {
      return null;
    }
  }

  public static Object empty() {
    if (OPTIONAL_CLASS == null) {
      return null;
    }
    try {
      return EMPTY.invoke(null, (Object[]) null);
    } catch (Exception e) {
      return null;
    }
  }

  public static Class<?> extractArgument(Type optional) {
    if (OPTIONAL_CLASS == null) {
      return null;
    }
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
