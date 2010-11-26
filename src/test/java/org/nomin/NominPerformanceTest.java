package org.nomin;

import java.util.*;
import org.junit.*;
import org.nomin.core.*;
import org.nomin.entity.*;
import org.nomin.mappings.*;
import org.nomin.util.*;
import static java.util.Arrays.*;

/**
 * Tests Nomin performance.
 * @author Dmitry Dobrynin
 *         Created 24.05.2010 19:56:41
 */
public class NominPerformanceTest {
    Nomin nomin = new Nomin(Person2Employee.class, DetailedPerson2LinearManager.class, Child2Kid.class);
    int N = 1;

    @Test
    public void testMap() {
        DetailedPerson dp = new DetailedPerson("Person's Name", "Person's Last Name", new Date(), Gender.MALE,
                asList(new Child("Child's Name")), "Just some textual description", "MIT", "High");

        LinearManager lm = new LinearManager("Manager's Name", "Manager's Lst Name", null,
                new Details(new Date(), true, new HashSet<Kid>(asList(new Kid("Kid's Name"))), asList(new Education("MIT", "High"))),
                "Just an employee");

        for (int i = 0; i < N; i++) {
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

    protected ArrayList<Long> times = new ArrayList<Long>(N);

    void calcTime(Object o, Class<?> clazz) {
        times.add(System.nanoTime());
        nomin.map(o, clazz);
        long d = System.nanoTime() - times.remove(times.size() - 1);
        times.add(d);
    }
}
