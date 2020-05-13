package myapp.presentationmodel;

import myapp.presentationmodel.person.Mountain;
import myapp.presentationmodel.presentationstate.ApplicationState;
import org.opendolphin.core.BasePresentationModel;
import org.opendolphin.core.Dolphin;

/**
 * @author Dieter Holz
 */
public interface BasePmMixin {
    //todo: for all your basePMs (as delivered by your Controllers) specify constants and getter-methods like these
    String MOUNTAIN_PROXY_PM_ID = PMDescription.MOUNTAIN.pmId(-777L);

    default BasePresentationModel getMountainProxyPM() {
        return (BasePresentationModel) getDolphin().getAt(MOUNTAIN_PROXY_PM_ID);
    }

    default Mountain getMountainProxy() {
        return new Mountain(getMountainProxyPM());
    }

    // always needed
    String APPLICATION_STATE_PM_ID = PMDescription.APPLICATION_STATE.pmId(-888);

    default BasePresentationModel getApplicationStatePM() {
        return (BasePresentationModel) getDolphin().getAt(APPLICATION_STATE_PM_ID);
    }

    default ApplicationState getApplicationState() {
        return new ApplicationState(getApplicationStatePM());
    }

    Dolphin getDolphin();

}
