/*PlayerAction*/
package com.example.ultimatetexasholdem

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerAction {
    fun deal(deck: Deck, gameState: GameState) {
        deck.reset() // Réinitialiser le paquet
        deck.shuffle()
        val dealtCards = deck.deal(9) // 5 cartes communauté + 2 cartes du joueur + 2 cartes du dealer
        gameState.communityCards.value = dealtCards.subList(0, 5) // 5 cartes communauté
        gameState.playerCards.value = dealtCards.subList(5, 7) // 2 cartes pour le joueur
        gameState.dealerCards.value = dealtCards.subList(7, 9) // 2 cartes pour le dealer
        gameState.dealClicked.value = true
        gameState.checkCount.value = 0 // Reset check count
        gameState.dealerCardsRevealed.value = false
        gameState.sessionActive.value = true
        gameState.currentBet.value = 0
        gameState.playerBet.value = 0
        gameState.gamePhase.value = "Preflop"
    }

    fun check(gameState: GameState) {
        when (gameState.checkCount.value) {
            0 -> {
                gameState.checkCount.value += 1
                gameState.gamePhase.value = "Flop"
            }
            1 -> {
                gameState.checkCount.value += 1
                gameState.gamePhase.value = "Complet"
            }
            2 -> {
                gameState.dealerCardsRevealed.value = true
            }
        }
    }

    fun bet(gameState: GameState) {
        if (gameState.checkCount.value == 0) {
            val multiplier = if ((0..1).random() == 0) 3 else 4
            val betAmount = gameState.betUnit.value * multiplier
            if (gameState.playerStack.value >= betAmount) {
                gameState.playerStack.value -= betAmount
                gameState.currentBet.value += betAmount
                gameState.playerBet.value += betAmount
                gameState.checkCount.value += 1 // Afficher les cartes flop
                gameState.gamePhase.value = "Flop"
                CoroutineScope(Dispatchers.Main).launch {
                    delay(2000) // Attendre 2 secondes
                    gameState.checkCount.value += 1 // Afficher les cartes turn et river
                    gameState.gamePhase.value = "Complet"
                    gameState.dealerCardsRevealed.value = true
                }
            }
        } else {
            val betAmount = gameState.betUnit.value
            if (gameState.playerStack.value >= betAmount) {
                gameState.playerStack.value -= betAmount
                gameState.currentBet.value += betAmount
                gameState.playerBet.value += betAmount
            }
        }
    }

    fun fold(gameState: GameState) {
        gameState.dealClicked.value = false
        gameState.sessionActive.value = false
        gameState.dealerCardsRevealed.value = true // Le dealer montre ses cartes
    }

    fun nextHand(deck: Deck, gameState: GameState) {
        deck.shuffle()
        gameState.communityCards.value = emptyList()
        gameState.playerCards.value = emptyList()
        gameState.dealerCards.value = emptyList()
        gameState.dealClicked.value = false
        gameState.checkCount.value = 0
        gameState.dealerCardsRevealed.value = false
        gameState.currentBet.value = 0
        gameState.playerBet.value = 0
        gameState.sessionActive.value = false
        gameState.gamePhase.value = "Preflop"
    }
}
