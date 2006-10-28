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

import com.we4tech.ideaPlugin.sticky.storage.StickyStorageManager;
import com.we4tech.ideaPlugin.sticky.storage.GlobalConfigurationManager;
import com.we4tech.ideaPlugin.sticky.storage.StickyStorageManagerApplicationComponent;
import com.we4tech.ideaPlugin.sticky.data.Sticky;
import com.we4tech.ideaPlugin.sticky.helper.StickyConstants;
import org.jetbrains.annotations.NonNls;
import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;

import java.io.File;
import java.util.List;

/**
 * Sticky storage manager for application component.
 *
 * @author nhm tanveer hossain khan (hasan)
 * @version 1.0-1
 * @see StickyStorageManager
 */
public class StickyStorageManagerApplicationComponentImpl
    implements StickyStorageManagerApplicationComponent {

    private Logger LOG = LogManager.getLogger( StickyStorageManagerApplicationComponentImpl.class );
    private final boolean DEBUG = LOG.isDebugEnabled();
    private final boolean INFO = LOG.isInfoEnabled();
    private StickyStorageManager mStickyStorageDelegate;

    public StickyStorageManagerApplicationComponentImpl() {
        mStickyStorageDelegate = new StickyStorageManagerImpl( new File( GlobalConfigurationManager.getInstance().getStickyStorageFile() ) );
    }

    @NonNls
    public String getComponentName() {
        return StickyConstants.STICKY_DEFAULT_STORAGE;
    }

    public void initComponent() {
        if ( INFO )
            LOG.info( "init StickyStorage Component" );
    }

    public void disposeComponent() {
        if ( INFO )
            LOG.info( "Dispose StickyStorage Component" );
        mStickyStorageDelegate = null;
    }

    public void saveSticky(Sticky sticky) {
        mStickyStorageDelegate.saveSticky( sticky );
    }

    public void deleteStickyById(String stickyId) {
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