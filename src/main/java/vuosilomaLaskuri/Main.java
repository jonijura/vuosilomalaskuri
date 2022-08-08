package vuosilomaLaskuri;

import java.math.BigDecimal;
import java.time.LocalDate;

import lomaLakiJaEhdot.VuosilomaEhdot;
import tiedostonKasittely.TiedostonKasittelija;
import tyosuhdeTiedot.TyoHistoria;
import tyosuhdeTiedot.TyoSuhdeTiedot;
import tyosuhdeTiedot.TyoSuhdeTiedot.AnsaintaSaanto;
import tyosuhdeTiedot.TyoSuhdeTiedot.LomapalkanLaskutapa;

/**
 * @author Joona1
 * @version 5.8.2022
 *
 */
public class Main {

    /**
     * @param args
     */
    public static void main(String[] args) {
        TyoHistoria th = TiedostonKasittelija
                .lueTyoHistoria("vuosiloma_vuositunnit2.txt");
        TyoSuhdeTiedot tst = new TyoSuhdeTiedot(LocalDate.of(2008, 6, 1), th,
                new BigDecimal("37.5"));
        tst.setAnsaintaSaanto(AnsaintaSaanto.Yli14PvKuukaudessa);
        tst.setLomapalkanLaskutapa(LomapalkanLaskutapa.ViikkoPalkka);

        var vle = new VuosilomaEhdot();
        vle.setLomapalkkaKerroinAlleVuosi(new BigDecimal("0.1"));
        vle.setLomapalkkaKerroinYliVuosi(new BigDecimal("0.125"));

        var vll = new VuosilomaLaskuri(tst, vle);
        var tulos = vll.laskePalkkalaskelma(2010);
        TiedostonKasittelija.tallenna(tulos.toString(), "palkkalaskelma.txt");
        System.out.println(tulos.toString());
    }
}
