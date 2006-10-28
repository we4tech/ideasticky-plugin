/* $Id: StickyToolBarButton.java 119 2006-10-02 20:53:47Z  $ */
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
package com.we4tech.ideaPlugin.sticky.ui;

import javax.swing.*;
import java.awt.*;

public class StickyToolBarButton extends JButton {

    private ImageIcon mButtonIcon;

    public StickyToolBarButton( ImageIcon icon ) {
        this.mButtonIcon = icon;
        setPreferredSize(new Dimension(15, getHeight()));
    }

    public void setButtonIcon( ImageIcon icon ) {
        mButtonIcon = icon;
        repaint();
    }

    protected void paintBorder(Graphics graphics) {
        graphics.setColor( new Color( 0xFF,0xFF, 0x97 ) );
        graphics.fillRect( 0, 0, getWidth(), 4 );
    }

    protected void paintComponent(Graphics graphics) {
        graphics.setColor( new Color( 0xFF, 0xFF, 0xCC ) );
        graphics.fillRoundRect( 0, 0, getWidth(), getHeight(), 0, 0 );

        if (mButtonIcon != null)
            graphics.drawImage(
                     mButtonIcon.getImage()  , 0, 7,
                     mButtonIcon.getIconWidth(),
                     mButtonIcon.getIconHeight(),
                     new Color( 0xFF, 0xFF, 0xCC ), null );
    }

    
}
