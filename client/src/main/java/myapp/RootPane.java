package myapp;

import javafx.beans.binding.Bindings;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.shape.Circle;
import myapp.presentationmodel.BasePmMixin;
import myapp.presentationmodel.person.Mountain;
import myapp.presentationmodel.person.MountainAtt;
import myapp.presentationmodel.person.MountainCommands;
import myapp.presentationmodel.presentationstate.ApplicationState;
import myapp.presentationmodel.presentationstate.ApplicationStateAtt;
import myapp.util.AdditionalTag;
import myapp.util.Language;
import myapp.util.ViewMixin;
import myapp.util.veneer.AttributeFX;
import myapp.util.veneer.BooleanAttributeFX;
import org.opendolphin.binding.Converter;
import org.opendolphin.binding.JFXBinder;
import org.opendolphin.core.Attribute;
import org.opendolphin.core.Dolphin;
import org.opendolphin.core.PresentationModel;
import org.opendolphin.core.Tag;
import org.opendolphin.core.client.ClientDolphin;
import org.opendolphin.core.client.ClientPresentationModel;

/**
 * Implementation of the view details, event handling, and binding.
 *
 * @author Dieter Holz
 *
 * todo : Replace it with your application UI
 */
class RootPane extends GridPane implements ViewMixin, BasePmMixin {

    private static final String DIRTY_STYLE     = "dirty";
    private static final String INVALID_STYLE   = "invalid";
    private static final String MANDATORY_STYLE = "mandatory";

    // clientDolphin is the single entry point to the PresentationModel-Layer
    private final ClientDolphin clientDolphin;

    private Label headerLabel;

    private Label idLabel;
    private Label idField;

    private Label     nameLabel;
    private TextField nameField;

    private Label     heightLabel;
    private TextField heightField;

    private Label     rankLabel;
    private TextField rankField;

    Image mountainImageName;
    ImageView mountainImg;
    final Circle clip = new Circle(80, 80,80);




    private Button saveButton;
    private Button resetButton;
    private Button nextButton;
    private Button germanButton;
    private Button englishButton;

    private final Mountain mountainProxy;

    //always needed
    private final ApplicationState ps;

    RootPane(ClientDolphin clientDolphin) {
        this.clientDolphin = clientDolphin;
        ps = getApplicationState();
        mountainProxy = getMountainProxy();

        init();
    }

    @Override
    public Dolphin getDolphin() {
        return clientDolphin;
    }

    @Override
    public void initializeSelf() {
        addStylesheetFiles("/fonts/fonts.css", "/myapp/myApp.css");
        getStyleClass().add("rootPane");
    }

    @Override
    public void initializeParts() {
        headerLabel = new Label();
        headerLabel.getStyleClass().add("heading");

        idLabel = new Label();
        idField = new Label();

        nameLabel = new Label();
        nameField = new TextField();

        heightLabel = new Label();
        heightField = new TextField();

        rankLabel = new Label();
        rankField = new TextField();



        mountainImageName = new Image("/0.jpg");

        mountainImg = new ImageView();
        mountainImg.setClip(clip);

        mountainImg.setImage(mountainImageName);


        mountainImg.setFitHeight(160.0);
        mountainImg.setFitWidth(160.0);



        saveButton    = new Button("Save");
        resetButton   = new Button("Reset");
        nextButton    = new Button("Next");
        germanButton  = new Button("German");
        englishButton = new Button("English");
    }

    @Override
    public void layoutParts() {
        ColumnConstraints grow = new ColumnConstraints();
        grow.setHgrow(Priority.ALWAYS);


        idField.setId("idField");

        idLabel.setId("bolder");
        nameLabel.setId("bolder");
        heightLabel.setId("bolder");
        rankLabel.setId("bolder");


        getColumnConstraints().setAll(new ColumnConstraints(), grow);
        setVgrow(headerLabel, Priority.ALWAYS);

        add(headerLabel    , 0, 0, 5, 1);
        add(idLabel        , 0, 1);
        add(idField        , 1, 1, 4, 1);
        add(nameLabel      , 0, 2);
        add(nameField      , 1, 2, 4, 1);
        add(heightLabel       , 0, 3);
        add(heightField       , 1, 3, 4, 1);
        add(rankLabel       , 0, 4);
        add(rankField       , 1, 4, 4, 1);
        add(mountainImg,5,0,3,8);
        HBox buttonBox = new HBox(10, saveButton, resetButton, nextButton, germanButton, englishButton);
        buttonBox.setId("buttonBox");
        add(buttonBox, 1, 6, 4, 1);
    }

    @Override
    public void setupEventHandlers() {
        // all events either send a command (needs to be registered in a controller on the server side)
        // or set a value on an Attribute

        ApplicationState ps = getApplicationState();
        saveButton.setOnAction(   $ -> clientDolphin.send(MountainCommands.SAVE));
        resetButton.setOnAction(  $ -> clientDolphin.send(MountainCommands.RESET));
        nextButton.setOnAction(   $ -> clientDolphin.send(MountainCommands.LOAD_SOME_MOUNTAINS));

        germanButton.setOnAction( $ -> ps.language.setValue(Language.GERMAN));
        englishButton.setOnAction($ -> ps.language.setValue(Language.ENGLISH));
    }

    @Override
    public void setupValueChangedListeners() {
        mountainProxy.mountainName.dirtyProperty().addListener((observable, oldValue, newValue)    -> updateStyle(nameField      , DIRTY_STYLE, newValue));
        mountainProxy.mountainHeight.dirtyProperty().addListener((observable, oldValue, newValue)     -> updateStyle(heightField       , DIRTY_STYLE, newValue));
        mountainProxy.mountainRank.dirtyProperty().addListener((observable, oldValue, newValue)     -> updateStyle(rankField       , DIRTY_STYLE, newValue));


        mountainProxy.mountainName.validProperty().addListener((observable, oldValue, newValue)    -> updateStyle(nameField      , INVALID_STYLE, !newValue));
        mountainProxy.mountainHeight.validProperty().addListener((observable, oldValue, newValue)     -> updateStyle(heightField       , INVALID_STYLE, !newValue));
        mountainProxy.mountainRank.validProperty().addListener((observable, oldValue, newValue)     -> updateStyle(rankField       , INVALID_STYLE, !newValue));


        mountainProxy.mountainName.mandatoryProperty().addListener((observable, oldValue, newValue)    -> updateStyle(nameField      , MANDATORY_STYLE, newValue));
        mountainProxy.mountainHeight.mandatoryProperty().addListener((observable, oldValue, newValue)     -> updateStyle(heightField       , MANDATORY_STYLE, newValue));
        mountainProxy.mountainRank.mandatoryProperty().addListener((observable, oldValue, newValue)     -> updateStyle(rankField       , MANDATORY_STYLE, newValue));


    }

    @Override
    public void setupBindings() {
        //setupBindings_DolphinBased();
        setupBindings_VeneerBased();
    }

    private void setupBindings_DolphinBased() {
        // you can fetch all existing PMs from the modelstore via clientDolphin
        ClientPresentationModel mountainProxyPM = clientDolphin.getAt(BasePmMixin.MOUNTAIN_PROXY_PM_ID);

        //JFXBinder is ui toolkit agnostic. We have to use Strings
        JFXBinder.bind(MountainAtt.MOUNTAINNAME.name())
                 .of(mountainProxyPM)
                 .using(value -> value + ", " + mountainProxyPM.getAt(MountainAtt.MOUNTAINHEIGHT.name()).getValue())
                 .to("text")
                 .of(headerLabel);

       /* JFXBinder.bind(MountainAtt.MOUNTAINHEIGHT.name())
                 .of(mountainProxyPM)
                 .using(value -> mountainProxyPM.getAt(MountainAtt.MOUNTAINHEIGHT.name()).getValue() + ", " + value)
                 .to("text")
                 .of(headerLabel);

        JFXBinder.bind(MountainAtt.MOUNTAINRANK.name())
                .of(mountainProxyPM)
                .using(value -> mountainProxyPM.getAt(MountainAtt.MOUNTAINRANK.name()).getValue() + ", " + value)
                .to("text")
                .of(rankLabel);
*/
        JFXBinder.bind(MountainAtt.MOUNTAINNAME.name(), Tag.LABEL).of(mountainProxyPM).to("text").of(nameLabel);
        JFXBinder.bind(MountainAtt.MOUNTAINNAME.name()).of(mountainProxyPM).to("text").of(nameField);
        JFXBinder.bind("text").of(nameField).to(MountainAtt.MOUNTAINNAME.name()).of(mountainProxyPM);

        JFXBinder.bind(MountainAtt.MOUNTAINHEIGHT.name(), Tag.LABEL).of(mountainProxyPM).to("text").of(heightLabel);
        JFXBinder.bind(MountainAtt.MOUNTAINHEIGHT.name()).of(mountainProxyPM).to("text").of(heightField);
        Converter toIntConverter = value -> {
            try {
                int newValue = Integer.parseInt(value.toString());
                mountainProxyPM.getAt(MountainAtt.MOUNTAINHEIGHT.name(), AdditionalTag.VALID).setValue(true);
                mountainProxyPM.getAt(MountainAtt.MOUNTAINHEIGHT.name(), AdditionalTag.VALIDATION_MESSAGE).setValue("OK");

                return newValue;
            } catch (NumberFormatException e) {
                mountainProxyPM.getAt(MountainAtt.MOUNTAINHEIGHT.name(), AdditionalTag.VALID).setValue(false);
                mountainProxyPM.getAt(MountainAtt.MOUNTAINHEIGHT.name(), AdditionalTag.VALIDATION_MESSAGE).setValue("Not a number");
                return mountainProxyPM.getAt(MountainAtt.MOUNTAINHEIGHT.name()).getValue();
            }
        };
        JFXBinder.bind("text").of(heightField).using(toIntConverter).to(MountainAtt.MOUNTAINHEIGHT.name()).of(mountainProxyPM);

        JFXBinder.bind(MountainAtt.MOUNTAINRANK.name(), Tag.LABEL).of(mountainProxyPM).to("text").of(rankLabel);
        JFXBinder.bind(MountainAtt.MOUNTAINRANK.name()).of(mountainProxyPM).to("text").of(rankField);
        Converter toIntConverter2 = value -> {
            try {
                int newValue = Integer.parseInt(value.toString());
                mountainProxyPM.getAt(MountainAtt.MOUNTAINRANK.name(), AdditionalTag.VALID).setValue(true);
                mountainProxyPM.getAt(MountainAtt.MOUNTAINRANK.name(), AdditionalTag.VALIDATION_MESSAGE).setValue("OK");

                return newValue;
            } catch (NumberFormatException e) {
                mountainProxyPM.getAt(MountainAtt.MOUNTAINRANK.name(), AdditionalTag.VALID).setValue(false);
                mountainProxyPM.getAt(MountainAtt.MOUNTAINRANK.name(), AdditionalTag.VALIDATION_MESSAGE).setValue("Not a number");
                return mountainProxyPM.getAt(MountainAtt.MOUNTAINRANK.name()).getValue();
            }
        };
        JFXBinder.bind("text").of(rankField).using(toIntConverter2).to(MountainAtt.MOUNTAINRANK.name()).of(mountainProxyPM);

        Converter not = value -> !(boolean) value;
        JFXBinder.bindInfo(Attribute.DIRTY_PROPERTY).of(mountainProxyPM).using(not).to("disable").of(saveButton);
        JFXBinder.bindInfo(Attribute.DIRTY_PROPERTY).of(mountainProxyPM).using(not).to("disable").of(resetButton);

        PresentationModel presentationStatePM = clientDolphin.getAt(BasePmMixin.APPLICATION_STATE_PM_ID);

        JFXBinder.bind(ApplicationStateAtt.LANGUAGE.name()).of(presentationStatePM).using(value -> value.equals(Language.GERMAN.name())).to("disable").of(germanButton);
        JFXBinder.bind(ApplicationStateAtt.LANGUAGE.name()).of(presentationStatePM).using(value -> value.equals(Language.ENGLISH.name())).to("disable").of(englishButton);
    }

    private void setupBindings_VeneerBased(){
        headerLabel.textProperty().bind(mountainProxy.mountainName.valueProperty().concat(", ").concat(mountainProxy.mountainHeight.valueProperty()));

        idLabel.textProperty().bind(mountainProxy.mountainId.labelProperty());
        idField.textProperty().bind(mountainProxy.mountainId.valueProperty().asString());

        setupBinding(nameLabel   , nameField      , mountainProxy.mountainName);
        setupBinding(heightLabel    , heightField       , mountainProxy.mountainHeight);
        setupBinding(rankLabel    , rankField       , mountainProxy.mountainRank);

        germanButton.disableProperty().bind(Bindings.createBooleanBinding(() -> Language.GERMAN.equals(ps.language.getValue()), ps.language.valueProperty()));
        englishButton.disableProperty().bind(Bindings.createBooleanBinding(() -> Language.ENGLISH.equals(ps.language.getValue()), ps.language.valueProperty()));

        saveButton.disableProperty().bind(mountainProxy.dirtyProperty().not());
        resetButton.disableProperty().bind(mountainProxy.dirtyProperty().not());
    }

    private void setupBinding(Label label, TextField field, AttributeFX attribute) {
        setupBinding(label, attribute);

        field.textProperty().bindBidirectional(attribute.userFacingStringProperty());
        field.tooltipProperty().bind(Bindings.createObjectBinding(() -> new Tooltip(attribute.getValidationMessage()),
                                                                  attribute.validationMessageProperty()
                                                                 ));
    }

    private void setupBinding(Label label, CheckBox checkBox, BooleanAttributeFX attribute) {
        setupBinding(label, attribute);
        checkBox.selectedProperty().bindBidirectional(attribute.valueProperty());

    }

    private void setupBinding(Label label, AttributeFX attribute){
        label.textProperty().bind(Bindings.createStringBinding(() -> attribute.getLabel() + (attribute.isMandatory() ? " *" : "  "),
                                                               attribute.labelProperty(),
                                                               attribute.mandatoryProperty()));

    }

    private void updateStyle(Node node, String style, boolean value){
        if(value){
            node.getStyleClass().add(style);
        }
        else {
            node.getStyleClass().remove(style);
        }
    }
}
