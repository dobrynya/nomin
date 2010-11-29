package org.nomin.entity;

import java.util.List;
import java.util.Map;

/**
 * An entity.
 * @author Dmitry Dobrynin
 *         Date: 08.11.2010 Time: 17:58:20
 */
public class MapHolder2 {
    private Map<Integer, Integer> integers;
    private Map<Integer, Employee> employees;
    private Map<Object, Object> objects;
    private List<String> props;

    public Map<Integer, Integer> getIntegers() { return integers; }

    public void setIntegers(Map<Integer, Integer> integers) { this.integers = integers; }

    public Map<Integer, Employee> getEmployees() { return employees; }

    public void setEmployees(Map<Integer, Employee> employees) { this.employees = employees; }

    public Map<Object, Object> getObjects() { return objects; }

    public void setObjects(Map<Object, Object> objects) { this.objects = objects; }

    public List<String> getProps() { return props; }

    public void setProps(List<String> props) { this.props = props; }
}
