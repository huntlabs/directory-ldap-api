/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *  
 *    http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License. 
 *  
 */
package org.apache.directory.shared.ldap.aci;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.HashSet;
import java.util.Set;

import com.mycila.junit.concurrent.Concurrency;
import com.mycila.junit.concurrent.ConcurrentJunitRunner;
import org.apache.directory.shared.ldap.aci.protectedItem.AttributeTypeItem;
import org.apache.directory.shared.ldap.model.schema.MutableAttributeTypeImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Unit tests class ProtectedItem.AttributeType.
 * 
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
@RunWith(ConcurrentJunitRunner.class)
@Concurrency()
public class ProtectedItem_AttributeTypeTest
{
    AttributeTypeItem attributeTypeA;
    AttributeTypeItem attributeTypeACopy;
    AttributeTypeItem attributeTypeB;
    AttributeTypeItem attributeTypeC;


    /**
     * Initialize name instances
     */
    @Before
    public void initNames() throws Exception
    {
        Set<MutableAttributeTypeImpl> colA = new HashSet<MutableAttributeTypeImpl>();
        colA.add( new MutableAttributeTypeImpl( "aa" ) );
        colA.add( new MutableAttributeTypeImpl( "bb" ) );
        colA.add( new MutableAttributeTypeImpl( "cc" ) );
        Set<MutableAttributeTypeImpl> colB = new HashSet<MutableAttributeTypeImpl>();
        colB.add( new MutableAttributeTypeImpl( "aa" ) );
        colB.add( new MutableAttributeTypeImpl( "bb" ) );
        colB.add( new MutableAttributeTypeImpl( "cc" ) );
        Set<MutableAttributeTypeImpl> colC = new HashSet<MutableAttributeTypeImpl>();
        colC.add( new MutableAttributeTypeImpl( "bb" ) );
        colC.add( new MutableAttributeTypeImpl( "cc" ) );
        colC.add( new MutableAttributeTypeImpl( "dd" ) );

        attributeTypeA = new AttributeTypeItem( colA );
        attributeTypeACopy = new AttributeTypeItem( colA );
        attributeTypeB = new AttributeTypeItem( colB );
        attributeTypeC = new AttributeTypeItem( colC );
    }


    @Test
    public void testEqualsNull() throws Exception
    {
        assertFalse( attributeTypeA.equals( null ) );
    }


    @Test
    public void testEqualsReflexive() throws Exception
    {
        assertEquals( attributeTypeA, attributeTypeA );
    }


    @Test
    public void testHashCodeReflexive() throws Exception
    {
        assertEquals( attributeTypeA.hashCode(), attributeTypeA.hashCode() );
    }


    @Test
    public void testEqualsSymmetric() throws Exception
    {
        assertEquals( attributeTypeA, attributeTypeACopy );
        assertEquals( attributeTypeACopy, attributeTypeA );
    }


    @Test
    public void testHashCodeSymmetric() throws Exception
    {
        assertEquals( attributeTypeA.hashCode(), attributeTypeACopy.hashCode() );
        assertEquals( attributeTypeACopy.hashCode(), attributeTypeA.hashCode() );
    }


    @Test
    public void testEqualsTransitive() throws Exception
    {
        assertEquals( attributeTypeA, attributeTypeACopy );
        assertEquals( attributeTypeACopy, attributeTypeB );
        assertEquals( attributeTypeA, attributeTypeB );
    }


    @Test
    public void testHashCodeTransitive() throws Exception
    {
        assertEquals( attributeTypeA.hashCode(), attributeTypeACopy.hashCode() );
        assertEquals( attributeTypeACopy.hashCode(), attributeTypeB.hashCode() );
        assertEquals( attributeTypeA.hashCode(), attributeTypeB.hashCode() );
    }


    @Test
    public void testNotEqualDiffValue() throws Exception
    {
        assertFalse( attributeTypeA.equals( attributeTypeC ) );
        assertFalse( attributeTypeC.equals( attributeTypeA ) );
    }
}
