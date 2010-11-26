package org.nomin.entity;

import static java.text.MessageFormat.*;
import java.util.*;

/**
 * Just represents a business entity.
 * @author Dmitry Dobrynin
 *         Created 13.04.2010 11:04:19
 */
public class Details {
    private Date birth;
    private Boolean sex; // true = male, false = female
    private Set<Kid> kids;
    private List<Education> educations;

    public Details() {}

    public Details(Date birth, Boolean sex, Set<Kid> kids, List<Education> educations) {
        this.birth = birth;
        this.sex = sex;
        this.kids = kids;
        this.educations = educations;
    }

    public Date getBirth() { return birth; }

    public void setBirth(Date birth) { this.birth = birth; }

    public Boolean getSex() { return sex; }

    public void setSex(Boolean sex) { this.sex = sex; }

    public Set<Kid> getKids() { return kids; }

    public void setKids(Set<Kid> kids) { this.kids = kids; }

    public List<Education> getEducations() { return educations; }

    public void setEducations(List<Education> educations) { this.educations = educations; }

    public String toString() {
        return format("Details [birth = {0} sex = {1} kids = {2}, educations = {3}]", birth, sex, kids, educations);
    }
}
