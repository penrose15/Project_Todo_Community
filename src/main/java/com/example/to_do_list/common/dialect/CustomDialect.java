package com.example.to_do_list.common.dialect;

import org.hibernate.dialect.MySQL8Dialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.StandardBasicTypes;

public class CustomDialect extends MySQL8Dialect {
    public CustomDialect() {
        super();

        registerFunction("match", new SQLFunctionTemplate(StandardBasicTypes.INTEGER, "match(?1, ?2) against (?3 in boolean mode) and 1"));
    }

}
