package convertsbml.model.entities.matlab;

import java.util.ArrayList;
import java.util.List;

/**
 * Dane pochodzące z pliku status change z modelu stochastycznego Matlab.
 *
 * @author Magda
 */
public class StatusChangeMatlabData {

    private String function;
    private List<String> functionVariables;
    private String params;
    private EquationM ro;
    private EquationM roint;
    private EquationM fd;
    private EquationM a;
    private EquationM Tchange;
    private List<EquationM> equations;
    private List<EquationM> assignments;

    public StatusChangeMatlabData() {
        functionVariables = new ArrayList<>();
        equations = new ArrayList<>();
        assignments = new ArrayList<>();
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public EquationM getRo() {
        return ro;
    }

    public void setRo(EquationM ro) {
        this.ro = ro;
    }

    public EquationM getRoint() {
        return roint;
    }

    public void setRoint(EquationM roint) {
        this.roint = roint;
    }

    public EquationM getFd() {
        return fd;
    }

    public void setFd(EquationM fd) {
        this.fd = fd;
    }

    public EquationM getA() {
        return a;
    }

    public void setA(EquationM a) {
        this.a = a;
    }

    public EquationM getTchange() {
        return Tchange;
    }

    public void setTchange(EquationM Tchange) {
        this.Tchange = Tchange;
    }

    public List<EquationM> getEquations() {
        return equations;
    }

    public void addEquations(EquationM equation) {
        equations.add(equation);
    }

    public List<String> getFunctionVariables() {
        return functionVariables;
    }

    public void addFunctionVariables(String functionVariable) {
        this.functionVariables.add(functionVariable);
    }

    public List<EquationM> getAssignments() {
        return assignments;
    }

    public void addAssignments(EquationM assignment) {
        this.assignments.add(assignment);
    }
}
