/**
 * 
 */
package tiedostonKasittely;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import tyosuhdeTiedot.TyoHistoria;
import tyosuhdeTiedot.TyoPaivaMerkinta;

/**
 * @author Joona1
 * @version 26.7.2022
 *
 */
public class TiedostonKasittelija {

    /**
     * @param polku
     * @return
     */
    public static TyoHistoria lueTyoHistoria(String polku) {
        ArrayList<TyoPaivaMerkinta> rivit = new ArrayList<TyoPaivaMerkinta>();
        try {
            File tiedosto = new File(polku);
            // seuraava try pitää huolen tiedoston sulkemisesta
            try (Scanner myReader = new Scanner(tiedosto)) {
                myReader.nextLine();
                while (myReader.hasNextLine()) {
                    rivit.add(new TyoPaivaMerkinta(myReader.nextLine()));
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (NumberFormatException e) {
            String msg = "Tiedosto " + polku
                    + " ei ollut halutulla tavalla muotoiltu.";
            throw new RuntimeException(msg, e);
        }
        return new TyoHistoria(rivit);
    }


    /**
     * @param data
     * @param polku
     */
    public static void tallenna(String data, String polku) {
        try {
            // seuraava try pitää huolen tiedoston sulkemisesta
            try (FileWriter kirjoittaja = new FileWriter(polku)) {
                kirjoittaja.write(data);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
