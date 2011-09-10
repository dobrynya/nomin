package org.nomin.performance;

import org.dozer.Mapper;
import org.nomin.NominMapper;
import org.nomin.entity.DetailedPerson;
import org.nomin.entity.Employee;
import org.nomin.entity.LinearManager;
import org.nomin.entity.Person;

import static java.lang.String.format;

/**
 * Measures some invocations.
 * @author Dmitry Dobrynin
 *         Creted 03.09.11 23:34
 */
public class Measurement {
    long min = Long.MAX_VALUE, max = Long.MIN_VALUE, avg, total, count;

    public Measurement measure(int numberOfIterations, NominMapper mapper) throws Exception {
        for (int i = 0; i < numberOfIterations; i++) {
            LinearManager lm = DataGenerator.createdLinearManager();
            DetailedPerson dp = DataGenerator.createDetailedPerson();

            long start = System.nanoTime();

            mapper.map(lm, DetailedPerson.class);
            mapper.map(lm, Person.class);
            mapper.map(dp, LinearManager.class);
            mapper.map(dp, Employee.class);

            long end = System.nanoTime();
            long delta = end - start;
            total += delta;
            if (min > delta) min = delta;
            if (max < delta) max = delta;
            count++;
        }
        avg = total / count;

        return this;
    }

    public Measurement measure(int numberOfIterations, MyMapper mapper) throws Exception {
        for (int i = 0; i < numberOfIterations; i++) {
            LinearManager lm = DataGenerator.createdLinearManager();
            DetailedPerson dp = DataGenerator.createDetailedPerson();

            long start = System.nanoTime();

            mapper.map(lm, DetailedPerson.class);
            mapper.map(lm, Person.class);
            mapper.map(dp, LinearManager.class);
            mapper.map(dp, Employee.class);

            long end = System.nanoTime();
            long delta = end - start;
            total += delta;
            if (min > delta) min = delta;
            if (max < delta) max = delta;
            count++;
        }
        avg = total / count;

        return this;
    }

    public Measurement measure(int numberOfIterations, Mapper mapper) throws Exception {
        for (int i = 0; i < numberOfIterations; i++) {
            LinearManager lm = DataGenerator.createdLinearManager();
            DetailedPerson dp = DataGenerator.createDetailedPerson();

            long start = System.nanoTime();

            mapper.map(lm, DetailedPerson.class);
            mapper.map(lm, Person.class);
            mapper.map(dp, LinearManager.class);
            mapper.map(dp, Employee.class);

            long end = System.nanoTime();
            long delta = end - start;
            total += delta;
            if (min > delta) min = delta;
            if (max < delta) max = delta;
            count++;
        }
        avg = total / count;

        return this;
    }

    public void printResult(String message) {
        System.out.println(message + "\nResults:");
        System.out.println(format("Total execution time: %s ns", total));
        System.out.println(format("Max execution time: %s ns", max));
        System.out.println(format("Min execution time: %s ns", min));
        System.out.println(format("Avg execution time: %s ns", avg));
    }
}
