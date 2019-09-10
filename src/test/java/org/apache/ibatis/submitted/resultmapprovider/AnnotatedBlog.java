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

import java.util.List;

public class AnnotatedBlog {
  @MyField(id = true)
  private Long id;
  @MyField
  private String title;
  @MyNestedSelect(select = "org.apache.ibatis.submitted.resultmapprovider.Mapper.selectAuthorById", column = "author_id")
  private AnnotatedAuthor author;
  @MyNestedResult(resultMap = "org.apache.ibatis.submitted.resultmapprovider.Mapper.postResultMap", columnPrefix = "p_")
  private List<AnnotatedPost> posts;

  public Long getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public AnnotatedAuthor getAuthor() {
    return author;
  }

  public List<AnnotatedPost> getPosts() {
    return posts;
  }
}
