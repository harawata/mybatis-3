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
package org.apache.ibatis.builder.xml.dynamic;

import static com.googlecode.catchexception.apis.BDDCatchException.*;
import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;

import org.apache.ibatis.builder.BuilderException;
import org.apache.ibatis.domain.blog.Author;
import org.apache.ibatis.domain.blog.Section;
import org.apache.ibatis.reflection.ReflectionException;
import org.apache.ibatis.scripting.xmltags.DynamicContext;
import org.apache.ibatis.scripting.xmltags.MvelExpressionLanguage;
import org.apache.ibatis.scripting.xmltags.OgnlExpressionLanguage;
import org.apache.ibatis.session.Configuration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class DynamicContextTest {

  @Parameters
  public static Iterable<? extends Object> data() {
    Configuration confOgnl = new Configuration();
    confOgnl.setExpressionLanguage(new OgnlExpressionLanguage());
    Configuration confMvel = new Configuration();
    confMvel.setExpressionLanguage(new MvelExpressionLanguage());
    return Arrays.asList(confOgnl, confMvel);
  }

  private final Configuration config;

  public DynamicContextTest(Configuration config) {
    super();
    this.config = config;
  }

  @Test
  public void shouldCompareStringsReturnTrue() {
    DynamicContext dynamicContext = new DynamicContext(config,
        new Author(1, "cbegin", "******", "cbegin@apache.org", "N/A", Section.NEWS));
    boolean value = dynamicContext.evaluateBoolean("username == 'cbegin'");
    assertEquals(true, value);
  }

  @Test
  public void shouldCompareStringsReturnFalse() {
    DynamicContext dynamicContext = new DynamicContext(config,
        new Author(1, "cbegin", "******", "cbegin@apache.org", "N/A", Section.NEWS));
    boolean value = dynamicContext.evaluateBoolean("username == 'norm'");
    assertEquals(false, value);
  }

  @Test
  public void shouldReturnTrueIfNotNull() {
    DynamicContext dynamicContext = new DynamicContext(config,
        new Author(1, "cbegin", "******", "cbegin@apache.org", "N/A", Section.NEWS));
    boolean value = dynamicContext.evaluateBoolean("username");
    assertEquals(true, value);
  }

  @Test
  public void shouldReturnFalseIfNull() {
    DynamicContext dynamicContext = new DynamicContext(config,
        new Author(1, "cbegin", null, "cbegin@apache.org", "N/A", Section.NEWS));
    boolean value = dynamicContext.evaluateBoolean("password");
    assertEquals(false, value);
  }

  @Test
  public void shouldReturnTrueIfNotZero() {
    DynamicContext dynamicContext = new DynamicContext(config,
        new Author(1, "cbegin", null, "cbegin@apache.org", "N/A", Section.NEWS));
    boolean value = dynamicContext.evaluateBoolean("id");
    assertEquals(true, value);
  }

  @Test
  public void shouldReturnFalseIfZero() {
    DynamicContext dynamicContext = new DynamicContext(config,
        new Author(0, "cbegin", null, "cbegin@apache.org", "N/A", Section.NEWS));
    boolean value = dynamicContext.evaluateBoolean("id");
    assertEquals(false, value);
  }

  @Test
  public void shouldNonExistentPropertyNameThrowException() {
    DynamicContext dynamicContext = new DynamicContext(config,
        new Author(0, "cbegin", null, "cbegin@apache.org", "N/A", Section.NEWS));
    when(dynamicContext).evaluateBoolean("idd");
    then(caughtException()).isInstanceOfAny(BuilderException.class, ReflectionException.class);
  }

  @Test
  public void shouldIterateOverIterable() {
    @SuppressWarnings("serial")
    final HashMap<String, String[]> parameterObject = new HashMap<String, String[]>() {
      {
        put("array", new String[] { "1", "2", "3" });
      }
    };
    DynamicContext dynamicContext = new DynamicContext(config, parameterObject);
    final Iterable<?> iterable = dynamicContext.evaluateIterable("array");
    int i = 0;
    for (Object o : iterable) {
      assertEquals(String.valueOf(++i), o);
    }
  }
}
