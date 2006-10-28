/* $Id: Sticky.java 119 2006-10-02 20:53:47Z i_am_working_on_java $ */
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
 * $LastChangedBy: i_am_working_on_java $
 * $LastChangedDate: 2006-10-02 20:53:47Z $
 * $LastChangedRevision: 119 $
 ******************************************************************************
*/
package com.we4tech.ideaPlugin.sticky.data;

import java.awt.*;

public class Sticky {

    private String stickyId = ""+Math.random()+System.currentTimeMillis();
    private Point location = null;
    private Dimension size = new Dimension( 300, 100 );
    private String title = "(...)";
    private String content = "";
    private Color backgroundColor = new Color(0xFF, 0xFF, 0xCC);
    private boolean minimized = false;
    private String linkedUpFile = null;
    private boolean projectStorage = true;

    // not stored
    private String replyToIp;
    private int replyToPort;

    public String getStickyId() {
        return stickyId;
    }

    public void setStickyId(String stickyId) {
        this.stickyId = stickyId;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public Dimension getSize() {
        return size;
    }

    public void setSize(Dimension size) {
        this.size = size;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public boolean isMinimized() {
        return minimized;
    }

    public void setMinimized(boolean minimized) {
        this.minimized = minimized;
    }

    public String getLinkedUpFile() {
        return linkedUpFile;
    }

    public void setLinkedUpFile(String linkedUpFile) {
        this.linkedUpFile = linkedUpFile;
    }

    public boolean isProjectStorage() {
        return projectStorage;
    }

    public void setProjectStorage(boolean projectStorage) {
        this.projectStorage = projectStorage;
    }

    public String getReplyToIp() {
        return replyToIp;
    }

    public void setReplyToIp(String replyToIp) {
        this.replyToIp = replyToIp;
    }

    public int getReplyToPort() {
        return replyToPort;
    }

    public void setReplyToPort(int replyToPort) {
        this.replyToPort = replyToPort;
    }

    public String toString() {
        return "{ID: '"+getStickyId()+"', location: '"+getLocation()+
                "', size: '"+getSize()+"', title: '"+getTitle()+
                "', content: '"+getContent()+"', backgroundColor: '"+
                getBackgroundColor()+"', minized: "+minimized+"}";
    }


}
