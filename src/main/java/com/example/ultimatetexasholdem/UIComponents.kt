/*UIComponents*/
package com.example.ultimatetexasholdem

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex


@Composable
fun GameTable(gameState: GameState) {
    val playerAction = PlayerAction()

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.ultimate_table),
            contentDescription = "Table de Texas Hold'em",
            modifier = Modifier
                .fillMaxSize()
        )

        // Bet Selection Dialog
        /*if (gameState.showBetSelectionDialog.value) {
            AlertDialog(
                onDismissRequest = { gameState.showBetSelectionDialog.value = false },
                title = { Text(text = "Sélectionnez votre mise") },
                text = {
                    LazyColumn(
                        modifier = Modifier.height(200.dp) // Ajuster la hauteur si nécessaire
                    ) {
                        items((100..2000 step 100).toList()) { amount ->
                            Text(
                                text = "Miser $amount",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        gameState.betUnit.value = amount
                                        gameState.showBetSelectionDialog.value = false
                                        playerAction.deal(gameState.deck, gameState) // Appeler la fonction de distribution après la sélection
                                    }
                                    .padding(16.dp)
                            )
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { gameState.showBetSelectionDialog.value = false }
                    ) {
                        Text("Annuler")
                    }
                }
            )
        }*/

        // Display Cards
        if (gameState.dealClicked.value) {
            CommunityCards(gameState)
            // Afficher les jetons si une mise a été placée
            if (gameState.playerBet.value >= 0) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 0.dp, top = 550.dp) // Ajuster cette valeur pour que le jeton soit en dessous des cartes communautaires
                        .zIndex(2f), // Mettre un zIndex suffisamment élevé pour que le jeton soit au-dessus
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.poker_chips), // Assurez-vous que l'image est dans le dossier res/drawable
                        contentDescription = "Jetons de pari",
                        modifier = Modifier
                            .size(50.dp) // Assurez-vous que la taille soit appropriée
                            .clip(CircleShape) // Pour rendre l'image circulaire

                    )
                }
            }
            DealerCards(gameState)
        }

        PlayerInfoUI(gameState)

        // Position des cartes du joueur
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 15.dp), // Ajuster la valeur pour une position optimale
            contentAlignment = Alignment.BottomCenter
        ) {
            PlayerCards(gameState)
        }
    }

    // Player Actions aligned to the bottom of the screen
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 32.dp), // Ajuster cette valeur si besoin
        contentAlignment = Alignment.BottomCenter
    ) {
        PlayerActions(playerAction, gameState)
    }
}




@Composable
fun CommunityCards(gameState: GameState) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 280.dp)
    ) {
        gameState.communityCards.value.forEachIndexed { index, card ->
            when (gameState.checkCount.value) {
                1 -> if (index < 3) CardPlaceholder(resourceId = card.resourceId) else CardPlaceholder(resourceId = R.drawable.card_back)
                2 -> CardPlaceholder(resourceId = card.resourceId)
                else -> CardPlaceholder(resourceId = R.drawable.card_back)
            }
        }
    }
}

@Composable
fun PlayerCards(gameState: GameState) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 100.dp), // Ajuste ce padding pour placer les cartes juste au-dessus des boutons
        contentAlignment = Alignment.Center // Centre la Box horizontalement
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp), // Espacement entre les cartes
            modifier = Modifier.wrapContentSize()
        ) {
            gameState.playerCards.value.forEach {
                CardPlaceholder(resourceId = it.resourceId)
            }
        }
    }
}
@Composable
fun DealerCards(gameState: GameState) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        gameState.dealerCards.value.forEach {
            val resourceId = if (gameState.dealerCardsRevealed.value) it.resourceId else R.drawable.card_back
            CardPlaceholder(resourceId = resourceId)
        }
    }
}

@Composable
fun PlayerInfoUI(gameState: GameState) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                gameState.isEditingStack.value = false
            }
    ) {
        if (gameState.isEditingStack.value) {
            TextField(
                value = gameState.playerStack.value.toString(),
                onValueChange = { newValue ->
                    newValue.toIntOrNull()?.let {
                        gameState.playerStack.value = it
                    }
                },
                label = { Text("Modifier Stack Joueur") },
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .fillMaxWidth(),
                singleLine = true,
                keyboardActions = KeyboardActions(
                    onDone = { gameState.isEditingStack.value = false }
                )
            )
        } else {
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Stack Joueur: ${gameState.playerStack.value}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.clickable { gameState.isEditingStack.value = true }
                )
                Text(
                    text = "Phase: ${gameState.gamePhase.value}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun PlayerActions(playerAction: PlayerAction, gameState: GameState, modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        /*Button(
            onClick = { gameState.showBetSelectionDialog.value = true },
            enabled = !gameState.dealClicked.value
        ) {
            Text(text = "Deal")
        }*/
        Button(
            onClick = {
                gameState.betUnit.value = 100 // Fixer automatiquement la mise à 100
                playerAction.deal(gameState.deck, gameState) // Appeler la fonction de distribution
            },
            enabled = !gameState.dealClicked.value
        ) {
            Text(text = "Deal")
        }
        Button(
            onClick = { playerAction.check(gameState) },
            enabled = gameState.dealClicked.value
        ) {
            Text(text = "Check")
        }
        Button(
            onClick = {
                playerAction.bet(gameState)
            },
            enabled = gameState.dealClicked.value && gameState.sessionActive.value
        ) {
            Text(text = "Bet")
        }
        Button(
            onClick = { playerAction.fold(gameState) },
            enabled = gameState.dealClicked.value && gameState.sessionActive.value
        ) {
            Text(text = "Fold")
        }
        Button(
            onClick = { playerAction.nextHand(gameState.deck, gameState) }
        ) {
            Text(text = "Main suivante")
        }
    }


}
@Composable
fun CardPlaceholder(resourceId: Int) {
    Image(
        painter = painterResource(id = resourceId),
        contentDescription = null,
        modifier = Modifier
            .width(60.dp)
            .height(90.dp)
            .padding(4.dp)
    )
}
