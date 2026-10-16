package com.example.currencyconverter

import android.graphics.Paint
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.currencyconverter.databinding.ActivityMainBinding
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // Taux de change fixes pour 1 EUR
    private val taux = mapOf(
        "EUR" to 1.0,
        "USD" to 1.17,
        "TND" to 3.40,
        "GBP" to 0.87,
        "JPY" to 172.50,
        "CAD" to 1.62
    )

    private var isResultCalculated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Soulignement des TextView montant et résultat
        binding.tvMontant.paintFlags = binding.tvMontant.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        binding.tvResultat.paintFlags = binding.tvResultat.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        setupSpinners()
        setupKeypad()
        setupListeners()
    }

    /**
     * Remplissage des Spinners source et cible avec le tableau de string "devises".
     * Par défaut : Source = USD, Cible = EUR.
     */
    private fun setupSpinners() {
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.devises,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spDeviseSource.adapter = adapter
        binding.spDeviseCible.adapter = adapter

        // Valeurs par défaut : source = USD, cible = EUR
        val devisesList = resources.getStringArray(R.array.devises)
        val usdIndex = devisesList.indexOf("USD")
        val eurIndex = devisesList.indexOf("EUR")

        if (usdIndex >= 0) binding.spDeviseSource.setSelection(usdIndex)
        if (eurIndex >= 0) binding.spDeviseCible.setSelection(eurIndex)

        // Écouteur de changement de devise pour conversion automatique (Bonus)
        val spinnerListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (isResultCalculated || binding.tvResultat.text.isNotEmpty()) {
                    lancerConversion(false)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        binding.spDeviseSource.onItemSelectedListener = spinnerListener
        binding.spDeviseCible.onItemSelectedListener = spinnerListener
    }

    /**
     * Configuration des clics sur les touches du clavier numérique.
     */
    private fun setupKeypad() {
        val numberButtons = listOf(
            binding.btn0, binding.btn1, binding.btn2, binding.btn3,
            binding.btn4, binding.btn5, binding.btn6, binding.btn7,
            binding.btn8, binding.btn9, binding.btnDot, binding.btnC
        )

        for (button in numberButtons) {
            button.setOnClickListener {
                when (val text = button.text.toString()) {
                    "C" -> reinitialiserClavier()
                    else -> ajouterCaractere(text)
                }
            }
        }
    }

    /**
     * Ajoute un caractère au montant saisi en respectant les règles de gestion :
     * - Remplace "0" initial.
     * - Un seul point décimal autorisé.
     * - Limité à 10 caractères.
     */
    private fun ajouterCaractere(c: String) {
        val currentText = binding.tvMontant.text.toString()

        // Règle : un seul point décimal
        if (c == "." && currentText.contains(".")) {
            return
        }

        val newText = if (currentText == "0" && c != ".") {
            c
        } else {
            currentText + c
        }

        // Règle : limité à 10 caractères
        if (newText.length > 10) {
            return
        }

        binding.tvMontant.text = newText

        // Bonus : conversion automatique à chaque saisie
        if (newText != "0" && newText.isNotEmpty() && newText != ".") {
            lancerConversion(false)
        } else if (newText == "0" || newText == ".") {
            binding.tvResultat.text = ""
            isResultCalculated = false
        }
    }

    /**
     * Remet le montant à "0" et vide le résultat.
     */
    private fun reinitialiserClavier() {
        binding.tvMontant.text = "0"
        binding.tvResultat.text = ""
        isResultCalculated = false
    }

    /**
     * Configuration des boutons Convertir et Inverser.
     */
    private fun setupListeners() {
        binding.btnConvertir.setOnClickListener {
            lancerConversion(true)
        }

        binding.btnInverser.setOnClickListener {
            inverserDevises()
        }
    }

    /**
     * Lance la conversion en vérifiant le montant et en affichant un Toast si nécessaire.
     */
    private fun lancerConversion(showToastOnZero: Boolean) {
        val montantStr = binding.tvMontant.text.toString()
        val montant = montantStr.toDoubleOrNull() ?: 0.0

        if (montant <= 0.0) {
            if (showToastOnZero) {
                Toast.makeText(this, R.string.veuillez_saisir_montant, Toast.LENGTH_SHORT).show()
            }
            binding.tvResultat.text = ""
            isResultCalculated = false
            return
        }

        val source = binding.spDeviseSource.selectedItem.toString()
        val cible = binding.spDeviseCible.selectedItem.toString()

        val resultat = convertir(montant, source, cible)
        binding.tvResultat.text = String.format(Locale.getDefault(), "%.2f", resultat)
        isResultCalculated = true
    }

    /**
     * Effectue le calcul de conversion en passant par l'euro comme devise pivot.
     * montant en EUR = montant / taux(source)
     * résultat = montant en EUR * taux(cible)
     */
    private fun convertir(montant: Double, source: String, cible: String): Double {
        val tauxSource = taux[source] ?: 1.0
        val tauxCible = taux[cible] ?: 1.0

        val montantEnEur = montant / tauxSource
        return montantEnEur * tauxCible
    }

    /**
     * Échange les sélections des deux Spinners et relance la conversion.
     */
    private fun inverserDevises() {
        val sourceIndex = binding.spDeviseSource.selectedItemPosition
        val cibleIndex = binding.spDeviseCible.selectedItemPosition

        binding.spDeviseSource.setSelection(cibleIndex)
        binding.spDeviseCible.setSelection(sourceIndex)

        lancerConversion(false)
    }
}
