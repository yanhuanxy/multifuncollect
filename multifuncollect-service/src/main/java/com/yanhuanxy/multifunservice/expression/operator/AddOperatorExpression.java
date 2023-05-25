package com.yanhuanxy.multifunservice.expression.operator;

import com.yanhuanxy.multifunservice.expression.ExpressionInterpret;
import com.yanhuanxy.multifunservice.expression.OperatorExpressionInterpret;

/**
 *  表达式 加法操作
 */
public class AddOperatorExpression extends OperatorExpressionInterpret {

    public AddOperatorExpression(ExpressionInterpret leftExpression, ExpressionInterpret rightExpression) {
        super(leftExpression, rightExpression);
    }

    @Override
    public Object interpret(){

        return null;
    }
}
