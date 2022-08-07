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
        String tyoPaiva = "23.1.2009|||||||||||||||||||||||||";
        var pvm = LocalDate.of(2009, 1, 23);
        TyoPaivaMerkinta tulos = new TyoPaivaMerkinta(pvm, "tasausPaiva", "",
                "", "0", "0", "0", "0", "10");
        assertEquals(true, tulos.equals(new TyoPaivaMerkinta(tyoPaiva)));

        String tyoPaiva2 = "17.4.2009|||9-14|5|5|5|||||||||||||||||||";
        var pvm2 = LocalDate.of(2009, 4, 17);
        TyoPaivaMerkinta tulos2 = new TyoPaivaMerkinta(pvm2, "", "", "9-14",
                "5", "5", "5", "0", "10");
        assertEquals(true, tulos2.equals(new TyoPaivaMerkinta(tyoPaiva2)));

        String tyoPaiva3 = "21.5.2009|Arkipyhäkorvaukset|||7,5|||||||||||||||||||||";
        var pvm3 = LocalDate.of(2009, 5, 21);
        TyoPaivaMerkinta tulos3 = new TyoPaivaMerkinta(pvm3,
                "Arkipyhäkorvaukset", "", "", "7.5", "0", "0", "0", "10");
        assertEquals(true, tulos3.equals(new TyoPaivaMerkinta(tyoPaiva3)));

        String tyoPaiva4 = "26.6.2009|||9-16:30|7,5|7,5|7,5|64,40 €||||||||||||||||||";
        var pvm4 = LocalDate.of(2009, 6, 26);

        TyoPaivaMerkinta tulos4 = new TyoPaivaMerkinta(pvm4, "", "", "9-16:30",
                "7.5", "7.5", "7.5", "64.40", "10");
        assertEquals(true, tulos4.equals(new TyoPaivaMerkinta(tyoPaiva4)));

        tulos4 = new TyoPaivaMerkinta(pvm4, "", "", "9-16:23", "7.5", "7.5",
                "7.5", "64.4", "10");
        assertEquals(false, tulos4.equals(new TyoPaivaMerkinta(tyoPaiva4)));

        tulos4 = new TyoPaivaMerkinta(pvm4, "", "", "9-16:30", "7.5", "7.5",
                "7.5", "64.2", "10");
        assertEquals(false, tulos4.equals(new TyoPaivaMerkinta(tyoPaiva4)));

        tulos4 = new TyoPaivaMerkinta(pvm4, "", "", "9-16:30", "7.5", "7.5",
                "7.5", "64.0", "3");
        assertEquals(false, tulos4.equals(new TyoPaivaMerkinta(tyoPaiva4)));

        String tyoPaiva5 = "15.10.2009|||9-16:30|7,5|7,5|7,5||Oletus: tuntipalkka on 11 euroa / tunti|||||||||||||||||";
        var pvm5 = LocalDate.of(2009, 10, 15);
        TyoPaivaMerkinta tulos5 = new TyoPaivaMerkinta(pvm5, "", "", "9-16:30",
                "7.5", "7.5", "7.5", "0", "11");
        assertEquals(true, tulos5.equals(new TyoPaivaMerkinta(tyoPaiva5)));

        String tyoPaiva6 = "14.10.2009|||9-16:30|7,5|7,5|7,5|||||||||||||||||||";
        var pvm6 = LocalDate.of(2009, 10, 14);
        TyoPaivaMerkinta tulos6 = new TyoPaivaMerkinta(pvm6, "", "", "9-16:30",
                "7.5", "7.5", "7.5", "0", "10");
        assertEquals(true, tulos6.equals(new TyoPaivaMerkinta(tyoPaiva6)));
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
