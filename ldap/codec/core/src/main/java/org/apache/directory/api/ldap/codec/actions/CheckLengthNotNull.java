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
package org.apache.directory.api.ldap.codec.actions;


import org.apache.directory.api.asn1.DecoderException;
import org.apache.directory.api.asn1.ber.grammar.GrammarAction;
import org.apache.directory.api.asn1.ber.tlv.TLV;
import org.apache.directory.api.i18n.I18n;
import org.apache.directory.api.ldap.codec.api.LdapMessageContainer;
import org.apache.directory.api.ldap.model.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The action used to check that the TLV length is not null
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class CheckLengthNotNull extends GrammarAction<LdapMessageContainer<Message>>
{
    /** The logger */
    private static final Logger LOG = LoggerFactory.getLogger( CheckLengthNotNull.class );


    /**
     * Instantiates the action.
     */
    public CheckLengthNotNull()
    {
        super( "Check that the length is not null" );
    }


    /**
     * {@inheritDoc}
     */
    public void action( LdapMessageContainer<Message> container ) throws DecoderException
    {
        TLV tlv = container.getCurrentTLV();
        int expectedLength = tlv.getLength();

        // The Length should be null
        if ( expectedLength == 0 )
        {
            String msg = I18n.err( I18n.ERR_08213_NULL_CONTROL_LENGTH );
            LOG.error( msg );

            // This will generate a PROTOCOL_ERROR
            throw new DecoderException( msg );
        }
    }
}
