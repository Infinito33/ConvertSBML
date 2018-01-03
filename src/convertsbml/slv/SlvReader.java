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
            readInnerNodes(element, model);

            //Łapanie wyjątków, czyli ewentualnych błędów, w razie złego zachowania aplikacji.
        } catch (ParserConfigurationException e) {
            System.out.println(e.getMessage());
        } catch (SAXException | IOException e) {
            System.out.println(e.getMessage());
        }
        return model;
    }

    /**
     * Funkcja odczytująca wewnętrzną gałąź drzewa.
     *
     * @param element wewnętrzna gałąź.
     * @param model model SLV, do którego zapisywane są dane.
     */
    private void readInnerNodes(Node element, ModelSlv model) {
        //Pobranie liści z aktualnej gałęzi
        NodeList nodeList = element.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node currentNode = nodeList.item(i);
            //Jeśli jest to liść o typie element, którego szukamy
            if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                //Jeśli znaleziono gałąź z nazwą modelu
                if (currentNode.getNodeName().equals("model1")) {
                    //Zapisz nazwę modelu wraz z usunięciem wszystkich białych znaków
                    model.setName(extractor.extractModelName(currentNode).replaceAll("\\s+", ""));
                }
                //Jeśli znaleziono gałąź z równaniami
                if (currentNode.getNodeName().equals("Eqs")) {
                    List<EquationSlv> equationsFromSlv = extractor.extractEquationsFrom(currentNode.getTextContent());
                    model.setEquations(equationsFromSlv);
                    //Jeśli znaleziono gałąź z parametrami
                } else if (currentNode.getNodeName().equals("parameters")) {
                    NodeList parameters = currentNode.getChildNodes();
                    model.setParameters(extractor.extractParametersFrom(parameters));
                    //Jeśli znaleziono gałąź z regułami
                } else if (currentNode.getNodeName().equals("parchangerules")) {
                    NodeList rules = currentNode.getChildNodes();
                    model.setRules(extractor.extractRulesFrom(rules));
                }
                //rekurencyjne wywołanie w celu odczytu kolejnej gałęzi
                readInnerNodes(currentNode, model);
            }
        }
    }

}
