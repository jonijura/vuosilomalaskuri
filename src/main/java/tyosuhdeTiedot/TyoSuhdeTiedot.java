package tyosuhdeTiedot;

import java.math.BigDecimal;
import java.time.LocalDate;

import lomaLakiJaEhdot.VuosilomaLaki;

/**
 * @author Joona Räty
 * @version 26.7.2022
 *
 */
public class TyoSuhdeTiedot implements TyosuhdeTiedotIF {

    /**
     * @author Joona Räty
     * @version 9.8.2022
     *
     */
    public enum LomarahanMaksuedellytys {
        /**
         * 
         */
        eiAsetettu
    }

    /**
     * @author Joona Räty
     * @version 7.8.2022
     *
     */
    public enum AnsaintaSaanto {
        /**
         * 
         */
        Yli14PvKuukaudessa,
        /**
         * 
         */
        Yli35TuntiaKuukaudessa,
        /**
         * 
         */
        Muu
    }

    /**
     * 
     */
    private AnsaintaSaanto ansaintaSaanto;

    /**
     * @author Joona Räty
     * @version 7.8.2022
     *
     */
    public enum LomapalkanLaskutapa {
        /**
         * 
         */
        Prosenttiperusteinen,
        /**
         * 
         */
        Kuukausipalkkainen,
        /**
         * 
         */
        ViikkoPalkka,
        /**
         * 
         */
        TuntiPalkka
    }

    /**
     * 
     */
    private LomapalkanLaskutapa lomapalkanLaskutapa = LomapalkanLaskutapa.Prosenttiperusteinen;

    /**
     * 
     */
    private LocalDate sopimuksenAlkuPv;

    /**
     * 
     */
    private TyoHistoria tyoHistoria;

    /**
     * 
     */
    private BigDecimal viikkoTyoAika;

    /**
     * 
     */
    private int tyoPaiviaViikossa = 5;

    /**
     * @param sopimuksenAlkuPv2 
     * @param th 
     * @param viikkoTyoAika2 
     * 
     */
    public TyoSuhdeTiedot(LocalDate sopimuksenAlkuPv2, TyoHistoria th,
            BigDecimal viikkoTyoAika2) {
        sopimuksenAlkuPv = sopimuksenAlkuPv2;
        tyoHistoria = th;
        viikkoTyoAika = viikkoTyoAika2;
    }


    /**
     * @param sopimuksenAlkuPv2 
     * @param th
     */
    public TyoSuhdeTiedot(LocalDate sopimuksenAlkuPv2, TyoHistoria th) {
        sopimuksenAlkuPv = sopimuksenAlkuPv2;
        tyoHistoria = th;
        viikkoTyoAika = null;
    }


    /**
     * @param vuosi
     */
    @Override
    public LomapalkanLaskutapa getLomapalkanLaskutapa() {
        return lomapalkanLaskutapa;
    }


    /**
     * @param as
     */
    public void setAnsaintaSaanto(AnsaintaSaanto as) {
        ansaintaSaanto = as;
    }


    /**
     * @param laskutapa
     */
    public void setLomapalkanLaskutapa(LomapalkanLaskutapa laskutapa) {
        this.lomapalkanLaskutapa = laskutapa;
    }


    /**
     * @return
     */
    @Override
    public BigDecimal getViikkotyoAika() {
        return viikkoTyoAika;
    }


    /**
     *
     */
    @Override
    public LomarahanMaksuedellytys getLomarahanMaksuedellytys() {
        return LomarahanMaksuedellytys.eiAsetettu;
    }


    @Override
    public boolean onkoSopimusKestanytYliVuoden(LocalDate pvm) {
        LocalDate vuosiSitten = pvm.minusYears(1);
        return sopimuksenAlkuPv.isBefore(vuosiSitten);
    }


    /**
     * @param kuukausi
     * @return 
     * 
     * Vuosilomalaki 6§
     * 
     * Täytenä lomanmääräytymiskuukautena pidetään kalenterikuukautta, 
     * jolloin työntekijälle on kertynyt vähintään 14 työssäolopäivää 
     * tai 7 §:n 1 ja 2 momentissa tarkoitettua työssäolon veroista päivää.
     * 
     * Jos työntekijä on sopimuksen mukaisesti työssä niin harvoina päivinä, 
     * että hänelle ei tästä syystä kerry ainoatakaan 14 työssäolopäivää 
     * sisältävää kalenterikuukautta tai vain osa kalenterikuukausista 
     * sisältää 14 työssäolopäivää, täydeksi lomanmääräytymiskuukaudeksi 
     * katsotaan sellainen kalenterikuukausi, jonka aikana työntekijälle 
     * on kertynyt vähintään 35 työtuntia tai 7 §:ssä tarkoitettua 
     * työssäolon veroista tuntia.
     */
    @Override
    public boolean onkoTaysiLomanMaaraytymisiKK(LocalDate kuukausi) {
        TyoHistoria kuunTyoHistoria = tyoHistoria
                .getKuukaudenMerkinnat(kuukausi);
        int tyonVeroisiaPaivia = VuosilomaLaki
                .tyonVeroisiaPaivia(kuunTyoHistoria);
        if (ansaintaSaanto == AnsaintaSaanto.Yli14PvKuukaudessa) {
            return tyonVeroisiaPaivia >= 14;
        }
        BigDecimal tyossaolonVeroisetVapaapaivat = new BigDecimal(
                tyonVeroisiaPaivia - kuunTyoHistoria.getTyoPaivienLkm());
        BigDecimal tyossaOlonVeroisiaTunteja = kuunTyoHistoria.getTyoAika()
                .add(tyossaolonVeroisetVapaapaivat
                        .multiply(sopimuksenMukainenPaivatyoaika()));
        return tyossaOlonVeroisiaTunteja.compareTo(new BigDecimal(35)) >= 0;
    }


    /**
     * @return
     */
    private BigDecimal sopimuksenMukainenPaivatyoaika() {
        if (viikkoTyoAika == null)
            return BigDecimal.ZERO;
        return viikkoTyoAika.multiply(new BigDecimal("0.2"));
    }


    /**
     * @param paiva
     * @return
     */
    @Override
    public BigDecimal viimeisinTuntipalkka(LocalDate paiva) {
        return new BigDecimal(
                paiva.isBefore(LocalDate.of(2009, 10, 15)) ? 10 : 11);
    }


    /**
     *
     */
    @Override
    public BigDecimal getTyopaiviaViikossa() {
        return new BigDecimal(tyoPaiviaViikossa);
    }


    /*
     * Vuosilomalaki 6§ Täytenä lomanmääräytymiskuukautena pidetään
     * kalenterikuukautta, jolloin työntekijälle on kertynyt vähintään 14
     * työssäolopäivää tai 7 §:n 1 ja 2 momentissa tarkoitettua työssäolon
     * veroista päivää. Jos työntekijä on sopimuksen mukaisesti työssä niin
     * harvoina päivinä, että hänelle ei tästä syystä kerry ainoatakaan 14
     * työssäolopäivää sisältävää kalenterikuukautta tai vain osa
     * kalenterikuukausista sisältää 14 työssäolopäivää, täydeksi
     * lomanmääräytymiskuukaudeksi katsotaan sellainen kalenterikuukausi, jonka
     * aikana työntekijälle on kertynyt vähintään 35 työtuntia tai 7 §:ssä
     * tarkoitettua työssäolon veroista tuntia.
     */
    @Override
    public AnsaintaSaanto selvitaAnsaintaSaanto(int vuosi) {
        if (viikkoTyoAika != null
                && viikkoTyoAika.compareTo(new BigDecimal(30)) > 0) {
            ansaintaSaanto = AnsaintaSaanto.Yli14PvKuukaudessa;
            return ansaintaSaanto;
        }

        for (var kk : VuosilomaLaki.getLomaVuodenKuukaudet(vuosi)) {
            if (tyoHistoria.mahdollisiaTyopaivia(kk) < 14) {
                ansaintaSaanto = AnsaintaSaanto.Yli35TuntiaKuukaudessa;
                return ansaintaSaanto;
            }
        }
        ansaintaSaanto = AnsaintaSaanto.Yli14PvKuukaudessa;
        return ansaintaSaanto;
    }


    @Override
    public TyoHistoria getValinMerkinnat(LocalDate alku, LocalDate loppu) {
        return tyoHistoria.getValinMerkinnat(alku, loppu);
    }

}
