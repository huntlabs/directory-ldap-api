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
package org.apache.directory.shared.ldap.model.schema;


import java.util.List;

import org.apache.directory.shared.i18n.I18n;
import org.apache.directory.shared.ldap.model.exception.LdapException;
import org.apache.directory.shared.ldap.model.schema.registries.Registries;


/**
 * A syntax definition. Each attribute stored in a directory has a defined
 * syntax (i.e. data type) which constrains the structure and format of its
 * values. The description of each syntax specifies how attribute or assertion
 * values conforming to the syntax are normally represented when transferred in
 * LDAP operations. This representation is referred to as the LDAP-specific
 * encoding to distinguish it from other methods of encoding attribute values.
 * <p>
 * According to ldapbis [MODELS]:
 * </p>
 * 
 * <pre>
 *  4.1.5. LDAP Syntaxes
 *  
 *    LDAP Syntaxes of (attribute and assertion) values are described in
 *    terms of ASN.1 [X.680] and, optionally, have an octet string encoding
 *    known as the LDAP-specific encoding.  Commonly, the LDAP-specific
 *    encoding is constrained to string of Universal Character Set (UCS)
 *    [ISO10646] characters in UTF-8 [UTF-8] form.
 * 
 *    Each LDAP syntax is identified by an object identifier (OID).
 * 
 *    LDAP syntax definitions are written according to the ABNF:
 * 
 *      SyntaxDescription = LPAREN WSP
 *          numericoid                ; object identifier
 *          [ SP &quot;DESC&quot; SP qdstring ] ; description
 *          extensions WSP RPAREN     ; extensions
 * 
 *    where:
 *      [numericoid] is object identifier assigned to this LDAP syntax;
 *      DESC [qdstring] is a short descriptive string; and
 *      [extensions] describe extensions.
 * </pre>
 * 
 * @see <a href="http://www.faqs.org/rfcs/rfc2252.html"> RFC2252 Section 4.3.3</a>
 * @see <a href=
 *      "http://www.ietf.org/internet-drafts/draft-ietf-ldapbis-models-09.txt">
 *      ldapbis [MODELS]</a>
 * @see DescriptionUtils#getDescription(Syntax)
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class MutableLdapSyntaxImpl extends AbstractMutableSchemaObject implements MutableLdapSyntax
{
    private static final long serialVersionUID = 1L;

    /** the human readable flag */
    protected boolean isHumanReadable = false;

    /** The associated SyntaxChecker */
    protected MutableSyntaxChecker syntaxChecker;


    /**
     * Creates a Syntax object using a unique OID.
     * 
     * @param oid the OID for this Syntax
     */
    public MutableLdapSyntaxImpl( String oid )
    {
        super( SchemaObjectType.LDAP_SYNTAX, oid );
    }


    /**
     * Creates a Syntax object using a unique OID.
     *
     * @param oid the OID for this syntax
     * @param description the description for this syntax
     */
    public MutableLdapSyntaxImpl( String oid, String description )
    {
        super( SchemaObjectType.LDAP_SYNTAX, oid );
        this.description = description;
    }


    /**
     * Creates a Syntax object using a unique OID.
     *
     * @param oid the OID for this syntax
     * @param description the description for this syntax
     * @param isHumanReadable true if this syntax is human readable
     */
    public MutableLdapSyntaxImpl( String oid, String description, boolean isHumanReadable )
    {
        super( SchemaObjectType.LDAP_SYNTAX, oid );
        this.description = description;
        this.isHumanReadable = isHumanReadable;
    }


    /* (non-Javadoc)
     * @see org.apache.directory.shared.ldap.model.schema.LdapSyntax#isHumanReadable()
     */
    public boolean isHumanReadable()
    {
        return isHumanReadable;
    }


    /* (non-Javadoc)
     * @see org.apache.directory.shared.ldap.model.schema.MutableLdapSyntax#setHumanReadable(boolean)
     */
    public void setHumanReadable( boolean humanReadable )
    {
        if ( locked )
        {
            throw new UnsupportedOperationException( I18n.err( I18n.ERR_04441, getName() ) );
        }

        if ( !isReadOnly )
        {
            this.isHumanReadable = humanReadable;
        }
    }


    /* (non-Javadoc)
     * @see org.apache.directory.shared.ldap.model.schema.LdapSyntax#getSyntaxChecker()
     */
    public MutableSyntaxChecker getSyntaxChecker()
    {
        return syntaxChecker;
    }


    /* (non-Javadoc)
     * @see org.apache.directory.shared.ldap.model.schema.MutableLdapSyntax#setSyntaxChecker(org.apache.directory.shared.ldap.model.schema.MutableSyntaxCheckerImpl)
     */
    public void setSyntaxChecker( MutableSyntaxChecker syntaxChecker )
    {
        if ( locked )
        {
            throw new UnsupportedOperationException( I18n.err( I18n.ERR_04441, getName() ) );
        }

        if ( !isReadOnly )
        {
            this.syntaxChecker = syntaxChecker;
        }
    }


    /**
     * Update the associated SyntaxChecker, even if the SchemaObject is readOnly
     *
     * @param newSyntaxChecker The associated SyntaxChecker
     */
    public void updateSyntaxChecker( MutableSyntaxCheckerImpl newSyntaxChecker )
    {
        if ( locked )
        {
            throw new UnsupportedOperationException( I18n.err( I18n.ERR_04441, getName() ) );
        }

        this.syntaxChecker = newSyntaxChecker;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return objectType + " " + DescriptionUtils.getDescription( this );
    }


    /**
     * {@inheritDoc}
     */
    public void removeFromRegistries( List<Throwable> errors, Registries registries ) throws LdapException
    {
        if ( registries != null )
        {
            /*
             * Remove the Syntax references (using and usedBy) : 
             * S -> SC
             */
            if ( syntaxChecker != null )
            {
                registries.delReference( this, syntaxChecker );
            }
        }
    }


    /* (non-Javadoc)
     * @see org.apache.directory.shared.ldap.model.schema.LdapSyntax#copy()
     */
    /* (non-Javadoc)
     * @see org.apache.directory.shared.ldap.model.schema.MutableLdapSyntax#copy()
     */
    public MutableLdapSyntaxImpl copy()
    {
        MutableLdapSyntaxImpl copy = new MutableLdapSyntaxImpl( oid );

        // Copy the SchemaObject common data
        copy.copy( this );

        // Copy the HR flag
        copy.isHumanReadable = isHumanReadable;

        // All the references to other Registries object are set to null.
        copy.syntaxChecker = null;

        return copy;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals( Object o )
    {
        if ( !super.equals( o ) )
        {
            return false;
        }

        if ( !( o instanceof MutableLdapSyntaxImpl ) )
        {
            return false;
        }

        MutableLdapSyntaxImpl that = ( MutableLdapSyntaxImpl ) o;

        // IsHR
        if ( isHumanReadable != that.isHumanReadable )
        {
            return false;
        }

        // Check the SyntaxChecker (not a equals)
        if ( syntaxChecker != null )
        {
            if ( that.syntaxChecker == null )
            {
                return false;
            }

            return syntaxChecker.getOid().equals( that.syntaxChecker.getOid() );
        }
        else
        {
            return that.syntaxChecker == null;
        }
    }


    /* (non-Javadoc)
     * @see org.apache.directory.shared.ldap.model.schema.MutableLdapSyntax#clear()
     */
    public void clear()
    {
        // Clear the common elements
        super.clear();

        // Clear the references
        syntaxChecker = null;
    }
}