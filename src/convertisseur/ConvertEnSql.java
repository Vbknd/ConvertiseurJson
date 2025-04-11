package convertisseur;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

// Classe dédiée à la conversion JSON vers SQL
public class ConvertEnSql implements IConvertisseur {

    private final String tableName;

    public ConvertEnSql(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public void convert(File inputFile, File outputFile) throws Exception {
        JsonFactory jsonFactory = new JsonFactory();
        try (JsonParser parser = jsonFactory.createParser(inputFile);
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {

            if (parser.nextToken() != JsonToken.START_ARRAY) {
                throw new Exception("Le fichier JSON doit être un tableau d'objets.");
            }

            // Pour chaque objet JSON, générer une instruction INSERT
            while (parser.nextToken() == JsonToken.START_OBJECT) {
                Map<String, String> recordMap = new HashMap<>();
                while (parser.nextToken() != JsonToken.END_OBJECT) {
                    String key = parser.getCurrentName();
                    parser.nextToken();
                    String value = parser.getText();
                    recordMap.put(key, value);
                }
                StringBuilder sql = new StringBuilder("INSERT INTO " + tableName + " (");
                StringBuilder fields = new StringBuilder();
                StringBuilder values = new StringBuilder();
                boolean first = true;
                for (Map.Entry<String, String> entry : recordMap.entrySet()) {
                    if (!first) {
                        fields.append(", ");
                        values.append(", ");
                    }
                    fields.append(entry.getKey());
                    // Échapper les apostrophes dans la valeur
                    String escapedValue = entry.getValue().replace("'", "''");
                    values.append("'").append(escapedValue).append("'");
                    first = false;
                }
                sql.append(fields).append(") VALUES (").append(values).append(");");
                writer.write(sql.toString());
                writer.newLine();
            }
        }
    }
}
