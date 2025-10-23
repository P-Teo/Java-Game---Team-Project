# 👑 **Warrior Princess and the Curse of the Dark King**

A **2D action-adventure** game developed in **Java**, where the player controls **Princess Isla**, the heir of the Kingdom of Velmira, on her heroic quest to rescue her father and defeat the **King of Shadows**.

---

## 🧙‍♀️ *Story*

The peaceful Kingdom of Velmira has fallen under a dark curse.  
After her father, King Alden, is captured by the Shadow King, Princess Isla embarks on an epic journey through cursed lands and monstrous foes.  
Armed with a magical sword and boundless courage, she must face her destiny and bring light back to her world.

---

## 🏰 *Game Levels*

The game includes **five levels**, each with unique enemies and challenges:

1. **Swamp of the Minotaurs** – Intro level with basic enemies and no traps.  
2. **Enchanted Forest** – Adds stone golems and magical traps that reduce health.  
3. **Land of Trolls** – Stronger enemies and falling rocks to avoid.  
4. **Cursed Bridge** – Waves of skeletons followed by two Night Knights; multiple traps.  
5. **Castle of Shadows** – Final boss fight against the Shadow King; no traps, but highest difficulty.

---

## ⚔️ *Main Features*

- **5 unique levels** with increasing difficulty:  
  *(Swamp of the Minotaurs, Enchanted Forest, Land of Trolls, Cursed Bridge, Castle of Shadows)*
- **Life system** with 3 hearts and a performance-based score  
- **Intuitive controls:**
  - Arrow keys → movement  
  - Spacebar → sword attack  
- **Automatic progress saving** using an SQLite database  
- **Dedicated screens for:**
  - Main Menu  
  - Level Selection  
  - Game Over & Victory   

---

## 🎥 *Camera and Visual Effects*

The game uses a **2D side-scrolling camera system** that follows the player across the map.  
When Isla moves, the background scrolls horizontally to simulate movement through the world, creating a smooth and dynamic exploration experience.

Additional visual features:
- **Fog of War** – darkens unexplored areas while keeping the player visible.
- **Mini-map** – shows Isla’s position and overall progress in the level.
- **Life indicator** – three hearts displayed on the screen, decreasing when taking damage.

---

## 🧱 *Architecture & Implementation*

The game is developed in **Java** using an **object-oriented, modular architecture**.  
It’s organized into several packages:

- `game` – main logic (Game, GameState, Level, Entity, etc.)  
- `entities` – Player, Enemy, and their subclasses  
- `objects` – traps, stars, castles, etc.  
- `ui` – graphical interfaces (StartMenu, GameOver, LevelSelect)  
- `database` – progress saving using SQLite  
- `graphics` – sprites, animations, and backgrounds
<img src="https://github.com/user-attachments/assets/ddab5ee8-858f-4ed7-8a2e-5fa79058833f" width="800"/>
<div>
  <img src="https://github.com/user-attachments/assets/0b7c5e8a-0c0a-420c-a20f-8b85d8887167" width="225"/>
  <img src="https://github.com/user-attachments/assets/b69a50ba-3a86-4423-a595-bee117a85ac8" width="330"/>
  <img src="https://github.com/user-attachments/assets/f7f227f2-aa2e-42ad-b334-5f146104565b" width="430"/>
</div>




---

## 🧩 *Design Patterns Used*

- **State Pattern** – manages game states (`START_MENU`, `LEVEL_SELECT`, etc.)  
- **Template Method** – abstract base classes `Level` and `Entity`  

---

## 💾 *Database*

**Local database:** `game_progress.db`

| Table | Description |
|--------|--------------|
| `LevelScores` | Stores scores for each completed level |
| `GameState` | Saves the last level reached |
| `GameResults` | Stores final scores, player names, and stars |
<img src="https://github.com/user-attachments/assets/9e77c864-ee03-4954-ae1e-9e39a9bc4b34" width="600"/>


---

## 🖼️ *Screenshots*

<img src="https://github.com/user-attachments/assets/ef7d66d3-f5fe-44ba-933d-f1d6e777a862" width="300"/>
<img src="https://github.com/user-attachments/assets/ca19712a-d92b-4f8b-a24a-5cc56928aa38" width="300"/>
<img src="https://github.com/user-attachments/assets/3f93876b-0d13-428d-9932-c0cd62464666" width="300"/>
<img src="https://github.com/user-attachments/assets/4bdab8e1-d53b-4c7a-b7e2-573351f6cde2" width="300"/>
<img src="https://github.com/user-attachments/assets/01a972a2-f031-47af-8e3c-dec95ca6296b" width="300"/>
<img src="https://github.com/user-attachments/assets/f7819414-323c-4958-a22a-6486bb50d884" width="300"/>



---

## 🧾 *License*

This project was created for **educational purposes**.  
All visual and written resources belong to the respective authors.
