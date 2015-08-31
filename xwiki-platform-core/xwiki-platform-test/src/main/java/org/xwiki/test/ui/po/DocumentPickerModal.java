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
package org.xwiki.test.ui.po;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

/**
 * Represents the modal opened when the user changes the location with the {@link DocumentPicker}.
 * 
 * @version $Id$
 * @since 7.2M3
 */
public class DocumentPickerModal extends BaseElement
{
    private WebElement container;

    private DocumentTreeElement tree;

    public DocumentPickerModal(WebElement container)
    {
        this.container = container;
    }

    public DocumentTreeElement getTree()
    {
        if (this.tree == null) {
            this.tree = new DocumentTreeElement(this.container.findElement(By.className("xtree")));
        }
        return this.tree;
    }

    public void cancel()
    {
        this.container.findElement(By.className("btn-default")).click();
    }

    public void select()
    {
        this.container.findElement(By.className("btn-primary")).click();
    }

    public void selectDocument(String... path)
    {
        getTree().clearSelection().openToDocument(path);
        select();
    }

    public DocumentPickerModal waitForIt()
    {
        // Wait for the modal fade animation.
        getDriver().waitUntilCondition(new ExpectedCondition<Boolean>()
        {
            @Override
            public Boolean apply(WebDriver driver)
            {
                try {
                    return container.getAttribute("class").endsWith(" in");
                } catch (StaleElementReferenceException e) {
                    // The element was removed from DOM in the meantime
                    return false;
                }
            }
        });

        // Wait for the document tree to load.
        getTree().waitForIt();

        return this;
    }
}
