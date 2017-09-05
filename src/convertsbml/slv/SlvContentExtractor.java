package convertsbml.slv;

import convertsbml.converters.NumberWithENotationConverter;
import convertsbml.model.entities.slv.EquationSlv;
import convertsbml.model.entities.slv.ParameterSlv;
import convertsbml.model.entities.slv.RuleSlv;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Klasa odpowiedzialna za odczytywanie takich elementów z pliku *.slv jak
 * {@link EquationSlv}, {@link ParameterSlv} i {@link RuleSlv}.
 *
 * @author Magda
 */
public class SlvContentExtractor {

    /**
     * Wydobywa wszystkie równania z podanego {@link String}.
     *
     * @param value string z równaniami.
     * @return lista {@link EquationSlv}.
     */
    public List<EquationSlv> extractEquationsFrom(String value) {
        String[] eqs = value.split("\\n");
        List<EquationSlv> equations = new ArrayList<>();
        for (String e : eqs) {
            String[] sides = e.split("=");

            EquationSlv eq = new EquationSlv(e, sides[0], sides[1]);
            equations.add(eq);
        }
        return equations;
    }

    /**
     * Wydobywa wszystkie parametry z {@link NodeList}.
     *
     * @param parameters {@link NodeList} z parametrami.
     * @return lista {@link ParameterSlv}.
     */
    public List<ParameterSlv> extractParametersFrom(NodeList parameters) {
        List<ParameterSlv> params = new ArrayList<>();
        for (int j = 0; j < parameters.getLength(); j++) {
            Node param = parameters.item(j);
            if (param.getNodeType() == Node.ELEMENT_NODE) {
                NamedNodeMap attributes = param.getAttributes();

                Node vTypeNode = attributes.getNamedItem("vType");
                String vTypeVal = vTypeNode.getTextContent();

                Node valueNode = attributes.getNamedItem("Value");
                Double valueVal = NumberWithENotationConverter.fromString(valueNode.getTextContent());

                Node descNode = attributes.getNamedItem("Desc");
                String descVal = descNode.getTextContent();

                ParameterSlv parameter = new ParameterSlv(param.getNodeName(), vTypeVal, valueVal, descVal);
                params.add(parameter);
            }
        }
        return params;
    }

    /**
     * Wydobywa wszystkie reguły z {@link NodeList}.
     *
     * @param list a {@link NodeList} with rules.
     * @return List of {@link RuleSlv}.
     */
    public List<RuleSlv> extractRulesFrom(NodeList list) {
        List<RuleSlv> rules = new ArrayList<>();
        for (int j = 0; j < list.getLength(); j++) {
            Node param = list.item(j);
            if (param.getNodeType() == Node.ELEMENT_NODE) {
                NamedNodeMap attributes = param.getAttributes();

                Node nameNode = attributes.getNamedItem("Name");
                String nameVal = nameNode.getTextContent();

                Node timeNode = attributes.getNamedItem("Time");
                String timeVal = timeNode.getTextContent();

                Node unitsNode = attributes.getNamedItem("Units");
                String unitsVal = unitsNode.getTextContent();

                Node valueNode = attributes.getNamedItem("Value");
                String valueVal = valueNode.getTextContent();

                RuleSlv rule = new RuleSlv(nameVal, timeVal, unitsVal, valueVal);
                rules.add(rule);
            }
        }
        return rules;
    }

    /**
     * Wydobywa nazwę modelu.
     *
     * @param node {@link Node} zawierający nazwę.
     * @return nazwa modelu.
     */
    public String extractModelName(Node node) {
        NamedNodeMap attributes = node.getAttributes();
        Node name = attributes.getNamedItem("name");
        return name.getTextContent();
    }
}
