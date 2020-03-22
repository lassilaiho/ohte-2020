# Requirements specification

## Overview

The application is a scientific calculator. In addition to basic operations such
as addition, subtraction, multiplication and division, the application supports
more advanced operations, such as logarithmic, trigonometric and exponential
functions.

## UI sketch

![UI sketch](./images/ui-sketch.png)

## Features

### Core features

These features form the core of the application.

- math operations
  - basic math operations (addition, subtraction, multiplication, division)
  - expression grouping using parentheses
  - trigonometric functions
  - logarithmic and exponential functions
  - roots
  - common constants (*π*, *e*)
- calculation history
  - can be cleared
  - preserved across application restarts

### Additional features

These features will be implemented if time permits.

- searchable catalogue (functions, constants, variables)
- user defined variables
- user defined functions
- sessions
  - stores the calculation history and defined variables and functions
  - can be saved to and restored from a file
- complex number support
