/* $Id: TestUI.java 83 2006-09-23 03:30:05Z i_am_working_on_java $ */
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
 * $LastChangedDate: 2006-09-22 20:30:05 -0700 (Fri, 22 Sep 2006) $
 * $LastChangedRevision: 83 $
 ******************************************************************************
*/
package com.we4tech.ideaPlugin.sticky.ui;

import javax.swing.*;

public class TestUI extends JFrame {

    public TestUI() {
        /*Thread t = new Thread() {
            public void run() {
                while( true ) {
                    try {
                        Thread.sleep( 1000 );
                    } catch (InterruptedException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                }
            }
        };
        t.start();*/
        for( int i=0; i< 1000; i++) {
            System.out.println("EEEEEEE");
            try {
                Thread.sleep( 100 );
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    public static void main(String[] args) {
        TestUI tu = new TestUI();
        tu.setVisible( true );
    }
}
