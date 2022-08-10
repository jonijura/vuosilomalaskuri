/**
 * 
 */
package testTyosuhdeTiedot;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.Test;

import lomalaki.TyoMerkinnanTyyppi;
import tyosuhdeTiedot.TyoHistoria;

/**
 * @author Joona1
 * @version 27.7.2022
 *
 */
public class TestTyoHistoria {

    /**
     * 
     */
    @Test
    public void testaaPaloittelu() {
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
        var palaset = th.paloittelePaivanTyypinMukaan();
        assertEquals(2, palaset.size());
        assertEquals(4, palaset.get(0).merkintojenLkm());
        assertEquals(11, palaset.get(1).merkintojenLkm());

        testikk[6] = "20.8.2009|Arkipyhäkorvaukset||9-15|6|6|6|";
        th = new TyoHistoria(testikk);
        palaset = th.paloittelePaivanTyypinMukaan();
        assertEquals(4, palaset.size());
        assertEquals(4, palaset.get(0).merkintojenLkm());
        assertEquals(2, palaset.get(1).merkintojenLkm());
        assertEquals(1, palaset.get(2).merkintojenLkm());
        assertEquals(8, palaset.get(3).merkintojenLkm());
    }


    /**
     * 
     */
    @Test
    public void testaaPalkka() {
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
        assertEquals(new BigDecimal("682.50"), th.getPalkka());
        assertEquals(new BigDecimal("682.50"),
                th.getTehdytTunnit().multiply(new BigDecimal(10)));
    }


    /**
     * 
     */
    @Test
    public void testaaValinmerkinnatTyypinMukaan() {
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
        assertEquals(3,
                th.getValinMerkinnat(LocalDate.of(2009, 8, 11),
                        LocalDate.of(2009, 8, 28))
                        .getTyyppisetMerkinnat(TyoMerkinnanTyyppi.Hoitovapaa)
                        .merkintojenLkm());
        assertEquals(4,
                th.getValinMerkinnat(LocalDate.of(2008, 8, 11),
                        LocalDate.of(2009, 8, 28))
                        .getTyyppisetMerkinnat(TyoMerkinnanTyyppi.Hoitovapaa)
                        .merkintojenLkm());
        assertEquals(10, th
                .getValinMerkinnat(LocalDate.of(2008, 8, 11),
                        LocalDate.of(2009, 8, 28))
                .getTyyppisetMerkinnat(TyoMerkinnanTyyppi.tavallinenTyopaiva)
                .merkintojenLkm());
    }


    /**
     * 
     */
    @Test
    public void testaaTasauspaivienPoisto() {
        String[] testikk = new String[] { "10.8.2009||", "11.8.2009||",
                "12.8.2009||", "13.8.2009|Hoitovapaa|",
                "14.8.2009|||9-16:30|7,5|7,5|7,5|",
                "17.8.2009|||9-16:30|7,5|7,5|7,5|", "20.8.2009|||9-15|6|6|6|",
                "21.8.2009|||9-16:30|7,5|7,5|7,5|",
                "22.8.2009|||9-16:30|7,5|7,5|7,5|",
                "23.8.2009|||9-13:30|4,5|4,5|4,5|", "24.8.2009|||9-13|4|4|4|",
                "25.8.2009|||9-15:30|6,5|6,5|6,5|", "27.8.2009|||9-16|7|7|7|",
                "28.8.2009||||", "29.8.2009|||9-15:15|6,25|6,25|6,25|" };
        var th = new TyoHistoria(testikk);
        assertEquals(testikk.length, th.merkintojenLkm());
        assertEquals(testikk.length - 4,
                th.ilmanTyoajanTasauksia().merkintojenLkm());
    }
}
