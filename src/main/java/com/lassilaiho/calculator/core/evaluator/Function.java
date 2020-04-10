package com.lassilaiho.calculator.core.evaluator;

import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;

/**
 * {@link Function} is used to wrap a function with an implementation of {@link Evaluatable}.
 */
public class Function implements Evaluatable {
    private final int argumentCount;
    private final NAryFunction function;

    /**
     * Constructs a {@link Function} that calls function when evaluated.
     * 
     * @param argumentCount the number of arguments required by function
     * @param function      the function to call when evaluated
     */
    public Function(int argumentCount, NAryFunction function) {
        this.argumentCount = argumentCount;
        this.function = function;
    }

    /**
     * A helper function for constructing unary functions.
     * 
     * @param  function the function to wrap
     * @return          the constructed {@link Function} instance
     */
    public static Function unary(DoubleUnaryOperator function) {
        return new Function(1, args -> function.applyAsDouble(args[0]));
    }

    /**
     * A helper function for constructing binary functions.
     * 
     * @param  function the function to wrap
     * @return          the constructed {@link Function} instance
     */
    public static Function binary(DoubleBinaryOperator function) {
        return new Function(2, args -> function.applyAsDouble(args[0], args[1]));
    }

    @Override
    public int getArgumentCount() {
        return argumentCount;
    }

    @Override
    public double evaluate(double... arguments) {
        return function.apply(arguments);
    }

    /**
     * A functional interface for n-ary functions with double parameters and return value.
     */
    @FunctionalInterface
    public interface NAryFunction {
        /**
         * Applies the function to arguments.
         * 
         * @param  arguments the arguments
         * @return           the return value
         */
        double apply(double... arguments);
    }
}
