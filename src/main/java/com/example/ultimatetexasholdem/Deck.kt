/*Deck*/
package com.example.ultimatetexasholdem

data class Card(val value: String, val suit: String, val resourceId: Int)

class Deck {
    private val cards: MutableList<Card> = mutableListOf()
    private val initialCards: List<Card>

    init {
        val suits = listOf("c", "d", "h", "s") // Clubs, Diamonds, Hearts, Spades
        val values = listOf( "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K")

        // Crée toutes les cartes avec leurs identifiants de ressource correspondants
        for (suit in suits) {
            for (value in values) {
                val resourceName = "card_${value.lowercase()}$suit"
                val resourceId = R.drawable::class.java.getDeclaredField(resourceName).getInt(null)
                cards.add(Card(value, suit, resourceId))
            }
        }
        initialCards = cards.toList()
    }

    fun shuffle() {
        cards.shuffle()
    }

    fun reset() {
        cards.clear()
        cards.addAll(initialCards)
    }


    fun deal(number: Int): List<Card> {
        return cards.take(number).also {
            cards.removeAll(it)
        }
    }
}
