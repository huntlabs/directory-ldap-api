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
import org.apache.directory.shared.ldap.model.schema.registries.AttributeTypeRegistry;
import org.apache.directory.shared.ldap.model.schema.registries.Registries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * An attributeType specification. attributeType specifications describe the
 * nature of attributes within the directory. The attributeType specification's
 * properties are accessible through this interface.
 * <p>
 * According to ldapbis [MODELS]:
 * </p>
 *
 * <pre>
 *  4.1.2. Attribute Types
 *
 *    Attribute Type definitions are written according to the ABNF:
 *
 *      AttributeTypeDescription = LPAREN WSP
 *          numericoid                   ; object identifier
 *          [ SP &quot;NAME&quot; SP qdescrs ]     ; short names (descriptors)
 *          [ SP &quot;DESC&quot; SP qdstring ]    ; description
 *          [ SP &quot;OBSOLETE&quot; ]            ; not active
 *          [ SP &quot;SUP&quot; SP oid ]          ; supertype
 *          [ SP &quot;EQUALITY&quot; SP oid ]     ; equality matching rule
 *          [ SP &quot;ORDERING&quot; SP oid ]     ; ordering matching rule
 *          [ SP &quot;SUBSTR&quot; SP oid ]       ; substrings matching rule
 *          [ SP &quot;SYNTAX&quot; SP noidlen ]   ; value syntax
 *          [ SP &quot;SINGLE-VALUE&quot; ]        ; single-value
 *          [ SP &quot;COLLECTIVE&quot; ]          ; collective
 *          [ SP &quot;NO-USER-MODIFICATION&quot; ]; not user modifiable
 *          [ SP &quot;USAGE&quot; SP usage ]      ; usage
 *          extensions WSP RPAREN        ; extensions
 *
 *      usage = &quot;userApplications&quot;     / ; user
 *              &quot;directoryOperation&quot;   / ; directory operational
 *              &quot;distributedOperation&quot; / ; DSA-shared operational
 *              &quot;dSAOperation&quot;           ; DSA-specific operational
 *
 *    where:
 *      [numericoid] is object identifier assigned to this attribute type;
 *      NAME [qdescrs] are short names (descriptors) identifying this
 *          attribute type;
 *      DESC [qdstring] is a short descriptive string;
 *      OBSOLETE indicates this attribute type is not active;
 *      SUP oid specifies the direct supertype of this type;
 *      EQUALITY, ORDERING, SUBSTRING provide the oid of the equality,
 *          ordering, and substrings matching rules, respectively;
 *      SYNTAX identifies value syntax by object identifier and may suggest
 *          a minimum upper bound;
 *      COLLECTIVE indicates this attribute type is collective [X.501];
 *      NO-USER-MODIFICATION indicates this attribute type is not user
 *          modifiable;
 *      USAGE indicates the application of this attribute type; and
 *      [extensions] describe extensions.
 *
 *    Each attribute type description must contain at least one of the SUP
 *    or SYNTAX fields.
 *
 *    Usage of userApplications, the default, indicates that attributes of
 *    this type represent user information.  That is, they are user
 *    attributes.
 *
 *    COLLECTIVE requires usage userApplications.  Use of collective
 *    attribute types in LDAP is not discussed in this technical
 *    specification.
 *
 *    A usage of directoryOperation, distributedOperation, or dSAOperation
 *    indicates that attributes of this type represent operational and/or
 *    administrative information.  That is, they are operational attributes.
 *
 *    directoryOperation usage indicates that the attribute of this type is
 *    a directory operational attribute.  distributedOperation usage
 *    indicates that the attribute of this DSA-shared usage operational
 *    attribute.  dSAOperation usage indicates that the attribute of this
 *    type is a DSA-specific operational attribute.
 *
 *    NO-USER-MODIFICATION requires an operational usage.
 *
 *    Note that the [AttributeTypeDescription] does not list the matching
 *    rules which can be used with that attribute type in an extensibleMatch
 *    search filter.  This is done using the 'matchingRuleUse' attribute
 *    described in Section 4.1.4.
 *
 *    This document refines the schema description of X.501 by requiring
 *    that the SYNTAX field in an [AttributeTypeDescription] be a string
 *    representation of an object identifier for the LDAP string syntax
 *    definition with an optional indication of the suggested minimum bound
 *    of a value of this attribute.
 *
 *    A suggested minimum upper bound on the number of characters in a value
 *    with a string-based syntax, or the number of bytes in a value for all
 *    other syntaxes, may be indicated by appending this bound count inside
 *    of curly braces following the syntax's OBJECT IDENTIFIER in an
 *
 *    Attribute Type Description.  This bound is not part of the syntax name
 *    itself.  For instance, &quot;1.3.6.4.1.1466.0{64}&quot; suggests that server
 *    implementations should allow a string to be 64 characters long,
 *    although they may allow longer strings.  Note that a single character
 *    of the Directory String syntax may be encoded in more than one octet
 *    since UTF-8 is a variable-length encoding.
 * </pre>
 *
 * @see <a href="http://www.faqs.org/rfcs/rfc2252.html">RFC 2252 Section 4.2</a>
 * @see <a
 *      href="http://www.ietf.org/internet-drafts/draft-ietf-ldapbis-models-11.txt">
 *      ldapbis [MODELS]</a>
 * @see DescriptionUtils#getDescription(AttributeType)
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class AttributeType extends AbstractSchemaObject implements Cloneable
{
    /** A logger for this class */
    private static final Logger LOG = LoggerFactory.getLogger( AttributeType.class );

    /** The syntax OID associated with this AttributeType */
    private String syntaxOid;

    /** The syntax associated with the syntaxID */
    private LdapSyntax syntax;

    /** The equality OID associated with this AttributeType */
    private String equalityOid;

    /** The equality MatchingRule associated with the equalityID */
    private MatchingRule equality;

    /** The substring OID associated with this AttributeType */
    private String substringOid;

    /** The substring MatchingRule associated with the substringID */
    private MatchingRule substring;

    /** The ordering OID associated with this AttributeType */
    private String orderingOid;

    /** The ordering MatchingRule associated with the orderingID */
    private MatchingRule ordering;

    /** The superior AttributeType OID */
    private String superiorOid;

    /** The superior AttributeType */
    private AttributeType superior;

    /** whether or not this type is single valued */
    private boolean isSingleValued = false;

    /** whether or not this type is a collective attribute */
    private boolean isCollective = false;

    /** whether or not this type can be modified by directory users */
    private boolean canUserModify = true;

    /** the usage for this attributeType */
    private UsageEnum usage = UsageEnum.USER_APPLICATIONS;

    /** the length of this attribute in bytes */
    private long syntaxLength = 0L;


    /**
     * Creates a AttributeType object using a unique OID.
     *
     * @param oid the OID for this AttributeType
     */
    public AttributeType( String oid )
    {
        super( SchemaObjectType.ATTRIBUTE_TYPE, oid );
    }


    /**
     * {@inheritDoc}
     *
     * If one of the referenced SchemaObject does not exist (SUP, EQUALITY, ORDERING, SUBSTR, SYNTAX),
     * an exception is thrown.
     */
    public void removeFromRegistries( List<Throwable> errors, Registries registries ) throws LdapException
    {
        if ( registries != null )
        {
            AttributeTypeRegistry attributeTypeRegistry = registries.getAttributeTypeRegistry();

            // Remove the attributeType from the oid/normalizer map
            attributeTypeRegistry.removeMappingFor( this );

            // Unregister this AttributeType into the Descendant map
            attributeTypeRegistry.unregisterDescendants( this, superior );

            /**
             * Remove the AT references (using and usedBy) :
             * AT -> MR (for EQUALITY, ORDERING and SUBSTR)
             * AT -> S
             * AT -> AT
             */
            if ( equality != null )
            {
                registries.delReference( this, equality );
            }

            if ( ordering != null )
            {
                registries.delReference( this, ordering );
            }

            if ( substring != null )
            {
                registries.delReference( this, substring );
            }

            if ( syntax != null )
            {
                registries.delReference( this, syntax );
            }

            if ( superior != null )
            {
                registries.delReference( this, superior );
            }
        }
    }


    /**
     * Gets whether or not this AttributeType is single-valued.
     *
     * @return true if only one value can exist for this AttributeType, false
     *         otherwise
     */
    public boolean isSingleValued()
    {
        return isSingleValued;
    }


    /**
     * Tells if this AttributeType is Single Valued or not
     *
     * @param singleValued True if the AttributeType is single-valued
     */
    public void setSingleValued( boolean singleValued )
    {
        if ( locked )
        {
            throw new UnsupportedOperationException( I18n.err( I18n.ERR_04441, getName() ) );
        }

        if ( !isReadOnly )
        {
            this.isSingleValued = singleValued;
        }
    }


    /**
     * Gets whether or not this AttributeType can be modified by a user.
     *
     * @return true if users can modify it, false if only the directory can.
     */
    public boolean isUserModifiable()
    {
        return canUserModify;
    }


    /**
     * Tells if this AttributeType can be modified by a user or not
     *
     * @param userModifiable The flag to set
     */
    public void setUserModifiable( boolean userModifiable )
    {
        if ( locked )
        {
            throw new UnsupportedOperationException( I18n.err( I18n.ERR_04441, getName() ) );
        }

        if ( !isReadOnly )
        {
            this.canUserModify = userModifiable;
        }
    }


    /**
     * Gets whether or not this AttributeType is a collective attribute.
     *
     * @return true if the attribute is collective, false otherwise
     */
    public boolean isCollective()
    {
        return isCollective;
    }


    /**
     * Updates the collective flag
     *
     * @param collective The new value to set
     */
    public void updateCollective( boolean collective )
    {
        if ( locked )
        {
            throw new UnsupportedOperationException( I18n.err( I18n.ERR_04441, getName() ) );
        }

        this.isCollective = collective;
    }


    /**
     * Sets the collective flag
     *
     * @param collective The new value to set
     */
    public void setCollective( boolean collective )
    {
        if ( locked )
        {
            throw new UnsupportedOperationException( I18n.err( I18n.ERR_04441, getName() ) );
        }

        if ( !isReadOnly )
        {
            this.isCollective = collective;
        }
    }


    /**
     * Determines the usage for this AttributeType.
     *
     * @return a type safe UsageEnum
     */
    public UsageEnum getUsage()
    {
        return usage;
    }


    /**
     * Sets the AttributeType usage, one of :
     * <ul>
     *   <li>USER_APPLICATIONS</li>
     *   <li>DIRECTORY_OPERATION</li>
     *   <li>DISTRIBUTED_OPERATION</li>
     *   <li>DSA_OPERATION</li>
     * </ul>
     * 
     * @see UsageEnum
     * @param usage The AttributeType usage
     */
    public void setUsage( UsageEnum usage )
    {
        if ( locked )
        {
            throw new UnsupportedOperationException( I18n.err( I18n.ERR_04441, getName() ) );
        }

        if ( !isReadOnly )
        {
            this.usage = usage;
        }
    }


    /**
     * Updates the AttributeType usage, one of :
     * <ul>
     *   <li>USER_APPLICATIONS</li>
     *   <li>DIRECTORY_OPERATION</li>
     *   <li>DISTRIBUTED_OPERATION</li>
     *   <li>DSA_OPERATION</li>
     * </ul>
     * 
     * @see UsageEnum
     * @param newUsage The AttributeType usage
     */
    public void updateUsage( UsageEnum newUsage )
    {
        if ( locked )
        {
            throw new UnsupportedOperationException( I18n.err( I18n.ERR_04441, getName() ) );
        }

        this.usage = newUsage;
    }


    /**
     * Gets a length limit for this AttributeType.
     *
     * @return the length of the attribute
     */
    public long getSyntaxLength()
    {
        return syntaxLength;
    }


    /**
     * Sets the length limit of this AttributeType based on its associated
     * syntax.
     *
     * @param length the new length to set
     */
    public void setSyntaxLength( long length )
    {
        if ( locked )
        {
            throw new UnsupportedOperationException( I18n.err( I18n.ERR_04441, getName() ) );
        }

        if ( !isReadOnly )
        {
            this.syntaxLength = length;
        }
    }


    /**
     * Gets the the superior AttributeType of this AttributeType.
     *
     * @return the superior AttributeType for this AttributeType
     */
    public AttributeType getSuperior()
    {
        return superior;
    }


    /**
     * Gets the OID of the superior AttributeType for this AttributeType.
     *
     * @return The OID of the superior AttributeType for this AttributeType.
     */
    public String getSuperiorOid()
    {
        return superiorOid;
    }


    /**
     * Gets the Name of the superior AttributeType for this AttributeType.
     *
     * @return The Name of the superior AttributeType for this AttributeType.
     */
    public String getSuperiorName()
    {
        if ( superior != null )
        {
            return superior.getName();
        }
        else
        {
            return superiorOid;
        }
    }


    /**
     * Sets the superior AttributeType OID of this AttributeType
     *
     * @param superiorOid The superior AttributeType OID of this AttributeType
     */
    public void setSuperiorOid( String superiorOid )
    {
        if ( locked )
        {
            throw new UnsupportedOperationException( I18n.err( I18n.ERR_04441, getName() ) );
        }

        if ( !isReadOnly )
        {
            this.superiorOid = superiorOid;
        }
    }


    /**
     * Sets the superior for this AttributeType
     *
     * @param superior The superior for this AttributeType
     */
    public void setSuperior( AttributeType superior )
    {
        if ( locked )
        {
            throw new UnsupportedOperationException( I18n.err( I18n.ERR_04441, getName() ) );
        }

        if ( !isReadOnly )
        {
            this.superior = superior;
            this.superiorOid = superior.getOid();
        }
    }


    /**
     * Sets the superior oid for this AttributeType
     *
     * @param newSuperiorOid The superior oid for this AttributeType
     */
    public void setSuperior( String newSuperiorOid )
    {
        if ( locked )
        {
            throw new UnsupportedOperationException( I18n.err( I18n.ERR_04441, getName() ) );
        }

        if ( !isReadOnly )
        {
            this.superiorOid = newSuperiorOid;
        }
    }


    /**
     * Update the associated Superior AttributeType, even if the SchemaObject is readOnly
     *
     * @param newSuperior The superior for this AttributeType
     */
    public void updateSuperior( AttributeType newSuperior )
    {
        if ( locked )
        {
            throw new UnsupportedOperationException( I18n.err( I18n.ERR_04441, getName() ) );
        }

        this.superior = newSuperior;
        this.superiorOid = newSuperior.getOid();
    }


    /**
     * Gets the Syntax for this AttributeType's values.
     *
     * @return the value syntax
     */
    public LdapSyntax getSyntax()
    {
        return syntax;
    }


    /**
     * Gets the Syntax name for this AttributeType's values.
     *
     * @return the value syntax name
     */
    public String getSyntaxName()
    {
        if ( syntax != null )
        {
            return syntax.getName();
        }
        else
        {
            return syntaxOid;
        }
    }


    /**
     * Gets the Syntax OID for this AttributeType's values.
     *
     * @return the value syntax's OID
     */
    public String getSyntaxOid()
    {
        return syntaxOid;
    }


    /**
     * Sets the Syntax OID for this AttributeType
     *
     * @param syntaxOid The syntax OID for this AttributeType
     */
    public void setSyntaxOid( String syntaxOid )
    {
        if ( locked )
        {
            throw new UnsupportedOperationException( I18n.err( I18n.ERR_04441, getName() ) );
        }

        if ( !isReadOnly )
        {
            this.syntaxOid = syntaxOid;
        }
    }


    /**
     * Sets the Syntax for this AttributeType
     *
     * @param syntax The Syntax for this AttributeType
     */
    public void setSyntax( LdapSyntax syntax )
    {
        if ( locked )
        {
            throw new UnsupportedOperationException( I18n.err( I18n.ERR_04441, getName() ) );
        }

        if ( !isReadOnly )
        {
            this.syntax = syntax;
            this.syntaxOid = syntax.getOid();
        }
    }


    /**
     * Update the associated Syntax, even if the SchemaObject is readOnly
     *
     * @param newSyntax The Syntax for this AttributeType
     */
    public void updateSyntax( LdapSyntax newSyntax )
    {
        if ( locked )
        {
            throw new UnsupportedOperationException( I18n.err( I18n.ERR_04441, getName() ) );
        }

        this.syntax = newSyntax;
        this.syntaxOid = newSyntax.getOid();
    }


    /**
     * Gets the MatchingRule for this AttributeType used for equality matching.
     *
     * @return the equality matching rule
     */
    public MatchingRule getEquality()
    {
        return equality;
    }


    /**
     * Gets the Equality OID for this AttributeType's values.
     *
     * @return the value Equality's OID
     */
    public String getEqualityOid()
    {
        return equalityOid;
    }


    /**
     * Gets the Equality Name for this AttributeType's values.
     *
     * @return the value Equality's Name
     */
    public String getEqualityName()
    {
        if ( equality != null )
        {
            return equality.getName();
        }
        else
        {
            return equalityOid;
        }
    }


    /**
     * Sets the Equality OID for this AttributeType
     *
     * @param equalityOid The Equality OID for this AttributeType
     */
    public void setEqualityOid( String equalityOid )
    {
        if ( locked )
        {
            throw new UnsupportedOperationException( I18n.err( I18n.ERR_04441, getName() ) );
        }

        if ( !isReadOnly )
        {
            this.equalityOid = equalityOid;
        }
    }


    /**
     * Sets the Equality MR for this AttributeType
     *
     * @param equality The Equality MR for this AttributeType
     */
    public void setEquality( MatchingRule equality )
    {
        if ( locked )
        {
            throw new UnsupportedOperationException( I18n.err( I18n.ERR_04441, getName() ) );
        }

        if ( !isReadOnly )
        {
            this.equality = equality;
            this.equalityOid = equality.getOid();
        }
    }


    /**
     * Update the associated Equality MatchingRule, even if the SchemaObject is readOnly
     *
     * @param newEquality The Equality MR for this AttributeType
     */
    public void updateEquality( MatchingRule newEquality )
    {
        if ( locked )
        {
            throw new UnsupportedOperationException( I18n.err( I18n.ERR_04441, getName() ) );
        }

        this.equality = newEquality;
        this.equalityOid = newEquality.getOid();
    }


    /**
     * Gets the MatchingRule for this AttributeType used for Ordering matching.
     *
     * @return the Ordering matching rule
     */
    public MatchingRule getOrdering()
    {
        return ordering;
    }


    /**
     * Gets the MatchingRule name for this AttributeType used for Ordering matching.
     *
     * @return the Ordering matching rule name
     */
    public String getOrderingName()
    {
        if ( ordering != null )
        {
            return ordering.getName();
        }
        else
        {
            return orderingOid;
        }
    }


    /**
     * Gets the Ordering OID for this AttributeType's values.
     *
     * @return the value Equality's OID
     */
    public String getOrderingOid()
    {
        return orderingOid;
    }


    /**
     * Sets the Ordering OID for this AttributeType
     *
     * @param orderingOid The Ordering OID for this AttributeType
     */
    public void setOrderingOid( String orderingOid )
    {
        if ( locked )
        {
            throw new UnsupportedOperationException( I18n.err( I18n.ERR_04441, getName() ) );
        }

        if ( !isReadOnly )
        {
            this.orderingOid = orderingOid;
        }
    }


    /**
     * Sets the Ordering MR for this AttributeType
     *
     * @param ordering The Ordering MR for this AttributeType
     */
    public void setOrdering( MatchingRule ordering )
    {
        if ( locked )
        {
            throw new UnsupportedOperationException( I18n.err( I18n.ERR_04441, getName() ) );
        }

        if ( !isReadOnly )
        {
            this.ordering = ordering;
            this.orderingOid = ordering.getOid();
        }
    }


    /**
     * Update the associated Ordering MatchingRule, even if the SchemaObject is readOnly
     *
     * @param newOrdering The Ordering MR for this AttributeType
     */
    public void updateOrdering( MatchingRule newOrdering )
    {
        if ( locked )
        {
            throw new UnsupportedOperationException( I18n.err( I18n.ERR_04441, getName() ) );
        }

        this.ordering = newOrdering;
        this.orderingOid = newOrdering.getOid();
    }


    /**
     * Gets the MatchingRule for this AttributeType used for Substr matching.
     *
     * @return the Substr matching rule
     */
    public MatchingRule getSubstring()
    {
        return substring;
    }


    /**
     * Gets the MatchingRule name for this AttributeType used for Substring matching.
     *
     * @return the Substring matching rule name
     */
    public String getSubstringName()
    {
        if ( substring != null )
        {
            return substring.getName();
        }
        else
        {
            return substringOid;
        }
    }


    /**
     * Gets the Substr OID for this AttributeType's values.
     *
     * @return the value Substr's OID
     */
    public String getSubstringOid()
    {
        return substringOid;
    }


    /**
     * Sets the Substr OID for this AttributeType
     *
     * @param substrOid The Substr OID for this AttributeType
     */
    public void setSubstringOid( String substrOid )
    {
        if ( locked )
        {
            throw new UnsupportedOperationException( I18n.err( I18n.ERR_04441, getName() ) );
        }

        if ( !isReadOnly )
        {
            this.substringOid = substrOid;
        }
    }


    /**
     * Sets the Substr MR for this AttributeType
     *
     * @param substring The Substr MR for this AttributeType
     */
    public void setSubstring( MatchingRule substring )
    {
        if ( locked )
        {
            throw new UnsupportedOperationException( I18n.err( I18n.ERR_04441, getName() ) );
        }

        if ( !isReadOnly )
        {
            this.substring = substring;
            this.substringOid = substring.getOid();
        }
    }


    /**
     * Update the associated Substring MatchingRule, even if the SchemaObject is readOnly
     *
     * @param newSubstring The Substr MR for this AttributeType
     */
    public void updateSubstring( MatchingRule newSubstring )
    {
        if ( locked )
        {
            throw new UnsupportedOperationException( I18n.err( I18n.ERR_04441, getName() ) );
        }

        this.substring = newSubstring;
        this.substringOid = newSubstring.getOid();
    }


    /**
     * Checks to see if this AttributeType is the ancestor of another
     * attributeType.
     *
     * @param descendant the perspective descendant to check
     * @return true if the descendant is truly a derived from this AttributeType
     */
    public boolean isAncestorOf( AttributeType descendant )
    {
        if ( ( descendant == null ) || this.equals( descendant ) )
        {
            return false;
        }

        return isAncestorOrEqual( this, descendant );
    }


    /**
     * Checks to see if this AttributeType is the descendant of another
     * attributeType.
     *
     * @param ancestor the perspective ancestor to check
     * @return true if this AttributeType truly descends from the ancestor
     */
    public boolean isDescendantOf( AttributeType ancestor )
    {
        if ( ( ancestor == null ) || equals( ancestor ) )
        {
            return false;
        }

        return isAncestorOrEqual( ancestor, this );
    }


    /**
     * Recursive method which checks to see if a descendant is really an ancestor or if the two
     * are equal.
     *
     * @param ancestor the possible ancestor of the descendant
     * @param descendant the possible descendant of the ancestor
     * @return true if the ancestor equals the descendant or if the descendant is really
     * a subtype of the ancestor. otherwise false
     */
    private boolean isAncestorOrEqual( AttributeType ancestor, AttributeType descendant )
    {
        if ( ( ancestor == null ) || ( descendant == null ) )
        {
            return false;
        }

        if ( ancestor.equals( descendant ) )
        {
            return true;
        }

        return isAncestorOrEqual( ancestor, descendant.getSuperior() );
    }


    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        return objectType + " " + DescriptionUtils.getDescription( this );
    }


    /**
     * {@inheritDoc}
     */
    public AttributeType copy()
    {
        AttributeType copy = new AttributeType( oid );

        // Copy the SchemaObject common data
        copy.copy( this );

        // Copy the canUserModify flag
        copy.canUserModify = canUserModify;

        // Copy the isCollective flag
        copy.isCollective = isCollective;

        // Copy the isSingleValue flag
        copy.isSingleValued = isSingleValued;

        // Copy the USAGE type
        copy.usage = usage;

        // All the references to other Registries object are set to null,
        // all the OIDs are copied
        // The EQUALITY MR
        copy.equality = null;
        copy.equalityOid = equalityOid;

        // The ORDERING MR
        copy.ordering = null;
        copy.orderingOid = orderingOid;

        // The SUBSTR MR
        copy.substring = null;
        copy.substringOid = substringOid;

        // The SUP AT
        copy.superior = null;
        copy.superiorOid = superiorOid;

        // The SYNTAX
        copy.syntax = null;
        copy.syntaxOid = syntaxOid;
        copy.syntaxLength = syntaxLength;

        return copy;
    }


    /**
     * {@inheritDoc}
     */
    public void clear()
    {
        // Clear the common elements
        super.clear();

        // Clear the references
        equality = null;
        ordering = null;
        substring = null;
        superior = null;
        syntax = null;
    }


    /**
     * {@inheritDoc}
     */
    public boolean equals( Object o )
    {
        if ( !super.equals( o ) )
        {
            return false;
        }

        if ( !( o instanceof AttributeType ) )
        {
            return false;
        }

        AttributeType that = ( AttributeType ) o;

        // The COLLECTIVE
        if ( isCollective != that.isCollective )
        {
            return false;
        }

        // The SINGLE_VALUE
        if ( isSingleValued != that.isSingleValued )
        {
            return false;
        }

        // The NO_USER_MODIFICATION
        if ( canUserModify != that.canUserModify )
        {
            return false;
        }

        // The USAGE
        if ( usage != that.usage )
        {
            return false;
        }

        // The equality
        if ( !compareOid( equalityOid, that.equalityOid ) )
        {
            return false;
        }

        if ( equality != null )
        {
            if ( !equality.equals( that.equality ) )
            {
                return false;
            }
        }
        else
        {
            if ( that.equality != null )
            {
                return false;
            }
        }

        // The ordering
        if ( !compareOid( orderingOid, that.orderingOid ) )
        {
            return false;
        }

        if ( ordering != null )
        {
            if ( !ordering.equals( that.ordering ) )
            {
                return false;
            }
        }
        else
        {
            if ( that.ordering != null )
            {
                return false;
            }
        }

        // The substring
        if ( !compareOid( substringOid, that.substringOid ) )
        {
            return false;
        }

        if ( substring != null )
        {
            if ( !substring.equals( that.substring ) )
            {
                return false;
            }
        }
        else
        {
            if ( that.substring != null )
            {
                return false;
            }
        }

        // The superior
        if ( !compareOid( superiorOid, that.superiorOid ) )
        {
            return false;
        }

        if ( superior != null )
        {
            if ( !superior.equals( that.superior ) )
            {
                return false;
            }
        }
        else
        {
            if ( that.superior != null )
            {
                return false;
            }
        }

        // The syntax
        if ( !compareOid( syntaxOid, that.syntaxOid ) )
        {
            return false;
        }

        if ( syntaxLength != that.syntaxLength )
        {
            return false;
        }

        if ( syntax == null )
        {
            return that.syntax == null;
        }

        if ( syntax.equals( that.syntax ) )
        {
            return syntaxLength == that.syntaxLength;
        }
        else
        {
            return false;
        }
    }
    
    
    public void unlock()
    {
        locked = false;
    }
}
