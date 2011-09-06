package org.nomin.performance;

import org.nomin.entity.*;

import java.util.Date;
import java.util.HashSet;

import static java.util.Arrays.asList;

/**
 * Generates data for tests.
 * @author Dmitry Dobrynin
 * Created 03.09.11 23:50
 */
public class DataGenerator {
    static int count;

    public static DetailedPerson createDetailedPerson() {
        count++;
        return new DetailedPerson("Person's Name" + count, "Person's Last Name" + count, new Date(), Gender.MALE,
                asList(new Child("Child's Name" + count)), "Just some textual description" + count, "Education" + count, "EducationDescription" + count);
    }

    public static LinearManager createdLinearManager() {
        count++;
        return new LinearManager("Manager's Name" + count, "Manager's Last Name" + count, "Properties" + count,
                new Details(new Date(), true, new HashSet<Kid>(asList(new Kid("Kid's Name" + count))), asList(new Education("Education" + count, "EducationDesc" + count))),
                "Just an employee" + count);
    }
}
