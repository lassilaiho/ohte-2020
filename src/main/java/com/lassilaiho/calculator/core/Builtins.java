package com.lassilaiho.calculator.core;

import java.util.Map;
import static java.util.Map.entry;
import com.lassilaiho.calculator.core.evaluator.Constant;
import com.lassilaiho.calculator.core.evaluator.Evaluatable;
import com.lassilaiho.calculator.core.evaluator.EvaluationException;
import static com.lassilaiho.calculator.core.evaluator.Function.binary;
import static com.lassilaiho.calculator.core.evaluator.Function.unary;

final class Builtins {
    public static final Map<String, Evaluatable> VALUES = Map.ofEntries(
        entry("pi", new Constant(Math.PI)),
        entry("e", new Constant(Math.exp(1))),
        entry("sin", unary(Math::sin)),
        entry("cos", unary(Math::cos)),
        entry("tan", unary(Math::tan)),
        entry("asin", unary(Math::asin)),
        entry("acos", unary(Math::acos)),
        entry("atan", unary(Math::atan)),
        entry("sqrt", unary(x -> Math.sqrt(nonNegative(x, "argument of sqrt")))),
        entry("nroot", binary((x, y) -> Math.pow(x, 1 / y))),
        entry("pow", binary(Math::pow)),
        entry("log", unary(x -> Math.log(nonNegative(x, "argument of log")))),
        entry("log10", unary(x -> Math.log10(nonNegative(x, "argument of log10")))),
        entry(
            "log2",
            unary(x -> Math.log(nonNegative(x, "argument to log2")) / Math.log(2))),
        entry("ceil", unary(Math::ceil)),
        entry("floor", unary(Math::floor)),
        entry("round", unary(Math::round)),
        entry("max", binary(Math::max)),
        entry("min", binary(Math::min)));

    private static double nonNegative(double x, String message) {
        if (x < 0) {
            throw new EvaluationException(message + " must be non-negative");
        }
        return x;
    }
}
