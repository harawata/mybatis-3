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
package org.apache.ibatis.submitted.usesjava8.optional;

import java.time.LocalDate;
import java.util.Optional;

public class UserWithConstructor {
  private Integer id;
  private Optional<String> name;
  private Optional<LocalDate> dob;

  public UserWithConstructor(Integer id, Optional<String> name, Optional<LocalDate> dob) {
    super();
    this.id = id;
    this.name = name;
    this.dob = dob;
  }

  public UserWithConstructor(Integer id, Optional<String> name) {
    super();
    this.id = id;
    this.name = name;
  }

  public Integer getId() {
    return id;
  }

  public Optional<String> getName() {
    return name;
  }

  public Optional<LocalDate> getDob() {
    return dob;
  }
}
