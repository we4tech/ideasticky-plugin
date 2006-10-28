/* $Id: StickyStorageManager.java 111 2006-09-30 06:00:41Z  $ */
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
package com.we4tech.ideaPlugin.sticky.storage;

import com.we4tech.ideaPlugin.sticky.data.Sticky;

import java.util.List;

/**
 * Create/edit/delete sticky object
 *
 * @author nhm tanveer hossain khan (hasan)
 * @version 1.0-1
 * @since 1.0
 */
public interface StickyStorageManager {

    /**
     * Store sticky object into Storage space.
     *
     * @param sticky object is passed through
     */
    public void saveSticky( Sticky sticky );

    /**
     * delete sticky from storage.
     *
     * @param stickyId is accepting for identifing a specific sticky
     */
    public void deleteStickyById( String stickyId );

    /**
     * retrieve a list of stored stickies, through a performing a search on sticky properties Set
     *
     * @return List<String> a list of stickies id which was stored previously
     */
    public List<String> getStickiesId( );

    /**
     * retrieve sticky object by sticky id
     *
     * @param stickyId
     * @return Sticky object will be returned
     */
    public Sticky getStickyById( String stickyId );

    /**
     * Retrieve sticky by connected/linked file name
     * @param fileName
     * @return return sticky object
     */
    public List<Sticky> getStickyByLinkedFile( String fileName );
}
