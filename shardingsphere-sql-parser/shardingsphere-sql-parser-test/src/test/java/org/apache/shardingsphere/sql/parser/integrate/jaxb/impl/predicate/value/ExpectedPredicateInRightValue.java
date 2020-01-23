/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
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

package org.apache.shardingsphere.sql.parser.integrate.jaxb.impl.predicate.value;

import lombok.Getter;
import lombok.Setter;
import org.apache.shardingsphere.sql.parser.integrate.jaxb.impl.expr.complex.ExpectedCommonExpression;
import org.apache.shardingsphere.sql.parser.integrate.jaxb.impl.expr.complex.ExpectedSubquery;
import org.apache.shardingsphere.sql.parser.integrate.jaxb.impl.expr.simple.ExpectedLiteralExpression;
import org.apache.shardingsphere.sql.parser.integrate.jaxb.impl.expr.simple.ExpectedParameterMarkerExpression;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.Collection;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public final class ExpectedPredicateInRightValue implements ExpectedPredicateRightValue {
    
    @XmlElementWrapper(name = "parameter-marker-expressions")
    @XmlElement(name = "parameter-marker-expression")
    private Collection<ExpectedParameterMarkerExpression> expectedParameterMarkerExpressions;
    
    @XmlElementWrapper(name = "literal-expressions")
    @XmlElement(name = "literal-expression")
    private Collection<ExpectedLiteralExpression> expectedLiteralExpressions;
    
    @XmlElementWrapper(name = "common-expressions")
    @XmlElement(name = "common-expression")
    private Collection<ExpectedCommonExpression> expectedCommonExpressions;

    @XmlElementWrapper(name = "subquery-segments")
    @XmlElement(name = "subquery-segment")
    private Collection<ExpectedSubquery> expectedSubqueries;
}
