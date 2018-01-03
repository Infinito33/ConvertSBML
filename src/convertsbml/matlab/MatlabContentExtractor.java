package convertsbml.matlab;

import convertsbml.model.entities.matlab.EquationM;
import convertsbml.model.entities.matlab.ParameterMatlab;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Magda
 */
public class MatlabContentExtractor {

    /**
     * Wydobywa równanie z tekstu.
     *
     * @param line tekst z równaniem.
     * @return
     */
    public EquationM extractEquationFrom(String line) {
        EquationM equation = null;
        String[] values = null;
        String[] parts = null;
        //Podział linii co średnik
        values = line.split("\\;");

        //Podział pierwszego elementu co znak '='
        parts = values[0].split("\\=");

        //Example  dAPIP3=a2*(PIPtot-APIP3)-c0*PTEN*APIP3; %APIP
        //Utworzenie równania: parts[0] = zmienna, parts[1] = równanie, parts[2] = komentarz
        equation = new EquationM(line, parts[0], parts[1], values[1]);

        return equation;
    }

    /**
     * Wydobywa równanie ze złożonego modelu.
     *
     * @param line linia z równaniem.
     * @param variables zmienne 'y'.
     * @return
     */
    public EquationM extractComplexEquationFrom(String line, Set<String> variables) {
        Pattern pattern = Pattern.compile("y\\([0-9]*\\)");

        //Podział linii co ';'
        String[] values = line.split("\\;");
        //Podział linii co znak '='.
        String[] parts = values[0].split("\\=");

        String rightSide = parts[1];

        String currentRightSide = rightSide;
        Matcher matcher = pattern.matcher(rightSide);

        //Dopóki istnieje dopasowanie wzorca zgodne z tym w pierwszej linii funkcji.
        while (matcher.find()) {
            int end = matcher.end();
            //Przypisz tekst od miejsca, w którym znaleziono dopasowanie do końca
            String yVar = currentRightSide.substring(matcher.start(), end);
            //Zamień wszystkie 'y(' na puste miejsce, tak samo z ')'.
            String var = yVar.replace("y(", "").replace(")", "");
            //Podmień wszystkie wystąpienia 'y(' i ')' z oryginalnego tekstu włącznie ze zmiennymi na y_liczba, np y(6) zamieni na y6
            //W celu pozbycia się nawiasów
            rightSide = rightSide.replaceAll("y\\(" + var + "\\)", "y" + var);

            //Dodanie zmiennej 'y' do listy
            variables.add("y" + var);

            //Obcięcie aktualnie przeszukiwanego równania tak aby znaleźć kolejne wystąpienia 'y(liczba)'
            currentRightSide = currentRightSide.substring(end, currentRightSide.length());
            //Przypisanie aktualnego tekstu do wyszukiwacza
            matcher = pattern.matcher(currentRightSide);
        }

        EquationM equation = new EquationM(line, parts[0], rightSide, values[1]);

        return equation;
    }

    /**
     * Wydobycie parametrów z tekstu.
     *
     * @param line tekst.
     * @return
     */
    public ParameterMatlab extractParameterFrom(String line) {
        String[] values = line.split("\\;");

        String[] parts = values[0].split("\\=");

        //Jeśli podzielono względem ';' i powstało mniej niż 2 elementy, przypisz pusty tekst, jeśli 2 lub więcej to przypisz wartość values[1] czyli ewentualny komentarz
        String comment = values.length < 2 ? "" : values[1];

        //Example  mm1=1*10^4;    % 2*10^5 p53 MM for DSB repair
        ParameterMatlab parameter = new ParameterMatlab(parts[0], parts[1], line, comment);

        return parameter;
    }

    /**
     * Wydobycie równań z 'y' z tekstu.
     *
     * @param line tekst.
     * @return
     */
    public EquationM extractYEquationFrom(String line) {
        //Podział ze względu na znak '='.
        String[] parts = line.split("\\=");

        //APIP3=y(1);
        EquationM equation = new EquationM(line, parts[0], parts[1], "");
        return equation;
    }

    /**
     * Wydobycie zmiennych z równania funkcji.
     *
     * @param function tekst z funkcji.
     * @return
     */
    public List<String> extractVariablesFrom(String function) {
        List<String> variables = new ArrayList<>();

        int startOfBracket = function.indexOf("(");
        int endOfBracket = function.indexOf(")");

        String vars = function.substring(startOfBracket + 1, endOfBracket);
        String[] splittedVars = vars.split("\\,");

        for (int i = 0; i < splittedVars.length; i++) {
            variables.add(splittedVars[i]);
        }

        return variables;
    }

    /**
     * Wydobycie równań nazwanych 'apoptopic factors' z tekstu.
     *
     * @param line tekst.
     * @return
     */
    String extractApoptopicFactorVariable(String line) {
        String[] values = line.split("\\=");
        return values[0];
    }
}
