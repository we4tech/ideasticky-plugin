/* $Id: StickyStorageManagerImpl.java 117 2006-10-02 19:08:17Z  $ */
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
 * $LastChangedDate: 2006-10-02 19:08:17Z $
 * $LastChangedRevision: 117 $
 ******************************************************************************
*/
package com.we4tech.ideaPlugin.sticky.storage.impl;

import com.we4tech.ideaPlugin.sticky.data.Sticky;
import com.we4tech.ideaPlugin.sticky.storage.StickyStorageManager;
import com.we4tech.ideaPlugin.sticky.helper.UIHelper;
import com.we4tech.ideaPlugin.sticky.helper.StringHelper;

import java.util.*;
import java.util.List;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.awt.*;

import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * Sticky Storage manager performs create/edit/delete sticky object
 * from specific storage location which is defined in construction
 *
 * @author nhm tanveer hossain khan (hasan)
 * @version 1.0-1
 * @since 1.0
 */
public class StickyStorageManagerImpl implements StickyStorageManager {

    private Logger LOG = LogManager.getLogger( StickyStorageManagerImpl.class );
    private final boolean DEBUG = LOG.isDebugEnabled();
    private final boolean INFO = LOG.isInfoEnabled();

    /*
     * prefix for sticky property name
     */
    private final String STICKY_PROPERTY_PREFIX = "sticky.";

    /**
     * sticky storage properties
     */
    protected Properties mStickyStorageProperties = null;
    private File mStoreFile;

    /**
     * Default constructor,
     * read properties from file system.
     */
    public StickyStorageManagerImpl( File storeFile ) {
        this.mStoreFile = storeFile;
        if (DEBUG)
            LOG.debug("Sticky storage managerImpl store file "+storeFile.toString());
        _loadStorageSource();
    }

    /*
     * lookup default file, if found load it as properties resource.
     */
    protected synchronized void _loadStorageSource() {
        if ( DEBUG )
            LOG.debug( "Load storage source - "+mStoreFile );
        try {
            mStickyStorageProperties = new Properties();
            if ( !mStoreFile.exists() )
                mStoreFile.createNewFile();

            FileInputStream inStream = new FileInputStream( mStoreFile );
            mStickyStorageProperties.load( inStream );
            inStream.close();
        }
        catch( Exception e ) {
            UIHelper.showErrorMessage( null, e );
            LOG.warn( e );
        }
    }

    /*
     * let properties to be stored on disk by invoking "store"
     * with output file name (default storage location is in use)
     */
    protected synchronized void _saveOnStorageSource() {
        if ( DEBUG )
            LOG.debug( "save changes on storage" );
        try {
            FileOutputStream outStream = new FileOutputStream( mStoreFile );
            mStickyStorageProperties.store( outStream, "Sticky storage" );
            outStream.close();
        }
        catch( Exception e ) {
            UIHelper.showErrorMessage( null, e );
            LOG.warn( e );
        }
    }

    public  synchronized void saveSticky( Sticky sticky ) {
        if ( DEBUG )
            LOG.debug( "save sticky configuration" );

        String stickyPrefix =  STICKY_PROPERTY_PREFIX + sticky.getStickyId() + ".";

        // store sticky id
        String key = stickyPrefix + "id";
        String value = sticky.getStickyId();
        mStickyStorageProperties.setProperty( key, value );

        // store sticky title
        key = stickyPrefix + "title";
        value = sticky.getTitle();
        mStickyStorageProperties.setProperty( key, value );

        // store sticky content
        key = stickyPrefix + "content";
        value = StringHelper.removeCrLf( sticky.getContent() );
        mStickyStorageProperties.setProperty( key, value );

        // store sticky location
        key = stickyPrefix + "location";
        value = sticky.getLocation().getX()+","+sticky.getLocation().getY() ;
        mStickyStorageProperties.setProperty( key, value );

        // store sticky window size
        key = stickyPrefix + "size";
        value = sticky.getSize().getWidth()+","+sticky.getSize().getHeight();
        mStickyStorageProperties.setProperty( key, value );

        // store sticky background color
        key = stickyPrefix + "backgroundColor";
        value = sticky.getBackgroundColor().getRed()+","+
                sticky.getBackgroundColor().getGreen()+","+
                sticky.getBackgroundColor().getBlue();
        mStickyStorageProperties.setProperty( key, value );

        // store minimized
        key = stickyPrefix + "minimized";
        value = sticky.isMinimized()+"";
        mStickyStorageProperties.setProperty( key, value );

        if( sticky.getLinkedUpFile() != null ) {
            // store linked file
            key = stickyPrefix + "linkedFile";
            value = sticky.getLinkedUpFile();
            mStickyStorageProperties.setProperty( key, value );

            // store linked file index
            key = "linkedFile_"+ DigestUtils.md5Hex( sticky.getLinkedUpFile() )+"_"+sticky.getStickyId();
            value = sticky.getStickyId();
            mStickyStorageProperties.setProperty( key, value );
        }

        // storage property
        key = stickyPrefix + "projectStorage";
        value = sticky.isProjectStorage()+"";
        mStickyStorageProperties.setProperty( key, value );

        // store changes
        _saveOnStorageSource();

    }

    public  synchronized void deleteStickyById(String stickyId) {
        if ( DEBUG )
            LOG.debug( "delete sticky by sticky id " );

        String stickyPrefix =  STICKY_PROPERTY_PREFIX + stickyId + ".";

        Sticky sticky = getStickyById( stickyId );

        if (DEBUG)
            LOG.debug( "Sticky - "+sticky );

        // need to remove the following keys
        String stickyIdKey = stickyPrefix + "id";
        String stickyTitleKey = stickyPrefix + "title";
        String stickyContentKey = stickyPrefix + "content";
        String stickyLocationKey = stickyPrefix + "location";
        String stickySizeKey = stickyPrefix + "size";
        String stickyBackgroundColor = stickyPrefix + "backgroundColor";
        String stickyMinimized = stickyPrefix + "minimized";
        String stickyLinkedFile = stickyPrefix + "linkedFile";
        String stickyProjectStorage = stickyPrefix + "projectStorage";

        // remove properties
        mStickyStorageProperties.remove( stickyIdKey );
        mStickyStorageProperties.remove( stickyTitleKey );
        mStickyStorageProperties.remove( stickyContentKey );
        mStickyStorageProperties.remove( stickyLocationKey );
        mStickyStorageProperties.remove( stickySizeKey );
        mStickyStorageProperties.remove( stickyBackgroundColor );
        mStickyStorageProperties.remove( stickyMinimized );
        mStickyStorageProperties.remove( stickyLinkedFile );
        if (sticky.getLinkedUpFile() != null ) {
            String stickyLinkedFileRef = "linkedFile_"+DigestUtils.md5Hex( sticky.getLinkedUpFile() )+"_"+stickyId;
            mStickyStorageProperties.remove( stickyLinkedFileRef );
        }
        mStickyStorageProperties.remove( stickyProjectStorage );

        // store changes
        _saveOnStorageSource();
    }

    public List<String> getStickiesId() {
        if ( DEBUG )
            LOG.debug( "retrieve collection of sticky id " );
        List<String> list = null;
        try {
            Set keys = mStickyStorageProperties.keySet();
            list = new Vector<String>();
            for ( Object key: keys )
                if (key.toString().endsWith(".id"))
                    list.add(mStickyStorageProperties.getProperty(key.toString()));
        }
        catch( Exception e ) {
            LOG.warn( e );
        }

        return list;
    }

    public Sticky getStickyById(String stickyId) {
        if ( DEBUG )
            LOG.debug( "retrieve sticky by sticky id" );
        Sticky sticky = null;
        try {
            String stickyPrefix =  STICKY_PROPERTY_PREFIX + stickyId + ".";
            boolean stickyIdPropertyFound = mStickyStorageProperties.getProperty( stickyPrefix + "id" ) != null;
            if ( stickyIdPropertyFound )
                sticky = _buildSticky(stickyPrefix);
        }
        catch( Exception e ) {
            LOG.warn( e );
        }

        return sticky;
    }

     public List<Sticky> getStickyByLinkedFile( String linkedFile ) {
        if (DEBUG)
            LOG.debug( "retrieve sticky by Linked file" );
        List<Sticky> list = new Vector<Sticky>();
        try {
            String md5LinkedFile = DigestUtils.md5Hex( linkedFile );
            Set keys = mStickyStorageProperties.keySet();
            for (Object key : keys ) {
                String[] splitKey = key.toString().split("_");
                if (splitKey.length > 2 ) {
                    String file = splitKey[1];
                    if (file.equals( md5LinkedFile ) ) {
                        Sticky sticky = getStickyById( mStickyStorageProperties.getProperty( key.toString() ) );
                        list.add( sticky );
                    }
                }
            }
        }
        catch( Exception e ) {
            LOG.warn( e );
        }
        return list;
    }

    private Sticky _buildSticky(String stickyPrefix) {
        Sticky sticky = new Sticky();
        sticky.setStickyId( mStickyStorageProperties.getProperty( stickyPrefix+"id" ) );
        sticky.setTitle( mStickyStorageProperties.getProperty( stickyPrefix+"title" ) );
        sticky.setContent( StringHelper.addLineBreak( mStickyStorageProperties.getProperty( stickyPrefix+"content" ) ) );
        sticky.setLocation( StringHelper.getStringToPoint( mStickyStorageProperties.getProperty( stickyPrefix+"location" ) ) );
        sticky.setSize( StringHelper.getStringToDimension( mStickyStorageProperties.getProperty( stickyPrefix+"size" ) ) );
        sticky.setBackgroundColor( StringHelper.getStringToColor( mStickyStorageProperties.getProperty( stickyPrefix +"backgroundColor" ) ) );
        sticky.setMinimized( _getStringToBoolean( mStickyStorageProperties.getProperty( stickyPrefix+"minimized") ) );
        sticky.setLinkedUpFile( mStickyStorageProperties.getProperty( stickyPrefix+"linkedFile" ) );
        sticky.setProjectStorage( _getStringToBoolean( mStickyStorageProperties.getProperty( stickyPrefix+"projectStorage") ) );

        return sticky;
    }

    private boolean _getStringToBoolean(String v) {
        boolean b = false;
        try {
            if ( "true".equalsIgnoreCase( v ))
                b = true;
        }
        catch( Exception e ) {
            LOG.warn( e );
        }
        return b;
    }
}
