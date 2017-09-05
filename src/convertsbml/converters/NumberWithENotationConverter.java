package convertsbml.converters;

/**
 * Konwerter dla wartości parametrów, które mogą przyjmować postać np 8,6E-5 do
 * liczby o typie double.
 * Przykład: 8,6E-5 zostanie przekonwertowane do wartości  1 / 0,000086
 *
 * @author Magda
 */
public class NumberWithENotationConverter {

    public static Double fromString(String value) {
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
}
