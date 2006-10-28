/* $Id: StickyPaperListener.java 116 2006-10-02 05:27:41Z  $ */
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
package com.we4tech.ideaPlugin.sticky.event;

import com.we4tech.ideaPlugin.sticky.data.Sticky;

/**
 * StickyManager has been used by sticky paper to fire on following events.
 *
 * @author nhm tanveer hossain khan (hasan)
 * @version  1.0-1
 * @see com.we4tech.ideaPlugin.sticky.manager.StickyManager
 */
public interface StickyPaperListener {

    /**
     * Fire on Sticky need to remove
     * @param s
     */
    public void stickyDeleted( Sticky s );

    /**
     * Fire on Sticky content/background color/location/title has been changed
     * @param s sticky object
     */
    public void stickyUpdated( Sticky s );

    /**
     * Fire on sticky is requested to minimize
     * @param s
     */
    public void stickyMinimized( Sticky s );

    /**
     * Fire on sticky is requested to un minimize (restore)
     * @param s
     */
    public void stickyUnMinimized( Sticky s );

    /**
     * Fire on stickies are requested to hide
     */
    public void hideStickies( );

    /**
     * Fire on stickies are requested to show
     */
    public void showStickies( );

    /**
     * Display next hidden sticky and hide all opened stickies
     */
    public void showNextSticky( );

    public void showPreviousSticky( );
    
}
