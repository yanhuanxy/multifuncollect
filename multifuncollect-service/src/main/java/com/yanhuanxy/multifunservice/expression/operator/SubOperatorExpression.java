package com.yanhuanxy.multifunservice.expression.operator;

import com.yanhuanxy.multifunservice.expression.ExpressionInterpret;
import com.yanhuanxy.multifunservice.expression.OperatorExpressionInterpret;

/**
 *  表达式 减法操作
 */
public class SubOperatorExpression extends OperatorExpressionInterpret {

    public SubOperatorExpression(ExpressionInterpret leftExpression, ExpressionInterpret rightExpression) {
        super(leftExpression, rightExpression);
    }

    @Override
    public Object interpret(){

        return null;
    }
}
