package org.nomin.performance;

import org.dozer.DozerBeanMapper;
import org.junit.*;
import org.nomin.*;
import org.nomin.core.*;
import org.nomin.entity.DetailedPerson;
import org.nomin.entity.Employee;
import org.nomin.entity.LinearManager;
import org.nomin.entity.Person;
import org.nomin.mappings.*;

import java.util.Arrays;
import java.util.List;
import static java.lang.String.format;
import static java.util.Arrays.asList;

/**
 * Tests Nomin & Dozer performance.
 * @author Dmitry Dobrynin
 *         Created 24.05.2010 19:56:41
 */
@SuppressWarnings({"unchecked"})
public class PerformanceTest {
    int N = 10000;

    @Test
    public void test() throws Exception {
        // TODO: Implement hardcoded mapping!
        List<Measurement> measurements = asList(
                new Measurement().measure(N, new Nomin(Person2Employee.class, DetailedPerson2LinearManager.class, Child2Kid.class).disableCache()),
                new Measurement().measure(N, new Nomin(Person2Employee.class, DetailedPerson2LinearManager.class, Child2Kid.class)
                        .defaultIntrospector(Mapping.getExploding()).disableCache()),
                new Measurement().measure(N, new Nomin(Person2Employee.class, DetailedPerson2LinearManager.class, Child2Kid.class)
                        .defaultIntrospector(Mapping.getFast()).disableCache()),
                new Measurement().measure(N, new DozerBeanMapper(Arrays.asList("person2employee.xml")))
        );

        System.out.println("-------------------------------------------------------------------------------------------------");
        System.out.println("| Test                                           | Max       | Min     | Avg    | Total         |");
        System.out.println("-------------------------------------------------------------------------------------------------");
        for (int i = 0; i < 4; i++)
            System.out.println(format("| %46s | %9s | %7s | %6s | %13s |",
                    asList("Nomin: Refection introspector", "Nomin: Exploding introspector", "Nomin: Fast introspector", "Dozer").get(i),
                    measurements.get(i).max, measurements.get(i).min, measurements.get(i).avg, measurements.get(i).total));
        System.out.println("-------------------------------------------------------------------------------------------------");
    }
}
