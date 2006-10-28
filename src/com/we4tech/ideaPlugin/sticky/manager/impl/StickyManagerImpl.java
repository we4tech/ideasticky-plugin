/* $Id: StickyManagerImpl.java 136 2006-10-06 07:08:22Z  $ */
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
 * $LastChangedDate: 2006-10-06 00:08:22 -0700 (Fri, 06 Oct 2006) $
 * $LastChangedRevision: 136 $
 ******************************************************************************
*/
package com.we4tech.ideaPlugin.sticky.manager.impl;

import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;
import org.jetbrains.annotations.NonNls;

import com.we4tech.ideaPlugin.sticky.storage.StickyStorageManager;
import com.we4tech.ideaPlugin.sticky.storage.StickyStorageManagerApplicationComponent;
import com.we4tech.ideaPlugin.sticky.storage.StickyStorageRegister;
import com.we4tech.ideaPlugin.sticky.data.Sticky;
import com.we4tech.ideaPlugin.sticky.ui.StickyPaperUI;
import com.we4tech.ideaPlugin.sticky.ui.MinimizedStickyContainerPanel;
import com.we4tech.ideaPlugin.sticky.event.StickyPaperListener;
import com.we4tech.ideaPlugin.sticky.manager.StickyManager;
import com.we4tech.ideaPlugin.sticky.helper.UIHelper;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.application.ApplicationManager;

import java.util.*;

/**
 * Sticky manager is responsible to load, create, delete, update,
 * minimize, unMinimize sticky papers. every sticky has to fire its
 * specific events to let Sticky manager change configuration.
 *
 * @author nhm tanveer hossain khan (hasan)
 * @version  1.0-2
 * @since  1.0
 *
 * @see StickyPaperListener
 * @see ApplicationComponent
 */
public class StickyManagerImpl
    implements StickyManager, StickyPaperListener, ApplicationComponent {

    private Logger LOG = LogManager.getLogger( StickyManagerImpl.class );
    private final boolean DEBUG = LOG.isDebugEnabled();
    private final boolean INFO = LOG.isInfoEnabled();

    private Map<String,StickyPaperUI> mStickiesMap = null;
    private Map<String, StickyPaperUI> mProjectSpecificStickiesMap = null;

    private boolean mStickyInit = false;
    private boolean mProjectStickyInit = false;
    private boolean mDefaultDisplayMode = true;

    private int mLastPreviewStickyIndex = 0;

    private StickyStorageManager mStickyStorageManager = null;
    private MinimizedStickyContainerPanel mMinimizedStickyContainer = null;
    private StickyPaperUI[] mCacheStickPaperUI = null;

    /**
     * Nothing is initialized on Construction, rather initComponent() is used for this purpose
     */
    public StickyManagerImpl() {
    }

    /**
     * Invoked by IDEA Open API
     * @return String
     */
    @NonNls
    public String getComponentName() {
        return "StickyManager";
    }

    /**
     * init. storage manager and minimized sticky holder
     */
    public void initComponent() {
        if ( INFO )
            LOG.info( "Sticky manager implementation is initializing...");

        mStickiesMap = new HashMap<String,StickyPaperUI>();
        mStickyStorageManager = ApplicationManager
                                .getApplication().getComponent( StickyStorageManagerApplicationComponent.class);
        mMinimizedStickyContainer = new MinimizedStickyContainerPanel( this );

        if ( INFO )
            LOG.info( "starting sticky server" );

    }

    /**
     * destroy stickies storage manager, minimized sticky container
     */
    public void disposeComponent() {
        if ( DEBUG )
            LOG.info( "Dispose StickyManager component" );

        mStickiesMap.clear();
        mStickiesMap = null;
        mStickyInit = false;
        mStickyStorageManager = null;
        mMinimizedStickyContainer = null;
    }

    /**
     * Create new instance of StickyPaperUI object. based on Sticky .
     * it will visiually appear a sticky paper.
     *
     * if project specific storage not available
     *  it will store in global scope.
     *
     * @param sticky
     */
    public StickyPaperUI createStickyPaper( Sticky sticky ) {
        if ( DEBUG )
            LOG.debug( "create new sticky StickyManager component" );

        if (_getProjectSpecificStorage() == null)
            sticky.setProjectStorage( false );

        StickyPaperUI s = new StickyPaperUI( sticky );
        s.setFocusableWindowState( true );
        s.setFocusable( true );
        if (sticky.getLocation() == null)
            s.setLocationByPlatform( true );
        s.setVisible( true );
        s.setEventListener( this );
        sticky.setLocation( s.getLocation() );

        boolean stored = false;
        if (sticky.isProjectStorage()) {
            if (_getProjectSpecificStorage() != null) {
                mProjectSpecificStickiesMap.put( sticky.getStickyId(), s );
                _getProjectSpecificStorage().saveSticky( sticky );
                stored = true;
            }
        }

        if (!stored) {
            mStickiesMap.put( sticky.getStickyId(), s );
            mStickyStorageManager.saveSticky( sticky );
        }

        return s;
    }

    public void loadStickyPapers(boolean display) {
        if ( DEBUG )
            LOG.debug( "load sticky papers (overloaded) from storage visible mode = "+display );

        mDefaultDisplayMode = display;
        loadStickyPapers();
    }

    /**
     * Load sticky configuration from sticky storage manager
     * and create real sticky paper ui. and publish them on desktop.
     */
    public void loadStickyPapers() {
        if ( DEBUG )
            LOG.debug( "loading sticky papers ...." );

        try {
            if (DEBUG)
                LOG.debug( "Sticky storage manager: "+mStickyStorageManager );

            List<String> ids = mStickyStorageManager.getStickiesId();
            for (String id: ids) {
                Sticky s = mStickyStorageManager.getStickyById( id );
                if (s != null) {
                    mStickiesMap.put( s.getStickyId(), _openStickyPaperUI( s, false) );
                }
            }
            mStickyInit = true;

            // enable Snap mode
            // snap mode is when window is deactivated it takes an screen snap
            // and place in the place of real component, it looks alike but it is ligther
            // than lot of Swing components.
//            _enableSnapMode( );
        }
        catch( Exception e ) {
            LOG.warn( e );

        }
    }

    public void loadProjectSpecificStickyPapers(boolean display) {
        if (DEBUG)
            LOG.debug( "loading project specific sticky papers" );
        try {
            mProjectSpecificStickiesMap = new HashMap<String, StickyPaperUI>();
            if (_getProjectSpecificStorage() != null) {
                List<String> ids = _getProjectSpecificStorage().getStickiesId();
                for (String id : ids) {
                    Sticky s = _getProjectSpecificStorage().getStickyById(id);
                    if (s != null) {
                        s.setMinimized(true);
                        StickyPaperUI ui = _openStickyPaperUI(s, display);
                        ui.setFocusableWindowState(true);
                        ui.setFocusable(true);
                        ui.setVisible(display);
                        mProjectSpecificStickiesMap.put(s.getStickyId(), ui);
                    }
                }
                mProjectStickyInit = true;
            }
        }
        catch( Exception e ) {
            LOG.warn( e );

        }
    }

    private void _enableSnapMode() {
        try {
            for ( StickyPaperUI ui: mStickiesMap.values() ) {
                ui.setOffSnapMode( false );
            }
        }
        catch (Exception e ) {
            LOG.warn( e );

        }
    }

    public void hideStickyPapers() {
        if ( DEBUG )
            LOG.debug( "hide sticky papers" );

        try {
            if (mStickyInit) {
                for (StickyPaperUI ui: mStickiesMap.values()) {
                    if (ui != null)
                        stickyMinimized( ui.getSticky() );
                }
            }
            if (mProjectStickyInit)
                for(StickyPaperUI ui: mProjectSpecificStickiesMap.values()) {
                    if (ui != null)
                        stickyMinimized( ui.getSticky() );
                }
        }
        catch( Exception e ) {
            LOG.warn( e );

        }
    }

    public void clearStickyMinimizePanel() {
        mMinimizedStickyContainer.removeAllMinimizedSticky();
    }

    public void showStickyPapers() {
        if ( DEBUG )
            LOG.debug( "show/display sticky papers" );
        try {
            if (mStickyInit) {
                for (StickyPaperUI ui: mStickiesMap.values()) {
                    if (ui != null) {
                        ui.setVisible( true );
                        ui.getSticky().setMinimized( false );
                        ui.setFocusableWindowState( true );
                        ui.setFocusable( true );
                        mStickyStorageManager.saveSticky( ui.getSticky() );
                    }
                }
            }
            else
                loadStickyPapers();

            // hide project specific sticky papers, if project init.
            if (mProjectStickyInit)
                for (StickyPaperUI ui: mProjectSpecificStickiesMap.values()) {
                    if (ui != null) {
                        ui.setVisible( true );
                        ui.getSticky().setMinimized( false );
                        ui.setFocusableWindowState( true );
                        ui.setFocusable( true );
                        _getProjectSpecificStorage().saveSticky( ui.getSticky() );
                    }
                }
            if (DEBUG)
                LOG.debug( "remove all minimized stickies" );
            mMinimizedStickyContainer.removeAllMinimizedSticky();
        }
        catch( Exception e ) {
            LOG.warn( e );

        }
    }

    public void showLinkedSticky(String linkedFile) {
        if (DEBUG)
            LOG.debug( "show linked stickies by ("+linkedFile+")" );

        try {
            List<Sticky> list = mStickyStorageManager.getStickyByLinkedFile( linkedFile );
            for (Sticky sticky : list )
                try {
                    StickyPaperUI ui = mStickiesMap.get(sticky.getStickyId());
                    if (ui != null) {
                        ui.setVisible(true);
                        ui.setFocusableWindowState( true );
                        ui.setFocusable( true );
                    }
                }
                catch (Exception e) {
                    LOG.warn( e );

                }

            list = _getProjectSpecificStorage().getStickyByLinkedFile( linkedFile );
            for (Sticky sticky : list )
                try {
                    StickyPaperUI ui = mProjectSpecificStickiesMap.get(sticky.getStickyId());
                    if (ui != null) {
                        ui.setVisible(true);
                        ui.setFocusableWindowState( true );
                        ui.setFocusable( true );
                    }
                }
                catch (Exception e) {
                    LOG.warn( e );

                }
        }
        catch( Exception e ) {
            LOG.warn( e );
            UIHelper.showErrorMessage( null , e );
        }
    }

    public boolean isInit() {
        return mStickyInit;
    }

    public MinimizedStickyContainerPanel getMinimizedStickyContainer() {
        return mMinimizedStickyContainer;
    }

    public synchronized void previewNextSticky() {
        try {
            _cacheStickyPaperUIArray();

            mLastPreviewStickyIndex +=1;
            if (mLastPreviewStickyIndex > mCacheStickPaperUI.length)
                mLastPreviewStickyIndex = mCacheStickPaperUI.length;

            StickyPaperUI ui = mCacheStickPaperUI[ mLastPreviewStickyIndex-1 ];
            ui.windowCollapse( true );
            ui.setLocation( UIHelper.getCenteredLocation( ui ));
            ui.setFocusableWindowState( true );
            ui.setVisible( true );
            ui.setFocusable( true );

            _hideOtherStickiesExcept( ui.getSticky().getStickyId() );
        }
        catch( Exception e ) {
            LOG.warn( e );

        }
    }

    public void projectGarbageCollect() {
        mProjectSpecificStickiesMap = null;
    }

    private void _cacheStickyPaperUIArray() {
        if (!mStickyInit)
            loadStickyPapers( false );
        if (mCacheStickPaperUI == null ) {
            _populateCacheSticky();
        }
        else {
            int size = 0;
            if (_getProjectSpecificStorage() != null)
                size = mStickiesMap.size() + mProjectSpecificStickiesMap.size();
            else
                size = mStickiesMap.size();
            if (mCacheStickPaperUI.length != size ) {
                _populateCacheSticky();
            }
        }
    }

    private void _populateCacheSticky() {
        Map<String,StickyPaperUI> c = new LinkedHashMap<String, StickyPaperUI>();
        c.putAll( mStickiesMap );
        if (mProjectSpecificStickiesMap != null)
            c.putAll( mProjectSpecificStickiesMap );

        mCacheStickPaperUI = new StickyPaperUI[ c.size() ];
        mCacheStickPaperUI = c.values().toArray( mCacheStickPaperUI );
    }

    private synchronized void previewPreviousSticky() {
        try {
            _cacheStickyPaperUIArray();
            mLastPreviewStickyIndex -=1;
            if (mLastPreviewStickyIndex < 1 )
                mLastPreviewStickyIndex = 1;

            StickyPaperUI ui = mCacheStickPaperUI[ mLastPreviewStickyIndex-1 ];
            ui.windowCollapse( true );
            ui.setLocation( UIHelper.getCenteredLocation( ui ));
            ui.setFocusableWindowState( true );
            ui.setVisible( true );
            ui.setFocusable( true );
            _hideOtherStickiesExcept( ui.getSticky().getStickyId() );
        }
        catch( Exception e ) {
            LOG.warn( e );

        }
    }

    private void _hideOtherStickiesExcept(String stickyId) {
        for (StickyPaperUI ui : mCacheStickPaperUI )
            if (! ui.getSticky().getStickyId().equals(stickyId))
                ui.setVisible(false);
    }

    private StickyPaperUI _getStickyPaperUIByStickyId(String stickyId) {
        for( StickyPaperUI ui : mStickiesMap.values() ) {
            if (ui.getSticky().getStickyId().equals( stickyId ))
                return ui;
        }
        return null;
    }

    private void _previewSticky(StickyPaperUI stickyPaper) {
        stickyPaper.setVisible( true );
    }

    /*
     * Create a new sticky paper UI from Sticky object.
     * it's default visibility can be override by setting up loadStickyPapers( boolean ).
     */
    private StickyPaperUI _openStickyPaperUI(Sticky s, boolean display) {
        if ( DEBUG )
            LOG.debug( "open up a new sticky paper UI for sticky id - "+s.getStickyId() );

        StickyPaperUI ui = new StickyPaperUI( s );
        ui.setOffSnapMode( true );
        if ( !mStickyInit )
            ui.setVisible( mDefaultDisplayMode );
        else
            ui.setVisible( !s.isMinimized() );
        ui.setEventListener( this );
        if ( s.isMinimized() )
            mMinimizedStickyContainer.addMinimizedSticky( s );

        ui.setFocusableWindowState( true );
        ui.setFocusable( true );

        return ui;
    }

    public void stickyDeleted(Sticky s) {
        if ( DEBUG )
            LOG.debug( "Sticky deleted ID: "+s.getStickyId() );

        if (mStickyStorageManager.getStickyById( s.getStickyId()) != null) {
            mStickyStorageManager.deleteStickyById(s.getStickyId());
            mStickiesMap.remove( s.getStickyId() );
        }
        else if (_hasStoredOnProjectScope( s.getStickyId())) {
            _deleteProjectSpecificSticky(s);
            mProjectSpecificStickiesMap.remove( s.getStickyId() );
        }

        mStickiesMap.remove( s.getStickyId() );
    }

    public void stickyUpdated(Sticky s) {
        if ( DEBUG )
            LOG.debug( "sticky content updated ID: "+s.getStickyId() );

        if (_getProjectSpecificStorage() == null)
            mProjectSpecificStickiesMap = null;
        if (s.isProjectStorage()) {
            if (DEBUG)
                LOG.debug( "Selected - project specific storage" );
            if (_getProjectSpecificStorage() != null) {
                _saveProjectSpecificSticky( s );
                if (!mProjectSpecificStickiesMap.containsKey( s.getStickyId())) {
                    mProjectSpecificStickiesMap.put( s.getStickyId(), mStickiesMap.get( s.getStickyId()));
                }
                if (mStickyStorageManager.getStickyById( s.getStickyId()) != null ) {
                    mStickiesMap.remove( s.getStickyId() );
                    mStickyStorageManager.deleteStickyById( s.getStickyId() );
                }
            }
            else {
                s.setProjectStorage( false );
            }
        }
        else {
            if (DEBUG)
                LOG.debug( "selected - none project specific storage" );
            if (_hasStoredOnProjectScope( s.getStickyId() )) {
                _deleteProjectSpecificSticky( s );
                if (mProjectSpecificStickiesMap.containsKey( s.getStickyId()))
                    mProjectSpecificStickiesMap.remove( s.getStickyId() );
            }
            mStickyStorageManager.saveSticky( s );
        }
    }

    private boolean _hasStoredOnProjectScope(String stickyId) {
        boolean state = false;
        try {
            StickyStorageManager ssm = _getProjectSpecificStorage();
            if (ssm != null)
                state = ssm.getStickyById( stickyId ) != null;
        }
        catch( Exception e ) {
            LOG.warn( e );

        }
        return state;
    }

    private void _deleteProjectSpecificSticky(Sticky s) {
        if (DEBUG)
            LOG.debug( "delete project specific sticky" );
        try {
            if (DEBUG)
                LOG.debug( "saving sticky paper - "+s );
            _getProjectSpecificStorage().deleteStickyById( s.getStickyId() );
        }
        catch( Exception e ) {
            LOG.warn( e );

        }
    }

    private StickyStorageManager _getProjectSpecificStorage() {
        StickyStorageManager mng = StickyStorageRegister.getInstance().getProjectStorage();
        return mng;
    }

    private void _saveProjectSpecificSticky(Sticky s) {
        if (DEBUG)
            LOG.debug( "creating project specific sticky" );
        try {
            if (DEBUG)
                LOG.debug( "saving sticky paper - "+s );
            _getProjectSpecificStorage().saveSticky( s );
        }
        catch( Exception e ) {
            LOG.warn( e );

        }
    }

    public void stickyMinimized(Sticky s) {
        if ( DEBUG )
            LOG.debug( "sticky content updated ID: "+s.getStickyId() );

        stickyUpdated( s );
        mMinimizedStickyContainer.addMinimizedSticky( s );
        if (_hasStoredOnProjectScope( s.getStickyId()))
            mProjectSpecificStickiesMap.get( s.getStickyId() ).setVisible( false );
        else
            mStickiesMap.get( s.getStickyId() ).setVisible( false );
    }

    public void stickyUnMinimized(Sticky s) {
        if ( DEBUG )
            LOG.debug( "Stikcy un minimized invoked ");
        StickyPaperUI ui = null;
        if (_hasStoredOnProjectScope( s.getStickyId()))
            ui = mProjectSpecificStickiesMap.get( s.getStickyId() );
        else
            ui = mStickiesMap.get( s.getStickyId() );
        ui.setVisible( true );
        ui.setFocusableWindowState( true );
        ui.setFocusable( true );
        stickyUpdated( s );
    }

    public void hideStickies() {
        hideStickyPapers();
    }

    public void showStickies() {
        showStickyPapers();
    }

    public void showNextSticky() {
        previewNextSticky();
    }

    public void showPreviousSticky() {
        previewPreviousSticky();
    }

}
