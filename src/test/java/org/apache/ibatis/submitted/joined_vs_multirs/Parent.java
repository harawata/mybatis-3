/*
 *    Copyright 2009-2013 The MyBatis Team
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
package org.apache.ibatis.submitted.joined_vs_multirs;

import java.util.List;

public class Parent {
  private Integer id;

  private String name;

  private Integer team;

  private List<Child> children1;

  private List<Child> children2;

  private List<Child> children3;

  private List<Child> children4;

  private List<Child> children5;

  private List<Child> children6;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getTeam() {
    return team;
  }

  public void setTeam(Integer team) {
    this.team = team;
  }

  public List<Child> getChildren1() {
    return children1;
  }

  public void setChildren1(List<Child> children1) {
    this.children1 = children1;
  }

  public List<Child> getChildren2() {
    return children2;
  }

  public void setChildren2(List<Child> children2) {
    this.children2 = children2;
  }

  public List<Child> getChildren3() {
    return children3;
  }

  public void setChildren3(List<Child> children3) {
    this.children3 = children3;
  }

  public List<Child> getChildren4() {
    return children4;
  }

  public void setChildren4(List<Child> children4) {
    this.children4 = children4;
  }

  public List<Child> getChildren5() {
    return children5;
  }

  public void setChildren5(List<Child> children5) {
    this.children5 = children5;
  }

  public List<Child> getChildren6() {
    return children6;
  }

  public void setChildren6(List<Child> children6) {
    this.children6 = children6;
  }
}
