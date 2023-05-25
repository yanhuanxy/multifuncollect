package com.yanhuanxy.multifunservice.expression.operator;

import com.yanhuanxy.multifunservice.expression.ExpressionInterpret;
import com.yanhuanxy.multifunservice.expression.OperatorExpressionInterpret;

/**
 *  表达式 除法操作
 */
public class DivOperatorExpression extends OperatorExpressionInterpret {

    public DivOperatorExpression(ExpressionInterpret leftExpression, ExpressionInterpret rightExpression) {
        super(leftExpression, rightExpression);
    }

    @Override
    public Object interpret(){

        return null;
    }
}
