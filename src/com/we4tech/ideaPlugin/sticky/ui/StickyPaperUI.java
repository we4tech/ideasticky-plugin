/* $Id: StickyPaperUI.java 136 2006-10-06 07:08:22Z i_am_working_on_java $ */
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
 * $LastChangedDate: 2006-10-06 00:08:22 -0700 (Fri, 06 Oct 2006) $
 * $LastChangedRevision: 136 $
 ******************************************************************************
*/
package com.we4tech.ideaPlugin.sticky.ui;

import com.we4tech.ideaPlugin.sticky.data.Sticky;
import com.we4tech.ideaPlugin.sticky.event.StickyPaperListener;
import com.we4tech.ideaPlugin.sticky.ui.friendList.FriendManagerUI;
import com.we4tech.ideaPlugin.sticky.helper.NetTransferHelper;
import com.we4tech.ideaPlugin.sticky.helper.IconHelper;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;

import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;

    public class StickyPaperUI extends JDialog {

    private Logger LOG = LogManager.getLogger( StickyPaperUI.class );
    private final boolean DEBUG = LOG.isDebugEnabled();
    private final boolean INFO = LOG.isInfoEnabled();

    private JPanel mRootPanel = null;
    private JTextArea mContentTextArea = null;
    private StickyMovablePanel mToolbarMenu = null;
    private JLabel mWindowTitleLabel = null;
    private StickyToolBarButton mCloseButton = null;
    private StickyToolBarButton mMinimizeButton = null;
    private StickyToolBarButton mCollapsibleButton = null;
    private StickyToolBarButton mStatusButton = null;

    private Sticky sticky;
    private StickyPaperListener eventListener;

    private boolean mOffSnapMode = false;
    private boolean mWindowStarted = false;
    private boolean mWindowSnapMode = false;
    private boolean mMoveStart = false;
    private boolean mHozResizeEnabled;
    private boolean mVerResizeEnabled;
    private boolean mCollapsed = false;

    private Point mMouseClickPoint = null;
    private int mLastX = 0;
    private int mLastY = 0;
    private int mCachedHeight;

    public void setOffSnapMode( boolean b ) {
        mOffSnapMode = b;
    }

    public Sticky getSticky() {
        return sticky;
    }

    public void setSticky(Sticky sticky) {
        this.sticky = sticky;
    }

    public StickyPaperListener getEventListener() {
        return eventListener;
    }

    public void setEventListener(StickyPaperListener eventListener) {
        this.eventListener = eventListener;
    }

    public StickyPaperUI( Sticky sticky ) throws HeadlessException {
        this.sticky = sticky;
        _initSize();
        setFocusableWindowState( true );
        mContentTextArea.setFocusable( true );
    }

    private void _initSize() {
        setSize( getSticky().getSize() );
        setAlwaysOnTop( true );
        setBackground( getSticky().getBackgroundColor() );
        setLocationByPlatform( true );
        setUndecorated( true );
        setResizable( true );

        if (getSticky().getLocation() != null)
            setLocation( getSticky().getLocation() );

        _initComponents();
    }

    private void _initComponents() {
        if (mWindowSnapMode)
            _removeAllComponents();

        setSize( sticky.getSize() );
        setTitle( getSticky().getTitle() );
        mRootPanel = new JPanel();
        mContentTextArea = new JTextArea();
        mToolbarMenu = new StickyMovablePanel();
        mWindowTitleLabel = new JLabel();
        mCloseButton = new StickyToolBarButton( IconHelper.CLOSE_ICON );
        mMinimizeButton = new StickyToolBarButton( IconHelper.MINIMIZE_ICON );
        mCollapsibleButton = new StickyToolBarButton( IconHelper.COLLAPSIBLE_ICON );
        mStatusButton = new StickyToolBarButton( null );

        _createWidgets();
        _createEvents();

        validate();

    }

    private void _createEvents() {
        setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE );
        _setUpSnapModeEvent();
        _setUpMinimizeCollapsibleAndCloseEvent();
        _setUpMoveAndChangeEvent();
        _setUpTextChangedEvent();
        _setUpWindowResizeEvent();
        _setUpCursorChangeEvent();
        _setUpKeyHandler();
    }

    private void _setUpKeyHandler() {
        addKeyListener(
                new KeyAdapter() {
                    public void keyReleased(KeyEvent keyEvent) {
                        _verifyRequestKey( keyEvent );
                    }
                }
        );
    }

    private void _verifyRequestKey(KeyEvent keyEvent) {
        if (DEBUG)
            LOG.debug("_verify request key ");

        if (keyEvent.isShiftDown() && keyEvent.isControlDown() &&
            keyEvent.isAltDown() && keyEvent.getKeyCode() == KeyEvent.VK_I )
            _fireShowAll();

        else if (keyEvent.isShiftDown() && keyEvent.isControlDown() &&
                 keyEvent.isAltDown() && keyEvent.getKeyCode() == KeyEvent.VK_H )
            _fireHideAll();

        else if (keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE)
            _windowClosing();

        else if (keyEvent.isControlDown() && keyEvent.getKeyCode() == KeyEvent.VK_F)
            _showFriendManager();

        else if (keyEvent.isControlDown() && keyEvent.isAltDown() &&
                 keyEvent.isShiftDown() && keyEvent.getKeyCode() == KeyEvent.VK_RIGHT)
            _fireShowNextSticky();

        else if (keyEvent.isControlDown() && keyEvent.isAltDown() &&
                 keyEvent.isShiftDown() && keyEvent.getKeyCode() == KeyEvent.VK_LEFT)
            _fireShowPreviousSticky();

        else if (keyEvent.isControlDown() && keyEvent.getKeyCode() == KeyEvent.VK_R) {
            if (NetTransferHelper.transmitTo(
                getSticky().getReplyToIp(), getSticky().getReplyToPort(), getSticky()))
                mStatusButton.setButtonIcon( IconHelper.STATUS_OK );
            else
                mStatusButton.setButtonIcon( IconHelper.STATUS_ERROR );
        }
    }

    private void _changeColor( Color color ) {

        sticky.setBackgroundColor( color );
        setBackground( color );

        if (mWindowTitleLabel != null)
            mWindowTitleLabel.setBackground(  color );

        if (mContentTextArea != null)
            mContentTextArea.setBackground( color );

        if (mToolbarMenu != null)
            mToolbarMenu.setBackground( color );

        validate();
        repaint();
        _fireStickyUpdated();
    }

    private void _setUpCursorChangeEvent() {
        mContentTextArea.addMouseMotionListener(
                new MouseMotionListener() {
                    public void mouseDragged(MouseEvent mouseEvent) {
                    }

                    public void mouseMoved(MouseEvent mouseEvent) {
                        setCursor( new Cursor( Cursor.DEFAULT_CURSOR ) );
                        mHozResizeEnabled = false;
                        mVerResizeEnabled = false;
                    }
                }
        );
    }

    private void _setUpWindowResizeEvent() {
        this.addMouseMotionListener(
                new MouseMotionListener() {
                    public void mouseDragged(MouseEvent mouseEvent) {
                        if (mHozResizeEnabled || mVerResizeEnabled) {
                            int x = mouseEvent.getX();
                            int y = mouseEvent.getY();
                            _resizeWindow( MouseInfo.getPointerInfo().getLocation() );
                            sticky.setSize( getSize() );
                            _fireStickyUpdated();
                        }
                    }

                    public void mouseMoved(MouseEvent mouseEvent) {
                        boolean horizontalResize = mouseEvent.getX() <= getWidth() && mouseEvent.getX() >= getWidth()-5;
                        boolean verticalResize = mouseEvent.getY() <= getHeight() && mouseEvent.getY() >= getHeight()-5;

                        if (horizontalResize) {
                            setCursor( new Cursor( Cursor.W_RESIZE_CURSOR ) );
                            mHozResizeEnabled = true;
                        }
                        else if (verticalResize) {
                            setCursor( new Cursor( Cursor.S_RESIZE_CURSOR ) );
                            mVerResizeEnabled = true;
                        }
                        else {
                            setCursor( new Cursor( Cursor.DEFAULT_CURSOR ) );
                            mHozResizeEnabled = false;
                            mVerResizeEnabled = false;
                        }

                    }
                }
        );
    }

    private void _resizeWindow(Point point) {
        int musDiffX = (point.x - (this.getX()+this.getWidth())  );
        int musDiffY = (point.y - (this.getY()+this.getHeight()) );

        if (mLastX != musDiffX || mLastY != musDiffY) {
            if (mVerResizeEnabled) {
                int increment = ((getHeight() + musDiffY)-2);
                if (increment < 60)
                    increment = 60;
                setSize( getWidth(), increment );
                validate();
                mLastX = musDiffX;
            }
            else if (mHozResizeEnabled) {
                int increment = ((getWidth() + musDiffX)-2);
                if (increment < 120)
                    increment = 120;
                setSize( increment, getHeight() );
                validate();
                mLastY = musDiffY;
            }
        }
    }

    private void _setUpTextChangedEvent() {
        mContentTextArea.addKeyListener(
                new KeyListener() {
                    public void keyTyped(KeyEvent keyEvent) {
                    }

                    public void keyPressed(KeyEvent keyEvent) {
                    }
                    public void keyReleased(KeyEvent keyEvent) {
                        sticky.setContent( mContentTextArea.getText( ) );
                        _fireStickyUpdated();
                        _verifyRequestKey( keyEvent );
                    }

                }
        );
    }


    private void _setUpMoveAndChangeEvent() {
        mWindowTitleLabel.addMouseMotionListener(
                new MouseMotionListener() {
                    public void mouseDragged(MouseEvent mouseEvent) {
                        if (mMoveStart)
                            _moveWindowTo(mouseEvent.getPoint());
                    }
                    public void mouseMoved(MouseEvent mouseEvent) {
                        setCursor( new Cursor( Cursor.DEFAULT_CURSOR ) );
                    }
                }
        );
        mWindowTitleLabel.addMouseListener(
                new MouseAdapter() {
                    public void mousePressed(MouseEvent mouseEvent) {

                        if( mouseEvent.getClickCount() == 2 ) {
                            _editTitle();
                        }
                        else {
                            mMouseClickPoint = mouseEvent.getPoint();
                            mMoveStart = true;
                            setCursor(new Cursor(Cursor.MOVE_CURSOR));
                        }
                    }
                    public void mouseReleased(MouseEvent mouseEvent) {
                        mMoveStart = false;
                        setCursor( new Cursor( Cursor.DEFAULT_CURSOR ) );
                        _fireStickyUpdated();
                    }
                }
        );
    }

    private void _editTitle() {

        final JPanel tmpPanel = new JPanel();
        tmpPanel.setLayout( new BorderLayout() );
        final JTextField tmpTextField = new JTextField( mWindowTitleLabel.getText() );
        mToolbarMenu.remove( mWindowTitleLabel );

        tmpPanel.add( tmpTextField, BorderLayout.CENTER );
        mToolbarMenu.add( tmpPanel, BorderLayout.CENTER );

        tmpTextField.addKeyListener(
                new KeyAdapter() {
                    public void keyReleased(KeyEvent keyEvent) {
                        if( keyEvent.getKeyCode() == KeyEvent.VK_ENTER ) {
                            mWindowTitleLabel.setText(tmpTextField.getText());
                            mToolbarMenu.remove(tmpPanel);
                            mToolbarMenu.add(mWindowTitleLabel, BorderLayout.CENTER);
                            validate();
                            repaint();

                            sticky.setTitle(mWindowTitleLabel.getText());
                            _fireStickyUpdated();
                        }
                    }
                }
        );
        tmpTextField.setFocusable( true );
        validate();
    }

    private void _moveWindowTo(Point point) {
        // determine window move new location
        int newX = getLocation().x + (point.x - mMouseClickPoint.x);
        int newY = getLocation().y + (point.y - mMouseClickPoint.y);

        setLocation( newX, newY );
        sticky.setLocation( getLocation() );
    }

    private void _setUpMinimizeCollapsibleAndCloseEvent() {
        mCloseButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent actionEvent) {
                        _windowClosing();
                    }
                }
        );
        mMinimizeButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent actionEvent) {
                        _windowMinimize();
                    }
                }
        );
        mCollapsibleButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent actionEvent) {
                        _windowCollapse();
                    }
                }
        );
        mStatusButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent actionEvent) {
                        mStatusButton.setButtonIcon( null );
                    }
                }
        );
    }

    private void _windowCollapse() {
        if (!mCollapsed) {
            mCachedHeight = getHeight();
            setSize( getWidth(), 29 );
            mCollapsed = true;
            mCollapsibleButton.setButtonIcon( IconHelper.EXPAND_ICON );
        }
        else {
            if (mCachedHeight == 0)
                mCachedHeight = (int)getSticky().getSize().getHeight();
            setSize( getWidth(), mCachedHeight );
            mCollapsed = false;
            mCollapsibleButton.setButtonIcon( IconHelper.COLLAPSIBLE_ICON );
            validate();
        }
    }

    public void windowCollapse( boolean b ) {
        mCollapsed = b;
        _windowCollapse();
    }


    private void _windowMinimize() {
        sticky.setMinimized(true);
        _fireStickyMinimized();
    }

    private void _setUpSnapModeEvent() {
        this.addWindowListener(
                new WindowAdapter() {
                    public void windowDeactivated(WindowEvent windowEvent) {
//                        if( mWindowStarted && !mWindowSnapMode && !mOffSnapMode )
//                            _takeScreenSanp();
                    }

                    public void windowActivated(WindowEvent windowEvent) {
                        /*if( mWindowSnapMode ) {
                            _restoreComponentsAftetBeingActivated();
                            mWindowSnapMode = false;
                        }*/

                    }

                    public void windowOpened(WindowEvent windowEvent) {
                        mWindowStarted = true;
                    }
                }
        );
    }

    private synchronized void _windowClosing() {
        if(DEBUG)
            LOG.debug("window closing remove sticky from storage");
        int rtn = JOptionPane.showConfirmDialog(
                  this, "Do you really want to remove this sticky?",
                  "Warning", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE );

        if( rtn == 0 ) {
            if(DEBUG)
                LOG.debug( "fire sticky deleted and dispose window" );
            _fireStickyDeleted();
            this.dispose();
        }

    }

    private void _restoreComponentsAftetBeingActivated() {
        if(DEBUG)
            LOG.debug("restoring all widgets ("+getTitle()+") ");
        _initComponents();
    }

    private void _takeScreenSanp() {
        try {
            _backupData();
            this.setFocusableWindowState( true );
            if (DEBUG)
                LOG.debug("taking screen snap");
            Robot r = new Robot();
            Rectangle rect = new Rectangle( getX(), getY(), getWidth(), getHeight() );
            BufferedImage buffImage = r.createScreenCapture( rect );

            // remove all components
            _removeAllComponents();
            _setNullToAllComponents();

            JLabel fakeLabel = new JLabel();
            fakeLabel.setIcon( new ImageIcon( buffImage ));

            getContentPane().setLayout( new BorderLayout() );
            mRootPanel = new JPanel();
            getContentPane().add( mRootPanel, BorderLayout.CENTER );

            mRootPanel.setLayout( new BorderLayout() );
            mRootPanel.add( fakeLabel, BorderLayout.CENTER );
            mWindowSnapMode = true;
            validate();
        }
        catch(Exception e ) {
            LOG.warn( e );
        }
    }

    private void _backupData() {
        if (mContentTextArea != null)
            getSticky().setContent( mContentTextArea.getText() );
        setBackground( getBackground() );
        getSticky().setSize( getSize() );
        getSticky().setTitle( getSticky().getTitle() );
    }

    private void _removeAllComponents() {
        if (mRootPanel != null)
            getContentPane().remove( mRootPanel );
      }

    private void _createWidgets() {
        if (DEBUG)
            LOG.debug("creating widgets ...");
        getContentPane().setLayout( new BorderLayout() );

        mRootPanel.setBorder( BorderFactory.createEmptyBorder( 2, 2, 2, 2 ) );
        mRootPanel.setLayout( new BorderLayout() );
        mRootPanel.setBackground( new Color( 0xcc, 0xcc, 0xcc ) );
        getContentPane().add( mRootPanel, BorderLayout.CENTER );

        _setUpToolbar();
        _setUpContentTextArea();
    }

    private void _setNullToAllComponents() {
        this.mRootPanel = null;
        this.mContentTextArea = null;
        this.mToolbarMenu = null;
        this.mWindowTitleLabel = null;
        this.mCloseButton = null;
        this.mMinimizeButton = null;
        this.mCollapsibleButton = null;
    }

    private void _setUpContentTextArea() {
        mContentTextArea.setOpaque( true );
        mContentTextArea.setBackground( getSticky().getBackgroundColor() );
        mContentTextArea.setLineWrap( true );
        mContentTextArea.setText( getSticky().getContent() );
        // -NO
        mContentTextArea.setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) );

        Border inBroder = BorderFactory.createLineBorder( new Color( 0xcc, 0xcc, 0xcc ) );
        Border outBroder = BorderFactory.createEmptyBorder( 1, 1, 1, 1 );
        Border scrollBorder = BorderFactory.createCompoundBorder( inBroder, outBroder );
        JScrollPane textScrollPanel = new JScrollPane( mContentTextArea );
        textScrollPanel.setBorder( scrollBorder );

        mRootPanel.add( textScrollPanel , BorderLayout.CENTER );
    }

    private void _setUpToolbar() {
        mToolbarMenu.setOpaque( true );
        mToolbarMenu.setLayout( new BorderLayout() );
        mRootPanel.add( mToolbarMenu, BorderLayout.NORTH );

        _setUpWindowLabel();
        _setUpCollapsibleMinimizeAndCloseButton();
        _setUpLeftButtonGroupHolder();
    }

    private void _setUpLeftButtonGroupHolder() {
        StickyLeftOptionMenuBar menuBar = new StickyLeftOptionMenuBar();
        menuBar.setBackground( new Color( 0xFF, 0xFF, 0xCC ) );

        // top level menu
        final JMenu menu = new JMenu();
        menuBar.add( menu );
        menu.setText("");

        if (getSticky().isProjectStorage())
            menu.setIcon( IconHelper.STICKY_PROJECT_ICON );
        else
            menu.setIcon( IconHelper.STICKY_ICON );

        // minimize item
        JMenuItem minimizeItem = new JMenuItem("Minimize");
        minimizeItem.setMnemonic( 'M' );
        minimizeItem.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent actionEvent) {
                        _windowMinimize();
                    }
                }
        );
        menu.add( minimizeItem );

        // color chooser item
        JMenuItem colorChooserItem = new JMenuItem("Select background");
        colorChooserItem.setMnemonic( 'B' );
        colorChooserItem.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent actionEvent) {
                        final JColorChooser cc = new JColorChooser( sticky.getBackgroundColor() );
                        JDialog ccDialog = JColorChooser.createDialog(
                                null, "Select background color", true, cc,
                                new ActionListener() {
                                    public void actionPerformed(ActionEvent actionEvent) {
                                        _changeColor( cc.getColor() );
                                    }
                                },
                                null);
                        ccDialog.setAlwaysOnTop( true );
                        ccDialog.setVisible( true );

                    }
                }
        );
        menu.add( colorChooserItem );

        // show friend list
        JMenuItem friendList = new JMenuItem("Show Friend list");
        friendList.setMnemonic( 'S' );
        friendList.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent actionEvent) {
                        _showFriendManager();
                    }
                }
        );
        menu.add( friendList );


        // sticky save option
        final JCheckBoxMenuItem saveItem = new JCheckBoxMenuItem( "Store locally?" );
        saveItem.setSelected( getSticky().isProjectStorage() );
        saveItem.setMnemonic( 'L' );
        saveItem.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent actionEvent) {
                        sticky.setProjectStorage( saveItem.isSelected() );
                        _fireStickyUpdated();
                        saveItem.setSelected( sticky.isProjectStorage() );
                        if (saveItem.isSelected())
                            menu.setIcon( IconHelper.STICKY_PROJECT_ICON );
                        else
                            menu.setIcon( IconHelper.STICKY_ICON );
                    }
                }
        );
        menu.add( saveItem );

        // delete sticky
        JMenuItem closeSticky = new JMenuItem( "Close" );
        closeSticky.setMnemonic( 'C' );
        closeSticky.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent actionEvent) {
                        _windowClosing();
                    }
                }
        );
        menu.add( closeSticky );
        mToolbarMenu.add( menuBar, BorderLayout.WEST );
    }

    private void _showFriendManager() {
        FriendManagerUI ui = FriendManagerUI.getInstance();
        ui.setSticky(getSticky());
        ui.displayDialog();

    }

    private void _setUpCollapsibleMinimizeAndCloseButton() {
        JPanel panel = new JPanel();
        panel.setLayout( new GridLayout(1,4) );
        panel.add( mStatusButton );
        panel.add( mCollapsibleButton );
        panel.add( mMinimizeButton );
        panel.add( mCloseButton );

        mToolbarMenu.add( panel, BorderLayout.EAST );
    }

    private void _setUpWindowLabel() {
        // -NO
        mWindowTitleLabel.setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5) );
        mWindowTitleLabel.setText( getSticky().getTitle() );
        mToolbarMenu.add( mWindowTitleLabel, BorderLayout.CENTER );
    }

    private void _fireStickyDeleted( ) {
        if (DEBUG)
            LOG.debug("fire sticky deleted");
        if (getEventListener() != null)
            getEventListener().stickyDeleted( getSticky() );
    }

    private void _fireStickyUpdated( ) {
        if (DEBUG)
            LOG.debug("fire sticky update");

        if (getEventListener() != null)
            getEventListener().stickyUpdated( getSticky() );
    }

    private void _fireStickyMinimized( ) {
        if (DEBUG)
            LOG.debug( "sticky minimized" );

        if (getEventListener() != null)
            getEventListener().stickyMinimized( getSticky() );
    }

    private void _fireShowAll() {
        if (eventListener != null ) {
            eventListener.showStickies();
        }
    }

    private void _fireHideAll() {
        if (eventListener != null ) {
            eventListener.hideStickies();
        }
    }

    private void _fireShowNextSticky() {
        if (eventListener != null)
            eventListener.showNextSticky();
    }

    private void _fireShowPreviousSticky() {
        if (eventListener != null)
            eventListener.showPreviousSticky();
    }

    public static void main( String[] args ) {
        Sticky s1 = new Sticky();
        StickyPaperUI sp = new StickyPaperUI( s1 );
        sp.setVisible( true );
        sp.setTitle("1");
        sp.setLocation( 0, 0 );

        StickyPaperUI sp2 = new StickyPaperUI( s1 );
        sp2.setVisible( true );
        sp2.setTitle( "2" );
        sp2.setLocation( 300, 0 );

        StickyPaperUI sp3 = new StickyPaperUI( s1 );
        sp3.setVisible( true );
        sp3.setTitle( "3" );
        sp3.setLocation( 600, 0 );
    }
}
