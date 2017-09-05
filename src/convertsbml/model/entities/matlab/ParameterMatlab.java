package convertsbml.model.entities.matlab;

/**
 *
 * Klasa reprezentuje pojedynczy parameter, który występuje w równaniach modelu
 * Matlab.
 *
 * @author Magda
 */
public class ParameterMatlab {

    private String name;
    private String value;
    private String full;
    private String comment;

    public ParameterMatlab() {

    }

    public ParameterMatlab(String name, String value, String full, String comment) {
        this.name = name;
        this.value = value;
        this.full = full;
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getFull() {
        return full;
    }

    public void setFull(String full) {
        this.full = full;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
