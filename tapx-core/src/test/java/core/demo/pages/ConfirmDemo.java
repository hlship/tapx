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

import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;

public class ConfirmDemo {

    @Inject
    private AlertManager alertManager;

    @Persist
    @Property
    private int updates;


    @InjectComponent
    private Zone zone;

    void onActionFromLink1() {
        alertManager.info("update from link1");
    }

    void onSelectedFromFormSubmit() {
        alertManager.info("formSubmit was selected");
    }

    void onSelectedFromZoneFormSubmit() {
        alertManager.info("zoneFormSubmit was selected");
    }

    Object onActionFromLink2() {

        updates++;

        alertManager.info("update from link2");

        return zone.getBody();
    }

    void onSuccessFromForm() {
        alertManager.info("update via form submit");
    }

    Object onSuccessFromZoneForm() {

        updates++;

        alertManager.info("update via zone form submit");

        return zone.getBody();

    }

    void onActionFromLink3() {
        alertManager.info("update from link3");
    }
}
