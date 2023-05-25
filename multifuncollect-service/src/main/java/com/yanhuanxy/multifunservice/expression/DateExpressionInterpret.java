package com.yanhuanxy.multifunservice.expression;

import java.sql.Timestamp;

/**
 *  时间表达式 解释器
 */
public class DateExpressionInterpret implements ExpressionInterpret {

    private final Timestamp timestamp;

    public DateExpressionInterpret(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public Object interpret(){

        return timestamp;
    }
}
