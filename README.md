# Calculator

The application is a scientific calculator. It supports both basic math
operations as well as more advanced features.

This is a project for the course "Ohjelmistotekniikka" at the University of
Helsinki.

## Documentation

[User manual](docs/user-manual.md)

[Requirements specification](docs/requirements-specification.md)

[Architecture description](docs/architecture.md)

[Testing](docs/testing.md)

[Working time record](docs/working-time-record.md)

## Releases

[Week 5](https://github.com/lassilaiho/ot-calculator/releases/tag/week5)

[Week 6](https://github.com/lassilaiho/ot-calculator/releases/tag/week6)

[Final release](https://github.com/lassilaiho/ot-calculator/releases/tag/1.0.0)

## Commands

### Running the program

To build and start the app, run
```
mvn compile exec:java -Dexec.mainClass=com.lassilaiho.calculator.ui.App
```

### Testing

Tests can be run with
```
mvn test
```
To generate a test coverage report, run
```
mvn test jacoco:report
```
The report can be viewed by opening *target/site/jacoco/index.html*.

### Checkstyle

Checkstyle checks can be run with
```
mvn jxr:jxr checkstyle:checkstyle
```
Results can be viewed by opening *target/site/checkstyle.html*.

### JavaDoc

To generate JavaDoc documentation, run
```
mvn javadoc:javadoc
```
The documentation can be viewed by opening *target/site/apidocs/index.html*.

### Packaging

To generate a distributable JAR file, run
```
mvn package
```
The generated JAR file is located at *target/calculator-\<version>.jar*.
