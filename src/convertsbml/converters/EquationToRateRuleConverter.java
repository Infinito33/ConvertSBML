package convertsbml.converters;

import convertsbml.model.entities.slv.EquationSlv;
import org.sbml.libsbml.ASTNode;
import org.sbml.libsbml.RateRule;
import org.sbml.libsbml.libsbml;

/**
 * Konwerter obiektu równania {@link Equation} do reguły {@link RateRule}.
 *
 * @author Magda
 */
public class EquationToRateRuleConverter {

    //ID reguły
    private String metaid;

    /**
     * Domyślny konstruktor konwertera, który inicjalizuje dane.
     */
    public EquationToRateRuleConverter() {
        initConverter();
    }

    /**
     * Inicjalizacja danych konwertera.
     */
    public void initConverter() {
        metaid = "metaid_0000001";
    }

    /**
     * Funkcja konwertująca równanie do reguły.
     *
     * @param equationSlv równanie wejściowe.
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
     * Aktualizacja ID reguły.
     */
    private void updateMetaId() {
        int underscoreIndex = metaid.indexOf("_");
        String valueStr = metaid.substring(underscoreIndex + 1);
        Integer value = Integer.parseInt(valueStr);
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

}
