/* $Id: StickyFriendListStorageManager.java 83 2006-09-23 03:30:05Z  $ */
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
 * $LastChangedDate: 2006-09-22 20:30:05 -0700 (Fri, 22 Sep 2006) $
 * $LastChangedRevision: 83 $
 ******************************************************************************
*/
package com.we4tech.ideaPlugin.sticky.storage;

import com.we4tech.ideaPlugin.sticky.data.StickyFriend;

import java.util.List;

public interface StickyFriendListStorageManager {

    /**
     * Store sticky friend
     * @param friend
     */
    public void saveFriend( StickyFriend friend );

    /**
     * retrieve a list of sticky friends
     * @return LIst of sticky friends
     */
    public List<StickyFriend> getFriends( );

    /**
     * retrieve sticky friend by friend id
     * @param friendId
     * @return sticky friend object is returned
     */
    public StickyFriend getFriend( String friendId );

    /**
     * retreive sticky friend by friend name
     * @param name
     * @return Sticky friend object is returned
     */
    public StickyFriend getFriendByName( String name );

    /**
     * Delete sticky friend by friend id
     * @param friendId
     */
    public void deleteFriend( String friendId );
    
}
