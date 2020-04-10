# Calculator

The application is a scientific calculator. It supports both basic math
operations as well as more advanced features.

This is a project for the course "Ohjelmistotekniikka" at the University of
Helsinki.

## Documentation

[Requirements specification](docs/requirements-specification.md)

[Architecture description](docs/architecture.md)

[Working time record](docs/working-time-record.md)

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
The report can be viewed by opening target/site/jacoco/index.html.

### Checkstyle

Checkstyle checks can be run with
```
mvn jxr:jxr checkstyle:checkstyle
```
Results can be viewed by opening target/site/checkstyle.html.

### JavaDoc

To generate JavaDoc documentation, run
```
mvn javadoc:javadoc
```

### Packaging

To generate a distributable JAR file, run
```
mvn package
```
The generated JAR file is located in target/calculator-\<version>.jar.
