package org.nomin;

import java.util.*;
import org.junit.*;
import org.nomin.core.*;
import org.nomin.entity.*;
import org.nomin.mappings.*;
import org.nomin.util.*;

/**
 * Tests Nomin performance.
 * @author Dmitry Dobrynin
 *         Created 24.05.2010 19:56:41
 */
public class NominPerformanceTest {
    Nomin nomin = new Nomin(Person2Employee.class, DetailedPerson2LinearManager.class, Child2Kid.class);

    @Test
    public void testMap() {
        DetailedPerson dp = new DetailedPerson();
        dp.setName("Person's Name");
        dp.setLastName("Person's Last Name");
        dp.setBirthDate(new Date());
        dp.setGender(Gender.MALE);
        dp.setChildren(Arrays.asList(new Child("Child's Name")));
        dp.setDescription("Just some textual description");
        dp.setEducationName("MIT");
        dp.setEducationDescription("High");

        LinearManager lm = new LinearManager();
        lm.setName("Manager's Name");
        lm.setLast("Manager's Lst Name");
        lm.setDetails(new Details());
        lm.getDetails().setBirth(new Date());
        lm.getDetails().setSex(true);
        lm.getDetails().setKids(new HashSet<Kid>(Arrays.asList(new Kid("Kid's Name"))));
        lm.setCharacteristics("Just an employee");
        lm.getDetails().setEducations(Arrays.asList(new Education("MIT", "High")));

        for (int i = 0; i < 5; i++) {
            calcTime(dp, Employee.class);
            calcTime(dp, LinearManager.class);
            calcTime(lm, Person.class);
            calcTime(lm, DetailedPerson.class);
        }
        System.out.println("Max = " + Collections.max(times));
        System.out.println("Min = " + Collections.min(times));
        long sum = 0;
        for (long t : times) {
            sum += t;
        }
        double d = sum / times.size();
        System.out.println("Avg = " + d);
        System.out.println("Sum = " + sum);
    }

    protected ArrayList<Long> times = new ArrayList<Long>(500001);

    void calcTime(Object o, Class<?> clazz) {
        times.add(System.nanoTime());
        nomin.map(o, clazz);
        long d = System.nanoTime() - times.remove(times.size() - 1);
        times.add(d);
    }

    @Test
    public void testIntrospectors() {
        Person p = new Person();
        PropertyAccessor refl = MappingConsts.jb.property("name", Person.class);
        PropertyAccessor asm = new AsmJbIntrospector().property("name", Person.class);
        PropertyAccessor impl = new PropertyAccessor() {
            public String getName() { return null; }
            public TypeInfo getTypeInfo() { return null; }
            public void setTypeInfo(TypeInfo typeInfo) {}
            public Object get(Object instance) { return ((Person) instance).getName(); }
            public void set(Object instance, Object value) { ((Person) instance).setName((String) value); }
        };
        long s2 = System.nanoTime();
        for (int i = 0; i < 500000; i++) { asm.get(p); asm.set(p, "value"); }
        long d2 = System.nanoTime() - s2;
        System.out.println("Delta using generated accessor      " + d2);
        long s1 = System.nanoTime();
        for (int i = 0; i < 500000; i++) { refl.get(p); refl.set(p, "value"); }
        long d1 = System.nanoTime() - s1;
        System.out.println("Delta using reflection              " + d1);
        long s3 = System.nanoTime();
        for (int i = 0; i < 500000; i++) { impl.get(p); impl.set(p, "value"); }
        long d3 = System.nanoTime() - s3;
        System.out.println("Delta using direct method call      " + d3);
    }
}
