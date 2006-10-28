/* $Id: GlobalConfigurationManager.java 116 2006-10-02 05:27:41Z  $ */
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

import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;
import org.jetbrains.annotations.NonNls;

import java.util.Properties;
import java.io.FileInputStream;
import java.io.File;
import java.io.FileOutputStream;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.application.ApplicationManager;
import com.we4tech.ideaPlugin.sticky.network.StickyServer;

public class GlobalConfigurationManager
    implements GlobalConfiguration, ApplicationComponent {

    private Logger LOG = LogManager.getLogger( GlobalConfigurationManager.class );
    private final boolean DEBUG = LOG.isDebugEnabled();
    private final boolean INFO = LOG.isInfoEnabled();

    private static final String GLOBAL_CONFIG_FILE = "global_config.properties";
    private Properties mGlobalConfigProperties;

    private final String PROP_ADDRESS_BOOK_STORAGE = "storage.address.book";
    private final String PROP_STICKY_STORAGE = "storage.sticky";
    private final String PROP_IP_ADDRESS = "server.ip.address";
    private final String PROP_PORT = "server.port";
    private final String PROP_SMTP_HOST = "smtp.host";
    private final String PROP_SMTP_PORT = "smtp.port";
    private final String PROP_MAIL_SENDER = "smtp.mail.sender";
    private final String PROP_SMTP_USER = "smtp.user";
    private final String PROP_SMTP_PASSWORD = "smtp.password";

    public static GlobalConfiguration getInstance() {
        return ApplicationManager.getApplication()
               .getComponent( GlobalConfiguration.class );
    }

    @NonNls
    public String getComponentName() {
        return "GlobalConfigurationManager";
    }

    public void initComponent() {
        if (INFO)
            LOG.info( "init. global configuration manager" );
        _loadProperties();
    }

    public void disposeComponent() {
        if (INFO)
            LOG.info( "Disposing component " );
        mGlobalConfigProperties = null;
    }

    private synchronized void _loadProperties() {
        if (DEBUG)
            LOG.debug( "loading stroage" );
        try {
            boolean newFile = false;
            if (!new File( _getGlobalFile() ).exists()) {
                new File( _getGlobalFile() ).createNewFile();
                newFile = true;
            }
            mGlobalConfigProperties = new Properties( );
            FileInputStream inStream = new FileInputStream( _getGlobalFile() );
            mGlobalConfigProperties.load( inStream );
            inStream.close();

            if (newFile) {
                mGlobalConfigProperties.setProperty( PROP_ADDRESS_BOOK_STORAGE,
                                                     System.getProperty("user.home")+File.separator+
                                                     ".ideaSticky"+File.separator+"FRIEND_LIST.properties" );
                mGlobalConfigProperties.setProperty( PROP_STICKY_STORAGE,
                                                     System.getProperty("user.home")+File.separator+
                                                     ".ideaSticky"+File.separator+"STICKY_RESOURCE.properties" );
                mGlobalConfigProperties.setProperty( PROP_PORT, "12345" );
                mGlobalConfigProperties.setProperty( PROP_SMTP_PORT, "25" );

                _saveConfiguration();

            }

        }
        catch( Exception e ) {
            LOG.warn( e );
        }
    }

    private String _getGlobalFile() {

        String gFile = new String( GLOBAL_CONFIG_FILE );
        // add user profile
        String userProfile = System.getProperty("user.home")+File.separator+".ideaSticky"+File.separator;
        if (!new File( userProfile ).exists())
            new File( userProfile ).mkdir();
        gFile =  userProfile + gFile;
        if (DEBUG)
            LOG.debug( "GlobalconfigurationLocation: "+gFile );
        return gFile;
    }

    private synchronized void _saveConfiguration() {
        if (DEBUG)
            LOG.debug( "saving configuration" );

        try {
            FileOutputStream out = new FileOutputStream( _getGlobalFile() );
            mGlobalConfigProperties.store( out, "GLOBAL CONFIGURATION FILE");
            out.close();
        }
        catch( Exception e ) {
            LOG.warn( e );
        }
    }

    // storage file name configuration
    public String getAddressBookStorageFile() {
        return mGlobalConfigProperties.getProperty( PROP_ADDRESS_BOOK_STORAGE );
    }

    public String getStickyStorageFile() {
        return mGlobalConfigProperties.getProperty( PROP_STICKY_STORAGE );
    }

    // sticky configuration
    public String getIPAddress() {
        return mGlobalConfigProperties.getProperty( PROP_IP_ADDRESS );
    }

    public int getPort() {
        return Integer.parseInt( mGlobalConfigProperties.getProperty( PROP_PORT ) );
    }

    // mail configuration
    public String getSMTPHost() {
        return mGlobalConfigProperties.getProperty( PROP_SMTP_HOST );
    }

    public int getSMTPPort() {
        return Integer.parseInt( mGlobalConfigProperties.getProperty( PROP_SMTP_PORT ) );
    }

    public void setSMTPPort(int port) {
        mGlobalConfigProperties.setProperty( PROP_SMTP_PORT, port+"" );
    }

    public void setMailFrom(String from) {
        mGlobalConfigProperties.setProperty( PROP_MAIL_SENDER, from );
    }

    public String getMailFrom() {
        return mGlobalConfigProperties.getProperty( PROP_MAIL_SENDER );
    }

    public String getSMTPUser() {
        return mGlobalConfigProperties.getProperty( PROP_SMTP_USER );
    }

    public String getSMTPPassword() {
        return mGlobalConfigProperties.getProperty( PROP_SMTP_PASSWORD );
    }

    public void setAddressBookStorageFile(String file) {
        mGlobalConfigProperties.setProperty( PROP_ADDRESS_BOOK_STORAGE, file );
    }

    public void setStickyStorageFile(String file) {
        mGlobalConfigProperties.setProperty( PROP_STICKY_STORAGE, file );
    }

    public void setIPAddress(String ip) {
        mGlobalConfigProperties.setProperty( PROP_IP_ADDRESS, ip );
    }

    public void setPort(int port) {
        mGlobalConfigProperties.setProperty( PROP_PORT, port+"" );
    }

    public void setSMTPHost(String host) {
        mGlobalConfigProperties.setProperty( PROP_SMTP_HOST, host );
    }

    public void setSMTPUser(String user) {
        mGlobalConfigProperties.setProperty( PROP_SMTP_USER, user );
    }

    public void setSMTPPassword(String pass) {
        mGlobalConfigProperties.setProperty( PROP_SMTP_PASSWORD, pass );
    }

    public void saveConfiguration( boolean changed ) {
        _saveConfiguration();
        _reloadConfiguration();
    }

    private void _reloadConfiguration() {
        if (INFO)
            LOG.info( "reloading global configuration related services" );
        try {
            StickyServer ss = ApplicationManager.getApplication().getComponent( StickyServer.class );
            if (DEBUG)
                LOG.debug( "disconnecting previous connection" );
            ss.disconnect();

            if (DEBUG)
                LOG.debug( "connecting to new configuration" );
            ss.connect();
        }
        catch( Exception e ) {
            LOG.warn( e );
        }
    }
}
