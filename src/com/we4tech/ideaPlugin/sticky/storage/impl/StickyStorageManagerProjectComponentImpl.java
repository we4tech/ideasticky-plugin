/* $Id$ */
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
 * $LastChangedBy$
 * $LastChangedDate$
 * $LastChangedRevision$
 ******************************************************************************
*/
package com.we4tech.ideaPlugin.sticky.storage.impl;

import org.jetbrains.annotations.NonNls;
import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;
import com.we4tech.ideaPlugin.sticky.helper.StickyConstants;
import com.we4tech.ideaPlugin.sticky.storage.StickyStorageManager;
import com.we4tech.ideaPlugin.sticky.storage.StickyStorageManagerProjectComponent;
import com.we4tech.ideaPlugin.sticky.storage.StickyStorageRegister;
import com.we4tech.ideaPlugin.sticky.data.Sticky;
import com.we4tech.ideaPlugin.sticky.manager.StickyManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.application.ApplicationManager;

import java.io.File;
import java.util.List;

/**
 * Delegate sticky storage for project component
 *
 * @author nhm tanveer hossain khan (hasan)
 * @version 1.0-1
 * @since 1.0
 * @see StickyStorageManager
 */
public class StickyStorageManagerProjectComponentImpl
    implements StickyStorageManagerProjectComponent {

    private Logger LOG = LogManager.getLogger( StickyStorageManagerProjectComponentImpl.class );
    private StickyStorageManager mStickyStorageDelegate;
    private Project mProject;

    public StickyStorageManagerProjectComponentImpl( Project project ) {
        this.mProject = project;
    }

    public void projectOpened() {
        if (LOG.isInfoEnabled())
            LOG.info( "project opened" );

        File path = new File( mProject.getProjectFilePath() );
        File file = new File( path.getParent() + File.separator + StickyConstants.PROJECT_STORAGE_FILE_NAME );

        if (LOG.isDebugEnabled())
            LOG.debug( "file to store project stickies - "+file.toString() );
        mStickyStorageDelegate = new StickyStorageManagerImpl( file );

        if (LOG.isDebugEnabled())
            LOG.debug( "registering project storage" );
        // register itself as project storage
        StickyStorageRegister.getInstance().addProjectStorage( this );

        // load repositories
        StickyManager sm = ApplicationManager.getApplication().getComponent( StickyManager.class );
        if (sm != null) {
            sm.clearStickyMinimizePanel();
            sm.loadProjectSpecificStickyPapers( false );
        }

    }

    public void projectClosed() {
        if (LOG.isInfoEnabled())
            LOG.info( "project closed");
        mStickyStorageDelegate = null;
        mProject = null;
        StickyStorageRegister.getInstance().removeProjectStorage();
        StickyManager sm = ApplicationManager.getApplication().getComponent( StickyManager.class );
        if (sm != null) {
            sm.projectGarbageCollect();
        }
    }

    @NonNls
    public String getComponentName() {
        return StickyConstants.STICKY_PROJECT_STORAGE;
    }

    public void initComponent() {
        if (LOG.isInfoEnabled())
            LOG.info( "init. StickyStorageManager project component" );

    }

    public void disposeComponent() {
        if (LOG.isInfoEnabled())
            LOG.info( "disposing StickyStorageManager project component" );

    }

    public void saveSticky(Sticky sticky) {
        if (LOG.isDebugEnabled())
            LOG.debug( "saving sticky on project component - "+sticky );
        mStickyStorageDelegate.saveSticky( sticky );
    }

    public void deleteStickyById(String stickyId) {
        if (LOG.isDebugEnabled())
            LOG.debug( "deleting sticky from project component" );
        mStickyStorageDelegate.deleteStickyById( stickyId );
    }

    public List<String> getStickiesId() {
        return mStickyStorageDelegate.getStickiesId();
    }

    public Sticky getStickyById(String stickyId) {
        return mStickyStorageDelegate.getStickyById( stickyId );
    }

    public List<Sticky> getStickyByLinkedFile(String fileName) {
        return mStickyStorageDelegate.getStickyByLinkedFile( fileName );
    }
}
