package org.nomin.core

import org.junit.Test
import org.nomin.NominMapper
import org.nomin.entity.*

/**
 * Tests Nomin.
 * @author Dmitry Dobrynin
 * Created: 18.05.2010 0:36:40
 */
class NominTest {
  NominMapper nomin = new Nomin().disableAutomapping()

  @Test
  void testWhetherAutomappingIsDisabled() {
    assert !nomin.isAutomappingEnabled()
  }

  @Test
  void testWhetherAutomappingIsEnabled() {
      nomin.enableAutomapping()
      assert nomin.isAutomappingEnabled()
  }

  @Test
  void testFindApplicable() {
    ParsedMapping m1, m2, m3, m4
    nomin.mappings = [
            m1 = new ParsedMapping("", Person, Employee, null, [], [:], false, null, null, nomin),
            m2 = new ParsedMapping("", Person, Employee, "1", [], [:], false, null, null, nomin),
            m3 = new ParsedMapping("", LinearManager, DetailedPerson, null, [], [:], false, null, null, nomin),
            m4 = new ParsedMapping("", LinearManager, DetailedPerson, "1", [], [:], false, null, null, nomin)
    ]

    def res = nomin.findApplicable(new MappingKey(Person, Employee, null))
    assert res.size() == 1 && res[0].mapping == m1 && res[0].direction
    res = nomin.findApplicable(new MappingKey(Person, LinearManager, null))
    assert res.size() == 1 && res[0].mapping == m1 && res[0].direction
    res = nomin.findApplicable(new MappingKey(DetailedPerson, Employee, null))
    assert res.size() == 1 && res[0].mapping == m1 && res[0].direction
    res = nomin.findApplicable(new MappingKey(DetailedPerson, LinearManager, null))
    assert res.size() == 2 && res[0].mapping == m1 && res[1].mapping == m3 && res[0].direction && !res[1].direction
    res = nomin.findApplicable(new MappingKey(Employee, Person, null))
    assert res.size() == 1 && res[0].mapping == m1 && !res[0].direction
    res = nomin.findApplicable(new MappingKey(LinearManager, Person, null))
    assert res.size() == 1 && res[0].mapping == m1 && !res[0].direction
    res = nomin.findApplicable(new MappingKey(Employee, DetailedPerson, null))
    assert res.size() == 1 && res[0].mapping == m1 && !res[0].direction
    res = nomin.findApplicable(new MappingKey(LinearManager, DetailedPerson, null))
    assert res.size() == 2 && res[0].mapping == m1 && res[1].mapping == m3 && !res[0].direction && res[1].direction

    // Finds applicable using mappingCase
    res = nomin.findApplicable(new MappingKey(Person, Employee, "1"))
    assert res.size() == 1 && res[0].mapping == m2 && res[0].direction
    res = nomin.findApplicable(new MappingKey(Person, LinearManager, "1"))
    assert res.size() == 1 && res[0].mapping == m2 && res[0].direction
    res = nomin.findApplicable(new MappingKey(DetailedPerson, Employee, "1"))
    assert res.size() == 1 && res[0].mapping == m2 && res[0].direction
    res = nomin.findApplicable(new MappingKey(DetailedPerson, LinearManager, "1"))
    assert res.size() == 2 && res[0].mapping == m2 && res[1].mapping == m4 && res[0].direction && !res[1].direction
    res = nomin.findApplicable(new MappingKey(Employee, Person, "1"))
    assert res.size() == 1 && res[0].mapping == m2 && !res[0].direction
    res = nomin.findApplicable(new MappingKey(LinearManager, Person, "1"))
    assert res.size() == 1 && res[0].mapping == m2 && !res[0].direction
    res = nomin.findApplicable(new MappingKey(Employee, DetailedPerson, "1"))
    assert res.size() == 1 && res[0].mapping == m2 && !res[0].direction
    res = nomin.findApplicable(new MappingKey(LinearManager, DetailedPerson, "1"))
    assert res.size() == 2 && res[0].mapping == m2 && res[1].mapping == m4 && !res[0].direction && res[1].direction

    // Finds non-existent mappings
    assert !nomin.findApplicable(new MappingKey(Person, Person, null))
    assert !nomin.findApplicable(new MappingKey(Person, DetailedPerson, null))
    assert !nomin.findApplicable(new MappingKey(Employee, Employee, null))
    assert !nomin.findApplicable(new MappingKey(Employee, LinearManager, null))
    assert !nomin.findApplicable(new MappingKey(String, Integer, null))
    assert !nomin.findApplicable(new MappingKey(Person, Employee, "non-existent"))
    assert !nomin.findApplicable(new MappingKey(DetailedPerson, LinearManager, "non-existent"))
  }

  @Test
  void testKey() {
    assert new MappingKey(Person, Employee, null) == new MappingKey(Person, Employee, null)
    assert new MappingKey(Person, Employee, "1") == new MappingKey(Person, Employee, "1")
    assert new MappingKey(Person, Employee, null) != new MappingKey(Employee, Person, null)
    assert new MappingKey(Person, Employee, "1") != new MappingKey(Employee, Person, "1")
    assert new MappingKey(Person, Employee, null) != new MappingKey(Person, Employee, "1")
    assert new MappingKey(Person, Employee, "1") != new MappingKey(Person, Employee, null)
  }

  @Test
  void testParsingScripts() {
    nomin.parse "mappings/person2employee.groovy", "mappings/child2kid.groovy"
    testMappedInstances()
  }

  @Test (expected = NominException.class)
  void testFailedParsingScripts() {
    nomin.parse "unexistentMapping.groovy"
  }

  @Test
  void testParseFiles() {
    nomin.parseFiles("src/test/resources/mappings/person2employee.groovy", "src/test/resources/mappings/child2kid.groovy")
    testMappedInstances()
  }

  @Test
  void testParseDirectory() {
    nomin.parseDirectory("src/test/resources/mappings")
    testMappedInstances()
  }

    private void testMappedInstances() {
        def now = new Date()
        Employee e = nomin.map(new Person(name: "Name", lastName: "Last", gender: Gender.MALE, birthDate: now,
                children: [new Child(name: "Child")]), Employee)
        assert e && e.name == "Name" && e.last == "Last" && e.details && e.details.birth == now && e.details.sex &&
                e.details.kids?.size() == 1
        Kid kid = e.details.kids.iterator().next()
        assert kid.kidName == "Child"
    }

  @Test
  void testParsingMappingsUsingAnotherClassLoader() {
      ClassLoader another = new ClassLoader(this.class.classLoader) {
          InputStream getResourceAsStream(String name) {
              return super.getResourceAsStream(name == "script.groovy" ?
                      "mappings/child2kid.groovy" : name)
          }
      }
      nomin = new Nomin().classLoader(another)

      try {
          nomin.parse("non-existent.groovy")
          assert false
      } catch (NominException e) {
          assert e.message.startsWith("Specified resource")
      }

      nomin.parse("script.groovy")

      Kid kid = new Kid("frodo baggins")
      Child child = nomin.map(kid, Child)
      assert child.name == "frodo baggins"
  }
}
