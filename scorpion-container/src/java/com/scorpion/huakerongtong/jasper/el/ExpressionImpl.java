/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.scorpion.huakerongtong.jasper.el;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.servlet.jsp.el.ELException;
import javax.servlet.jsp.el.Expression;
import javax.servlet.jsp.el.VariableResolver;

@Deprecated
public final class ExpressionImpl extends Expression {

    private final ValueExpression ve;
    
    public ExpressionImpl(ValueExpression ve) {
        this.ve = ve;
    }

    @Override
    public Object evaluate(VariableResolver vResolver) throws ELException {
        ELContext ctx = new ELContextImpl(new ELResolverImpl(vResolver));
        return ve.getValue(ctx);
    }

}