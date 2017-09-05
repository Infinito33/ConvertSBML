package convertsbml.model.entities.slv;

/**
 * Klasa reprezentuje pojedynczą regułę, która może wystąpić w modelu.
 *
 * @author Magda
 */
public class RuleSlv {

    private String name;
    private String time;
    private String units;
    private String value;

    public RuleSlv() {

    }

    public RuleSlv(String name, String time, String units, String value) {
        this.name = name;
        this.time = time;
        this.units = units;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public String getUnits() {
        return units;
    }

    public String getValue() {
        return value;
    }
}
