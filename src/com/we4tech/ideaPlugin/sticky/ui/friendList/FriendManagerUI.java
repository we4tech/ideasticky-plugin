/* $Id: FriendManagerUI.java 119 2006-10-02 20:53:47Z  $ */
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
package com.we4tech.ideaPlugin.sticky.ui.friendList;

import com.we4tech.ideaPlugin.sticky.storage.StickyFriendListStorageManager;
import com.we4tech.ideaPlugin.sticky.storage.GlobalConfigurationManager;
import com.we4tech.ideaPlugin.sticky.storage.GlobalConfiguration;
import com.we4tech.ideaPlugin.sticky.data.StickyFriend;
import com.we4tech.ideaPlugin.sticky.data.Sticky;
import com.we4tech.ideaPlugin.sticky.helper.UIHelper;
import com.we4tech.ideaPlugin.sticky.helper.NetTransferHelper;
import com.intellij.openapi.application.ApplicationManager;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.List;

import org.apache.commons.mail.SimpleEmail;
import org.apache.commons.mail.EmailException;
import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;

public class FriendManagerUI extends JDialog {

    private Logger LOG = LogManager.getLogger( FriendManagerUI.class );
    private final boolean DEBUG = LOG.isDebugEnabled();
    private final boolean INFO = LOG.isInfoEnabled();

    private static final FriendManagerUI INSTANCE = new FriendManagerUI();
    private static final int STICKY_SERVER_DEFAULT_PORT = 12345;

    private JPanel contentPane;

    private JButton mButtonSave;
    private JButton mButtonClose;
    private JButton mButtonSendMail;
    private JButton mButtonNetSend;

    private JList mFriendList;

    private JTextField mPortText;
    private JTextField mNameTextField;
    private JTextField mEmailTextField;
    private JTextField mIpAddressTextField;
    private String mEditId = null;

    private StickyFriendListStorageManager mStorageManager;

    private Sticky sticky;
    private boolean statusSuccessful;

    public boolean isStatusSuccessful() {
        return statusSuccessful;
    }

    public Sticky getSticky() {
        return sticky;
    }

    public void setSticky(Sticky sticky) {
        this.sticky = sticky;
    }

    private FriendManagerUI() {
        _initUI();
        _initFriendListPopup();
        _initFriendListEvent();
        _initStorage();
        _setUpMailSendingEvent();
        _setUpNetTransmitEvent();
    }

    private void _setUpNetTransmitEvent() {
        mButtonNetSend.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent actionEvent) {
                        _transmitTo();
                    }
                }
        );
    }

    private void _transmitTo() {
        try {
            Object[] friends = mFriendList.getSelectedValues();
            if( friends != null ) {
                for( Object frnd: friends ) {
                    StickyFriend sf = getStickyFriendListStorageManager().getFriendByName( frnd.toString() );
                    NetTransferHelper.transmitTo(
                                      sf.getIpAddress(),
                                      STICKY_SERVER_DEFAULT_PORT,
                                      getSticky() );
                }
            }
            JOptionPane.showMessageDialog( this, "Message transmitted.", "Network transmit status", JOptionPane.INFORMATION_MESSAGE );
        }
        catch( Exception e ) {
            LOG.warn( e );
            UIHelper.showErrorMessage( this, e );
        }
    }

    private void _setUpMailSendingEvent() {
        mButtonSendMail.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent actionEvent) {
                        _sendMail();
                    }
                }
        );
    }

    private void _sendMail() {
        if( getSticky() != null ) try {
            GlobalConfiguration gConf = GlobalConfigurationManager.getInstance();

            boolean smtpConfigured = (gConf.getSMTPHost() != null &&
                    gConf.getSMTPHost().length() > 0) &&
                    gConf.getSMTPPort() != 0;

            if (smtpConfigured) {
                // change cursor
                setCursor( new Cursor( Cursor.WAIT_CURSOR ) );

                // send mail
                SimpleEmail se = new SimpleEmail();

                boolean needAuthentication = gConf.getSMTPUser() != null && gConf.getSMTPUser().length() > 0 &&
                                             gConf.getSMTPPassword() != null && gConf.getSMTPPassword().length() > 0;
                if (needAuthentication)
                    se.setAuthentication(gConf.getSMTPUser(), gConf.getSMTPPassword());
                se.setDebug(true);

                se.setHostName(gConf.getSMTPHost());
                se.setSmtpPort(gConf.getSMTPPort());

                // send to
                _getSelectedRecpts( se );

                // send from
                boolean senderDefined = gConf.getMailFrom() != null && gConf.getMailFrom().length() > 0;
                if( !senderDefined )
                    se.setFrom( gConf.getSMTPUser(), "IdeaSticky" );
                else
                    se.setFrom( gConf.getMailFrom(), "IdeaSticky" );

                se.setSubject( "IdeaSticky :: "+sticky.getTitle() );
                se.setMsg( sticky.getContent() );

                se.send();

                // alter cursor
                setCursor( new Cursor( Cursor.DEFAULT_CURSOR ) );

                JOptionPane.showMessageDialog( this, "Message sent.", "Message Status", JOptionPane.INFORMATION_MESSAGE );
            }
            else {
                JOptionPane.showMessageDialog(
                        this,
                        "SMTP server is not configured.\r\n" +
                                " please go to File > Settings > IdeaSticky Configuration",
                        "SMTP Configration error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
        catch (Exception e) {
            UIHelper.showErrorMessage( this, e );
            // alter cursor
            setCursor( new Cursor( Cursor.DEFAULT_CURSOR ) );
        }
        else {
            JOptionPane.showMessageDialog( this, "No sticky selected", "WARN", JOptionPane.WARNING_MESSAGE );
        }

    }

    private void _getSelectedRecpts(SimpleEmail se) throws EmailException {
        Object[] friends = mFriendList.getSelectedValues();

        if( friends != null ) {
            for( Object friend : friends ) {
                StickyFriend sf = getStickyFriendListStorageManager().getFriendByName( friend.toString() );
                if( sf != null )
                    se.addTo( sf.getEmail(), sf.getName() );
            }
        }
    }

    private StickyFriend _getSelectedStickyFriend() {
        String selectedName = mFriendList.getSelectedValue().toString();
        StickyFriend sf = getStickyFriendListStorageManager().getFriendByName( selectedName );

        return sf;
    }

    private void _initFriendListPopup() {
        final JPopupMenu pop = new JPopupMenu();

        JMenuItem editItem = new JMenuItem("Edit");
        editItem.setMnemonic( 'E' );
        editItem.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent actionEvent) {
                        _editStickyFriend();
                    }
                }
        );
        pop.add( editItem );

        JMenuItem deleteItem = new JMenuItem("Delete");
        deleteItem.setMnemonic( 'D' );
        deleteItem.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent actionEvent) {
                        _deleteStickyFriend();
                    }
                }
        );
        pop.add( deleteItem );

        mFriendList.add( pop );
        mFriendList.addMouseListener(
                new MouseListener() {
                    public void mouseClicked(MouseEvent mouseEvent) {
                        if( mouseEvent.getClickCount() == 2 )
                            _editStickyFriend();
                    }

                    public void mousePressed(MouseEvent mouseEvent) {
                        if( mouseEvent.isPopupTrigger() )
                            pop.show( mFriendList, mouseEvent.getX(), mouseEvent.getY() );
                    }

                    public void mouseReleased(MouseEvent mouseEvent) {
                        if( mouseEvent.isPopupTrigger() )
                            pop.show( mFriendList, mouseEvent.getX(), mouseEvent.getY() );
                    }

                    public void mouseEntered(MouseEvent mouseEvent) {
                    }

                    public void mouseExited(MouseEvent mouseEvent) {
                    }
                }
        );
    }

    private void _deleteStickyFriend() {
        try {
            int r = JOptionPane.showConfirmDialog(
                                this, "Do you really want to give up this friend ;) ?",
                                "Confirm", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE );

            if( r == JOptionPane.OK_OPTION ) {
                String selectedName = mFriendList.getSelectedValue().toString();
                StickyFriend sf = getStickyFriendListStorageManager().getFriendByName( selectedName );
                getStickyFriendListStorageManager().deleteFriend( sf.getId() );
                _updateFriendList();
                validate();
            }
        }
        catch( Exception e ) {
            UIHelper.showErrorMessage( this, e );
            LOG.warn( e );
        }
    }

    private void _editStickyFriend() {
        try {
            String selectedName = mFriendList.getSelectedValue().toString();
            if( DEBUG )
                LOG.debug( "selectedName: "+selectedName );
            StickyFriend sf = getStickyFriendListStorageManager().getFriendByName( selectedName );

            mEditId = sf.getId();
            mNameTextField.setText( sf.getName() );
            mEmailTextField.setText( sf.getEmail() );
            mIpAddressTextField.setText( sf.getIpAddress() );
            mPortText.setText( sf.getPort()+"" );

        }
        catch( Exception e ) {
            UIHelper.showErrorMessage( this, e );
            LOG.warn( e );
        }
    }

    private void _initFriendListEvent() {

    }

    private void _initStorage() {
        mStorageManager = ApplicationManager.getApplication()
                          .getComponent( StickyFriendListStorageManager.class );
        _updateFriendList();
    }

    private void _updateFriendList() {
        try {
            mFriendList.setModel( new DefaultListModel() );
            DefaultListModel dlm = new DefaultListModel();
            mFriendList.setModel( dlm );

            List<StickyFriend> friends = getStickyFriendListStorageManager().getFriends();
            for( StickyFriend sf: friends ) {
                dlm.addElement( sf.getName() );
            }
            validate();
        }
        catch( Exception e ) {
            LOG.warn( e );
        }
    }

    private void _initUI() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(mButtonSave);


        mButtonSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        mButtonClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

// call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

// call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        _saveStickyFriend();
    }

    private void _saveStickyFriend() {

        try {
            StickyFriend sf = new StickyFriend();
            if( mEditId != null )
                sf.setId( mEditId );
            sf.setName( mNameTextField.getText() );
            sf.setEmail( mEmailTextField.getText() );
            sf.setIpAddress( mIpAddressTextField.getText() );
            sf.setPort( Integer.parseInt( mPortText.getText() ) );

            getStickyFriendListStorageManager().saveFriend( sf );

            _clearFields();
            _updateFriendList();
        }
        catch( Exception e ) {
            UIHelper.showErrorMessage( this, e );
        }
    }

    private void _clearFields() {
        mNameTextField.setText( "" );
        mEmailTextField.setText( "" );
        mIpAddressTextField.setText( "" );
        mEditId = null;
    }

    private void onCancel() {
        setSticky( null );
        dispose();
    }

    public StickyFriendListStorageManager getStickyFriendListStorageManager() {
        return mStorageManager;
    }

    public static FriendManagerUI getInstance() {
        return INSTANCE;
    }

    public void displayDialog() {
        INSTANCE.setAlwaysOnTop( true );
        INSTANCE.setTitle( "Sticky Friends ::");
        INSTANCE.setSize( 500, 400 );
        INSTANCE.setLocation( UIHelper.getCenteredLocation( INSTANCE ) );
        INSTANCE.setVisible( true );
    }
}
