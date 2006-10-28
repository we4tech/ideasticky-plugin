/* $Id: StickyConfigurationUI.java 116 2006-10-02 05:27:41Z  $ */
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
 * $LastChangedDate: 2006-10-02 05:27:41Z $
 * $LastChangedRevision: 116 $
 ******************************************************************************
*/
package com.we4tech.ideaPlugin.sticky.ui.config;

import com.we4tech.ideaPlugin.sticky.storage.GlobalConfigurationManager;
import com.we4tech.ideaPlugin.sticky.storage.GlobalConfiguration;
import com.we4tech.ideaPlugin.sticky.helper.UIHelper;
import com.we4tech.ideaPlugin.sticky.event.StickyConfigurationChangeListener;

import javax.swing.*;
import java.awt.event.*;

import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;

public class StickyConfigurationUI extends JDialog {

    private Logger LOG = LogManager.getLogger( StickyConfigurationUI.class );
    private final boolean DEBUG = LOG.isDebugEnabled();
    private final boolean INFO = LOG.isInfoEnabled();

    private JPanel contentPane;
    private JTabbedPane tabbedPane1;

    private JButton mButtonSave;
    private JButton mButtonCancel;
    private JButton mButtonBrowseStickyStorage;
    private JButton mButtonBrowseStickyAddressBookStorage;

    private JTextField mStickyStorageText;
    private JTextField mAddressBookStorageText;
    private JTextField mIpAddressText;
    private JTextField mPortText;
    private JTextField mSmtpHost;
    private JTextField mSmtpPort;
    private JTextField mSmtpUser;
    private JPasswordField mSmtpPassword;
    private JTextField mMailSenderText;

    private boolean mChanged=false;

    private StickyConfigurationChangeListener mChangeListener;

    public StickyConfigurationUI() {
        _initUI();
        _loadConfiguration();
        _addBrowseEvents();
        _setUpChangeEvent();
    }

    public void setStickyConfigurationChangeListener( StickyConfigurationChangeListener l) {
        this.mChangeListener = l;
    }

    private void _setUpChangeEvent() {
        KeyListener kl = new ChangeListener();

        mStickyStorageText.addKeyListener( kl );
        mAddressBookStorageText.addKeyListener( kl );
        mIpAddressText.addKeyListener( kl );
        mPortText.addKeyListener( kl );
        mSmtpHost.addKeyListener( kl );
        mSmtpPort.addKeyListener( kl );
        mMailSenderText.addKeyListener( kl );
        mSmtpUser.addKeyListener( kl );
        mSmtpPassword.addKeyListener( kl );

    }

    private class ChangeListener implements KeyListener {
        public void keyTyped(KeyEvent keyEvent) {
        }

        public void keyPressed(KeyEvent keyEvent) {
        }

        public void keyReleased(KeyEvent keyEvent) {
            mChanged = true;
            if (mChangeListener != null)
                mChangeListener.configurationChanged();
        }
    }

    public boolean isChanged() {
        return mChanged;
    }

    private void _addBrowseEvents() {
        mButtonBrowseStickyAddressBookStorage.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent actionEvent) {
                        UIHelper.showSaveDialog( getContentPane(), mAddressBookStorageText );
                    }
                }
        );

        mButtonBrowseStickyStorage.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent actionEvent) {
                        UIHelper.showSaveDialog( getContentPane(), mStickyStorageText );
                    }
                }
        );
    }

    private void _loadConfiguration() {
        try {
            if( DEBUG )
                LOG.debug( "loading configuration file " );
            GlobalConfiguration gConf = GlobalConfigurationManager.getInstance();

            // populate text file with global properties
            mStickyStorageText.setText( gConf.getStickyStorageFile() );
            mAddressBookStorageText.setText( gConf.getAddressBookStorageFile() );

            mIpAddressText.setText( gConf.getIPAddress() );
            mPortText.setText( gConf.getPort()+"" );

            mSmtpHost.setText( gConf.getSMTPHost() );
            mSmtpPort.setText( gConf.getSMTPPort()+"" );
            mMailSenderText.setText( gConf.getMailFrom() );
            mSmtpUser.setText( gConf.getSMTPUser() );
            mSmtpPassword.setText( gConf.getSMTPPassword() );
        }
        catch( Exception e ) {
            UIHelper.showErrorMessage( this, e );
        }
    }

    private void _initUI() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(mButtonSave);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    public void hideButtons() {
        mButtonCancel.setVisible( false );
        mButtonSave.setVisible( false );
    }

    public void onSave() {
        _saveGlobalConfiguration();
    }

    private boolean _saveGlobalConfiguration() {
        boolean saved = false;
        try {
            GlobalConfiguration gConf = GlobalConfigurationManager.getInstance();

            gConf.setStickyStorageFile( mStickyStorageText.getText() );
            gConf.setAddressBookStorageFile( mAddressBookStorageText.getText() );

            gConf.setIPAddress( mIpAddressText.getText() );
            gConf.setPort( Integer.parseInt( mPortText.getText() ) );

            gConf.setSMTPHost( mSmtpHost.getText() );
            gConf.setSMTPPort( Integer.parseInt( mSmtpPort.getText() ) );
            gConf.setMailFrom( mMailSenderText.getText() );
            gConf.setSMTPUser( mSmtpUser.getText() );
            gConf.setSMTPPassword( new String( mSmtpPassword.getPassword() ) );

            gConf.saveConfiguration( mChanged );
            saved = true;
            mChanged = false;
        }
        catch( Exception e) {
            UIHelper.showErrorMessage( this, e );
        }

        return saved;
    }

    private void onCancel() {
        dispose();
    }

    public static void showDialog() {
        StickyConfigurationUI dialog = new StickyConfigurationUI();
        dialog.setTitle( "Sticky configuration ::" );
        dialog.pack();
        dialog.setModal( true );
        dialog.setAlwaysOnTop( true );
        dialog.setLocationByPlatform( true );
        dialog.setVisible( true );
    }

    public static StickyConfigurationUI getStickyConfigurationUI() {
        StickyConfigurationUI dialog = new StickyConfigurationUI();
        dialog.setTitle( "Sticky configuration ::" );
        dialog.pack();

        return dialog;
    }

    public JComponent getStickyConfigurationComponent() {
        return (JComponent)getComponent(0);
    }


    public static void main(String[] args) {
        StickyConfigurationUI.showDialog();
    }
}
