package convertsbml.basic;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Magda
 */
public abstract class AbstractReader {

    /**
     * Odczytanie pliku jako listz linii.
     *
     * @param path ścieżka do pliku.
     * @return lista linii w postaci {@link String}.
     * @throws IOException wyjątek rzucany w przypadku niepoprawnego odczytu
     * pliku.
     */
    public List<String> readFileAsList(String path) throws IOException {
        if(path == null || path.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        List<String> lines = new ArrayList<>();
        FileReader fileReader = new FileReader(path);
        try (BufferedReader br = new BufferedReader(fileReader)) {
            br.lines().forEach(s -> {
                lines.add(s + System.lineSeparator());
            });
        }
        return lines;
    }
}
