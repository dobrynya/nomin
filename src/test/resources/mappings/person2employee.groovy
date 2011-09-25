package mappings

import org.nomin.entity.*

mappingFor a: Person, b: Employee
a.name = b.name
a.lastName = b.last
b.details.birth = a.birthDate
a.children = b.details.kids
a.gender = b.details.sex
convert to_a: { it ? Gender.MALE : Gender.FEMALE }, to_b: { it == Gender.MALE }
