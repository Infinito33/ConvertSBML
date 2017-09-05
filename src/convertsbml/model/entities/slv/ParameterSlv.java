package convertsbml.model.entities.slv;

/**
 *
 * Klasa reprezentuje pojedynczy parameter, który występuje w równaniach modelu
 * Solvary.
 *
 * @author Magda
 */
public class ParameterSlv {

    private String name;
    private String type;
    private Double value;
    private String description;

    public ParameterSlv() {

    }

    public ParameterSlv(String name, String type, Double value, String description) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Double getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Name: " + name + "  Type: " + type + "  Value: " + value + "  Description: " + description;
    }
}
