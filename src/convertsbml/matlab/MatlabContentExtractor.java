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
    
    private Integer variableNumber = 1;
    
    public void resetVariableNumber() {
        variableNumber = 1;
    }

    public EquationM extractEquationFrom(String line) {
        EquationM equation = null;
        String[] values = null;
        String[] parts = null;
        try {
            values = line.split("\\;");

            parts = values[0].split("\\=");

            //Example  dAPIP3=a2*(PIPtot-APIP3)-c0*PTEN*APIP3; %APIP
            equation = new EquationM(line, parts[0], parts[1], values[1]);
        } catch (Exception e) {
            System.out.println("Error");
        }

        return equation;
    }
    
    public EquationM extractComplexEquationFrom(String line, Set<String> variables) {     
        Pattern pattern = Pattern.compile("y\\([0-9]*\\)");
        
        
        String[] values = line.split("\\;");
        String[] parts = values[0].split("\\=");
        
        String rightSide = parts[1];
        
        String currentRightSide = rightSide;
        Matcher matcher = pattern.matcher(rightSide);
        
        while(matcher.find()) {
            int end = matcher.end();
            String yVar = currentRightSide.substring(matcher.start(), end);
            String var = yVar.replace("y(", "").replace(")", "");
            rightSide = rightSide.replaceAll("y\\(" + var + "\\)", "y" + var);
            
            variables.add("y" + var);
            
            currentRightSide = currentRightSide.substring(end, currentRightSide.length());
            matcher = pattern.matcher(currentRightSide);
        }
        
        EquationM equation = new EquationM(line, parts[0], rightSide, values[1]);
        
        return equation;
    }

    public ParameterMatlab extractParameterFrom(String line) {
        String[] values = line.split("\\;");

        String[] parts = values[0].split("\\=");

        String comment = values.length < 2 ? "" : values[1];
        
        //Example  mm1=1*10^4;    % 2*10^5 p53 MM for DSB repair
        ParameterMatlab parameter = new ParameterMatlab(parts[0], parts[1], line, comment);

        return parameter;
    }

    public EquationM extractYEquationFrom(String line) {

        String[] parts = line.split("\\=");

        //APIP3=y(1);
        EquationM equation = new EquationM(line, parts[0], parts[1], "");
        return equation;
    }

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

    String extractApoptopicFactorVariable(String line) {
        String[] values = line.split("\\=");
        return values[0];
    }
}
