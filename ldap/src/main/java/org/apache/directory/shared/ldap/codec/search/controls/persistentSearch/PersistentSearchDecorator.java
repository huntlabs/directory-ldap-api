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
package org.apache.directory.shared.ldap.codec.search.controls.persistentSearch;


import java.nio.ByteBuffer;

import org.apache.directory.shared.asn1.EncoderException;
import org.apache.directory.shared.asn1.ber.tlv.TLV;
import org.apache.directory.shared.asn1.ber.tlv.UniversalTag;
import org.apache.directory.shared.asn1.ber.tlv.Value;
import org.apache.directory.shared.i18n.I18n;
import org.apache.directory.shared.ldap.codec.controls.ControlDecorator;
import org.apache.directory.shared.ldap.codec.search.controls.ChangeType;


/**
 * A persistence search object
 * 
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class PersistentSearchDecorator extends ControlDecorator
{
    /** A temporary storage for a psearch length */
    private int psearchSeqLength;


    /**
     * Default constructor
     *
     */
    public PersistentSearchDecorator()
    {
        super( new PersistentSearch(), new PersistentSearchDecoder() );
    }


    /**
     * Compute the PagedSearchControl length, which is the sum
     * of the control length and the value length.
     * 
     * <pre>
     * PersistentSearchDecorator value length :
     * 
     * 0x30 L1 
     *   | 
     *   +--> 0x02 0x0(1-4) [0..2^31-1] (changeTypes) 
     *   +--> 0x01 0x01 [0x00 | 0xFF] (changeOnly) 
     *   +--> 0x01 0x01 [0x00 | 0xFF] (returnRCs)
     * </pre> 
     */
    public int computeLength()
    {
        int changeTypesLength = 1 + 1 + Value.getNbBytes( ( ( PersistentSearch ) getDecorated() ).getChangeTypes() );
        int changesOnlyLength = 1 + 1 + 1;
        int returnRCsLength = 1 + 1 + 1;

        psearchSeqLength = changeTypesLength + changesOnlyLength + returnRCsLength;
        int valueLength = 1 + TLV.getNbBytes( psearchSeqLength ) + psearchSeqLength;

        // Call the super class to compute the global control length
        return super.computeLength( valueLength );
    }


    /**
     * Encodes the persistent search control.
     * 
     * @param buffer The encoded sink
     * @return A ByteBuffer that contains the encoded PDU
     * @throws EncoderException If anything goes wrong.
     */
    public ByteBuffer encode( ByteBuffer buffer ) throws EncoderException
    {
        if ( buffer == null )
        {
            throw new EncoderException( I18n.err( I18n.ERR_04023 ) );
        }

        // Encode the Control envelop
        super.encode( buffer );
        
        // Encode the OCTET_STRING tag
        buffer.put( UniversalTag.OCTET_STRING.getValue() );
        buffer.put( TLV.getBytes( valueLength ) );

        // Now encode the PagedSearch specific part
        buffer.put( UniversalTag.SEQUENCE.getValue() );
        buffer.put( TLV.getBytes( psearchSeqLength ) );

        Value.encode( buffer, ( ( PersistentSearch ) getDecorated() ).getChangeTypes() );
        Value.encode( buffer, ( ( PersistentSearch ) getDecorated() ).isChangesOnly() );
        Value.encode( buffer, ( ( PersistentSearch ) getDecorated() ).isReturnECs() );
        
        return buffer;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public byte[] getValue()
    {
        if ( getDecorated().getValue() == null )
        {
            try
            { 
                computeLength();
                ByteBuffer buffer = ByteBuffer.allocate( valueLength );
                
                // Now encode the PagedSearch specific part
                buffer.put( UniversalTag.SEQUENCE.getValue() );
                buffer.put( TLV.getBytes( psearchSeqLength ) );

                Value.encode( buffer, ( ( PersistentSearch ) getDecorated() ).getChangeTypes() );
                Value.encode( buffer, ( ( PersistentSearch ) getDecorated() ).isChangesOnly() );
                Value.encode( buffer, ( ( PersistentSearch ) getDecorated() ).isReturnECs() );

                getDecorated().setValue( buffer.array() );
            }
            catch ( Exception e )
            {
                return null;
            }
        }
        
        return getDecorated().getValue();
    }
}