package convertsbml.model.entities.slv;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Klasa ta zawiera model danych takich jak równania, parametry, reguły oraz
 * inne ogólne informacje.
 *
 * @author Magda.
 */
public class ModelSlv {

    // plik modelu
    private File slvFile;
    // lista równań
    private List<EquationSlv> equations;
    // lista parametrów
    private List<ParameterSlv> parameters;
    // lista reguł
    private List<RuleSlv> rules;
    // zawartosc pliku slv (w wielu liniach)
    private List<String> content;
    // nazwa modelu
    private String name;

    // konstruktor tworzący puste listy
    public ModelSlv() {
        equations = new ArrayList<>();
        parameters = new ArrayList<>();
        rules = new ArrayList<>();
        content = new ArrayList<>();
    }

    public ModelSlv(List<EquationSlv> equations, List<ParameterSlv> parameters, List<RuleSlv> rules) {
        this.equations = equations;
        this.parameters = parameters;
        this.rules = rules;
    }

    public List<EquationSlv> getEquations() {
        return equations;
    }

    public void setEquations(List<EquationSlv> equations) {
        this.equations = equations;
    }

    public List<ParameterSlv> getParameters() {
        return parameters;
    }

    public void setParameters(List<ParameterSlv> parameters) {
        this.parameters = parameters;
    }

    public List<RuleSlv> getRules() {
        return rules;
    }

    public void setRules(List<RuleSlv> rules) {
        this.rules = rules;
    }

    public List<String> getContent() {
        return content;
    }

    public void setContent(List<String> content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public File getSlvFile() {
        return slvFile;
    }

    public void setSlvFile(File slvFile) {
        this.slvFile = slvFile;
    }

}
