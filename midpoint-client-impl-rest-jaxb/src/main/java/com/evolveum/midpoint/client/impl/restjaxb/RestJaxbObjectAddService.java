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

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;

import com.evolveum.midpoint.client.api.ObjectAddService;
import com.evolveum.midpoint.client.api.ObjectReference;
import com.evolveum.midpoint.client.api.TaskFuture;
import com.evolveum.midpoint.client.api.exception.*;
import com.evolveum.midpoint.xml.ns._public.common.common_3.ObjectType;

/**
 * @author semancik
 * @author katkav
 *
 */
public class RestJaxbObjectAddService<O extends ObjectType> extends AbstractObjectTypeWebResource<O> implements ObjectAddService<O> {

	private final O object;
	
	public RestJaxbObjectAddService(final RestJaxbService service, final Class<O> type, final O object) {
		super(service, type);
		this.object = object;
	}

	@Override
	public TaskFuture<ObjectReference<O>> apost() throws ObjectAlreadyExistsException, ObjectNotFoundException {
		// TODO: item object

		// if object created (sync):
		String restPath = Types.findType(getType()).getRestPath();
		Response response = getService().post(restPath, object);

		switch(response.getStatus()) {
			case 409:
				throw new ObjectAlreadyExistsException(response.getStatusInfo().getReasonPhrase());
			case 201:
			case 202:
				String oid = null;
				// Fixed location null: When you enabled policy rule in Midpoint, for instance an approval step on user's creation
				// The HTTP response is 202 without location reference
				if(response.getLocation() != null) {
					String location = response.getLocation().toString();
					String[] locationSegments = location.split(restPath + "/");
					oid = locationSegments[1];
				}
				RestJaxbObjectReference<O> ref = new RestJaxbObjectReference<>(getService(), getType(), oid);
				return new RestJaxbCompletedFuture<>(ref);
			default:
				throw new UnsupportedOperationException("Implement other status codes, unsupported return status: " + response.getStatus());
		}
		
//		switch (response.getStatus()) {
//			case 400:
//				throw new BadRequestException(response.getStatusInfo().getReasonPhrase());
//			case 250:
//				throw new PartialErrorException(response.getStatusInfo().getReasonPhrase());
//			case 401:
//			case 403:
//				throw new AuthorizationException(response.getStatusInfo().getReasonPhrase());
//			case 409:
//				throw new ObjectAlreadyExistsException(response.getStatusInfo().getReasonPhrase());
//			case 201:
//			case 202:
//				String location = response.getLocation().toString();
//				String[] locationSegments = location.split(restPath + "/");
//				oid = locationSegments[1];
//				RestJaxbObjectReference<O> ref = new RestJaxbObjectReference<>(getService(), getType(), oid);
//				return new RestJaxbCompletedFuture<>(ref);
//		default:
//			throw new UnsupportedOperationException("Implement other status codes, unsupported return status: " + response.getStatus());
//		}

	}

	
}
