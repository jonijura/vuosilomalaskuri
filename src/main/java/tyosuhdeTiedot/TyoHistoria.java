/**
 * 
 */
package tyosuhdeTiedot;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;

import lomaLakiJaEhdot.TyoMerkinnanTyyppi;

/**
 * @author Joona1
 * @version 26.7.2022
 *
 */
public class TyoHistoria implements Iterable<TyoPaivaMerkinta> {

    /**
     * 
     */
    private ArrayList<TyoPaivaMerkinta> historia;

    /**
     * @param rivit 
     * 
     */
    public TyoHistoria() {
        historia = new ArrayList<TyoPaivaMerkinta>();
    }


    /**
     * @param rivit
     */
    public TyoHistoria(ArrayList<TyoPaivaMerkinta> rivit) {
        historia = rivit;
    }


    /**
     * @param rivit
     */
    public TyoHistoria(String[] rivit) {
        historia = new ArrayList<TyoPaivaMerkinta>(rivit.length);
        for (String rivi : rivit)
            historia.add(new TyoPaivaMerkinta(rivi));
    }


    /**
     * @param i
     * @return
     */
    public TyoPaivaMerkinta get(int i) {
        return historia.get(i);
    }


    /**
     * @return
     */
    public BigDecimal getTyoTunnit() {
        BigDecimal tunteja = BigDecimal.ZERO;
        for (var tp : historia) {
            tunteja = tunteja.add(tp.getTehdytTunnit());
        }
        return tunteja;
    }


    /**
     * @param alku 
     * @param loppu 
     * @return
     */
    public TyoHistoria getValinMerkinnat(LocalDate alku, LocalDate loppu) {
        var merkinnat = new ArrayList<TyoPaivaMerkinta>();
        for (var merkinta : historia)
            if (merkinta.sisaltyyValille(alku, loppu))
                merkinnat.add(merkinta);
        return new TyoHistoria(merkinnat);
    }


    /**
     * @return
     */
    public BigDecimal getPalkka() {
        BigDecimal palkka = BigDecimal.ZERO;
        for (var tp : historia) {
            palkka = palkka
                    .add(tp.getTehdytTunnit().multiply(tp.getTuntipalkka()));
        }
        return palkka;
    }


    /**
     * @return
     */
    public BigDecimal getBonukset() {
        BigDecimal bonukset = BigDecimal.ZERO;
        for (var tp : historia) {
            bonukset = bonukset.add(tp.getBonukset());
        }
        return bonukset;
    }


    /**
     * @return
     */
    public BigDecimal getTehdytTunnit() {
        BigDecimal tunteja = BigDecimal.ZERO;
        for (var tp : historia) {
            tunteja = tunteja.add(tp.getTyoAika());
        }
        return tunteja;
    }


    /**
     * @param localDate
     * @return
     */
    public TyoHistoria getKuukaudenMerkinnat(LocalDate localDate) {
        var merkinnat = new ArrayList<TyoPaivaMerkinta>();
        for (var merkinta : historia)
            if (merkinta.sisaltyyKuukauteen(localDate))
                merkinnat.add(merkinta);
        return new TyoHistoria(merkinnat);
    }


    /**
     * @return
     */
    public int merkintojenLkm() {
        return historia.size();
    }


    /**
     * @return
     */
    public ArrayList<TyoHistoria> paloittelePaivanTyypinMukaan() {
        var palaset = new ArrayList<TyoHistoria>();
        var th = new ArrayList<TyoPaivaMerkinta>();
        th.add(historia.get(0));
        for (int i = 1; i < historia.size(); i++) {
            var tp = historia.get(i);
            if (tp.getPaivanTyyppi().equals(th.get(0).getPaivanTyyppi())) {
                th.add(tp);
            } else {
                palaset.add(new TyoHistoria(th));
                th = new ArrayList<TyoPaivaMerkinta>();
                th.add(tp);
            }
        }
        palaset.add(new TyoHistoria(th));
        return palaset;
    }


    /**
     * @return
     */
    public TyoMerkinnanTyyppi merkintojenTyyppi() {
        return historia.get(0).getPaivanTyyppi();
    }


    @Override
    public Iterator<TyoPaivaMerkinta> iterator() {
        return historia.iterator();
    }


    /**
     * @return
     */
    public TyoHistoria ilmanTyoajanTasauksia() {
        var th = new ArrayList<TyoPaivaMerkinta>();
        for (var tm : historia) {
            if (tm.getPaivanTyyppi() != TyoMerkinnanTyyppi.tasausPaiva)
                th.add(tm);
        }
        return new TyoHistoria(th);
    }


    /**
     * @return
     */
    public long kestoKuukausina() {
        return ChronoUnit.MONTHS.between(historia.get(0).getPvm(),
                historia.get(historia.size() - 1).getPvm());
    }


    /**
     * @return
     */
    public int getTyoPaivienLkm() {
        int tyoPaivia = 0;
        for (var tp : historia)
            if (tp.getPaivanTyyppi() == TyoMerkinnanTyyppi.tavallinenTyopaiva)
                tyoPaivia++;
        return tyoPaivia;
    }
}
