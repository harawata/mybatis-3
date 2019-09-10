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

import org.apache.ibatis.annotations.ResultMapProvider;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

public interface Mapper {

  @ResultMapProvider(type = MyResultMapProvider.class, method = "provideResultMap")
  @Select("select * from author where id = #{id}")
  AnnotatedAuthor selectAuthorById(Integer id);

  @Results(id = "postResultMap")
  @ResultMapProvider(type = MyResultMapProvider.class, method = "provideResultMap")
  @Select("select * from post where blog_id = #{blogId}")
  List<AnnotatedPost> selectPostsByBlogId(Integer blogId);

  @ResultMapProvider(type = MyResultMapProvider.class, method = "provideResultMap")
  @Select({ "select b.id, b.title, b.author_id, p.id p_id, p.body p_body, p.blog_id p_blog_id",
    "from blog b left join post p on p.blog_id = b.id",
    "where b.id = #{id}" })
  AnnotatedBlog selectBlogById(Integer id);

}
