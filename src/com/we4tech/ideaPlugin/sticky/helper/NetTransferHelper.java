/* $Id: NetTransferHelper.java 119 2006-10-02 20:53:47Z  $ */
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
 * $LastChangedDate: 2006-10-02 20:53:47Z $
 * $LastChangedRevision: 119 $
 ******************************************************************************
*/
package com.we4tech.ideaPlugin.sticky.helper;

import com.we4tech.ideaPlugin.sticky.data.Sticky;
import com.we4tech.ideaPlugin.sticky.helper.StringHelper;
import com.we4tech.ideaPlugin.sticky.ui.friendList.FriendManagerUI;

import java.net.Socket;
import java.net.InetAddress;
import java.io.OutputStream;
import java.awt.*;

import org.apache.commons.codec.binary.Base64;

public class NetTransferHelper {

    /**
     * Send sticky paper over network
     *
     * @param ipAddrs
     * @param port
     * @param sticky
     * @return if sticky paper transfered successfully
     */
    public static boolean transmitTo( String ipAddrs, int port, Sticky sticky ) {
        boolean state = false;
        try {
            if (ipAddrs != null || port !=0 ) {
                System.out.println( "Connecting : "+ipAddrs+", PORT: "+port );
                Socket socket = new Socket( InetAddress.getByName( ipAddrs ), port );
                OutputStream out = socket.getOutputStream();

                String message = new StringBuilder()
                                .append(StickyConstants.PROP_STICKY_TITLE)
                                .append("=").append(sticky.getTitle())
                                .append("\r\n").append(StickyConstants.PROP_STICKY_CONTENT)
                                .append("=")
                                .append(StringHelper.removeCrLf(
                                        new String(Base64.encodeBase64(sticky.getContent().getBytes())))).append("\r\n").append(StickyConstants.PROP_STICKY_BACKGROUND).append("=").append(sticky.getBackgroundColor().getRed()).append(",").append(sticky.getBackgroundColor().getGreen()).append(",").append(sticky.getBackgroundColor().getBlue()).append("\r\n").append(StickyConstants.PROP_STICKY_SIZE).append("=").append(sticky.getSize().getWidth()).append(",").append(sticky.getSize().getHeight()).append("\r\n").toString();
                System.out.println( "Sending request - "+message );
                out.write( message.getBytes() );
                out.close();
                socket.close();
                state = true;
            }
        }
        catch( Exception e ) {
            UIHelper.showErrorMessage( FriendManagerUI.getInstance(), e );
            e.printStackTrace();
        }

        return state;
    }
}
