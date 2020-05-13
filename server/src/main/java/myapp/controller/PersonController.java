package myapp.controller;

import myapp.presentationmodel.BasePmMixin;
import myapp.presentationmodel.PMDescription;
import myapp.presentationmodel.person.Mountain;
import myapp.presentationmodel.person.MountainCommands;
import myapp.service.SomeService;
import myapp.util.Controller;
import org.opendolphin.core.Dolphin;
import org.opendolphin.core.server.DTO;
import org.opendolphin.core.server.ServerPresentationModel;
import org.opendolphin.core.server.comm.ActionRegistry;

import java.util.List;

/**
 * This is an example for an application specific controller.
 * <p>
 * Controllers may have many actions that serve a common purpose.
 * <p>
 * Todo: Replace this with your Controller
 */
class PersonController extends Controller implements BasePmMixin {

    private final SomeService service;

    private Mountain mountainProxy;

    PersonController(SomeService service) {
        this.service = service;
    }

    @Override
    public void registerCommands(ActionRegistry registry) {
        registry.register(MountainCommands.LOAD_SOME_MOUNTAINS, ($, $$) -> loadMountain());
        registry.register(MountainCommands.SAVE            , ($, $$) -> save());
        registry.register(MountainCommands.RESET           , ($, $$) -> reset(PMDescription.MOUNTAIN));
    }

    @Override
    protected void initializeBasePMs() {
        ServerPresentationModel pm = createProxyPM(PMDescription.MOUNTAIN, MOUNTAIN_PROXY_PM_ID);

        mountainProxy = new Mountain(pm);
    }

    @Override
    protected void setDefaultValues() {
        mountainProxy.mountainName.setMandatory(true);
    }

    @Override
    protected void setupValueChangedListener() {
        getApplicationState().language.valueProperty().addListener((observable, oldValue, newValue) -> translate(mountainProxy, newValue));
    }

    ServerPresentationModel loadMountain() {
        DTO dto = service.loadSomeEntity();
        ServerPresentationModel pm = createPM(PMDescription.MOUNTAIN, dto);

        mountainProxy.getPresentationModel().syncWith(pm);

        return pm;
    }

    void save() {
        List<DTO> dtos = dirtyDTOs(PMDescription.MOUNTAIN);
        service.save(dtos);
        rebase(PMDescription.MOUNTAIN);
    }

    @Override
    public Dolphin getDolphin() {
        return getServerDolphin();
    }
}
