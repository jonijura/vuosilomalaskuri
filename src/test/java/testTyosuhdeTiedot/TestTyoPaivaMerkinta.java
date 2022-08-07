/**
 * 
 */
package testTyosuhdeTiedot;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.time.LocalDate;

import org.junit.Test;

import lomaLakiJaEhdot.TyoMerkinnanTyyppi;
import tyosuhdeTiedot.TyoPaivaMerkinta;

/**
 * @author Joona1
 * @version 26.7.2022
 *
 */
public class TestTyoPaivaMerkinta {

    /**
     * 
     */
    @Test
    public void testaaMerkkijonostaParsiminen() {
        String[] tyoPaivat = { "23.1.2009|||||||||||||||||||||||||",
                "17.4.2009|||9-14|5|5|5|||||||||||||||||||",
                "21.5.2009|Arkipyhäkorvaukset|||7,5|||||||||||||||||||||",
                "26.6.2009|||9-16:30|7,5|7,5|7,5|64,40 €||||||||||||||||||",
                "15.10.2009|||9-16:30|7,5|7,5|7,5||Oletus: tuntipalkka on 11 euroa / tunti|||||||||||||||||",
                "14.10.2009|||9-16:30|7,5|7,5|7,5|||||||||||||||||||" };
        LocalDate[] paivaMaarat = { LocalDate.of(2009, 1, 23),
                LocalDate.of(2009, 4, 17), LocalDate.of(2009, 5, 21),
                LocalDate.of(2009, 6, 26), LocalDate.of(2009, 10, 15),
                LocalDate.of(2009, 10, 14) };
        TyoPaivaMerkinta tulokset[] = {
                new TyoPaivaMerkinta(paivaMaarat[0], "tasausPaiva", "", "", "0",
                        "0", "0", "0", "10"),
                new TyoPaivaMerkinta(paivaMaarat[1], "", "", "9-14", "5", "5",
                        "5", "0", "10"),
                new TyoPaivaMerkinta(paivaMaarat[2], "Arkipyhäkorvaukset", "",
                        "", "7.5", "0", "0", "0", "10"),
                new TyoPaivaMerkinta(paivaMaarat[3], "", "", "9-16:30", "7.5",
                        "7.5", "7.5", "64.40", "10"),
                new TyoPaivaMerkinta(paivaMaarat[4], "", "", "9-16:30", "7.5",
                        "7.5", "7.5", "0", "11"),
                new TyoPaivaMerkinta(paivaMaarat[5], "", "", "9-16:30", "7.5",
                        "7.5", "7.5", "0", "10") };
        for (int i = 0; i < tyoPaivat.length; i++)
            assertEquals("kierros " + i, true,
                    tulokset[i].equals(new TyoPaivaMerkinta(tyoPaivat[i])));

        TyoPaivaMerkinta tulos = new TyoPaivaMerkinta(paivaMaarat[3], "", "",
                "9-16:23", "7.5", "7.5", "7.5", "64.4", "10");
        assertEquals(false, tulos.equals(new TyoPaivaMerkinta(tyoPaivat[3])));

        tulos = new TyoPaivaMerkinta(paivaMaarat[3], "", "", "9-16:30", "7.5",
                "7.5", "7.5", "64.2", "10");
        assertEquals(false, tulos.equals(new TyoPaivaMerkinta(tyoPaivat[3])));

        tulos = new TyoPaivaMerkinta(paivaMaarat[3], "", "", "9-16:30", "7.5",
                "7.5", "7.5", "64.0", "3");
        assertEquals(false, tulos.equals(new TyoPaivaMerkinta(tyoPaivat[3])));
    }


    /**
     * 
     */
    @Test
    public void testaaSisaltyykoValille() {
        LocalDate alku = LocalDate.of(2001, 12, 4);
        LocalDate loppu = LocalDate.of(2003, 4, 24);
        LocalDate[] testiPaivat = { LocalDate.of(2001, 12, 4),
                LocalDate.of(2001, 12, 21), LocalDate.of(2002, 3, 25),
                LocalDate.of(2003, 2, 28), LocalDate.of(2003, 4, 24),
                LocalDate.of(2003, 4, 25), LocalDate.of(2000, 4, 24) };
        boolean[] tulokset = { true, true, true, true, true, false, false };
        TyoPaivaMerkinta tp;
        for (int i = 0; i < testiPaivat.length; i++) {
            tp = new TyoPaivaMerkinta(testiPaivat[i], "", "", "", "0", "0", "0",
                    "0", "0");
            assertEquals("rivi " + i, tulokset[i],
                    tp.sisaltyyValille(alku, loppu));
        }
    }


    /**
     * 
     */
    @Test
    public void testaaTyoMerkinnanTyyppi() {
        var pvm = LocalDate.of(2009, 1, 23);
        TyoPaivaMerkinta tulos = new TyoPaivaMerkinta(pvm, "", "", "", "0", "0",
                "0", "0", "10");
        assertEquals(tulos.getPaivanTyyppi(),
                TyoMerkinnanTyyppi.tavallinenTyopaiva);

        try {
            tulos = new TyoPaivaMerkinta(pvm, "ok", "", "", "0", "0", "0", "0",
                    "10");
            fail("tuntematon merkintätyyppi ei kaatanut ohjelmaa!");
        } catch (RuntimeException e) {
            //
        }

        tulos = new TyoPaivaMerkinta(pvm, "Arkipyhäkorvaukset", "", "", "0",
                "0", "0", "0", "10");
        assertEquals(tulos.getPaivanTyyppi(),
                TyoMerkinnanTyyppi.Arkipyhakorvaukset);
    }
}
