# Architecture

## Package structure

- com.lassilaiho.calculator
  - core
    - lexer
    - parser
    - evaluator
  - persistence
  - ui

The root package of the application is `com.lassilaiho.calculator`.

Package `com.lassilaiho.calculator.core` implements the main API of the program,
the `Calculator` class. It provides a simpler interface with some additional
features for the lower-level functionality implemented in subpackages of `core`.

Package `com.lassilaiho.calculator.core.lexer` implements a lexer for
mathematical expressions.

Package `com.lassilaiho.calculator.core.parser` implements a parser for
sequences of lexemes produced by the lexer.

Package `com.lassilaiho.calculator.core.evaluator` implements an evaluator for
abstract syntax trees produced by the parser.

Package `com.lassilaiho.calculator.persistence` implements persistence
operations used by the application.

Package `com.lassilaiho.calculator.ui` implements a graphical user interface for
the application using JavaFX.

## UI

The application has a single window for all interactions. The UI is mostly
defined in FXML, with some dynamically created components, such as error alerts
and history view entries, defined programmatically. The main view has a single
controller, [`MainViewController`], which processes input actions and displays
results. The UI is separated from the application logic: it wires the
application logic and persistence operations to the UI.

## Application logic

### Lexer

The main class in the lexer is [`Lexer`], which implements the lexing algorithm.
The input `Reader` is passed to the constructor, and the `lex` method returns a
list of [`Lexeme`] instances representing the input string. Each `Lexeme` has a
type, denoted by a member of enum [`LexemeType`]. Certain types of `Lexeme`s
also have an associated value.

### Parser

The central class in the parser is [`Parser`], which implements the parsing
algorithm. It requires a list of `Lexeme`s as its input. Method `parseNode`
parses the input and returns an abstract syntax tree (AST) representing the
input.

The AST is a tree of classes implementing the [`Node`] interface. It has two
subinterfaces, [`Statement`] and [`Expression`]. `Statement` is implemented by
nodes that don't have a meaningful value, such as variable definitions.
`Expression` is implemented by all nodes representing actual mathematical
expressions, such as binary operations and function calls.

Together with interface [`NodeVisitor`], `Node` implements the visitor design
pattern. The only method defined by `Node`, `void accept(NodeVisitor)`, visits
the current node using the passed visitor object.

### Evaluator

Class [`Evaluator`] traverses and evaluates an AST produced by the parser.
`Evaluator` is implemented as a visitor of the `Node` interface. If the
evaluated `Node` was an expression, the value can be retrieved by calling method
`getValue`.

Constants, variables and functions are implemented as classes implementing
interface [`Evaluatable`]. The interface defines method `int getArgumentCount()`
for querying the argument count for the object (0 for constants and variables)
and method `double evaluate(double...)` for evaluating the object.

Class [`Scope`] stores named values. A named value is a name associated with an
`Evaluatable`. When evaluating statements that define names, such as variable
definitions, the result is stored in a `Scope` as a named value. Named values
can be defined to be immutable, which prevents redefining them. The separation
of constants and variables is at this level.

### Calculator

Class [`Calculator`] wraps the lexer, parser and evaluator into a simpler API.
`Calculator` holds a `Scope` instance to keep track of variable and function
definitions. Some commonly used constants and functions are predefined in the
`Scope` of a `Calculator` as immutable built-ins. These built-ins are
implemented in the [`Builtins`] class.

Expressions can be calculated by calling the `Number calculate(String)` method
on `Calculator`, which lexes, parses and evaluates the input and returns the
result. The calculation history is stored as [`HistoryEntry`] instances in a
[`SessionDao`].

### Class diagram depicting the high level relationships of the classes in the program

![Class diagram](images/class-diagram.svg)

## Persistence

The persistence layer provides three interfaces: [`HistoryDao`] for persisting
history entries, [`NamedValueDao`] for named values and [`SessionDao`] which
wraps the other two. These interfaces follow the Data Access Object design
pattern. The interfaces are implemented by classes [`SqlHistoryDao`],
[`SqlNamedValueDao`] and [`SqlSessionDao`]. These classes are database-agnostic.

Class [`SqliteSessionManager`] implements session management using SQLite
specifically, since the databases are stored locally in files.
`SqliteSessionManager` provides a database connection to the DAO classes. The
class handles opening and switching session files.

### Database schema

```SQL
CREATE TABLE history (
    id INTEGER PRIMARY KEY,
    expression TEXT NOT NULL,
    value NUMERIC NOT NULL
);
CREATE TABLE named_value (
    id INTEGER PRIMARY KEY,
    name TEXT UNIQUE NOT NULL,
    body TEXT NOT NULL
);
CREATE TABLE named_value_param (
    named_value_id INTEGER NOT NULL REFERENCES named_value(id) ON DELETE CASCADE,
    name TEXT NOT NULL
);
```

## Example operation: calculating "2+3"

![Calculate diagram](images/calculate-diagram.svg)

The sequence diagram shows the interactions of classes when calculating the
expression "2+3". The user first types the expression to the expression input
field and presses the *Calculate* button. The click is processed by an input
handler in class `MainViewController`. The controller calls `calculate` on the
current `Calculator` instance passing the input string as an argument.
`Calculator` processes the input as described earlier and returns the result.
`MainViewController` then retreives the `HistoryEntry` instance generated by the
`calculate` and displays it in the UI.


<!-- Links -->
[`MainViewController`]: ../src/main/java/com/lassilaiho/calculator/ui/MainViewController.java

[`Lexer`]: ../src/main/java/com/lassilaiho/calculator/core/lexer/Lexer.java
[`Lexeme`]: ../src/main/java/com/lassilaiho/calculator/core/lexer/Lexeme.java
[`LexemeType`]: ../src/main/java/com/lassilaiho/calculator/core/lexer/LexemeType.java

[`Parser`]: ../src/main/java/com/lassilaiho/calculator/core/parser/Parser.java
[`Node`]: ../src/main/java/com/lassilaiho/calculator/core/parser/Node.java
[`Statement`]: ../src/main/java/com/lassilaiho/calculator/core/parser/Statement.java
[`Expression`]: ../src/main/java/com/lassilaiho/calculator/core/parser/Expression.java
[`NodeVisitor`]: ../src/main/java/com/lassilaiho/calculator/core/parser/NodeVisitor.java

[`Evaluator`]: ../src/main/java/com/lassilaiho/calculator/core/evaluator/Evaluator.java
[`Evaluatable`]: ../src/main/java/com/lassilaiho/calculator/core/evaluator/Evaluatable.java
[`Scope`]: ../src/main/java/com/lassilaiho/calculator/core/evaluator/Scope.java

[`Calculator`]: ../src/main/java/com/lassilaiho/calculator/core/Calculator.java
[`Builtins`]: ../src/main/java/com/lassilaiho/calculator/core/Builtins.java
[`HistoryEntry`]: ../src/main/java/com/lassilaiho/calculator/core/HistoryEntry.java

[`HistoryDao`]: ../src/main/java/com/lassilaiho/calculator/persistence/HistoryDao.java
[`NamedValueDao`]: ../src/main/java/com/lassilaiho/calculator/persistence/NamedValueDao.java
[`SessionDao`]: ../src/main/java/com/lassilaiho/calculator/persistence/SessionDao.java
[`SqlHistoryDao`]: ../src/main/java/com/lassilaiho/calculator/persistence/SqlHistoryDao.java
[`SqlNamedValueDao`]: ../src/main/java/com/lassilaiho/calculator/persistence/SqlNamedValueDao.java
[`SqlSessionDao`]: ../src/main/java/com/lassilaiho/calculator/persistence/SqlSessionDao.java
[`SqliteSessionManager`]: ../src/main/java/com/lassilaiho/calculator/persistence/SqliteSessionManager.java
