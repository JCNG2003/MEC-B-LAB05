/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfreeinter02;

import com.opencsv.CSVWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


/**
 *
 * @author CESAR
 */
public class NewClass  {
    
    public static void write(File file, List<String[]> data) throws IOException {
        FileWriter writer = new FileWriter(file);
        CSVWriter csvWriter = new CSVWriter(writer);

        // Escribir los datos en el archivo CSV
        csvWriter.writeAll(data);

        // Cerrar el escritor
        csvWriter.close();
    }

    
}
