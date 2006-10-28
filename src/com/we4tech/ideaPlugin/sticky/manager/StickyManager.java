/* $Id: StickyManager.java 136 2006-10-06 07:08:22Z  $ */
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
package com.we4tech.ideaPlugin.sticky.manager;

import com.we4tech.ideaPlugin.sticky.data.Sticky;
import com.we4tech.ideaPlugin.sticky.ui.MinimizedStickyContainerPanel;
import com.we4tech.ideaPlugin.sticky.ui.StickyPaperUI;

public interface StickyManager {

    /**
     * Sticky UI will be rendered by passed Sticky object
     * @param sticky
     * @return sticky paper dialog window will be returned
     */
    public StickyPaperUI createStickyPaper( Sticky sticky );

    /**
     * Load stickies from sticky storage.
     * if display == true it will hide all loaded stickies.
     * @param display
     */
    public void loadStickyPapers( boolean display );

    /**
     * Load stickies from sticky storage and default behavior is
     * setVisible( true );
     */
    public void loadStickyPapers();

    /**
     * Load only project specific sticky papers
     * @param display
     */
    public void loadProjectSpecificStickyPapers( boolean display );

    /**
     * hide(minimize) all stickies by setVisible( false );
     */
    public void hideStickyPapers();

    /**
     * Remove all stickies from minimize panel
     */
    public void clearStickyMinimizePanel();

    /**
     * display (restore) all stickies by setVisible( true );
     * if stickies are minimized with in Minimize panel, it will be removed from
     * minimize panel.
     */
    public void showStickyPapers();

    /**
     * Sticky can be hooked up with a open project file.
     * this method will lookup in storage for finding out linked file.
     * @param linkedFile
     */
    public void showLinkedSticky( String linkedFile );

    /**
     * "true" if loadStickyPapers() or loadStickyPapers( boolean ) is already
     * performed.
     * @return true if stickies are loaded
     */
    public boolean isInit();

    /**
     * return minimized sticky holder.
     * @return panel which holds minimized stickies.
     */
    public MinimizedStickyContainerPanel getMinimizedStickyContainer();

    /**
     * Display next sticky or start from begining
     */
    public void previewNextSticky( );

    /**
     * Remove all unessential project related variables
     */
    public void projectGarbageCollect();

}
