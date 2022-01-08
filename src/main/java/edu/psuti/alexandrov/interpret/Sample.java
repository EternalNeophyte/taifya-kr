package edu.psuti.alexandrov.interpret;

import edu.psuti.alexandrov.exp.Expression;

public enum Sample {

    COMPARE(expression().one("==|!=|>=|<=|>|<"))
    ;

    private final Expression<String> expression;

    public static Expression<String> expression() {
        return Expression.start();
    }

    Sample(Expression<String> expression) {
        this.expression = expression;
    }
}
