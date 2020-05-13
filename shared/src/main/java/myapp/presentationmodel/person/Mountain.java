package myapp.presentationmodel.person;

import myapp.util.veneer.*;
import org.opendolphin.core.BasePresentationModel;

/**
 * Created by Julien on 14.06.2017.
 */
public class Mountain extends PresentationModelVeneer {
    public Mountain(BasePresentationModel pm) {
        super(pm);
    }

    public final LongAttributeFX mountainId      = new LongAttributeFX(getPresentationModel()   , MountainAtt.MOUNTAINID);
    public final StringAttributeFX mountainName    = new StringAttributeFX(getPresentationModel() ,MountainAtt.MOUNTAINNAME);
    public final  IntegerAttributeFX mountainHeight     = new IntegerAttributeFX(getPresentationModel(), MountainAtt.MOUNTAINHEIGHT);
    public final  IntegerAttributeFX mountainRank     = new IntegerAttributeFX(getPresentationModel(), MountainAtt.MOUNTAINRANK);
    public final StringAttributeFX language     = new StringAttributeFX(getPresentationModel(), MountainAtt.LANGUAGE);

}
