/* $Id: StickyServerImpl.java 127 2006-10-03 06:43:05Z  $ */
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
 * $LastChangedDate: 2006-10-03 06:43:05Z $
 * $LastChangedRevision: 127 $
 ******************************************************************************
*/
package com.we4tech.ideaPlugin.sticky.network.impl;

import com.we4tech.ideaPlugin.sticky.storage.GlobalConfigurationManager;
import com.we4tech.ideaPlugin.sticky.storage.GlobalConfiguration;
import com.we4tech.ideaPlugin.sticky.data.Sticky;
import com.we4tech.ideaPlugin.sticky.helper.StringHelper;
import com.we4tech.ideaPlugin.sticky.helper.StickyConstants;
import com.we4tech.ideaPlugin.sticky.network.StickyServer;
import com.we4tech.ideaPlugin.sticky.manager.StickyManager;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.application.ApplicationManager;

import java.net.ServerSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;
import org.apache.commons.codec.binary.Base64;
import org.jetbrains.annotations.NonNls;

public class StickyServerImpl
    implements StickyServer, ApplicationComponent {

    private Logger LOG = LogManager.getLogger(StickyServerImpl.class);
    private final boolean DEBUG = LOG.isDebugEnabled();
    private final boolean INFO = LOG.isInfoEnabled();

    private ServerSocket mServerSocket;
    private int mBackLog = 5;
    private boolean mServerState = true;
    private StickyManager mStickyManager;
    private boolean mConnected = false;

    @NonNls
    public String getComponentName() {
        return "StickyServer";
    }

    /**
     * Load <tt>StickyManager</tt> instance
     */
    public void initComponent() {
        if (INFO)
            LOG.info("Connecting StickyServer..");
        setStickyManager(ApplicationManager.getApplication().getComponent(StickyManager.class));
        try {
            connect();
        } catch (IOException e) {
            LOG.warn(e);
        }
    }

    public void disposeComponent() {
        if (INFO)
            LOG.info("disponse StickyServer component");
        try {
            disconnect();
            mStickyManager = null;
        }
        catch (IOException e) {
            LOG.warn(e);
        }
    }

    /**
     * sticky server instance which will be returned from ApplicationManager container.
     * @return if successfully init. StickyServer object will be returned otherwise null
     */
    public static StickyServer getInstance() {
        return ApplicationManager.getApplication().getComponent(StickyServer.class);
    }

    public StickyManager getStickyManager() {
        return mStickyManager;
    }

    public void setStickyManager(StickyManager mStickyManager) {
        this.mStickyManager = mStickyManager;
    }

    /**
     * Connection IPAddress and Port will be looked up from <tt>GlobalConfiguration</tt><br>
     * <tt>StickyServerHandler</tt> will start a thread which is iterating handle<br>
     * new incoming connection.<br>
     *
     * @throws IOException
     * @see GlobalConfiguration
     */
    public void connect() throws IOException {
        if (INFO)
            LOG.info("connect( )");

        if (mConnected)
            return;

        GlobalConfiguration gConf = GlobalConfigurationManager.getInstance();
        InetAddress inetAddrs = null;
        if (gConf.getIPAddress() != null && gConf.getIPAddress().length() > 0)
            inetAddrs = InetAddress.getByName(gConf.getIPAddress());
        else
            inetAddrs = InetAddress.getLocalHost();

        if (DEBUG)
            LOG.debug("connecting sticky server on " + inetAddrs.getHostAddress() + ":" + gConf.getPort());
        mServerSocket = new ServerSocket(gConf.getPort(), mBackLog, inetAddrs);

        if (DEBUG)
            LOG.debug("sticky server handler is initializing ...");
        // server state reminds server is running
        mServerState = true;

        StickyServerHandler ssh = new StickyServerHandler();
        Thread t = new Thread(ssh);
        t.start();
        mConnected = true;
    }

    /**
     * Close ServerSocket, mServerState = false [no thread running]
     * mConnected = false, [No server connected]
     * ServerSocket instance is cleared.
     *
     * @throws IOException
     */
    public void disconnect() throws IOException {
        if (INFO)
            LOG.info("disconnect Sticky server");

        if (mConnected) {
            mServerSocket.close();
            mServerState = false;
            mServerSocket = null;
            mConnected = false;
        }
    }

    private class StickyServerHandler implements Runnable {
        public void run() {
            if (DEBUG)
                LOG.debug("Staring Sticky server handler");

            while (mServerState && mServerSocket != null && !mServerSocket.isClosed()) {
                try {
                    Socket client = mServerSocket.accept();
                    _handleClient(client);
                }
                catch (Exception e) {
                    LOG.warn(e);
                }
            }
        }

        private void _handleClient(final Socket client) {
            Thread t = new Thread() {
                public void run() {
                    try {
                        if (DEBUG)
                            LOG.debug("handling client " + client.getInetAddress().getHostAddress());
                        // read user request
                        InputStream inStream = client.getInputStream();
                        int c;
                        StringBuffer sBuff = new StringBuffer();
                        while ((c = inStream.read()) != -1) {
                            sBuff.append((char) c);
                        }

                        // parse request to properties
                        ByteArrayInputStream bais = new ByteArrayInputStream(sBuff.toString().getBytes());
                        Properties p = new Properties();
                        p.load(bais);
                        bais.close();

                        // create sticky
                        if (DEBUG)
                            LOG.debug("creating new sticky from " + client.getInetAddress().getHostAddress());
                        Sticky s = new Sticky();

                        // title
                        s.setTitle("Sent from " + client.getInetAddress().getHostAddress() +
                                " <" + p.getProperty(StickyConstants.PROP_STICKY_TITLE) + ">");
                        // content
                        s.setContent(StringHelper.addLineBreak(
                                new String(Base64.decodeBase64(
                                        p.getProperty(StickyConstants.PROP_STICKY_CONTENT).getBytes()))));
                        // background
                        s.setBackgroundColor(StringHelper.getStringToColor(p.getProperty(StickyConstants.PROP_STICKY_BACKGROUND)));
                        // size
                        s.setSize(StringHelper.getStringToDimension(p.getProperty(StickyConstants.PROP_STICKY_SIZE)));
                        // set sender info
                        s.setReplyToIp(client.getInetAddress().getHostAddress());
                        // TODO: remove hardcoded port
                        s.setReplyToPort(12345);

                        s.setLocation(null);
                        getStickyManager().createStickyPaper(s);
                    }
                    catch (IOException e) {
                        LOG.warn(e);
                    }
                }
            };
            t.start();
        }
    }
}
