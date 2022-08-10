/**
 * 
 */
package testtyosuhdetiedot;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;

import tiedostonkasittely.TiedostonKasittelija;
import tyosuhdetiedot.TyoHistoria;

/**
 * @author Joona1
 * @version 26.7.2022
 *
 */
public class TestTiedostonKasittelija {

    /**
     * 
     */
    @Test
    public void testaaTyoPaivienLukeminen() {
        TyoHistoria th = TiedostonKasittelija
                .lueTyoHistoria("vuosiloma_vuositunnit2.txt");
        assertEquals(new BigDecimal("1668.25"), th.getTehdytTunnit());
    }
}
