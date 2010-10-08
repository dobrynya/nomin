package org.nomin.core

import org.nomin.core.NominException
import org.nomin.entity.*

/**
 * Tests Nomin.
 * @author Dmitry Dobrynin
 * Created: 18.05.2010 0:36:40
 */
class NominTest {
  Nomin nomin = new Nomin()

  @org.junit.Test
  void testFindApplicable() {
    ParsedMapping m1, m2, m3, m4
    nomin.mappings = [
            m1 = new ParsedMapping("", Person, Employee, null, [], [:], false, null, nomin),
            m2 = new ParsedMapping("", Person, Employee, "1", [], [:], false, null, nomin),
            m3 = new ParsedMapping("", LinearManager, DetailedPerson, null, [], [:], false, null, nomin),
            m4 = new ParsedMapping("", LinearManager, DetailedPerson, "1", [], [:], false, null, nomin)
    ]

    def res = nomin.findApplicable(Person, Employee, null)
    assert res.size() == 1 && res[0].mapping == m1 && res[0].direction
    res = nomin.findApplicable(Person, LinearManager, null)
    assert res.size() == 1 && res[0].mapping == m1 && res[0].direction
    res = nomin.findApplicable(DetailedPerson, Employee, null)
    assert res.size() == 1 && res[0].mapping == m1 && res[0].direction
    res = nomin.findApplicable(DetailedPerson, LinearManager, null)
    assert res.size() == 2 && res[0].mapping == m1 && res[1].mapping == m3 && res[0].direction && !res[1].direction
    res = nomin.findApplicable(Employee, Person, null)
    assert res.size() == 1 && res[0].mapping == m1 && !res[0].direction
    res = nomin.findApplicable(LinearManager, Person, null)
    assert res.size() == 1 && res[0].mapping == m1 && !res[0].direction
    res = nomin.findApplicable(Employee, DetailedPerson, null)
    assert res.size() == 1 && res[0].mapping == m1 && !res[0].direction
    res = nomin.findApplicable(LinearManager, DetailedPerson, null)
    assert res.size() == 2 && res[0].mapping == m1 && res[1].mapping == m3 && !res[0].direction && res[1].direction

    // Finds applicable using mappingCase
    res = nomin.findApplicable(Person, Employee, "1")
    assert res.size() == 1 && res[0].mapping == m2 && res[0].direction
    res = nomin.findApplicable(Person, LinearManager, "1")
    assert res.size() == 1 && res[0].mapping == m2 && res[0].direction
    res = nomin.findApplicable(DetailedPerson, Employee, "1")
    assert res.size() == 1 && res[0].mapping == m2 && res[0].direction
    res = nomin.findApplicable(DetailedPerson, LinearManager, "1")
    assert res.size() == 2 && res[0].mapping == m2 && res[1].mapping == m4 && res[0].direction && !res[1].direction
    res = nomin.findApplicable(Employee, Person, "1")
    assert res.size() == 1 && res[0].mapping == m2 && !res[0].direction
    res = nomin.findApplicable(LinearManager, Person, "1")
    assert res.size() == 1 && res[0].mapping == m2 && !res[0].direction
    res = nomin.findApplicable(Employee, DetailedPerson, "1")
    assert res.size() == 1 && res[0].mapping == m2 && !res[0].direction
    res = nomin.findApplicable(LinearManager, DetailedPerson, "1")
    assert res.size() == 2 && res[0].mapping == m2 && res[1].mapping == m4 && !res[0].direction && res[1].direction

    // Finds non-existent mappings
    assert !nomin.findApplicable(Person, Person, null)
    assert !nomin.findApplicable(Person, DetailedPerson, null)
    assert !nomin.findApplicable(Employee, Employee, null)
    assert !nomin.findApplicable(Employee, LinearManager, null)
    assert !nomin.findApplicable(String, Integer, null)
    assert !nomin.findApplicable(Person, Employee, "non-existent")
    assert !nomin.findApplicable(DetailedPerson, LinearManager, "non-existent")
  }

  @org.junit.Test
  void testKey() {
    assert new Nomin.Key(Person, Employee, null) == new Nomin.Key(Person, Employee, null)
    assert new Nomin.Key(Person, Employee, "1") == new Nomin.Key(Person, Employee, "1")
    assert new Nomin.Key(Person, Employee, null) != new Nomin.Key(Employee, Person, null)
    assert new Nomin.Key(Person, Employee, "1") != new Nomin.Key(Employee, Person, "1")
    assert new Nomin.Key(Person, Employee, null) != new Nomin.Key(Person, Employee, "1")
    assert new Nomin.Key(Person, Employee, "1") != new Nomin.Key(Person, Employee, null)
  }

  @org.junit.Test
  void testParsingScripts() {
    nomin.parse "person2employee.groovy", "child2kid.groovy"
    def now = new Date()
    Employee e = nomin.map(new Person(name: "Name", lastName: "Last", gender: Gender.MALE, birthDate: now,
            children: [new Child(name: "Child")]), Employee)
    assert e && e.name == "Name" && e.last == "Last" && e.details && e.details.birth == now && e.details.sex &&
            e.details.kids?.size() == 1
    Kid kid = e.details.kids.iterator().next();
    assert kid.kidName == "Child"
  }

  @org.junit.Test (expected = NominException.class)
  void testFailedParsingScripts() {
    nomin.parse "unexistentMapping.groovy"
  }
}
