/* $Id: MinimizedStickyContainerPanel.java 128 2006-10-03 07:02:01Z  $ */
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
 * $LastChangedDate: 2006-10-03 07:02:01Z $
 * $LastChangedRevision: 128 $
 ******************************************************************************
*/
package com.we4tech.ideaPlugin.sticky.ui;

import com.we4tech.ideaPlugin.sticky.data.Sticky;
import com.we4tech.ideaPlugin.sticky.event.StickyPaperListener;
import com.we4tech.ideaPlugin.sticky.helper.IconHelper;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Vector;

public class MinimizedStickyContainerPanel extends JPanel {

    private java.util.List<String> mMinimizedStickyIds = null;
    private JPanel mHolder = null;
    private StickyPaperListener mStickyManager = null;

    public MinimizedStickyContainerPanel( StickyPaperListener stickyManager ) {
        mStickyManager = stickyManager;
        _init();
    }

    private void _init() {
        setLayout( new BorderLayout() );
        setMinimumSize( new Dimension( 100, 100 ) );
        mMinimizedStickyIds = new Vector<String>();
        _createWidgets();
    }

    private void _fakeWindow() {
        JDialog w = new JDialog();
        w.getContentPane().setLayout( new BorderLayout() );
        w.getContentPane().add( this, BorderLayout.CENTER );
        w.setSize( 400, 100 );
        w.setVisible( true );
    }

    private void _createWidgets() {
        mHolder = new JPanel();
        FlowLayout fl = new FlowLayout();
        fl.setAlignment( FlowLayout.LEFT );
        mHolder.setLayout( fl );

        JScrollPane jsPane = new JScrollPane( mHolder );
        add( jsPane , BorderLayout.CENTER );
    }

    public void addMinimizedSticky( Sticky sticky ) {
        if( !mMinimizedStickyIds.contains( sticky.getStickyId() ))
            _createMinimizedStickyButton( sticky );
    }

    public void removeAllMinimizedSticky( ) {
        mMinimizedStickyIds = new Vector<String>();
        mHolder.removeAll();
        validate();
        repaint();
    }

    public java.util.List<String> getMinimizedStickieIds() {
        return mMinimizedStickyIds;
    }

    private void _createMinimizedStickyButton(final Sticky sticky) {
        FlatButton button = new FlatButton( sticky.getTitle() );
        Border b = BorderFactory.createCompoundBorder(
                   BorderFactory.createLineBorder( Color.DARK_GRAY ),
                   BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) );
        button.setBorder( b );
        if (sticky.isProjectStorage())
            button.setIcon( IconHelper.STICKY_PROJECT_ICON);
        else
            button.setIcon( IconHelper.STICKY_ICON);
        button.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent actionEvent) {
                        _fireStickyUnMinimized( sticky, (FlatButton)actionEvent.getSource() );
                    }
                }
        );
        mHolder.add( button );
        mMinimizedStickyIds.add( sticky.getStickyId() );

        validate();
    }

    private void _fireStickyUnMinimized(Sticky sticky, FlatButton source) {

        // notify sticky manager
        sticky.setMinimized( false );
        mStickyManager.stickyUnMinimized( sticky );

        // remove button
        mHolder.remove( source );
        mMinimizedStickyIds.remove( sticky.getStickyId() );

        // update ui
        validate();
        repaint();
    }

}
