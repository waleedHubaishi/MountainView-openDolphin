package myapp.presentationmodel.person;

/**
 * Created by Julien on 14.06.2017.
 */
public interface MountainCommands {
    String LOAD_SOME_MOUNTAINS = unique("loadSomeMountains");
    String SAVE             = unique("save");
    String RESET            = unique("reset");

    static String unique(String key) {
        return MountainCommands.class.getName() + "." + key;
    }

}
