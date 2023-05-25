package com.yanhuanxy.multifunservice.expression;

/**
 *  函数表达式 解释器
 */
public class FunctionExpressionInterpret implements ExpressionInterpret {

    private final String function;

    public FunctionExpressionInterpret(String function) {
        this.function = function;
    }

    @Override
    public Object interpret(){

        return function;
    }
}
