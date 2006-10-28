/* $Id: StickyFriendListStorageManagerImpl.java 109 2006-09-27 13:18:48Z  $ */
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
 * $LastChangedDate: 2006-09-27 13:18:48Z $
 * $LastChangedRevision: 109 $
 ******************************************************************************
*/
package com.we4tech.ideaPlugin.sticky.storage.impl;

import com.we4tech.ideaPlugin.sticky.storage.StickyFriendListStorageManager;
import com.we4tech.ideaPlugin.sticky.storage.GlobalConfigurationManager;
import com.we4tech.ideaPlugin.sticky.data.StickyFriend;
import com.we4tech.ideaPlugin.sticky.helper.UIHelper;
import com.intellij.openapi.components.ApplicationComponent;

import java.util.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.jetbrains.annotations.NonNls;
import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;

public class StickyFriendListStorageManagerImpl implements StickyFriendListStorageManager, ApplicationComponent {

    private Logger LOG = LogManager.getLogger( StickyFriendListStorageManagerImpl.class );
    private final boolean DEBUG = LOG.isDebugEnabled();
    private final boolean INFO = LOG.isInfoEnabled();

    private Properties mFriendListProperties = null;
    private Map<String, String> mFriendNameIdMap = null;

    public StickyFriendListStorageManagerImpl() {
    }

    @NonNls
    public String getComponentName() {
        return "StickyFriendListStorageManager";  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void initComponent() {
        if ( INFO )
            LOG.info( "init component - StickyFriendListStorageManager" );

        try {
            mFriendListProperties = new Properties();
            File f = new File( GlobalConfigurationManager.getInstance().getAddressBookStorageFile() );
            if( !f.exists() )
                f.createNewFile();

            FileInputStream inStream = new FileInputStream( f );
            mFriendListProperties.load( inStream );
            inStream.close();
        }
        catch( Exception e ) {
            LOG.warn( e );
            UIHelper.showErrorMessage( null, e );
        }
    }

    public void disposeComponent() {
        mFriendListProperties = null;
    }


    private void _saveStorage() {
        if ( DEBUG )
            LOG.debug( "saving friend list storage" );

        try {
            FileOutputStream outStream = new FileOutputStream( GlobalConfigurationManager.getInstance().getAddressBookStorageFile() );
            mFriendListProperties.store( outStream, "Stored : "+new java.util.Date() );
            outStream.close();
        }
        catch( Exception e ) {
            LOG.warn( e );
            UIHelper.showErrorMessage( null, e );
        }
    }

    public void saveFriend(StickyFriend friend) {

        if ( DEBUG )
            LOG.debug("saving stick friend");

        // create id
        String key = "friend."+friend.getId()+".id";
        String value = friend.getId();
        mFriendListProperties.setProperty( key, value );

        // create name
        key = "friend."+friend.getId()+".name";
        value = friend.getName();
        mFriendListProperties.setProperty( key, value );

        // create email
        key = "friend."+friend.getId()+".email";
        value= friend.getEmail();
        mFriendListProperties.setProperty( key, value );

        // create ip
        key = "friend."+friend.getId()+".ipAddress";
        value = friend.getIpAddress();
        mFriendListProperties.setProperty( key, value );

        // create port
        key = "friend."+friend.getId()+".port";
        value = friend.getPort()+"";
        mFriendListProperties.setProperty( key, value );

        _saveStorage();

        if ( mFriendNameIdMap == null )
            mFriendNameIdMap = new HashMap<String, String>();
        mFriendNameIdMap.put( friend.getName(), friend.getId() );

    }

    public List<StickyFriend> getFriends() {
        List<StickyFriend> list = new Vector<StickyFriend>();

        if ( DEBUG )
            LOG.debug( "retrieving list of friends" );
        try {
            List<String> ids = _getFriendIds();
            if ( ids != null && ids.size() > 0 )
                for (String id : ids)
                    list.add(getFriend(id));
        }
        catch( Exception e ) {
            LOG.warn( e );
        }

        return list;
    }

    private List<String> _getFriendIds() {
        List<String> list = new Vector<String>();
        if ( DEBUG )
            LOG.debug( "retrieving list of friends id stored on storage " );

        try {
            for (Object o : mFriendListProperties.keySet()) {
                String key = o.toString();

                if (key.endsWith(".id")) {
                    String value = mFriendListProperties.getProperty(key);
                    list.add(value);
                }
            }
        }
        catch( Exception e ) {
            LOG.warn( e );
        }
        return list;
    }

    public StickyFriend getFriend(String friendId) {
        if ( DEBUG )
            LOG.debug( "retrieving friend by friend id - "+friendId );

        StickyFriend friend = null;
        try {
            if ( $( "friend."+friendId+".id" ) != null ) {
                friend = new StickyFriend();

                friend.setId( $( "friend."+friendId+".id" ) );
                friend.setName( $( "friend."+friendId+".name" ) );
                friend.setEmail( $( "friend."+friendId+".email" ) );
                friend.setIpAddress( $("friend."+friendId+".ipAddress" ));
                if ( $("friend."+friendId+".port") != null && $("friend."+friendId+".port").length() > 0 )
                    friend.setPort( Integer.parseInt( $("friend."+friendId+".port") ) );
            }
        }
        catch( Exception e ) {
            LOG.warn( e );
        }
        return friend;
    }

    public StickyFriend getFriendByName(String name) {
        if ( DEBUG )
            LOG.debug( "retrieving friend by name - "+name );

        StickyFriend sf = null;
        try {
            if ( mFriendNameIdMap == null ) {
                mFriendNameIdMap = new HashMap<String, String>();
                List<StickyFriend> list = getFriends();
                for ( StickyFriend f: list )
                    mFriendNameIdMap.put(f.getName(), f.getId());
            }

            if ( mFriendNameIdMap.containsKey( name ))
                sf = getFriend( mFriendNameIdMap.get( name ) );
        }
        catch( Exception e ) {
            LOG.warn( e );
        }

        return sf;
    }

    public void deleteFriend(String friendId) {
        if ( DEBUG )
            LOG.debug( "delete sticky friend by id - "+friendId );

        if ( $("friend."+friendId+".id") != null ) {
            StickyFriend sf = getFriend( friendId );
            $R("friend."+friendId+".id");
            $R("friend."+friendId+".name");
            $R("friend."+friendId+".email");
            $R("friend."+friendId+".ipAddress");
            _saveStorage();
            mFriendNameIdMap.remove( sf.getId() );
        }
    }

    private String $( String key  ) {
        return mFriendListProperties.getProperty( key );
    }

    private void $R( String key ) {
        mFriendListProperties.remove( key );
    }
}
