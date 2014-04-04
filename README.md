Introduction
============

Nomin is a mapping engine for the Java platform transforming object trees according to declarative mapping rules. 
This Java mapping framework aims to reduce efforts when it's needed to map different structures to each other. 
The current stable version of the Nomin framework is 1.1.2.

##What mapping is

Quite often we encounter tasks requiring object trees conversions. Recall transfering DTOs/value objects through layered architecture, 
integration with external/legacy systems, using different frameworks.

Sometimes conversions between different classes/hierarchies are hand coded. It consumes too much efforts. Drawbacks of hand coded solutions are:

* In case when conversions should be performed for both directions it will be coded twice
* A developer takes care about all the concerns such as null-checking, type-safety, processing collections and other related tasks during coding conversions
* Too hard to maintain the conversion code in case when a hierarchy is rich

Yet another way to do that is to use a generalized mapping engine performing transformations. The engine lays on user defined declarative mappings 
between the classes/hierarchies. In general a mapping is a set of mapping rules. A mapping rule, in its turn, is description of how the engine 
should transfer data from one point to another.

##What Nomin is

I was involved in development of several projects as an integration developer. My goal on that projects was to perform integration with external 
or legacy systems. So I looked for convenient and easy-to-use framework for mapping different structures to each other. I've found [Dozer](http://dozer.sourceforge.net/). One day 
I encountered too legacy code that Dozer couldn't introspect. Then I decided to write my own mapping framework. I was inspired by Dozer but also I 
saw its disadvantages such as configuring mapping rules in XML documents (too much overhead coding), inabilities to apply expressions and so on.

My assumptions were to reduce mapping configuration, make it more intuitively looking and improve functionality giving a developer such abilities 
as mapping expressions, mapping method invocations and other nice features. In other words to do simple things simply.

##Why Nomin mappings are written in Groovy

Groovy provides very powerful mechanisms that we can use. Why reinvent the wheel? Groovy parses classes and scripts into Java byte code and does it better 
than I could write. So I've eased my work by reusing Groovy. By the way, respect to the Groovy creators!

##How to write mappings

There are two ways to define your mappings

* write Groovy class

   Mappings will be compiled as the rest of your application. Please don't forget configure your build system to compile Groovy sources. 
   To define a mapping create a Groovy class extending org.nomin.Mapping and implement build method in there. It takes no time to parse 
   Groovy scripts at runtime.
* write Groovy scripts in plain text and put these on the classpath

    This way provides more flexibility than the first one, for example mappings can be changed and reparsed at runtime without restarting 
	of an application. Just create a file with .groovy extension and put it with other resources of your application.

Which approach to use? You decide.

#Features

* a mapping isn't just a static document with mapping rules such as XML

	A Nomin mapping listing is nothing else as a groovy class or script. So you are able to use all mechanisms Groovy provides. Although you may 
	not know Groovy at all you can easily use Nomin. 
	
* intuitively looking mapping, even a non-developer can read it

	In the majority of cases a mapping rule is looking as usual assignment. For example,
	
``` groovy
a.name = b.firstName
```

	means "map property 'name' of the first class to property 'firstName' of the second class and vice versa"

