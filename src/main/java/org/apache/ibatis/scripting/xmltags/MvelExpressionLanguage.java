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
package org.apache.ibatis.scripting.xmltags;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.ibatis.builder.BuilderException;
import org.apache.ibatis.scripting.xmltags.DynamicContext.ContextMap;
import org.mvel2.CompileException;
import org.mvel2.MVEL;
import org.mvel2.integration.PropertyHandler;
import org.mvel2.integration.PropertyHandlerFactory;
import org.mvel2.integration.VariableResolverFactory;

public class MvelExpressionLanguage implements ExpressionLanguage {

  private final Map<String, Serializable> expressionCache = new ConcurrentHashMap<String, Serializable>();

  static {
    MVEL.COMPILER_OPT_ALLOW_OVERRIDE_ALL_PROPHANDLING = true;
    PropertyHandlerFactory.registerPropertyHandler(ContextMap.class, new PropertyHandler() {
      @SuppressWarnings("unchecked")
      @Override
      public Object getProperty(String name, Object contextObj,
          VariableResolverFactory variableFactory) {
        Map<String, Object> map = (Map<String, Object>) contextObj;
        Object result = map.get(name);
        if (map.containsKey(name) || result != null) {
          return result;
        }

        Object parameterObject = map.get(DynamicContext.PARAMETER_OBJECT_KEY);
        if (parameterObject instanceof Map) {
          return ((Map<String, Object>) parameterObject).get(name);
        }
        return null;
      }

      @Override
      public Object setProperty(String name, Object contextObj,
          VariableResolverFactory variableFactory, Object value) {
        @SuppressWarnings("unchecked")
        Map<String, Object> map = (Map<String, Object>) contextObj;
        map.put(name, value);
        return value;
      }
    });
  }

  @Override
  public Object getValue(String expression, Object root) {
    try {
      return MVEL.executeExpression(compile(expression), root);
    } catch (CompileException e) {
      throw new BuilderException("Error evaluating expression '" + expression + "'. Cause: " + e, e);
    }
  }


  private Serializable compile(String expression) {
    Serializable compiled = expressionCache.get(expression);
    if (compiled == null) {
      compiled = MVEL.compileExpression(expression);
      if (compiled != null) {
        expressionCache.put(expression, compiled);
      }
    }
    return compiled;
  }
}
