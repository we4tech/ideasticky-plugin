/* $Id: GlobalConfiguration.java 116 2006-10-02 05:27:41Z  $ */
/*
 * Copyright (c) 2006, nhm tanveer hossain khan (hasan)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 *    0 Redistributions of source code must retain the above copyright notice,
 *      this list of conditions and the following disclaimer.
 *
 *    0 Redistributions in binary form must reproduce the above copyright notice,
 *      this list of conditions and the following disclaimer in the documentation
 *      and/or other materials provided with the distribution.
 *
 *    0 Neither the name of the <ORGANIZATION> nor the names of its contributors
 *      may be used to endorse or promote products derived from this software without
 *      specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
 * CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT
 * OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY
 * OF SUCH DAMAGE.
 ******************************************************************************
 * $LastChangedBy: $
 * $LastChangedDate: 2006-10-02 05:27:41Z $
 * $LastChangedRevision: 116 $
 ******************************************************************************
*/
package com.we4tech.ideaPlugin.sticky.storage;

public interface GlobalConfiguration {

    // storage file name configuration

    /**
     * return path of Address book storage file.
     * @return address book path
     */
    public String getAddressBookStorageFile();

    /**
     * Change address book location
     * @param file
     */
    public void setAddressBookStorageFile( String file );

    /**
     * Return path of sticky storage file. so it could possible to move else where
     * @return path of sticky storage file
     */
    public String getStickyStorageFile();

    /**
     * change default location of sticky path
     * @param file
     */
    public void setStickyStorageFile( String file );

    // sticky configuration

    /**
     * return ip address to connect sticky itself.
     * @return ip address or host name
     */
    public String getIPAddress();

    /**
     * change ip address or host name
     * @param ip or host can be used
     */
    public void setIPAddress( String ip );

    /**
     * return port to which sticky itself will be plugged on
     * @return int port address
     */
    public int getPort();

    /**
     * set available port number or it will use default 12345
     * @param port
     */
    public void setPort( int port );

    // mail configuration

    /**
     * return host/ip address of SMTP server by which it will deliver sticky over smtp
     * @return smtp server host or ip address
     */
    public String getSMTPHost();

    /**
     * change smtp host or ip address
     * @param host or ip address can be used
     */
    public void setSMTPHost( String host );

    /**
     * return smtp server port or it will use default 25
     * @return int smtp port
     */
    public int getSMTPPort();

    /**
     * change smtp default 25 port number
     * @param port int value will be used
     */
    public void setSMTPPort( int port );

    /**
     * Sometimes SMTP user and sender email does not match.
     * to resolve this uncertain issue.
     * @param from
     */
    public void setMailFrom( String from );

    /**
     * return Sticky identity
     * @return email address
     */
    public String getMailFrom();

    /**
     * return smtp user name NOTE: no encryption is used
     * @return String smtp user
     */
    public String getSMTPUser();

    /**
     * change smtp user name
     * @param user string value is used
     */
    public void setSMTPUser( String user );

    /**
     * return smtp password NOTE: plain text is used to store no encryption
     * @return smtp user password
     */
    public String getSMTPPassword();

    /**
     * change smtp password NOTE: plain text is used no encryption
     * @param pass
     */
    public void setSMTPPassword( String pass );

    /**
     * to store all changes on disk
     * @param changed if content really changes
     */
    public void saveConfiguration( boolean changed );

}
