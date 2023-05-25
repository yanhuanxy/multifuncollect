package com.yanhuanxy.multifunservice.expression;

/**
 *  数值表达式 解释器
 */
public class NumberExpressionInterpret implements ExpressionInterpret {

    private final Long number;

    public NumberExpressionInterpret(Long number) {
        this.number = number;
    }

    @Override
    public Object interpret(){

        return number;
    }
}
