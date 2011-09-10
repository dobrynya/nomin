package org.nomin.performance;

import org.nomin.entity.*;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Performs hardcoded mapping.
 * @author Dmitry Dobrynin
 *         Created 10.09.11 0:26
 */
@SuppressWarnings({"ConstantConditions"})
public class HardcodedMapper implements MyMapper {
    public Object map(Object source, Class<?> targetClass) {
        if (source == null) return null;
        else if (targetClass == Person.class && source instanceof Employee) return mapPerson((Employee) source, new Person());
        else if (targetClass == Employee.class && source instanceof Person) return mapEmployee((Person) source, new Employee());
        else if (targetClass == DetailedPerson.class) return source instanceof LinearManager ?
                mapDetailedPerson((LinearManager) source, new DetailedPerson()) : mapPerson((Employee) source, new DetailedPerson());
        else if (targetClass == LinearManager.class) return source instanceof DetailedPerson ?
                mapLinearManager((DetailedPerson) source, new LinearManager()) : mapEmployee((Person) source, new LinearManager());
        else return null;
    }

    protected Child createChild(Kid kid) {
        if (kid == null) return null;
        Child child = new Child();
        child.setName(kid.getKidName());
        return child;
    }

    protected Kid createKid(Child child) {
        if (child == null) return null;
        Kid kid = new Kid();
        kid.setKidName(child.getName());
        return kid;
    }

    protected Person mapPerson(Employee e, Person p) {
        if (e == null) return null;
        p.setName(e.getName());
        p.setLastName(e.getLast());
        if (e.getDetails() != null) {
            p.setBirthDate(e.getDetails().getBirth());
            p.setGender(e.getDetails().getSex() == null ? null : e.getDetails().getSex() ? Gender.MALE : Gender.FEMALE);
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            p.setStrDate(e.getDetails().getBirth() == null ? null : sdf.format(e.getDetails().getBirth()));
            if (e.getDetails().getKids() != null) {
                List<Child> children = new ArrayList<Child>(e.getDetails().getKids().size());
                for (Kid kid : e.getDetails().getKids()) children.add(createChild(kid));
                p.setChildren(children);
            }
        }
        return p;
    }

    protected Employee mapEmployee(Person p, Employee e) {
        if (p == null) return null;
        e.setName(p.getName());
        e.setLast(p.getLastName());
        e.setDetails(new Details());
        e.getDetails().setBirth(p.getBirthDate());
        e.getDetails().setSex(p.getGender() == null ? null : p.getGender() == Gender.MALE);
        if (p.getChildren() != null) {
            Set<Kid> kids = new HashSet<Kid>(p.getChildren().size());
            for (Child child : p.getChildren()) kids.add(createKid(child));
            e.getDetails().setKids(kids);
        }
        return e;
    }

    protected LinearManager mapLinearManager(DetailedPerson dp, LinearManager lm) {
        mapEmployee(dp, lm);
        lm.setCharacteristics(dp.getDescription());
        lm.getDetails().setEducations(new ArrayList<Education>());
        lm.getDetails().getEducations().add(new Education());
        lm.getDetails().getEducations().get(0).setName(dp.getEducationName());
        lm.getDetails().getEducations().get(0).setDescription(dp.getEducationDescription());
        return lm;
    }

    protected DetailedPerson mapDetailedPerson(LinearManager lm, DetailedPerson dp) {
        mapPerson(lm, dp);
        dp.setDescription(lm.getCharacteristics());
        dp.setEducationName(lm.getDetails() == null ? null : lm.getDetails().getEducations() != null &&
                lm.getDetails().getEducations().size() >= 1 ? lm.getDetails().getEducations().get(0).getName() : null);
        dp.setEducationDescription(lm.getDetails() == null ? null : lm.getDetails().getEducations() != null &&
                lm.getDetails().getEducations().size() >= 1 ? lm.getDetails().getEducations().get(0).getDescription() : null);
        return dp;
    }
}
