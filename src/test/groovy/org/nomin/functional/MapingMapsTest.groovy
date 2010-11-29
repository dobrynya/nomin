package org.nomin.functional

import org.nomin.core.Nomin
import org.nomin.mappings.*
import org.nomin.entity.*

/**
 * Tests mapping the map instances.
 * @author Dmitry Dobrynin
 * Date: 08.11.2010 Time: 18:01:59
 */
class MapingMapsTest {
  def nomin = new Nomin(MapHolder1ToMapHolder2, Person2Employee)

  def mh1 = new MapHolder1(strings: ["1": "1", "2": "2", "3": "3"],
          persons: ["1": new Person(name: "Name1", lastName: "LastName1"), "2": new Person(name: "Name2", lastName: "LastName2")],
          objects: [abc: new Person(name: "Abc", lastName: "AbcLastName"), "4": new Person(name: "Name4", lastName: "LastName4")],
          strings2: [propertyName: "propertyValue"]
  )

  def mh2 = new MapHolder2(integers: [1: 1, 2: 2, 3: 3],
          employees: [(1): new Employee(name: "Name1", last: "Last1"), (2): new Employee(name: "Name2", last: "Last2")],
          objects: [abc: new Employee(name: "AbcName", last: "AbcLastName"), "5": new Employee(name: "Name5", last: "LastName5")],
          props: ["property1=value1"]
  )

  @org.junit.Test
  void test1To2() {
    MapHolder2 mh2 = nomin.map(mh1, MapHolder2)
    assert mh2 && mh2.integers?.size() == 3 && mh2.integers[1] == 1 && mh2.integers[2] == 2 && mh2.integers[3] == 3
    assert mh2.employees?.size() == 2 && mh2.employees[1].name == "Name1" && mh2.employees[2].name == "Name2"
    assert mh2.employees[1].last == "LastName1" && mh2.employees[2].last == "LastName2"
    assert mh2.objects?.size() == 2 && mh2.objects["abc"].name == "Abc" && mh2.objects["abc"].last == "AbcLastName"
    assert mh2.objects[4].name == "Name4" && mh2.objects[4].last == "LastName4"
    mh2.objects.each { k, v -> assert v instanceof LinearManager }
    assert ["propertyName=propertyValue"] == mh2.props
  }

  @org.junit.Test
  void test2To1() {
    MapHolder1 mh1 = nomin.map(mh2, MapHolder1)
    assert mh1 && mh1.strings?.size() == 3 && mh1.strings["1"] == "1" && mh1.strings["2"] == "2" && mh1.strings["3"] == "3"
    assert mh1.persons?.size() == 2 && mh1.persons["1"].name == "Name1" && mh1.persons["2"].name == "Name2"
    assert mh1.persons["1"].lastName == "Last1" && mh1.persons["2"].lastName == "Last2"
    assert mh1.objects?.size() == 2 && mh1.objects[5].name == "Name5" && mh1.objects[5].lastName == "LastName5"
    assert mh1.objects["abc"].name == "AbcName" && mh1.objects["abc"].lastName == "AbcLastName"
    mh1.objects.each { k, v -> assert v instanceof DetailedPerson }
    assert [property1: "value1"] == mh1.strings2
  }
}
