/* $Id: MinimizedStickyHolder.java 118 2006-10-02 19:38:09Z  $ */
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
 * $LastChangedDate: 2006-10-02 19:38:09Z $
 * $LastChangedRevision: 118 $
 ******************************************************************************
*/
package com.we4tech.ideaPlugin.sticky.module;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.project.Project;
import com.we4tech.ideaPlugin.sticky.manager.StickyManager;
import com.we4tech.ideaPlugin.sticky.helper.IconHelper;
import org.jetbrains.annotations.NonNls;
import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;

import javax.swing.*;

public class MinimizedStickyHolder implements ProjectComponent {

    private Logger LOG = LogManager.getLogger( MinimizedStickyHolder.class );
    private final boolean DEBUG = LOG.isDebugEnabled();
    private final boolean INFO = LOG.isInfoEnabled();

    private StickyManager mStickyManager;
    private Project mProject;

    public MinimizedStickyHolder( Project project ) {
        if ( INFO )
            LOG.info( "constructing MinimizedStickyHolder component" );

        this.mProject = project;
    }

    public void projectOpened() {
        if ( DEBUG )
            LOG.debug( "Project opened - creating tab on bottom anchor" );

        ToolWindowManager toolWindow = mProject.getComponent( com.intellij.openapi.wm.ToolWindowManager.class );
        toolWindow.registerToolWindow(
                    "Minimized stickies",
                    mStickyManager.getMinimizedStickyContainer(),
                    ToolWindowAnchor.BOTTOM ).setIcon( IconHelper.MINIMIZED_TAB_ICON );

    }

    public void projectClosed() {
        if ( DEBUG )
            LOG.debug( "project closed" );
    }

    @NonNls
    public String getComponentName() {
        return "MinimizedStickyHolder";
    }

    public void initComponent() {
        if ( DEBUG )
            LOG.debug( "initializing Minimized sticky holder component..." );

        mStickyManager = ApplicationManager.getApplication().getComponent( StickyManager.class );

        if ( !mStickyManager.isInit() ) {
            mStickyManager.loadStickyPapers( false );
            mStickyManager.hideStickyPapers();
        }

    }

    public void disposeComponent() {
        if ( DEBUG )
            LOG.debug( "Disposing Minimized sticky holder component..." );

        mProject = null;
    }
}
