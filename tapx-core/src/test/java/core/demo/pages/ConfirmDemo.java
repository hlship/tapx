// Copyright 2010, 2011 Howard M. Lewis Ship
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package core.demo.pages;

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;

public class ConfirmDemo {
	@Property
	@Persist(PersistenceConstants.FLASH)
	private String message;

	@InjectComponent
	private Zone zone, messageZone;

	void onActionFromLink1() {
		message = "update from link1";
	}

	Object onActionFromLink2() {
		return zone.getBody();
	}

	void onSuccessFromForm() {
		message = "update via form submit";
	}

	Object onSuccessFromZoneForm() {
		message = "update via zone form submit";

		return messageZone.getBody();
	}

	void onActionFromLink3() {
		message = "update from link3";
	}
}
