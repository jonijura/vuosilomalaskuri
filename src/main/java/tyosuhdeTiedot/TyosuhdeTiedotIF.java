/**
 * 
 */
package tyosuhdetiedot;

import java.math.BigDecimal;
import java.time.LocalDate;

import tyosuhdetiedot.TyoSuhdeTiedot.AnsaintaSaanto;
import tyosuhdetiedot.TyoSuhdeTiedot.LomapalkanLaskutapa;
import tyosuhdetiedot.TyoSuhdeTiedot.LomarahanMaksuedellytys;

/**
 * @author Joona1
 * @version 26.7.2022
 *
 */
public interface TyosuhdeTiedotIF {

    /**
     * @param pvm 
     * @return
     */
    public boolean onkoSopimusKestanytYliVuoden(LocalDate pvm);


    /**
     * @param kk
     * @return
     */
    public boolean onkoTaysiLomanMaaraytymisiKK(LocalDate kk);


    /**
     * @return
     */
    public BigDecimal getViikkotyoAika();


    /**
     * @param alku
     * @param loppu
     * @return
     */
    public TyoHistoria getValinMerkinnat(LocalDate alku, LocalDate loppu);


    /**
     * @param vuosi 
     * @return
     */
    public LomapalkanLaskutapa getLomapalkanLaskutapa();


    /**
     * @return
     */
    public BigDecimal getTyopaiviaViikossa();


    /**
     * @return
     */
    public LomarahanMaksuedellytys getLomarahanMaksuedellytys();


    /**
     * @param pvm
     * @return
     */
    public BigDecimal viimeisinTuntipalkka(LocalDate pvm);


    /**
     * @param vuosi
     * @return 
     */
    public AnsaintaSaanto selvitaAnsaintaSaanto(int vuosi);


    /**
     * @return
     */
    public LomapalkanLaskutapa selvitaLomapalkanLaskutapa();

}
