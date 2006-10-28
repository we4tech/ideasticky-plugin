package com.we4tech.ideaPlugin.sticky.ui.editor;

import com.we4tech.ideaPlugin.sticky.helper.UIHelper;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.html.HTMLDocument;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import java.io.BufferedWriter;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.text.AttributedCharacterIterator;

public class XHTMLEditor extends JDialog {

    private static final Map<String, Integer> HEADING_LIST = new LinkedHashMap<String, Integer>();
    static{
        HEADING_LIST.put("H1 head line", 35);
        HEADING_LIST.put("H2 head line", 30);
        HEADING_LIST.put("H3 head line", 25);
        HEADING_LIST.put("H4 head line", 20);
        HEADING_LIST.put("H5 head line", 15);
        HEADING_LIST.put("Paragraph", 10);
        HEADING_LIST.put("Quote", 10);
    }

    /**
     * Content type for JEditorPane
     */
    private static final String CONTENT_TYPE = "text/html";

    private JPanel contentPane;
    private JTextPane mTextEditor;
    private JButton mButtonBold;
    private JButton button1;
    private JButton mButtonItalic;
    private JButton mButtonUnderline;
    private JButton mButtonFont;
    private JButton button5;
    private JComboBox mHeadingComboBox;
    private JTextPane textPane1;

    public XHTMLEditor() {
        _initEditor();
    }

    private void _initEditor() {
        setContentPane(contentPane);
        setModal(true);

        _setUpResources();
        _setUpEvents();
    }

    private void _setUpResources() {
        DefaultComboBoxModel dlm = new DefaultComboBoxModel();
        Set<String> keys = HEADING_LIST.keySet();
        for (String key : keys ) {
            dlm.addElement( key );
        }
        mHeadingComboBox.setModel( dlm );
    }

    private void _setUpEvents() {
        mHeadingComboBox.addItemListener(
                new ItemListener() {
                    public void itemStateChanged(ItemEvent itemEvent) {
                        if( itemEvent.getStateChange() == ItemEvent.SELECTED ) {
                            String key = itemEvent.getItem().toString();
                            Integer fontSize = HEADING_LIST.get( key );

                            Font cFont = mTextEditor.getFont();
                            Font font = new Font( cFont.getFamily(), cFont.getStyle(), fontSize );
                            _applyFontStyleForSelection( font );
                        }
                    }
                }
        );
        mTextEditor.addKeyListener(
                new KeyListener() {
                    public void keyTyped(KeyEvent keyEvent) {
                    }

                    public void keyPressed(KeyEvent keyEvent) {
                    }

                    public void keyReleased(KeyEvent keyEvent) {
                        if (keyEvent.isControlDown() && keyEvent.getKeyCode() == KeyEvent.VK_B) {
                            Font font = new Font( mTextEditor.getFont().getFamily(), Font.BOLD, 12 );
                            _applyFontStyleForSelection( font );
                            CharArrayWriter caw = new CharArrayWriter();
                            try {
                                mTextEditor.write( caw );
                                MutableAttributeSet set = mTextEditor.getInputAttributes();
                                Enumeration e = set.getAttributeNames();
                                while (e.hasMoreElements()) {
                                    try {
                                        StyleConstants.FontConstants at = (StyleConstants.FontConstants) e.nextElement();
                                        
                                    }
                                    catch( Exception ex ) {
                                        ex.printStackTrace();
                                    }
                                }
                                System.out.println( caw.toString());
                            } catch (IOException e) {
                                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                            }

                        }

                    }
                }
        );
    }

    private void _applyFontStyleForSelection(Font font) {
        StyledDocument doc = mTextEditor.getStyledDocument();
        MutableAttributeSet attrs = mTextEditor.getInputAttributes();
        StyleConstants.setFontFamily( attrs, font.getFamily() );
        StyleConstants.setFontSize( attrs, font.getSize() );
        StyleConstants.setBold( attrs, ((font.getStyle() & Font.BOLD) !=0 ) );
        StyleConstants.setItalic( attrs, ((font.getStyle() & Font.ITALIC) !=0 ));
        StyleConstants.setUnderline( attrs, ((font.getStyle() & Font.CENTER_BASELINE) !=0 ));

        int start = mTextEditor.getSelectionStart();
        int end = mTextEditor.getSelectionEnd();
        doc.setCharacterAttributes(start, (end-start), attrs, false);
    }

//    public MutableAttributeSet _getStyleForSelection( ) {
//        StyledDocument doc = mTextEditor.getStyledDocument();
//
//    }

    private void _setTagForSelectedText(String tagElement) {
        try {

            System.out.println( mTextEditor.getText() );
        }
        catch( Exception e ) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        XHTMLEditor dialog = new XHTMLEditor();
        dialog.pack();
        dialog.setSize( 500, 500 );
        dialog.setLocation( UIHelper.getCenteredLocation( dialog ) );
        dialog.setVisible( true );
        System.exit(0);
    }
}
