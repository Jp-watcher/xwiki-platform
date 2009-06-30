/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package com.xpn.xwiki.wysiwyg.client.plugin.macro;

import java.util.ArrayList;
import java.util.List;

import org.xwiki.gwt.dom.client.Element;
import org.xwiki.gwt.dom.client.Range;
import org.xwiki.gwt.dom.client.Selection;

import com.google.gwt.dom.client.Node;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.Widget;
import com.xpn.xwiki.wysiwyg.client.util.DeferredUpdater;
import com.xpn.xwiki.wysiwyg.client.util.Updatable;
import com.xpn.xwiki.wysiwyg.client.widget.rta.cmd.Command;
import com.xpn.xwiki.wysiwyg.client.widget.rta.cmd.CommandListener;
import com.xpn.xwiki.wysiwyg.client.widget.rta.cmd.CommandManager;

/**
 * Controls the currently selected macros.
 * 
 * @version $Id$
 */
public class MacroSelector implements Updatable, MouseListener, KeyboardListener, CommandListener
{
    /**
     * The displayer used to select macros.
     */
    private final MacroDisplayer displayer;

    /**
     * Schedules updates and executes only the most recent one.
     */
    private final DeferredUpdater updater = new DeferredUpdater(this);

    /**
     * The list of currently selected macro containers.
     */
    private final List<Element> selectedContainers = new ArrayList<Element>();

    /**
     * Creates a new macro selector.
     * 
     * @param displayer the displayer to be used for selecting the macros
     */
    public MacroSelector(MacroDisplayer displayer)
    {
        this.displayer = displayer;

        // Listen to events generated by the rich text area in order to keep track of the select macros.
        displayer.getTextArea().addMouseListener(this);
        displayer.getTextArea().addKeyboardListener(this);
        displayer.getTextArea().getCommandManager().addCommandListener(this);
    }

    /**
     * Destroys this selector.
     */
    public void destroy()
    {
        displayer.getTextArea().removeMouseListener(this);
        displayer.getTextArea().removeKeyboardListener(this);
        displayer.getTextArea().getCommandManager().removeCommandListener(this);
    }

    /**
     * {@inheritDoc}
     * 
     * @see MouseListener#onMouseDown(Widget, int, int)
     */
    public void onMouseDown(Widget sender, int x, int y)
    {
        if (sender == displayer.getTextArea()) {
            // See if the target is a selected macro.
            Element target = (Element) displayer.getTextArea().getCurrentEvent().getTarget();
            if (displayer.isMacroContainer(target) && displayer.isSelected(target)) {
                // If already selected then toggle the collapsed state.
                displayer.setCollapsed(target, !displayer.isCollapsed(target));
            } else {
                updater.deferUpdate();
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see MouseListener#onMouseEnter(Widget)
     */
    public void onMouseEnter(Widget sender)
    {
        // ignore
    }

    /**
     * {@inheritDoc}
     * 
     * @see MouseListener#onMouseLeave(Widget)
     */
    public void onMouseLeave(Widget sender)
    {
        // ignore
    }

    /**
     * {@inheritDoc}
     * 
     * @see MouseListener#onMouseMove(Widget, int, int)
     */
    public void onMouseMove(Widget sender, int x, int y)
    {
        // ignore
    }

    /**
     * {@inheritDoc}
     * 
     * @see MouseListener#onMouseUp(Widget, int, int)
     */
    public void onMouseUp(Widget sender, int x, int y)
    {
        // ignore
    }

    /**
     * {@inheritDoc}
     * 
     * @see KeyboardListener#onKeyDown(Widget, char, int)
     */
    public void onKeyDown(Widget sender, char keyCode, int modifiers)
    {
        // ignore
    }

    /**
     * {@inheritDoc}
     * 
     * @see KeyboardListener#onKeyPress(Widget, char, int)
     */
    public void onKeyPress(Widget sender, char keyCode, int modifiers)
    {
        // ignore
    }

    /**
     * {@inheritDoc}
     * 
     * @see KeyboardListener#onKeyUp(Widget, char, int)
     */
    public void onKeyUp(Widget sender, char keyCode, int modifiers)
    {
        if (sender == displayer.getTextArea()) {
            updater.deferUpdate();
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see CommandListener#onBeforeCommand(CommandManager, Command, String)
     */
    public boolean onBeforeCommand(CommandManager sender, Command command, String param)
    {
        // ignore
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see CommandListener#onCommand(CommandManager, Command, String)
     */
    public void onCommand(CommandManager sender, Command command, String param)
    {
        if (sender == displayer.getTextArea().getCommandManager()) {
            updater.deferUpdate();
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see Updatable#update()
     */
    public void update()
    {
        // Clear previously selected macros.
        for (Element container : selectedContainers) {
            displayer.setSelected(container, false);
        }
        selectedContainers.clear();

        // Mark currently selected macros.
        Selection selection = displayer.getTextArea().getDocument().getSelection();
        for (int i = 0; i < selection.getRangeCount(); i++) {
            Range range = selection.getRangeAt(i);
            if (range.getStartContainer() == range.getEndContainer()
                && range.getStartContainer().getNodeType() == Node.ELEMENT_NODE
                && range.getEndOffset() - range.getStartOffset() == 1) {
                Node selectedNode = range.getStartContainer().getChildNodes().getItem(range.getStartOffset());
                if (displayer.isMacroContainer(selectedNode)) {
                    Element container = (Element) selectedNode;
                    selectedContainers.add(container);
                    displayer.setSelected(container, true);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see Updatable#canUpdate()
     */
    public boolean canUpdate()
    {
        return displayer.getTextArea().isAttached() && displayer.getTextArea().isEnabled();
    }

    /**
     * @return the number of macros currently selected
     */
    public int getMacroCount()
    {
        return selectedContainers.size();
    }

    /**
     * @param index the index of the selected macro to return
     * @return the selected macro at the specified index
     */
    public Element getMacro(int index)
    {
        return selectedContainers.get(index);
    }

    /**
     * @return the displayer used to select and detect macros
     */
    public MacroDisplayer getDisplayer()
    {
        return displayer;
    }
}
