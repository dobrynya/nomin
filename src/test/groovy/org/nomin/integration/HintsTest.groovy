package org.nomin.integration

import org.nomin.NominMapper
import org.nomin.core.Nomin
import org.nomin.entity.Employee
import org.nomin.entity.Kid
import org.nomin.entity.LegacyDetails
import org.nomin.entity.LegacyPerson
import org.nomin.mappings.LegacyPerson2Employee

/**
 * Tests hint processing.
 * @author Dmitry Dobrynin
 * Created 11.05.2010 12:54:11
 */
class HintsTest {
  NominMapper mapper = new Nomin()

  // TODO: Write tests with using DEFAULT constant

  @org.junit.Test
  void testHints() {
    mapper.parse LegacyPerson2Employee
    def lp = new LegacyPerson(name: "Legacy", lastName: "Person", details: new LegacyDetails(birthday: new Date(),
            sex: true, children: [new Kid(kidName: "Kid")]))
    Employee em = mapper.map(lp, Employee)
    assert em && em.name == "Legacy" && em.last == "Person" && em.details && em.details.birth == lp.details.birthday &&
            em.details.sex == true && em.details.kids && em.details.kids.size() == 1 &&
            em.details.kids.iterator().next().kidName == "Kid"
    LegacyPerson lp2 = mapper.map(em, LegacyPerson)
    assert lp2 && lp2.name == "Legacy" && lp2.lastName == "Person" && lp2.details.children?.length == 1 &&
            lp2.details.children[0].kidName == "Kid"
  }
}
