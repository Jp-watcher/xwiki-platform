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
package org.xwiki.ratings.internal;

import javax.inject.Singleton;

import org.xwiki.component.annotation.Component;
import org.xwiki.ratings.RatingsConfiguration;

/**
 * Default configuration for Ratings.
 * By default the configuration considers a scale of 5, doesn't use a dedicated core, stores the zero values and the
 * average. And it uses the solr manager.
 * FIXME: Change this to be based on a RatingConfiguration document.
 *
 * @version $Id$
 * @since 12.9RC1
 */
@Component
@Singleton
public class DefaultRatingsConfiguration implements RatingsConfiguration
{
    @Override
    public boolean storeZero()
    {
        return true;
    }

    @Override
    public int getScale()
    {
        return 5;
    }

    @Override
    public boolean hasDedicatedCore()
    {
        return false;
    }

    @Override
    public boolean storeAverage()
    {
        return true;
    }

    @Override
    public String getStorageHint()
    {
        return "solr";
    }

    @Override
    public String getAverageRatingStorageHint()
    {
        return "xobject";
    }
}
