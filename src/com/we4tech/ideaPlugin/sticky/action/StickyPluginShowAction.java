/* $Id: StickyPluginShowAction.java 111 2006-09-30 06:00:41Z  $ */
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
 * $LastChangedDate: 2006-09-30 06:00:41Z $
 * $LastChangedRevision: 111 $
 ******************************************************************************
*/
package com.we4tech.ideaPlugin.sticky.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.we4tech.ideaPlugin.sticky.manager.StickyManager;
import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;

/**
 * show all stickies when this action has been fired
 *
 * @author nhm tanveer hossain khan (hasan)
 * @version 1.0-1
 * @since 1.0
 * @see StickyManager
 */
public class StickyPluginShowAction extends AnAction {

    private Logger LOG = LogManager.getLogger( StickyPluginShowAction.class );
    private final boolean DEBUG = LOG.isDebugEnabled();

    public void actionPerformed(AnActionEvent anActionEvent) {
        if( DEBUG )
            LOG.debug("AnAction - StickyPluginShowAction");

        if( DEBUG )
            LOG.debug( "retrieving instance of Sticky Manager" );
        StickyManager stickyManager = ApplicationManager.getApplication().getComponent( StickyManager.class );
        if ( !stickyManager.isInit() )
            stickyManager.loadStickyPapers( false );

        stickyManager.showStickyPapers( );
    }
}
