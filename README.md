# ESA Commons

![Build](https://github.com/esastack/esa-commons/workflows/Build/badge.svg?branch=main)
[![codecov](https://codecov.io/gh/esastack/esa-commons/branch/main/graph/badge.svg?token=HUHT6S30PD)](https://codecov.io/gh/esastack/esa-commons)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.esastack/commons/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.esastack/commons/)
[![GitHub license](https://img.shields.io/github/license/esastack/esa-commons)](https://github.com/esastack/esa-commons/blob/main/LICENSE)


ESA Commons is the common lib of `ESA Stack`.

## Features

- `SPI` Enhancement:  Allows loading `SPI` by name, group, tag and so on..
- `Logger`: Detect the `slf4j` automatically.
- `InternalLogger`: Write log  without any log library.
- Lambda Enhancement: lambda interfaces receiving multiple parameters,  lambda interfaces allows to throw an exception.
- `MultiValueMap`: Implementation of `Map<K, List<V>`.
- `UnsafeUtils`, `UnsafeArrayUtils`: Unity classes for unsafe options.
- Reflection support: `AnnotationUtils`, `ClassUtis`...
- more features...

## Maven Dependency

```xml
<dependency>
    <groupId>com.github.esastack</groupId>
    <artifactId>commons</artifactId>
    <version>${esa-httpserver.version}</version>
</dependency>
```

## Reference Guide

### `Logger` & `LoggerFactory`

There are many excellent logging frameworks such as log4j, log4j2, logback and so on... Although the slf4j framework serves as a simple facade or abstraction for various logging frameworks, maybe you still do not want to use slf4j as the facade in your own framework which will be used by the end user.  So we provides a couple of  APIs( `Logger`, `LoggerFactory` ) to serve as a facade of slf4j and JDK logger(may be we will support log4j and logback in the future).

eg.

just declare the logger as usual

```java
private static final Logger logger = LoggerFactory.getLogger("foo");
```

also use it as usual

```java
logger.info("hell world!");
logger.debug("hello {}!", "world");
logger.warn("{} {}!", "hello", "world");
logger.error("oops!", new Exception("there's something wrong with it"));
```

> Please do not forget that we are using the `Logger` and `LoggerFactory` that are provided in `esa-commons` instead of the org.slf4j.Logger.

### `InternalLogger`

`InternalLogger` allows you to write logs without any thirdparty log dependency such as log4j, logback.

> In some thirdparty log frameworks， you may need a xxx.xml to configure your logs to the target file which depends on your end user.

eg.

```java
final InternalLogger logger = InternalLoggers.logger("foo", new File("foo.log")).build();
logger.info("hello world!");
```

and you will see the appended log in the file named foo.log

```
2020-12-10 11:42:10.141 INFO [main] foo : hello world!
```

#### log pattern

you can configure the log patterns below

- `%d`, `%date`: log date 
- `%l`, `%level`：log level
- `%t`, `%thread`: caller thread
- `%logger`: logger name
- `%m`, `%msg`, `%message`: log content
- `%n`: new line
- `%ex`, `%exception`, `%thrown`:  error detail if present

eg.

the default log pattern: `%date %level [%thread] %logger : %msg%n%thrown`

and the output of `logger.info("hello world!")`

```
2020-12-10 11:42:10.141 INFO [main] foo : hello world!
```

eg.

log pattern: `%msg%n`

and the output of 

```java
logger.info("hello");
logger.info("world");
```

is

```
hello
world
```

#### File Rolling

- `SIZE_BASED` rolling
- `TIME_BASED` rolling
- `TIME_AND_SIZE_BASED` rolling

eg.

`SIZE_BASED` rolling with max size of 10M, and the max history log file's number of 3

```java
final long maxSize = 10 * 1024 * 1024;
final int maxHistory = 3;
final InternalLogger logger = InternalLoggers.logger("foo", new File("foo.log"))
         .useSizeBasedRolling(maxSize, maxHistory)
         .build();
```

`TIME_BASED` rolling everyday 

```java
final InternalLogger logger = InternalLoggers.logger("foo", new File("foo.log"))
         .useTimeBasedRolling("yyyy-MM-dd")
         .build();
```

`TIME_BASED` rolling every hour

```java
final InternalLogger logger = InternalLoggers.logger("foo", new File("foo.log"))
         .useTimeBasedRolling("yyyy-MM-dd_HH")
         .build();
```

> patter of date only support: Hour and Day level

`TIME_AND_SIZE_BASED` rolling

```java
final long maxSize = 10 * 1024 * 1024;
final int maxHistory = 3;
final InternalLogger logger = InternalLoggers.logger("foo", new File("foo.log"))
         .useTimeAndSizeBasedRolling("yyyy-MM-dd", maxSize, maxHistory)
         .build();
```

### Lambda Enhancement

#### Arguments Enhancement

JDK only supports a part of lambda interfaces, and here we declare more interfaces like

- `Consumer3`, `Consumer4`,  `Consumer5`: `Consumer` with 3(4 or5) arguments
- `Function3`, `Function4`,  `Function5`: `Function` with 3(4 or5) arguments
- `Predicate3`, `Predicate4`,  `Predicate5`: `Predicate` with 3(4 or5) arguments
- and so on...

eg.

```java
Consumer3<Integer, String, Foo> c = (num, str, foo) -> {};
```

#### Throwing Enhancement

JDK lambda interface is not allowed to throw an exception, and here we declare the interfaces like

- `ThrowingConsumer`, `ThrowingBiConsumer`, `ThrowingConsumer3`, `ThrowingConsumer4`

- `ThrowingFunction`, `ThrowingBiFunction`, `ThrowingFunction3`, `ThrowingFunction4`
- `ThrowingPredicate`, `ThrowingBiPredicate`, `ThrowingPredicate3`, `ThrowingPredicate4`
- `ThrowingSupplier`
- `ThrowingRunnable`
- and so on...

eg.

```java
ThrowingConsumer<String> c = str -> {
    mayThrowIOException();
    // ...
}

static void mayThrowIOException() {
    if (condition) {
        throw new IOException();
    }
}
```

this will transfer your `ThrowingConsumer` to `Consumer` which will rethrow the exception(if present).

```java
Consumer<String> c = ThrowingConsumer.rethrow(str -> {
    mayThrowIOException();
    // ...
});
```

this will just suppress the exception

```java
Consumer<String> c = ThrowingConsumer.suppress(str -> {
    mayThrowIOException();
    // ...
});
```

this will handle the exception

```java
Function<String, String> c = ThrowingFunction.failover(str -> {
    mayThrowIOException();
    return "foo";
}, (v, t) -> {
    // handle error 't'
    return "bar";
});
```

#### Auto-(Un)Box Enhancement

Allows you to use lambda interfaces without Auto-Box or Auto-Unbox.

- `ObjIntFunction`, `ObjDoubleFunction`, `ObjLongFunction`
- `ObjIntPredicate`, `ObjDoublePredicate`, `ObjLongPredicate`
- `ThrowingIntConsumer`, `ThrowingDoubleConsumer`, `ThrowingLongConsumer`
- `ThrowingIntFunction`, `ThrowingDoubleFunction`, `ThrowingLongFunction`
- `ThrowingIntPredicate`, `ThrowingDoublePredicate`, `ThrowingLongPredicate`
- `ThrowingIntSupplier`, `ThrowingDoubleSupplier`, `ThrowingLongSupplier`
- and so on...

> just find what you want.

### SPI

usage

use `@SPI` to annotate it is a SPI interface

```java
@SPI
public interface Shape {
    // ...
}
```

use `@Feature` to add properties for implementation

```java
@Feature(groups = "foo", order = 1, tags = "a:1", excludeTags = "b:1")
public class Circle implements Shape {
    // ...
}
@Feature(groups = "bar", order = -1, tags = "a:2", excludeTags = "b:2")
public class Triangle implements Shape {
    // ...
}
```

add spec file to `META-INF/services/`, or `META-INF/esa/`, or  `META-INF/esa/internal/`(any directory is ok)

so we add a spec file

> META-INF/services/com.github.esastack.commons.Shape
>
> ```
> com.github.esastack.commons.Circle
> com.github.esastack.commons.Triangle
> ```

use `SpiLoader` to get SPI extensions

instances should be in sort by `@Feature#order`

```java
final List<Shape> shapes = SpiLoader.cached(Shape.class).getAll();
assertEquals(2, shapes.size());
assertTrue(shapes.get(0) instanceof Triangle);
assertTrue(shapes.get(1) instanceof Circle);
```

get by groups

```java
final List<Shape> shapes = SpiLoader.cached(Shape.class).getByGroup("foo");
assertEquals(1, shapes.size());
assertTrue(shapes.get(0) instanceof Circle);
```

get by tags

```java
final List<Shape> shapes = SpiLoader.cached(Shape.class).getByTags(Collections.singletonMap("a", "2"));
assertEquals(1, shapes.size());
assertTrue(shapes.get(0) instanceof Triangle);
```

get by features

```java
final List<Shape> shapes = SpiLoader.cached(Shape.class)
        .getByFeature("foo", Collections.singletonMap("a", "1"));
assertEquals(1, shapes.size());
assertTrue(shapes.get(0) instanceof Circle);
```

