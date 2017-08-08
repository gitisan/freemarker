/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.freemarker.core.userpkg;

import java.io.IOException;
import java.io.Writer;

import org.apache.freemarker.core.model.TemplateCallableModel;
import org.apache.freemarker.core.model.TemplateHashModelEx2;
import org.apache.freemarker.core.model.TemplateModelException;
import org.apache.freemarker.core.model.TemplateNumberModel;
import org.apache.freemarker.core.model.TemplateScalarModel;
import org.apache.freemarker.core.model.TemplateSequenceModel;
import org.apache.freemarker.core.util.FTLUtil;
import org.apache.freemarker.core.util._StringUtil;

public abstract class TestTemplateCallableModel implements TemplateCallableModel {

    protected void printParam(String name, Object value, StringBuilder sb) throws TemplateModelException {
        printParam(name, value, sb, false);
    }

    protected void printParam(String name, Object value, StringBuilder sb, boolean first)
            throws TemplateModelException {
        if (!first) {
            sb.append(", ");
        }
        sb.append(name);
        sb.append("=");
        printValue(value, sb);
    }

    protected void printParam(String name, Object value, Writer out) throws IOException, TemplateModelException {
        printParam(name, value, out, false);
    }

    protected void printParam(String name, Object value, Writer out, boolean first)
            throws IOException, TemplateModelException {
        StringBuilder sb = new StringBuilder();
        printParam(name, value, sb, first);
        out.write(sb.toString());
    }

    private void printValue(Object value, StringBuilder sb) throws TemplateModelException {
        if (value == null) {
            sb.append("null");
        } else if (value instanceof TemplateNumberModel) {
            sb.append(((TemplateNumberModel) value).getAsNumber().toString());
        } else if (value instanceof TemplateScalarModel) {
            sb.append(FTLUtil.toStringLiteral(((TemplateScalarModel) value).getAsString()));
        } else if (value instanceof TemplateSequenceModel) {
            int len = ((TemplateSequenceModel) value).size();
            sb.append('[');
            for (int i = 0; i < len; i++) {
                if (i != 0) {
                    sb.append(", ");
                }
                printValue(((TemplateSequenceModel) value).get(i), sb);
            }
            sb.append(']');
        } else if (value instanceof TemplateHashModelEx2) {
            TemplateHashModelEx2.KeyValuePairIterator it = ((TemplateHashModelEx2) value).keyValuePairIterator();
            sb.append('{');
            while (it.hasNext()) {
                TemplateHashModelEx2.KeyValuePair kvp = it.next();

                printValue(kvp.getKey(), sb);
                sb.append(": ");
                printValue(kvp.getValue(), sb);

                if (it.hasNext()) {
                    sb.append(", ");
                }
            }
            sb.append('}');
        } else if (value instanceof String) {
            sb.append(_StringUtil.jQuote(value));
        } else if (value instanceof Number) {
            sb.append(value.toString());
        } else if (value instanceof Boolean) {
            sb.append(value.toString());
        } else {
            throw new IllegalArgumentException("Unsupported value class: " + value.getClass().getName());
        }
    }

}
