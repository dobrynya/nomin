<p align="center">
  <img src="https://raw.githubusercontent.com/zerounix/nomin/master/doc/nomin_logo_small.png?raw=true" alt="nomin - japanese peasant"/>
</p>

<a href="https://travis-ci.org/dobrynya/nomin"><img src="https://travis-ci.org/dobrynya/nomin.svg?branch=master"></a> Introduction
============

Nomin is a mapping engine for the Java platform transforming object trees according to declarative mapping rules. 
This Java mapping framework aims to reduce efforts when it's needed to map different structures to each other. 
The current stable version of the Nomin framework is 1.2.

## Releases

### 1.2

* Resolved the problem with leaking memory in ContextManager due to dropping ThreadLocal use. More details at https://github.com/dobrynya/nomin/issues/17
* Upgraded JVM up to 11
* Upgraded Groovy up to 2.5.9
* Upgraded SLF4j API up to 1.7.30
* Upgraded CGLIB up to 3.3.0

### 1.1.7

* minor optimizations

## What mapping is

Quite often we encounter tasks requiring object trees conversions. Recall transfering DTOs/value objects through layered architecture, 
integration with external/legacy systems, using different frameworks.

Sometimes conversions between different classes/hierarchies are hand coded. It consumes too much efforts. Drawbacks of hand coded solutions are:

* In case when conversions should be performed for both directions it will be coded twice
* A developer takes care about all the concerns such as null-checking, type-safety, processing collections and other related tasks during coding conversions
* Too hard to maintain the conversion code in case when a hierarchy is rich

Yet another way to do that is to use a generalized mapping engine performing transformations. The engine lays on user defined declarative mappings 
between the classes/hierarchies. In general a mapping is a set of mapping rules. A mapping rule, in its turn, is description of how the engine 
should transfer data from one point to another.

## What Nomin is

I was involved in development of several projects as an integration developer. My goal on that projects was to perform integration with external 
or legacy systems. So I looked for convenient and easy-to-use framework for mapping different structures to each other. I've found [Dozer](http://dozer.sourceforge.net/). One day 
I encountered too legacy code that Dozer couldn't introspect. Then I decided to write my own mapping framework. I was inspired by Dozer but also I 
saw its disadvantages such as configuring mapping rules in XML documents (too much overhead coding), inabilities to apply expressions and so on.

My assumptions were to reduce mapping configuration, make it more intuitively looking and improve functionality giving a developer such abilities 
as mapping expressions, mapping method invocations and other nice features. In other words to do simple things simply.

## Why Nomin mappings are written in Groovy

Groovy provides very powerful mechanisms that we can use. Why reinvent the wheel? Groovy parses classes and scripts into Java byte code and does it better 
than I could write. So I've eased my work by reusing Groovy. By the way, respect to the Groovy creators!

## How to write mappings

There are two ways to define your mappings

* write Groovy class

   Mappings will be compiled as the rest of your application. Please don't forget configure your build system to compile Groovy sources. 
   To define a mapping create a Groovy class extending `org.nomin.Mapping` and implement build method in there. It takes no time to parse 
   Groovy scripts at runtime.
* write Groovy scripts in plain text and put these on the classpath

    This way provides more flexibility than the first one, for example mappings can be changed and reparsed at runtime without restarting 
	of an application. Just create a file with .groovy extension and put it with other resources of your application.

Which approach to use? You decide.

## Features

* a mapping isn't just a static document with mapping rules such as XML

	A Nomin mapping listing is nothing else as a groovy class or script. So you are able to use all mechanisms Groovy provides. Although you may 
	not know Groovy at all you can easily use Nomin. 
	
* intuitively looking mapping, even a non-developer can read it

	In the majority of cases a mapping rule is looking as usual assignment. For example,
	
``` groovy
a.name = b.firstName
```
means "map property `name` of the first class to property `firstName` of the second class and vice versa"

* map arbitrary expressions  

    In other words now you can map result of calulating arbitrary block of code to properties. Expressions have access to objects being mapped and will be calculated at runtime.

* method invocation

    So there is the ability to map result of method invocation on some property.

* automapping facility

    Nomin can automatically create mapping rules between properties of the same names.

* abilities to easily customize the mapping engine behavior

    You can choose a class introspector, advise the engine what is the type of a property with hints including dynamic hints which are calculated at mapping time.   
    You are not restricted to use only JavaBeans naming convention, you can use anything suitable for your needs.

* hooks
    Sometimes it needs to perform pre and/or postprocessing entities being mapped. So that code can be placed right in a mapping.

* context management
    Expressions and hooks can use objects from the context passed to the mapper. Context can be just a Map instance with string keys. Also there is integration with Spring, so you are able to use beans defined in the Spring context.

* high performance and thread-safety

    Nomin is developed for working in multi-threading environments. Its performance exceeds performance of frameworks with the same functionality, the following table shows the comparison results.

| Environment / framework  | Nomin 1.1.3 | Dozer 5.3.1 |
| ------------- | -------------: | -------------: |
| Mapping complex objects, 400000 iterations  |  |  |
| Pentium Dual E2180 2GHz, RAM 3 GB Win 32 XP SP 3, JVM 1.6.0_18  | ReflectionIntrospector <br/> ~5.51 sec    | with disabled statistics 60.93 sec |
|  | ExplodingIntrospector <br/>~4.96 sec | |
|  | FastIntrospector <br/>~3.49 sec | |
| AMD x64 2GHz, RAM 2 GB Win 7 x64, JVM x64 1.6.0_22| with ReflectionIntrospector <br/> ~4.33 sec  | with disabled statistics 33.87 sec |
|  | ExplodingIntrospector <br/>~4.17 sec | |
|  | FastIntrospector <br/>~3.68 sec | |


* recursive mapping of complex types
* full supporting collections, arrays and maps
* implicit conversions of primitive/wrapper types
* type and null-safe
* ready for deployment into OSGi container

## Getting started

To start working with Nomin it's necessary to download [Nomin](http://search.maven.org/remotecontent?filepath=net/sf/nomin/nomin/1.1.3/nomin-1.1.3.jar) and put it on the classpath of your application. 
If you use Maven to build an application, just add Nomin as a dependency into your pom.xml.

``` xml
<dependency>
	<groupId>net.sf.nomin</groupId>
	<artifactId>nomin</artifactId>
	<version>1.1.3</version>
</dependency>
```

Suppose we have an integration task to feed an external system with some data, so we have two domain models, ours and theirs. Despite the transport layer both of the domains are represented by sets of POJOs.   
The following diagram shows all classes and its relations.

<p align="center">
  <img src="https://raw.githubusercontent.com/zerounix/nomin/master/doc/domains.jpg?raw=true" alt="example domain class diagram"/>
</p>

To perform mappings between the domains it needs to create mappings rules. Just create these files and put them into classpath.

``` groovy
// person2employee.groovy
import org.nomin.entity.*

mappingFor a: Person, b: Employee
a.name = b.name
a.lastName = b.last
a.birthDate = b.details.birth
a.children = b.details.kids

a.strDate = b.details.birth
dateFormat "dd-MM-yyyy"

a.gender = b.details.sex
simple ([a: Gender.MALE, b: true], [a: Gender.FEMALE, b: false])

a.snn = b.employeeId
convert to_a: { eId -> repositry.findByEmployeeId(eId) }, 
		to_b: { snn -> repository.findBySnn(snn) }
```
``` groovy
// child2kid.groovy
import org.nomin.entity.*

mappingFor a: Child, b: Kid
a.name = b.kidName
```

Well, it's not looking too complicated. The last question that remains is how to make it work? The next code snippet shows that.

``` java
// MappingTest.java
public class MappingTest {
  NominMapper nomin = new Nomin("person2employee.groovy", "child2kid.groovy");
  Person person;
  Employee employee;

  @Before
  public void before() { /* create and initialize a person and an employee instance */ }

  @Test
  public void test() {
      Employee e = nomin.map(person, Employee.class);
      // here should be assertions to ensure that everything is ok

      Person p = nomin.map(employee, Person.class);
      // assertions again
  }
}
```

Let's look more deeply at the mappings. There are only a couple of things I should clarify because they are not so obvious as others.

``` groovy
a.gender = b.details.sex
simple ([a: Gender.MALE, b: true], [a: Gender.FEMALE, b: false])
```

The simple conversion defines pairs of corresponding values for both sides.

``` groovy
a.snn = b.employeeId
convert to_a: { eId -> service.convertEmployeeId2snn(eId) }, 
		to_b: { snn -> service.convertSnn2EmployeeId(snn) }
```

We have string properties on both sides, but they can be converted to each other only using an external component or service. 
Nomin provides access to a particular context to get services which are able to perform conversions to and from. How to configure Nomin 
to use required context during mapping will be shown in the rest of this guide.

Now you have basic knowledge to start working with Nomin.

Find more documentation in the [wiki](https://github.com/dobrynya/nomin/wiki)
