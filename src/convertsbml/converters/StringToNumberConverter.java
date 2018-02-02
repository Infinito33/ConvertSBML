package convertsbml.converters;

/**
 * Konwerter dla wartości parametrów, które mogą przyjmować postać np 8,6E-5 do
 * liczby o typie double. Przykład: 8,6E-5 zostanie przekonwertowane do wartości
 * 1 / 0,000086
 *
 * @author Magda
 */
public class StringToNumberConverter {

    /**
     * Konwersja stringa - parametru slv, które może być zapisany z notacją E,
     * np.: 8,6E-5.
     *
     * @param value wartość do skonwertowania.
     * @return wartość w postaci Double.
     */
    public static Double slvStringToNumber(String value) {
        try {
            String replacedValue = value.replace(",", ".");
            //Jeśli jest to liczba zawierająca E
            if (replacedValue.contains("E")) {
                //Podziel na liczbę i wykładnik
                String[] values = replacedValue.split("E");
                //Konwertuj liczbę
                double number = Double.parseDouble(values[0]);

                //Jeśli wykładnik jest ze znakiem -
                if (values[1].contains("-")) {
                    values[1] = values[1].replace("-", "");
                    Double powValue = Double.parseDouble(values[1]);
                    number = number / Math.pow(10.0, powValue);
                } else {
                    Double powValue = Double.parseDouble(values[1]);
                    number = number * Math.pow(10.0, powValue);
                }
                return number;
            } else {
                return Double.parseDouble(replacedValue);
            }
        } catch (NumberFormatException e) {
            System.out.println("Error when converting value of slv parameter: " + e.getMessage());
            return 0.0;
        }
    }

    /**
     * Konwersja wartości parametru do liczby double.
     *
     * @param value wartość w postaci String.
     * @return wartość double po konwersji.
     */
    public Double convertDecimalWithPowToDouble(String value) {
        if (value.contains("^")) {
            //Wyszukaj początek potęgi
            int startOfExponent = value.lastIndexOf("^");
            //Wyciągnięcie całości wykładnika
            String exponentWithElements = value.substring(startOfExponent + 1, value.length());
            //Wyciągnięcie wartości wykładnika
            String exponentWithPossibleTail = exponentWithElements.replace("(", "").replace(")", "");
            
            String exponent = "";
            if(exponentWithPossibleTail.contains("/")) {
                String[] values = exponentWithPossibleTail.split("/");
                exponent = values[0];
            } else {
                exponent = exponentWithPossibleTail;
            }
            
            //Pobranie indeksu znaku mnożenia
            int signIndex = value.lastIndexOf("*");

            String number = "";
            if (signIndex == -1) {
                number = "1";
            } else {
                number = value.substring(0, signIndex);
            }

            //Finalny numer wraz z potęgą
            String resultNumber = number + "E" + exponent;

            double resultValue = 0;
            try {
                //Konwersja string do double
                resultValue = Double.parseDouble(resultNumber);
            } catch (Exception e) {
                System.out.println("Exception while parsing decimal to double: " + e.getMessage());
                return resultValue;
            }
            return resultValue;
        } else {
            return Double.parseDouble(value);
        }
    }
}
