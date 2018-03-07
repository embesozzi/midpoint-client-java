/**
 * Copyright (c) 2017-2018 Evolveum
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.evolveum.midpoint.client.impl.restjaxb;

import com.evolveum.midpoint.client.api.ObjectReference;
import com.evolveum.midpoint.client.api.exception.AuthenticationException;
import com.evolveum.midpoint.client.api.exception.ObjectNotFoundException;
import com.evolveum.midpoint.xml.ns._public.common.common_3.ObjectType;

/**
 * @author semancik
 *
 */
public class RestJaxbObjectReference<O extends ObjectType> extends AbstractObjectWebResource<O> implements ObjectReference<O> {

	public RestJaxbObjectReference(RestJaxbService service, Class<O> type, String oid) {
		super(service, type, oid);
	}

	private O object = null;
	
	@Override
	public String getOid() {
		return super.getOid();
	}

	@Override
	public Class<O> getType() {
		return super.getType();
	}

	@Override
	public O getObject() {
		return object;
	}

	@Override
	public boolean containsObject() {
		return object != null;
	}

	@Override
	public O get() throws ObjectNotFoundException, AuthenticationException {
		if (object == null) {
			object = getService().getObject(getType(), getOid());
		}
		return object;
	}

}
