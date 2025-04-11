package convertisseur;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

// Classe dédiée à la conversion JSON vers CSV
public class ConvertEnCsv implements IConvertisseur {

    private final char delimiter;

    public ConvertEnCsv(char delimiter) {
        this.delimiter = delimiter;
    }

    @Override
    public void convert(File inputFile, File outputFile) throws Exception {
        JsonFactory jsonFactory = new JsonFactory();
        try (JsonParser parser = jsonFactory.createParser(inputFile);
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {

            CSVFormat csvFormat = CSVFormat.DEFAULT.withDelimiter(delimiter);
            CSVPrinter csvPrinter = new CSVPrinter(writer, csvFormat);

            // Vérifier que le fichier JSON est un tableau
            if (parser.nextToken() != JsonToken.START_ARRAY) {
                throw new Exception("Le fichier JSON doit être un tableau d'objets.");
            }

            // Traitement du premier objet pour déterminer les en-têtes
            if (parser.nextToken() != JsonToken.START_OBJECT) {
                throw new Exception("Le tableau JSON est vide ou mal formé.");
            }
            List<String> headers = new ArrayList<>();
            List<String> values = new ArrayList<>();
            while (parser.nextToken() != JsonToken.END_OBJECT) {
                String fieldName = parser.getCurrentName();
                headers.add(fieldName);
                parser.nextToken();
                values.add(parser.getText());
            }
            csvPrinter.printRecord(headers);
            csvPrinter.printRecord(values);

            // Traitement des objets suivants
            while (parser.nextToken() == JsonToken.START_OBJECT) {
                List<String> record = new ArrayList<>();
                while (parser.nextToken() != JsonToken.END_OBJECT) {
                    // On suppose le même ordre que les en-têtes détectés
                    parser.getCurrentName(); // Le nom du champ n'est pas utilisé ici
                    parser.nextToken();
                    record.add(parser.getText());
                }
                csvPrinter.printRecord(record);
            }
            csvPrinter.flush();
        }
    }
}
