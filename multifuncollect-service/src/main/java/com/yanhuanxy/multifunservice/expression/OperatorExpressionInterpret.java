package com.yanhuanxy.multifunservice.expression;

/**
 *  操作表达式 解释器 抽象类
 */
public abstract class OperatorExpressionInterpret implements ExpressionInterpret {

    protected final ExpressionInterpret leftExpression;

    protected final ExpressionInterpret rightExpression;

    public OperatorExpressionInterpret(ExpressionInterpret leftExpression, ExpressionInterpret rightExpression) {
        this.leftExpression = leftExpression;
        this.rightExpression = rightExpression;
    }
}
