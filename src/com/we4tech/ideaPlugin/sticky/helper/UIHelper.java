/* $Id: UIHelper.java 101 2006-09-26 17:47:43Z  $ */
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
 * $LastChangedDate: 2006-09-26 17:47:43Z $
 * $LastChangedRevision: 101 $
 ******************************************************************************
*/
package com.we4tech.ideaPlugin.sticky.helper;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class UIHelper {

    public static Point getCenteredLocation(Window w) {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int x = d.width - (d.width / 2 + w.getWidth() / 2);
        int y = d.height - (d.height / 2 + w.getHeight() / 2);
        Point p = new Point(x, y);
        return p;
    }

    public static void showErrorMessage( Component c, Exception e ) {
        JOptionPane.showMessageDialog( c,
                    "Error caught: "+e.getMessage(),
                    "ERROR", JOptionPane.ERROR_MESSAGE );
    }

    public static void showSaveDialog( Component c, JTextField textField ) {
        try {
            JFileChooser jfc = new JFileChooser( textField.getText() );
            jfc.setSelectedFile( new File( textField.getText() ) );
            jfc.setMultiSelectionEnabled( false );
            jfc.setDialogType( JFileChooser.FILES_ONLY );
            jfc.showSaveDialog( c );

            if ( jfc.getSelectedFile() != null )
                textField.setText( jfc.getSelectedFile().getAbsoluteFile().toString() );
        }
        catch( Exception e ) {
            UIHelper.showErrorMessage( c, e );
        }
    }

}
