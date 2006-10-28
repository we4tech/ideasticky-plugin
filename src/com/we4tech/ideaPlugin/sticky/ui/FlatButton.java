/* $Id: FlatButton.java 83 2006-09-23 03:30:05Z  $ */
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
package com.we4tech.ideaPlugin.sticky.ui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

public class FlatButton extends JComponent {

    private JLabel mTextLabel = new JLabel();
    private String text;
    private Icon icon;
    private java.util.List<ActionListener> mActionListeners = new Vector<ActionListener>();

    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
        mTextLabel.setIcon( icon );
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        mTextLabel.setText( text );
    }

    public void addActionListener( ActionListener al ) {
        if( al != null ) {
            mActionListeners.add( al );
        }
    }

    public FlatButton( String text ) {
        setText( text );
        _init();
    }
    public FlatButton() {
        _init();
    }

    private void _init() {
        Border mBorder = BorderFactory.createEmptyBorder( 10, 10, 10, 10 );
        Border lBorder = BorderFactory.createLineBorder( Color.GRAY );
        Border rBorder = BorderFactory.createCompoundBorder( lBorder, mBorder );
        setBorder( rBorder );

        _drawText();
        _setEvents();
    }

    private void _setEvents() {
        _setHoverEvent();
    }

    private void _setHoverEvent() {
        addMouseListener(
                 new MouseAdapter() {
                     public void mouseReleased(MouseEvent mouseEvent) {
                         _fireActionListener( mouseEvent );
                     }

                     public void mouseEntered(MouseEvent mouseEvent) {
                         setBackground( Color.LIGHT_GRAY );
                     }

                     public void mouseExited(MouseEvent mouseEvent) {
                         setBackground( Color.WHITE );
                     }
                 }
         );
        mTextLabel.addMouseListener(
                 new MouseAdapter() {
                     public void mouseReleased(MouseEvent mouseEvent) {
                         _fireActionListener( mouseEvent );
                     }

                     public void mouseEntered(MouseEvent mouseEvent) {
                         setBackground( Color.LIGHT_GRAY );
                     }

                     public void mouseExited(MouseEvent mouseEvent) {
                         setBackground( Color.WHITE );
                     }
                 }
         );
    }

    private void _fireActionListener(MouseEvent mouseEvent) {
        for( ActionListener al: mActionListeners ) {
            ActionEvent e = new ActionEvent( this, mouseEvent.getID(), "none");
            al.actionPerformed( e );
        }
    }

   private void _drawText() {
        setLayout( new BorderLayout() );
        mTextLabel.setText( getText() );
        add( mTextLabel, BorderLayout.CENTER );
    }

}
