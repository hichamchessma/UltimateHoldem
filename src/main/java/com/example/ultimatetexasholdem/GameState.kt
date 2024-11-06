package com.example.ultimatetexasholdem

import androidx.compose.runtime.mutableStateOf

class GameState(val deck: Deck) {
    val dealClicked = mutableStateOf(false)
    val communityCards = mutableStateOf<List<Card>>(emptyList())
    val playerCards = mutableStateOf<List<Card>>(emptyList())
    val dealerCards = mutableStateOf<List<Card>>(emptyList())
    val checkCount = mutableStateOf(0)
    val dealerCardsRevealed = mutableStateOf(false)
    val playerStack = mutableStateOf(1000) // Stack initial du joueur
    val betUnit = mutableStateOf(0)
    val currentBet = mutableStateOf(0) // Mise actuelle
    val playerBet = mutableStateOf(0) // Mise du joueur pour la main
    val sessionActive = mutableStateOf(false) // Indique si une session de jeu est active
    val isEditingStack = mutableStateOf(false)
    val showBetUnitDialog = mutableStateOf(true)
    val gamePhase = mutableStateOf("Preflop") // Phase du jeu
    var showBetSelectionDialog = mutableStateOf(false)

    init {
        deck.reset()
    }
}
