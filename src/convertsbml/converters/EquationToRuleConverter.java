package convertsbml.converters;

import convertsbml.model.entities.matlab.EquationM;
import convertsbml.model.entities.slv.EquationSlv;
import org.sbml.libsbml.ASTNode;
import org.sbml.libsbml.AlgebraicRule;
import org.sbml.libsbml.AssignmentRule;
import org.sbml.libsbml.RateRule;
import org.sbml.libsbml.libsbml;

/**
 * Konwerter obiektu równania {@link Equation} do reguły {@link RateRule}.
 *
 * @author Magda
 */
public class EquationToRuleConverter {

    //ID reguły
    private String metaid;

    /**
     * Inicjalizacja danych konwertera.
     */
    public void initConverter() {
        metaid = "metaid_0000001";
    }

    /**
     * Domyślny konstruktor konwertera, który inicjalizuje dane.
     */
    public EquationToRuleConverter() {
        initConverter();
    }

    /**
     * Funkcja konwertująca równanie do reguły.
     *
     * @param equationSlv równanie wejściowe - slv.
     * @param rateRule reguła wyjściowa.
     */
    public void convertToRateRuleFrom(EquationSlv equationSlv, RateRule rateRule) {
        rateRule.setMetaId(metaid);
        rateRule.setVariable(equationSlv.getLeftSideName());

        //Zastąpienie wszystkich nawiasów klamrowych nawiasami prostymi.
        String replacedLeft = equationSlv.getRightSide().replaceAll("\\{", "\\(");
        String replacedRight = replacedLeft.replaceAll("\\}", "\\)");

        //Przetworzenie równania na drzewo z node'ami, które można bezpośrednio wstawić do SBML.
        ASTNode node = libsbml.parseFormula(replacedRight);
        rateRule.setMath(node);

        updateMetaId();
    }

    /**
     * Funkcja konwertująca równanie do reguły.
     *
     * @param equationM równanie wejściowe - matlab.
     * @param rateRule reguła wyjściowa.
     */
    public void convertToRateRuleFrom(EquationM equationM, RateRule rateRule) {
        rateRule.setMetaId(metaid);
        String variable = equationM.getLeftSide();
        if (equationM.getLeftSide().startsWith("dy")) {
            variable = equationM.getLeftSide().substring(1).replace("(", "").replace(")", "");
        }
        rateRule.setVariable(variable);

        //Przetworzenie równania na drzewo z node'ami, które można bezpośrednio wstawić do SBML.
        ASTNode node = libsbml.parseFormula(equationM.getRightSide());

        rateRule.setMath(node);

        updateMetaId();
    }

    /**
     * Funkcja konwertująca równanie do reguły.
     *
     * @param equationM równanie wejściowe - matlab.
     * @param algebraicRule reguła wyjściowa.
     */
    public void convertToAlgebraicRuleFrom(EquationM equationM, AlgebraicRule algebraicRule) {
        algebraicRule.setMetaId(metaid);

        algebraicRule.setVariable(equationM.getLeftSide());

        //Przetworzenie równania na drzewo z node'ami, które można bezpośrednio wstawić do SBML.
        ASTNode node = libsbml.parseFormula(equationM.getRightSide());

        algebraicRule.setMath(node);

        updateMetaId();
    }

    /**
     * Aktualizacja ID reguły.
     */
    protected void updateMetaId() {
        int underscoreIndex = metaid.indexOf("_");
        //Wyciągnięcie samej liczby z ID: metaid_xxxxxx gdzie xxxxxx to liczba
        String valueStr = metaid.substring(underscoreIndex + 1);
        Integer value = Integer.parseInt(valueStr);
        //Zwiększenie liczby i zapisanie prawidłowej wartości jako String.
        value++;
        if (value < 10) {
            metaid = "metaid_000000" + value;
        } else if (value < 100) {
            metaid = "metaid_00000" + value;
        } else if (value < 1000) {
            metaid = "metaid_0000" + value;
        } else {
            metaid = "metaid_000" + value;
        }
    }

    /**
     * Funkcja konwertująca równanie do reguły.
     *
     * @param equationM równanie wejściowe - matlab.
     * @param assignmentRule reguła wyjściowa.
     */
    public void convertToAssignmentRuleFrom(EquationM equationM, AssignmentRule assignmentRule) {
        assignmentRule.setMetaId(metaid);

        assignmentRule.setVariable(equationM.getLeftSide());

        //Przetworzenie równania na drzewo z node'ami, które można bezpośrednio wstawić do SBML.
        ASTNode node = libsbml.parseFormula(equationM.getRightSide());

        assignmentRule.setMath(node);

        updateMetaId();
    }

}
