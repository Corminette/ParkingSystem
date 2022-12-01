package com.parkit.parkingsystem.constants;

/* communication du taux constant à appliquer à la durée 
 * passée selon le type de véhicule **/
public final class Fare {
    private Fare() {
        throw new AssertionError("Instantiating utility class");
    }
    /**
     * @apiNote taux financier pour un vélo.
     */
    public static final double BIKE_RATE_PER_HOUR = 1.0;
    /**
     * @apiNote taux financier pour une voiture.
     */
    public static final double CAR_RATE_PER_HOUR = 1.5;
    /**
     * Obtenir la valeur en chiffre des heures par rapport aux secondes.
     */
    public static final double GIVE_SECOND_3600D = 3600D;
    /**
     * une demi-heure en chiffre par rapport à une heure.
     */
    public static final double GIVE_UNE_DEMI_HEURE = 0.5;
    /**
     * en fait c'est 1-le % accordé, que l'on peut soustraire directement au
     * prix.
     */
    public static final double TAUX_DE_REDUCTION = 0.95;
    /**
     * nombre de passage dans le parking pour être considéré comme fidèle.
     */
    public static final double NUMBER_OF_PASSAGE_NECESSARY_LOYALTY = 5;
}