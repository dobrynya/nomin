package org.nomin.performance;

import org.dozer.DozerBeanMapper;
import org.junit.Test;
import org.nomin.Mapping;
import org.nomin.core.Nomin;
import org.nomin.mappings.Child2Kid;
import org.nomin.mappings.DetailedPerson2LinearManager;
import org.nomin.mappings.Person2Employee;

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
    int N = 1000000;

    @Test
    public void test() throws Exception {
        List<Measurement> measurements = asList(
                new Measurement().measure(N, new HardcodedMapper()),
                new Measurement().measure(N, new Nomin(Person2Employee.class, DetailedPerson2LinearManager.class, Child2Kid.class).disableCache()),
                new Measurement().measure(N, new Nomin(Person2Employee.class, DetailedPerson2LinearManager.class, Child2Kid.class)
                        .defaultIntrospector(Mapping.exploding).disableCache()),
                new Measurement().measure(N, new Nomin(Person2Employee.class, DetailedPerson2LinearManager.class, Child2Kid.class)
                        .defaultIntrospector(Mapping.fast).disableCache()),
                new Measurement().measure(N, new DozerBeanMapper(Arrays.asList("person2employee.xml")))
        );

        System.out.println("-------------------------------------------------------------------------------------------------");
        System.out.println("| Test                                           | Max       | Min     | Avg    | Total         |");
        System.out.println("-------------------------------------------------------------------------------------------------");
        for (int i = 0; i <= 4; i++)
            System.out.println(format("| %46s | %9s | %7s | %6s | %13s |",
                    asList("Hardcoded Mapper", "Nomin: Refection introspector", "Nomin: Exploding introspector", "Nomin: Fast introspector", "Dozer", "Hardcoded Mapper").get(i),
                    measurements.get(i).max, measurements.get(i).min, measurements.get(i).avg, measurements.get(i).total));
        System.out.println("-------------------------------------------------------------------------------------------------");
    }
}
