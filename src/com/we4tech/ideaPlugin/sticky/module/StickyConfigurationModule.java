/* $Id: StickyConfigurationModule.java 118 2006-10-02 19:38:09Z  $ */
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
 * $LastChangedDate: 2006-10-02 19:38:09Z $
 * $LastChangedRevision: 118 $
 ******************************************************************************
*/
package com.we4tech.ideaPlugin.sticky.module;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.we4tech.ideaPlugin.sticky.ui.config.StickyConfigurationUI;
import com.we4tech.ideaPlugin.sticky.event.StickyConfigurationChangeListener;
import com.we4tech.ideaPlugin.sticky.helper.IconHelper;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.Nullable;
import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;

import javax.swing.*;

public class StickyConfigurationModule implements ApplicationComponent, Configurable {

    private Logger LOG = LogManager.getLogger( StickyConfigurationModule.class );
    private final boolean DEBUG = LOG.isDebugEnabled();
    private final boolean INFO = LOG.isInfoEnabled();

    private StickyConfigurationUI ui = null;
    private boolean changed = false;

    @NonNls
    public String getComponentName() {
        return "StickyConfigurationModule";
    }

    public void initComponent() {
        if ( INFO )
            LOG.info( "Init - configuration manager" );
    }

    public void disposeComponent() {
        if ( INFO )
            LOG.info( "Dispose - configuraiton manager" );
    }

    public String getDisplayName() {
        return "IdeaSticky";
    }

    public Icon getIcon() {
        return IconHelper.STICKY_CONFIG_ICON;
    }

    @Nullable
    @NonNls
    public String getHelpTopic() {
        return null;
    }

    public JComponent createComponent() {
        if ( DEBUG )
            LOG.debug( "creating Configuration component" );

        ui = StickyConfigurationUI.getStickyConfigurationUI();
        ui.setStickyConfigurationChangeListener(
                new StickyConfigurationChangeListener() {
                    public void configurationChanged() {
                        changed = true;
                    }
                }
        );
        return ui.getStickyConfigurationComponent();
    }

    public boolean isModified() {
        return changed;
    }

    public void apply() throws ConfigurationException {
        ui.onSave();
        changed = false;
    }

    public void reset() {
    }

    public void disposeUIResources() {
        ui = null;
    }
}
