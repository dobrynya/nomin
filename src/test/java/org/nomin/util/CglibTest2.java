package org.nomin.util;

import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;
import org.junit.Test;
import org.nomin.core.MappingConsts;
import org.nomin.entity.Person;

import java.util.concurrent.Callable;

/**
 * @author Dmitry Dobrynin
 *         Date: 28.10.2010 Time: 22:27:13
 */
public class CglibTest2 implements MappingConsts {
    @Test
    public void test() throws Exception {
        final PropertyAccessor pa = jb.property("name", Person.class);
        Callable refl = new Callable<Object>() {
            public Object call() throws Exception {
                Person p = (Person) Person.class.newInstance();
                pa.set(p, "Name");
                return Person.class.newInstance(); }
        };

        final FastClass fastClass = FastClass.create(Person.class);
        final FastMethod fastMethod = fastClass.getMethod("setName", new Class[] { String.class });

        Callable fc = new Callable() {
            public Object call() throws Exception {
                Person p = (Person) fastClass.newInstance();
                fastMethod.invoke(p, new Object[] {"Name"});
                return p;
            }
        };

        Callable direct = new Callable() {
            public Object call() throws Exception {
                Person p = new Person();
                p.setName("Name");
                return p;
            }
        };

        calc("Using cglib      ", 1000000, fc);
        calc("Using direct     ", 1000000, direct);
        calc("Using reflection ", 1000000, refl);
        calc("Using direct     ", 1000000, direct);
        calc("Using reflection ", 1000000, refl);
        calc("Using cglib      ", 1000000, fc);
        calc("Using direct     ", 1000000, direct);
        calc("Using cglib      ", 1000000, fc);
        calc("Using reflection ", 1000000, refl);
        calc("Using direct     ", 1000000, direct);
    }



    void calc(String desc, int count, Callable r) throws Exception {
        long s = System.nanoTime();
        while (count-- > 0) r.call();
        s = System.nanoTime() - s;
        System.out.println(String.format("%s \t %d ms", desc, s));
    }
                    /*
    Using cglib       	 101325657 ms
    Using cglib       	 142846996 ms
    Using cglib       	 57022540 ms
    Using reflection  	 232586474 ms
    Using reflection  	 150664330 ms
    Using reflection  	 132729928 ms
    Using direct      	 79372588 ms
    Using direct      	 46362806 ms
    Using direct      	 47407562 ms
    Using direct      	 47781561 ms
    */
}
