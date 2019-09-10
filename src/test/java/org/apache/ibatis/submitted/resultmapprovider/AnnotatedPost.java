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

public class AnnotatedPost {
  private Long id;
  private String body;
  private Long blogId;

  public AnnotatedPost(@MyField(id = true) Long id, String body, @MyField(column = "blog_id") Long blogId) {
    this.id = id;
    this.body = body;
    this.blogId = blogId;
  }

  public Long getId() {
    return id;
  }

  public String getBody() {
    return body;
  }

  public Long getBlogId() {
    return blogId;
  }
}
