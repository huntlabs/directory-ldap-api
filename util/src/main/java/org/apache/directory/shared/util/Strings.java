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
package org.apache.directory.shared.util;


import java.io.UnsupportedEncodingException;

/**
 * Various string manipulation methods that are more efficient then chaining
 * string operations: all is done in the same buffer without creating a bunch of
 * string objects.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class Strings
{

    /**
     * Helper function that dump an array of bytes in hex form
     *
     * @param buffer The bytes array to dump
     * @return A string representation of the array of bytes
     */
    public static String dumpBytes( byte[] buffer )
    {
        if ( buffer == null )
        {
            return "";
        }

        StringBuffer sb = new StringBuffer();

        for ( int i = 0; i < buffer.length; i++ )
        {
            sb.append( "0x" ).append( ( char ) ( CharConstants.HEX_CHAR[( buffer[i] & 0x00F0 ) >> 4] ) ).append(
                ( char ) ( CharConstants.HEX_CHAR[buffer[i] & 0x000F] ) ).append( " " );
        }

        return sb.toString();
    }

    /**
     * Test if the current character is a digit &lt;digit> ::= '0' | '1' | '2' |
     * '3' | '4' | '5' | '6' | '7' | '8' | '9'
     *
     * @param car the character to test
     *
     * @return <code>true</code> if the character is a Digit
     */
    public static boolean isDigit( char car )
    {
        return ( car >= '0' ) && ( car <= '9' );
    }

    /**
     * Test if the current byte is an Alpha character :
     * &lt;alpha> ::= [0x41-0x5A] | [0x61-0x7A]
     *
     * @param c The byte to test
     *
     * @return <code>true</code> if the byte is an Alpha
     *         character
     */
    public static boolean isAlpha( byte c )
    {
        return ( ( c > 0 ) && ( c <= 127 ) && CharConstants.ALPHA[c] );
    }

    /**
     * Test if the current character is an Alpha character :
     * &lt;alpha> ::= [0x41-0x5A] | [0x61-0x7A]
     *
     * @param c The char to test
     *
     * @return <code>true</code> if the character is an Alpha
     *         character
     */
    public static boolean isAlpha( char c )
    {
        return ( ( c > 0 ) && ( c <= 127 ) && CharConstants.ALPHA[c] );
    }

    /**
     * Check if the current char is in the unicodeSubset : all chars but
     * '\0', '(', ')', '*' and '\'
     *
     * @param c The char to check
     * @return True if the current char is in the unicode subset
     */
    public static boolean isUnicodeSubset( char c )
    {
        return ( ( c > 127 ) || CharConstants.UNICODE_SUBSET[c] );
    }

    /**
     * <p>
     * Checks if a String is empty ("") or null.
     * </p>
     *
     * <pre>
     *  StringUtils.isEmpty(null)      = true
     *  StringUtils.isEmpty(&quot;&quot;)        = true
     *  StringUtils.isEmpty(&quot; &quot;)       = false
     *  StringUtils.isEmpty(&quot;bob&quot;)     = false
     *  StringUtils.isEmpty(&quot;  bob  &quot;) = false
     * </pre>
     *
     * <p>
     * NOTE: This method changed in Lang version 2.0. It no longer trims the
     * String. That functionality is available in isBlank().
     * </p>
     *
     * @param str the String to check, may be null
     * @return <code>true</code> if the String is empty or null
     */
    public static boolean isEmpty( String str )
    {
        return str == null || str.length() == 0;
    }

    /**
     * Checks if a bytes array is empty or null.
     *
     * @param bytes The bytes array to check, may be null
     * @return <code>true</code> if the bytes array is empty or null
     */
    public static boolean isEmpty( byte[] bytes )
    {
        return bytes == null || bytes.length == 0;
    }

    /**
     * Return an UTF-8 encoded String
     *
     * @param bytes The byte array to be transformed to a String
     * @return A String.
     */
    public static String utf8ToString( byte[] bytes )
    {
        if ( bytes == null )
        {
            return "";
        }

        try
        {
            return new String( bytes, "UTF-8" );
        }
        catch ( UnsupportedEncodingException uee )
        {
            // if this happens something is really strange
            throw new RuntimeException( uee );
        }
    }

    /**
     * Return an UTF-8 encoded String
     *
     * @param bytes The byte array to be transformed to a String
     * @param length The length of the byte array to be converted
     * @return A String.
     */
    public static String utf8ToString( byte[] bytes, int length )
    {
        if ( bytes == null )
        {
            return "";
        }

        try
        {
            return new String( bytes, 0, length, "UTF-8" );
        }
        catch ( UnsupportedEncodingException uee )
        {
            // if this happens something is really strange
            throw new RuntimeException( uee );
        }
    }

    /**
     * Return an UTF-8 encoded String
     *
     * @param bytes  The byte array to be transformed to a String
     * @param start the starting position in the byte array
     * @param length The length of the byte array to be converted
     * @return A String.
     */
    public static String utf8ToString( byte[] bytes, int start, int length )
    {
        if ( bytes == null )
        {
            return "";
        }

        try
        {
            return new String( bytes, start, length, "UTF-8" );
        }
        catch ( UnsupportedEncodingException uee )
        {
            // if this happens something is really strange
            throw new RuntimeException( uee );
        }
    }

    /**
     * Return UTF-8 encoded byte[] representation of a String
     *
     * @param string The string to be transformed to a byte array
     * @return The transformed byte array
     */
    public static byte[] getBytesUtf8( String string )
    {
        if ( string == null )
        {
            return new byte[0];
        }

        try
        {
            return string.getBytes( "UTF-8" );
        }
        catch ( UnsupportedEncodingException uee )
        {
            // if this happens something is really strange
            throw new RuntimeException( uee );
        }
    }
}
