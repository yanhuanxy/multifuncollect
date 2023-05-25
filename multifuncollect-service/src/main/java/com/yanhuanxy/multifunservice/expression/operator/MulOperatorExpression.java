package com.yanhuanxy.multifunservice.expression.operator;

import com.yanhuanxy.multifunservice.expression.ExpressionInterpret;
import com.yanhuanxy.multifunservice.expression.OperatorExpressionInterpret;

/**
 *  表达式 乘法操作
 */
public class MulOperatorExpression extends OperatorExpressionInterpret {

    public MulOperatorExpression(ExpressionInterpret leftExpression, ExpressionInterpret rightExpression) {
        super(leftExpression, rightExpression);
    }

    @Override
    public Object interpret(){

        return null;
    }
}
