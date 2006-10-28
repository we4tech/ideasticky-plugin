/* $Id: StringHelper.java 117 2006-10-02 19:08:17Z  $ */
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
 * $LastChangedDate: 2006-10-02 19:08:17Z $
 * $LastChangedRevision: 117 $
 ******************************************************************************
*/
package com.we4tech.ideaPlugin.sticky.helper;

import java.awt.*;

public class StringHelper {

    /*
     * remove CrLf from multi line string and create internal keyword like "*CR* *LF*"
     * which will be re transformed to CrLf on displaying mode.
     */
    public static String removeCrLf( String value ) {
        String nValue = value.replaceAll("\n","*LF*");
        nValue = nValue.replaceAll("\r","*CR*");
        return nValue;
    }

    /**
     * Remove *CR* and *LF* and re transform to \n\r chars
     *
     * @param v string to be transformed
     * @return transformed string will be returned
     */
    public static String addLineBreak(String v) {
        v = v.replaceAll("\\*CR\\*","\r");
        v = v.replaceAll("\\*LF\\*","\n");
        return v;
    }

    /*
     * take string and return Color object where string follows the following format:
     * R,G,B example: 255,0,200
     */
    public static Color getStringToColor(String value) {
        Color color = Color.WHITE;
        try {
            if ( value != null ) {
                String[] s = value.split(",");
                int red = Integer.parseInt( s[0] );
                int green = Integer.parseInt( s[1] );
                int blue = Integer.parseInt( s[2] );

                color = new Color( red, green, blue );
            }
        }
        catch( Exception e ) {
            e.printStackTrace();
        }
        return color;
    }

    /*
     * take string and return Dimension by following format:
     * width,height
     */
    public static Dimension getStringToDimension(String value) {
        Dimension dim = new Dimension( 300,300 );
        try {
            if ( value != null ) {
                String[] s = value.split(",");
                double w = Double.parseDouble( s[0] );
                double h = Double.parseDouble( s[1] );
                dim = new Dimension( );
                dim.setSize( w, h );
            }
        }
        catch( Exception e) {
            e.printStackTrace();
        }
        return dim;
    }

    /*
     * Take string and return Point by following format:
     * X,Y
     */
    public static Point getStringToPoint(String value) {
        Point point = null;
        try {
            if ( value != null ) {
                String[] s = value.split(",");
                double x = Double.parseDouble( s[0] );
                double y = Double.parseDouble( s[1] );
                point = new Point( );
                point.setLocation( x, y );
            }
        }
        catch( Exception e ) {
            e.printStackTrace();
        }
        return point;
    }
}
