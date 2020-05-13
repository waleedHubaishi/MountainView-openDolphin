package myapp.presentationmodel.person;

import myapp.presentationmodel.PMDescription;
import myapp.util.AttributeDescription;
import myapp.util.ValueType;

/**
 * Created by Julien on 14.06.2017.
 */
public enum MountainAtt implements AttributeDescription {
    MOUNTAINID(ValueType.ID),
    MOUNTAINNAME(ValueType.STRING),
    MOUNTAINHEIGHT(ValueType.INT),
    MOUNTAINRANK(ValueType.INT),
    LANGUAGE(ValueType.STRING);

    private final ValueType valueType;


    MountainAtt(ValueType type) {
        valueType = type;
    }

    @Override
    public ValueType getValueType() {
        return valueType;
    }

    @Override
    public PMDescription getPMDescription() {
        return PMDescription.MOUNTAIN;
    }
}
