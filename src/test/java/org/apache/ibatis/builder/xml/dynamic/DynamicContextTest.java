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

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;

import org.apache.ibatis.builder.BuilderException;
import org.apache.ibatis.domain.blog.Author;
import org.apache.ibatis.domain.blog.Section;
import org.apache.ibatis.scripting.xmltags.ExpressionEvaluator;
import org.apache.ibatis.scripting.xmltags.ExpressionLanguage;
import org.apache.ibatis.scripting.xmltags.MvelExpressionLanguage;
import org.apache.ibatis.scripting.xmltags.OgnlExpressionLanguage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.mvel2.MVEL;

@RunWith(Parameterized.class)
public class ExpressionEvaluatorTest {

  @Parameters
  public static Iterable<? extends Object> data() {
    return Arrays.asList(new OgnlExpressionLanguage(), new MvelExpressionLanguage());
  }

  private final ExpressionEvaluator evaluator;

  public ExpressionEvaluatorTest(ExpressionLanguage el) {
    super();
    this.evaluator = new ExpressionEvaluator(el);
  }

  @Test
  public void shouldCompareStringsReturnTrue() {
    boolean value = evaluator.evaluateBoolean("username == 'cbegin'", new Author(1, "cbegin", "******", "cbegin@apache.org", "N/A", Section.NEWS));
    assertEquals(true, value);
  }

  @Test
  public void shouldCompareStringsReturnFalse() {
    boolean value = evaluator.evaluateBoolean("username == 'norm'", new Author(1, "cbegin", "******", "cbegin@apache.org", "N/A", Section.NEWS));
    assertEquals(false, value);
  }

  @Test
  public void shouldReturnTrueIfNotNull() {
    boolean value = evaluator.evaluateBoolean("username", new Author(1, "cbegin", "******", "cbegin@apache.org", "N/A", Section.NEWS));
    assertEquals(true, value);
  }

  @Test
  public void shouldReturnFalseIfNull() {
    boolean value = evaluator.evaluateBoolean("password", new Author(1, "cbegin", null, "cbegin@apache.org", "N/A", Section.NEWS));
    assertEquals(false, value);
  }

  @Test
  public void shouldReturnTrueIfNotZero() {
    boolean value = evaluator.evaluateBoolean("id", new Author(1, "cbegin", null, "cbegin@apache.org", "N/A", Section.NEWS));
    assertEquals(true, value);
  }

  @Test
  public void shouldReturnFalseIfZero() {
    boolean value = evaluator.evaluateBoolean("id", new Author(0, "cbegin", null, "cbegin@apache.org", "N/A", Section.NEWS));
    assertEquals(false, value);
  }

  @Test
  public void shouldNonExistentPropertyNameThrowException() {
    when(evaluator).evaluateBoolean("idd", new Author(0, "cbegin", null, "cbegin@apache.org", "N/A", Section.NEWS));
    then(caughtException()).isInstanceOf(BuilderException.class)
      .hasMessageStartingWith("Error evaluating expression");
  }

  @Test
  public void shouldIterateOverIterable() {
    final HashMap<String, String[]> parameterObject = new HashMap<String, String[]>() {{
      put("array", new String[]{"1", "2", "3"});
    }};
    final Iterable<?> iterable = evaluator.evaluateIterable("array", parameterObject);
    int i = 0;
    for (Object o : iterable) {
      assertEquals(String.valueOf(++i), o);
    }
  }
}
