# Compte Rendu de TP : Currency Converter (Android Kotlin)

## 1. Introduction et Objectifs
Ce travail pratique (TP) a consisté à développer une application Android native en **Kotlin** intitulée **« Currency Converter »**. L'application permet de convertir un montant d'une devise source vers une devise cible en s'appuyant sur des taux de change fixes et une devise pivot (l'Euro).

Les principaux objectifs pédagogiques atteints sont :
- La construction d'une interface utilisateur adaptative avec `ConstraintLayout` et `TableLayout`.
- L'utilisation de composants interactifs (`Spinner`, `ArrayAdapter`, `ImageButton`).
- La mise en place d'un clavier numérique personnalisé intégré à l'application.
- L'application rigoureuse de règles de gestion (validation, formatage, limitations de saisie).
- L'utilisation du **View Binding** pour un accès sécurisé aux vues.

---

## 2. Choix d'interface et Architecture UI (`activity_main.xml`)
L'interface respecte strictement la maquette fournie :
- **Zone supérieure (52 % de l'écran)** :
  - Fond en bleu turquoise uni (`#2BB8E8`).
  - Contient le titre (*Currency Converter*), les sélecteurs de devises (`Spinner` `spDeviseSource` et `spDeviseCible`), les affichages du montant saisi et du résultat (polices de 44sp, alignés à droite et soulignés dynamiquement), le bouton d'inversion des devises, ainsi que le bouton de lancement de la conversion (*Convertir*).
- **Zone inférieure (Clavier numérique)** :
  - Un `LinearLayout` structuré en 4 lignes × 3 colonnes simulant un pavé numérique.
  - Les chiffres utilisent la couleur bleue turquoise (`#1BA6C9`), tandis que la touche de réinitialisation **« C »** est mise en valeur en rouge clair (`#F0727A`).
  - Les boutons sont sans bordure (`borderlessButtonStyle`) en taille 32sp.

---

## 3. Logique de conversion et Algorithmique (`MainActivity.kt`)

### A. Remplissage des Listes et Taux Fixes
Les `Spinner` sont alimentés dynamiquement à l'aide d'un `ArrayAdapter` construit sur la ressource `R.array.devises` (`USD`, `EUR`, `TND`, `GBP`, `JPY`, `CAD`). Par défaut, la source est positionnée sur **USD** et la cible sur **EUR**.

Les taux de change fixes pour 1 EUR sont stockés dans une `Map<String, Double>` :
```kotlin
private val taux = mapOf(
    "EUR" to 1.0, "USD" to 1.17, "TND" to 3.40,
    "GBP" to 0.87, "JPY" to 172.50, "CAD" to 1.62
)
```

### B. Algorithme de Conversion (Devise Pivot)
La fonction de conversion passe par l'Euro comme pivot :
1. $\text{Montant en EUR} = \frac{\text{Montant}}{\text{Taux}_{\text{source}}}$
2. $\text{Résultat} = \text{Montant en EUR} \times \text{Taux}_{\text{cible}}$
3. Le résultat est formaté et arrondi strictement à 2 décimales via `String.format(Locale.getDefault(), "%.2f", resultat)`.

### C. Gestion du Clavier et Règles de Gestion
- **Saisie personnalisée** : Aucun clavier système n'est affiché (`tvMontant` est un `TextView`). Chaque appui sur le clavier numérique appelle `ajouterCaractere(c)`.
- **Règle du zéro initial** : Le montant initial est `"0"`. Le premier chiffre tapé le remplace.
- **Point décimal unique** : Une vérification empêche l'ajout d'un second point `.`.
- **Limite de longueur** : La saisie est plafonnée à 10 caractères maximum.
- **Validation** : Si le montant est nul ou égal à 0 lors du clic sur *Convertir*, un `Toast` (*« Veuillez saisir un montant »*) est affiché.
- **Inversion (`btnInverser`)** : Échange les sélections des deux `Spinner` et relance la conversion.
- **Bonus** : Conversion automatique en temps réel à chaque appui sur le clavier ou changement de devise.

---

## 4. Cas de Test et Résultats

| Montant saisi | Devise Source | Devise Cible | Résultat Attendu | Résultat Obtenu | Statut |
| :--- | :---: | :---: | :---: | :---: | :---: |
| **120** | USD | EUR | 102.56 | 102.56 | ✅ Validé |
| **100** | EUR | TND | 340.00 | 340.00 | ✅ Validé |
| **50** | GBP | USD | 67.24 | 67.24 | ✅ Validé |
| **10** | EUR | EUR | 10.00 | 10.00 | ✅ Validé |

*(Les captures d'écran correspondantes sont disponibles dans le dossier `screenshots/` du dépôt GitHub).*

---

## 5. Difficultés rencontrées et Solutions
1. **Contraintes d'agencement dans le `ConstraintLayout`** :
   * *Difficulté* : Le positionnement côte à côte des listes et des montants avec un bouton d'inversion vertical risquait de provoquer des chevauchements.
   * *Solution* : Utilisation d'une structure hiérarchique claire avec une ligne de guidage (`Guideline` à 52 %) séparant nettement la zone d'affichage et le clavier numérique.
2. **Rendu des icônes vectorielles** :
   * *Difficulté* : Assurer une visibilité parfaite des icônes sur le fond turquoise.
   * *Solution* : Ajout d'un fond circulaire blanc (`bg_circle.xml`) sous le bouton d'inversion pour garantir un contraste optimal et une clarté visuelle irréprochable.

---

## 6. Conclusion
Ce TP a permis d'appréhender la création d'une application Android interactive complète en Kotlin. Le respect minutieux de la maquette, la rigueur dans la gestion des règles de saisie et l'implémentation d'une logique de calcul robuste (devise pivot) garantissent une expérience utilisateur fluide et fonctionnelle.
