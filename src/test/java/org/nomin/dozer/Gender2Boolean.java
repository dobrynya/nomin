package org.nomin.dozer;

import org.dozer.DozerConverter;
import org.nomin.entity.Gender;

/**
 * Document please.
 * @author Dmitry Dobrynin
 *         Created 13.04.2010 17:41:14
 */
public class Gender2Boolean extends DozerConverter<Gender, Boolean> {

    public Gender2Boolean() {
        super(Gender.class, Boolean.class);
    }

    public Boolean convertTo(Gender gender, Boolean aBoolean) {
        return gender != null & gender == Gender.MALE;
    }

    public Gender convertFrom(Boolean aBoolean, Gender gender) {
        return aBoolean != null && aBoolean ? Gender.MALE : Gender.FEMALE;
    }
}
