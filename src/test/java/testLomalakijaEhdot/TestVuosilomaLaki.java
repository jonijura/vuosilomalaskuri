/**
 * 
 */
package testLomalakijaEhdot;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;

import lomalaki.VuosilomaLaki;
import tyosuhdeTiedot.TyoHistoria;

/**
 * @author Joona1
 * @version 7.8.2022
 *
 */
public class TestVuosilomaLaki {

    /**
     * Vuosilomalaki 7 §
     * Työssäolon veroisena pidetään työstä poissaoloaikaa, 
     * jolta työnantaja on lain mukaan velvollinen maksamaan 
     * työntekijälle palkan.
     */
    @Test
    public void testaaTyonVeroisienPaivienLakseminen() {
        String[] testikk = new String[] { "10.8.2009|Hoitovapaa|",
                "11.8.2009|Hoitovapaa|", "12.8.2009|Hoitovapaa|",
                "13.8.2009|Hoitovapaa|", "14.8.2009|||9-16:30|7,5|7,5|7,5|",
                "17.8.2009|||9-16:30|7,5|7,5|7,5|", "20.8.2009|||9-15|6|6|6|",
                "21.8.2009|||9-16:30|7,5|7,5|7,5|",
                "22.8.2009|||9-16:30|7,5|7,5|7,5|",
                "23.8.2009|||9-13:30|4,5|4,5|4,5|", "24.8.2009|||9-13|4|4|4|",
                "25.8.2009|||9-15:30|6,5|6,5|6,5|", "27.8.2009|||9-16|7|7|7|",
                "28.8.2009|||9-13|4|4|4|",
                "29.8.2009|||9-15:15|6,25|6,25|6,25|" };
        var th = new TyoHistoria(testikk);
        assertEquals(11, VuosilomaLaki.tyonVeroisiaPaivia(th));
    }


    /**
     * Vuosilomalaki 7 §
     * Työssäolon veroisena pidetään työstä poissaoloaikaa, 
     * jolta työnantaja on lain mukaan velvollinen maksamaan 
     * työntekijälle palkan. Työssäolon veroisena pidetään myös aikaa, 
     * jolloin työntekijä on poissa työstä sellaisen työajan tasaamiseksi 
     * annetun vapaan vuoksi, jolla hänen keskimääräinen viikkotyöaikansa 
     * tasataan laissa säädettyyn enimmäismäärään. Saman kalenterikuukauden 
     * aikana viikkotyöajan tasaamiseksi annetuista vapaapäivistä työssäolon 
     * veroisina pidetään kuitenkin vain neljä päivää ylittäviä vapaapäiviä, 
     * jollei vapaata ole annettu yli kuuden arkipäivän pituisena yhtenäisenä 
     * vapaana.
     */
    @Test
    public void testaaTasausPaivat() {
        String[] testikk = new String[] { "10.8.2009|tasausPaiva|",
                "11.8.2009|tasausPaiva|", "12.8.2009|tasausPaiva|",
                "13.8.2009|tasausPaiva|", "14.8.2009|||9-16:30|7,5|7,5|7,5|",
                "17.8.2009|||9-16:30|7,5|7,5|7,5|", "20.8.2009|||9-15|6|6|6|",
                "21.8.2009|||9-16:30|7,5|7,5|7,5|",
                "22.8.2009|||9-16:30|7,5|7,5|7,5|",
                "23.8.2009|||9-13:30|4,5|4,5|4,5|", "24.8.2009|||9-13|4|4|4|",
                "25.8.2009|||9-15:30|6,5|6,5|6,5|", "27.8.2009|||9-16|7|7|7|",
                "28.8.2009|||9-13|4|4|4|",
                "29.8.2009|||9-15:15|6,25|6,25|6,25|" };
        var th = new TyoHistoria(testikk);
        assertEquals(11, VuosilomaLaki.tyonVeroisiaPaivia(th));

        testikk = new String[] { "10.8.2009|tasausPaiva|",
                "11.8.2009|tasausPaiva|", "12.8.2009|tasausPaiva|",
                "13.8.2009|tasausPaiva|", "14.8.2009|||9-16:30|7,5|7,5|7,5|",
                "17.8.2009|||9-16:30|7,5|7,5|7,5|", "20.8.2009|||9-15|6|6|6|",
                "21.8.2009|tasausPaiva||9-16:30|7,5|7,5|7,5|",
                "22.8.2009|||9-16:30|7,5|7,5|7,5|",
                "23.8.2009|||9-13:30|4,5|4,5|4,5|", "24.8.2009|||9-13|4|4|4|",
                "25.8.2009|tasausPaiva||9-15:30|6,5|6,5|6,5|",
                "27.8.2009|||9-16|7|7|7|", "28.8.2009|||9-13|4|4|4|",
                "29.8.2009|||9-15:15|6,25|6,25|6,25|" };
        th = new TyoHistoria(testikk);
        assertEquals(11, VuosilomaLaki.tyonVeroisiaPaivia(th));

        testikk = new String[] { "10.8.2009|tasausPaiva|",
                "11.8.2009|tasausPaiva|", "12.8.2009|tasausPaiva|",
                "13.8.2009|tasausPaiva|",
                "14.8.2009|tasausPaiva||9-16:30|7,5|7,5|7,5|",
                "17.8.2009|tasausPaiva||9-16:30|7,5|7,5|7,5|",
                "20.8.2009|||9-15|6|6|6|", "21.8.2009|||9-16:30|7,5|7,5|7,5|",
                "22.8.2009|||9-16:30|7,5|7,5|7,5|",
                "23.8.2009|||9-13:30|4,5|4,5|4,5|", "24.8.2009|||9-13|4|4|4|",
                "25.8.2009|||9-15:30|6,5|6,5|6,5|", "27.8.2009|||9-16|7|7|7|",
                "28.8.2009|||9-13|4|4|4|",
                "29.8.2009|||9-15:15|6,25|6,25|6,25|" };
        th = new TyoHistoria(testikk);
        assertEquals(15, VuosilomaLaki.tyonVeroisiaPaivia(th));
    }


    /**
     * Vuosilomalaki 11§ taulukko,
     * Jos lomapäivien määrä on suurempi kuin 30, kerrointa korotetaan luvulla 0,9 lomapäivää kohden.
     */
    @Test
    public void testaaTuntipalkkaisenLomapalkkaKerroin() {
        var vll = new VuosilomaLaki();
        assertEquals(new BigDecimal("22.2"),
                vll.getTuntipalkkaisenLomapalkkaKerroin(24));
        assertEquals(new BigDecimal("29.6"),
                vll.getTuntipalkkaisenLomapalkkaKerroin(32));
    }
}
