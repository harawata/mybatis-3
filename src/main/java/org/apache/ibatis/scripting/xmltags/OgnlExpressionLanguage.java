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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.ibatis.builder.BuilderException;
import org.apache.ibatis.scripting.xmltags.DynamicContext.ContextMap;

import ognl.Ognl;
import ognl.OgnlContext;
import ognl.OgnlException;
import ognl.OgnlRuntime;
import ognl.PropertyAccessor;

/**
 * Caches OGNL parsed expressions.
 *
 * @author Eduardo Macarron
 *
 * @see <a href='http://code.google.com/p/mybatis/issues/detail?id=342'>Issue 342</a>
 */
public class OgnlExpressionLanguage implements ExpressionLanguage {

  private final Map<String, Object> expressionCache = new ConcurrentHashMap<String, Object>();

  static {
    OgnlRuntime.setPropertyAccessor(ContextMap.class, new PropertyAccessor() {
      @SuppressWarnings("rawtypes")
      @Override
      public Object getProperty(Map context, Object target, Object name)
          throws OgnlException {
        Map map = (Map) target;

        Object result = map.get(name);
        if (map.containsKey(name) || result != null) {
          return result;
        }

        Object parameterObject = map.get(DynamicContext.PARAMETER_OBJECT_KEY);
        if (parameterObject instanceof Map) {
          return ((Map)parameterObject).get(name);
        }

        return null;
      }

      @SuppressWarnings({ "unchecked", "rawtypes" })
      @Override
      public void setProperty(Map context, Object target, Object name, Object value)
          throws OgnlException {
        Map<Object, Object> map = (Map<Object, Object>) target;
        map.put(name, value);
      }

      @Override
      public String getSourceAccessor(OgnlContext arg0, Object arg1, Object arg2) {
        return null;
      }

      @Override
      public String getSourceSetter(OgnlContext arg0, Object arg1, Object arg2) {
        return null;
      }
    });
  }

  @Override
  public Object getValue(String expression, Object root) {
    try {
      @SuppressWarnings("unchecked")
      Map<Object, OgnlClassResolver> context = Ognl.createDefaultContext(root, new OgnlClassResolver());
      return Ognl.getValue(parseExpression(expression), context, root);
    } catch (OgnlException e) {
      throw new BuilderException("Error evaluating expression '" + expression + "'. Cause: " + e, e);
    }
  }

  private Object parseExpression(String expression) throws OgnlException {
    Object node = expressionCache.get(expression);
    if (node == null) {
      node = Ognl.parseExpression(expression);
      expressionCache.put(expression, node);
    }
    return node;
  }
}
