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
package org.apache.ibatis.submitted.keygen;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

public interface CountryMapper {

  int insertList(List<Country> countries);

  int insertNamedList(@Param("countries") List<Country> countries);

  int insertSet(Set<Country> countries);

  int insertNamedSet(@Param("countries") Set<Country> countries);

  int insertArray(Country[] countries);

  int insertNamedArray(@Param("countries") Country[] countries);

  int insertCountryAndSomeId(@Param("country") Country country, @Param("someId") Integer someId);

  int insertListAndSomeId(@Param("list") List<Country> countries, @Param("someId") Integer someId);

  int insertNamedListAndSomeId(@Param("countries") List<Country> countries, @Param("someId") Integer someId);

  int insertUndefineKeyProperty(Country country);

}
