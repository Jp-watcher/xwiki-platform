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
package org.xwiki.livedata.internal.rest;

import java.util.Optional;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.xwiki.component.annotation.Component;
import org.xwiki.livedata.LiveDataSource;
import org.xwiki.livedata.rest.LiveDataSourceResource;
import org.xwiki.livedata.rest.model.jaxb.Source;
import org.xwiki.livedata.rest.model.jaxb.StringMap;

/**
 * Default implementation of {@link LiveDataSourceResource}.
 * 
 * @version $Id$
 * @since 12.10RC1
 */
@Component
@Named("org.xwiki.livedata.internal.rest.DefaultLiveDataSourceResource")
@Singleton
public class DefaultLiveDataSourceResource extends AbstractLiveDataResource implements LiveDataSourceResource
{
    @Override
    public Source getSource(String hint, StringMap parameters, String namespace) throws Exception
    {
        Optional<LiveDataSource> source = getLiveDataSource(hint, parameters, namespace);
        if (source.isPresent()) {
            return createSource(hint, parameters, namespace);
        } else {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
    }
}
