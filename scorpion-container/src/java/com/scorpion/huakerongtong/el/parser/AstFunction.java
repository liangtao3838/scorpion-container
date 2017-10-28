/*
 * Licensed to the HKRT Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the HKRT License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/* Generated By:JJTree: Do not edit this line. AstFunction.java */

package com.scorpion.huakerongtong.el.parser;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.el.ELException;
import javax.el.FunctionMapper;

import com.scorpion.huakerongtong.el.lang.EvaluationContext;
import com.scorpion.huakerongtong.el.util.MessageFactory;


/**
 * @author Jacob Hookom [jacob@hookom.net]
 */
public final class AstFunction extends SimpleNode {

    protected String localName = "";

    protected String prefix = "";

    public AstFunction(int id) {
        super(id);
    }

    public String getLocalName() {
        return localName;
    }

    public String getOutputName() {
        if (this.prefix == null) {
            return this.localName;
        } else {
            return this.prefix + ":" + this.localName;
        }
    }

    public String getPrefix() {
        return prefix;
    }

    @Override
    public Class<?> getType(EvaluationContext ctx)
            throws ELException {

        FunctionMapper fnMapper = ctx.getFunctionMapper();

        // quickly validate again for this request
        if (fnMapper == null) {
            throw new ELException(MessageFactory.get("error.fnMapper.null"));
        }
        Method m = fnMapper.resolveFunction(this.prefix, this.localName);
        if (m == null) {
            throw new ELException(MessageFactory.get("error.fnMapper.method",
                    this.getOutputName()));
        }
        return m.getReturnType();
    }

    @Override
    public Object getValue(EvaluationContext ctx)
            throws ELException {

        FunctionMapper fnMapper = ctx.getFunctionMapper();

        // quickly validate again for this request
        if (fnMapper == null) {
            throw new ELException(MessageFactory.get("error.fnMapper.null"));
        }
        Method m = fnMapper.resolveFunction(this.prefix, this.localName);
        if (m == null) {
            throw new ELException(MessageFactory.get("error.fnMapper.method",
                    this.getOutputName()));
        }

        Class<?>[] paramTypes = m.getParameterTypes();
        Object[] params = null;
        Object result = null;
        int inputParameterCount = this.jjtGetNumChildren();
        int methodParameterCount = paramTypes.length;
        if (inputParameterCount == 0 && methodParameterCount == 1 && m.isVarArgs()) {
            params = new Object[] { null };
        } else if (inputParameterCount > 0) {
            params = new Object[methodParameterCount];
            try {
                for (int i = 0; i < methodParameterCount; i++) {
                    if (m.isVarArgs() && i == methodParameterCount - 1) {
                        if (inputParameterCount < methodParameterCount) {
                            params[i] = new Object[] { null };
                        } else if (inputParameterCount == methodParameterCount &&
                                paramTypes[i].isArray()) {
                            params[i] = this.jjtGetChild(i).getValue(ctx);
                        } else {
                            Object[] varargs =
                                    new Object[inputParameterCount - methodParameterCount + 1];
                            Class<?> target = paramTypes[i].getComponentType();
                            for (int j = i; j < inputParameterCount; j++) {
                                varargs[j-i] = this.jjtGetChild(j).getValue(ctx);
                                varargs[j-i] = coerceToType(varargs[j-i], target);
                            }
                            params[i] = varargs;
                        }
                    } else {
                        params[i] = this.jjtGetChild(i).getValue(ctx);
                    }
                    params[i] = coerceToType(params[i], paramTypes[i]);
                }
            } catch (ELException ele) {
                throw new ELException(MessageFactory.get("error.function", this
                        .getOutputName()), ele);
            }
        }
        try {
            result = m.invoke(null, params);
        } catch (IllegalAccessException iae) {
            throw new ELException(MessageFactory.get("error.function", this
                    .getOutputName()), iae);
        } catch (InvocationTargetException ite) {
            Throwable cause = ite.getCause();
            if (cause instanceof ThreadDeath) {
                throw (ThreadDeath) cause;
            }
            if (cause instanceof VirtualMachineError) {
                throw (VirtualMachineError) cause;
            }
            throw new ELException(MessageFactory.get("error.function", this
                    .getOutputName()), cause);
        }
        return result;
    }

    public void setLocalName(String localName) {
        this.localName = localName;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }


    @Override
    public String toString()
    {
        return ELParserTreeConstants.jjtNodeName[id] + "[" + this.getOutputName() + "]";
    }
}
