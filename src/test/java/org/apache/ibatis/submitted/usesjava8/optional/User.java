/**
 *    Copyright 2009-2016 the original author or authors.
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
import java.util.List;
import java.util.Optional;

public class User {
  private Integer id;
  private Optional<String> name;
  private Optional<LocalDate> dob;
  private Optional<Pet> pet;
  private List<Optional<Pet>> pets;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Optional<String> getName() {
    return name;
  }

  public void setName(Optional<String> name) {
    this.name = name;
  }

  public Optional<LocalDate> getDob() {
    return dob;
  }

  public Optional<Pet> getPet() {
    return pet;
  }

  public void setPet(Optional<Pet> pet) {
    this.pet = pet;
  }

  public List<Optional<Pet>> getPets() {
    return pets;
  }

  public void setPets(List<Optional<Pet>> pets) {
    this.pets = pets;
  }
}
