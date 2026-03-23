package com.eseo.mediastock.model.Enum;

/**
 * Énumération définissant les états de disponibilité d'un {@link com.eseo.mediastock.model.Exemplaire}.
 * <p>Valeurs possibles : DISPONIBLE, EMPRUNTE, RESERVE, RETIRE, etc.</p>
 */
public enum EnumDispo {
    /**
     * Disponible enum dispo.
     */
    DISPONIBLE,
    /**
     * Emprunte enum dispo.
     */
    EMPRUNTE,
    /**
     * Rendu enum dispo.
     */
    RENDU,
    /**
     * Retire enum dispo.
     */
    RETIRE
}
