package PaooGame;

/**
 * Enum care definește stările posibile ale jocului.
 * Este utilizat pentru a controla tranzițiile între diferite ecrane sau secțiuni ale jocului.
 */

public enum GameState {
    START_MENU,     // Meniul principal (ecranul de start al jocului)
    LEVEL_SELECT,   // Ecranul de selecție a nivelului (harta)
    LEVEL_1, LEVEL_2, LEVEL_3, LEVEL_4, LEVEL_5,    // Nivelurile
    GAME_OVER,  // Ecranul de Game Over (jucătorul a pierdut)
    GAME_WIN    // Ecranul de victorie (jucătorul a câștigat jocul)
}
