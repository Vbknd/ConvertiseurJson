package convertisseur;

import java.io.File;

public interface IConvertisseur {
    void convert(File inputFile, File outputFile) throws Exception;
}