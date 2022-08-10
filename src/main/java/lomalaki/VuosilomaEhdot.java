/**
 * 
 */
package lomalaki;

import java.math.BigDecimal;

/**
 * @author Joona1
 * @version 27.7.2022
 *
 */
public class VuosilomaEhdot extends VuosilomaLaki {

    /**
     * 
     */
    public VuosilomaEhdot() {
        super();
    }


    /**
     * @param lomaPaivaKerroinAlleVuosi
     */
    public void setLomaPaivaKerroinAlleVuosi(
            BigDecimal lomaPaivaKerroinAlleVuosi) {
        this.lomaPaivaKerroinAlleVuosi = lomaPaivaKerroinAlleVuosi;
    }


    /**
     * @param lomaPaivaKerroinYliVuosi
     */
    public void setLomaPaivaKerroinYliVuosi(
            BigDecimal lomaPaivaKerroinYliVuosi) {
        this.lomaPaivaKerroinYliVuosi = lomaPaivaKerroinYliVuosi;
    }


    /**
     * @param lomapalkkaKerroinAlleVuosi 
     */
    public void setLomapalkkaKerroinAlleVuosi(
            BigDecimal lomapalkkaKerroinAlleVuosi) {
        this.lomapalkkaKerroinAlleVuosi = lomapalkkaKerroinAlleVuosi;
    }


    /**
     * @param lomapalkkaKerroinYliVuosi
     */
    public void setLomapalkkaKerroinYliVuosi(
            BigDecimal lomapalkkaKerroinYliVuosi) {
        this.lomapalkkaKerroinYliVuosi = lomapalkkaKerroinYliVuosi;
    }

}
