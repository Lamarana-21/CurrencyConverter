# Currency Converter - Application Android Kotlin

Application Android développée dans le cadre du TP de développement mobile « Currency Converter ». Elle permet de convertir un montant d'une devise vers une autre à partir de taux de change fixes, en passant par l'Euro comme devise pivot.

## 🚀 Fonctionnalités principales
- **Interface utilisateur fidèle à la maquette** :
  - Zone supérieure sur fond bleu turquoise uni (`#2BB8E8`) regroupant le titre, les listes de devises (`Spinner`), les montants soulignés et le bouton de conversion.
  - Bouton d'inversion des devises avec icône dédiée.
- **Clavier numérique personnalisé** :
  - `TableLayout` de 4 lignes × 3 colonnes.
  - Touches numériques en bleu turquoise (`#1BA6C9`) et touche **C** en rouge clair (`#F0727A`).
  - Saisie gérée exclusivement via l'application (pas de clavier système).
- **Règles de gestion respectées** :
  - Un seul point décimal autorisé (les appuis supplémentaires sur `.` sont ignorés).
  - Longueur maximale du montant limitée à 10 caractères.
  - Vérification du montant à zéro avec affichage d'un `Toast` (`« Veuillez saisir un montant »`).
  - Arrondi strict à 2 décimales pour le résultat (`String.format`).
- **Bonus implémenté** :
  - Conversion automatique en temps réel dès qu'un chiffre est saisi ou qu'une devise est modifiée.

## 🛠️ Stack Technique
- **Langage** : Kotlin
- **UI** : ConstraintLayout, TableLayout, Spinners, ArrayAdapter
- **Liaison des vues** : View Binding
- **Compatibilité** : SDK minimum API 24 (Android 7.0+)

---
*Développé dans le cadre du TP Currency Converter.*
