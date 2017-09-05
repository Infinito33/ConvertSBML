package convertsbml.slv;

import convertsbml.basic.AbstractReader;
import convertsbml.model.entities.slv.EquationSlv;
import convertsbml.model.entities.slv.ModelSlv;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Klasa odpowiedzialna za odczyt pliku *.slv.
 *
 * @author Magda
 */
public class SlvReader extends AbstractReader {

    private SlvContentExtractor extractor;

    /**
     * Domyślny konstruktor, w którym tworzony jest obiekt klasy
     * {@link SlvContentExtractor} odpowiedzialnej za wyciąganie danych z plików
     * *.slv.
     */
    public SlvReader() {
        extractor = new SlvContentExtractor();
    }

    /**
     * Funkcja służy do odczytu pliku *.slv.
     *
     * @param path ścieżka pliku *.slv.
     * @return model slv {@link ModelSlv} zawierający wszystkie dane pliku
     * *.slv.
     */
    public ModelSlv readSlv(String path) {
        ModelSlv model = new ModelSlv();
        try {
            File fXmlFile = new File(path);
            //Stworzenie dokumentu ktory bedzie zawieral plik slv w postaci xml.
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            //optional, but recommended
            //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();

            //Odczyt linii z pliku slv
            List<String> content = readFileAsList(path);
            model.setContent(content);

            Element element = doc.getDocumentElement();
            System.out.println(element.getNodeName());
            readInnerNodes(element, model);

            //Łapanie wyjątków, czyli ewentualnych błędów, w razie złego zachowania aplikacji.
        } catch (ParserConfigurationException e) {
            System.out.println(e.getMessage());
        } catch (SAXException | IOException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("koniec czytania");

        return model;
    }

    /**
     * Funkcja odczytująca wewnętrzną gałąź drzewa.
     *
     * @param element wewnętrzna gałąź.
     * @param model model SLV, do którego zapisywane są dane.
     */
    private void readInnerNodes(Node element, ModelSlv model) {
        NodeList nodeList = element.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node currentNode = nodeList.item(i);
            if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                if (currentNode.getNodeName().equals("model1")) {
                    model.setName(extractor.extractModelName(currentNode));
                }
                if (currentNode.getNodeName().equals("Eqs")) {
                    List<EquationSlv> equationsFromSlv = extractor.extractEquationsFrom(currentNode.getTextContent());
                    model.setEquations(equationsFromSlv);
                } else if (currentNode.getNodeName().equals("parameters")) {
                    NodeList parameters = currentNode.getChildNodes();
                    model.setParameters(extractor.extractParametersFrom(parameters));
                } else if (currentNode.getNodeName().equals("parchangerules")) {
                    NodeList rules = currentNode.getChildNodes();
                    model.setRules(extractor.extractRulesFrom(rules));
                }
                readInnerNodes(currentNode, model);
            }
        }
    }

}
