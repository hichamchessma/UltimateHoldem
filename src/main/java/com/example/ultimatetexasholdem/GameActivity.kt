/*GameActivity*/
package com.example.ultimatetexasholdem

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.ultimatetexasholdem.ui.theme.UltimateTexasHoldemTheme

class GameActivity : ComponentActivity() {

    private val deck = Deck()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UltimateTexasHoldemTheme {
                val gameState = GameState(deck)
                GameTable(gameState)
            }
        }
    }
}
