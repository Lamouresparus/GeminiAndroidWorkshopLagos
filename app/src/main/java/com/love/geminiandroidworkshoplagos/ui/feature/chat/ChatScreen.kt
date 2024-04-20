package com.love.geminiandroidworkshoplagos.ui.feature.chat

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import java.util.*
import kotlinx.coroutines.launch

enum class Participant {
    USER, MODEL, ERROR
}

// TODO() When creating an instance of this, use UUID.randomUUID().toString() to generate the ID
data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    var text: String,
    val participant: Participant,
    var isPending: Boolean = false
)

val dummyChatHistory = listOf(
    ChatMessage(
        text = "Hi",
        participant = Participant.USER,
    ),
    ChatMessage(
        text = "Hello, How can I help you today?",
        participant = Participant.MODEL,
    ),
    ChatMessage(
        text = "What role do you see AI playing in the future of technology?",
        participant = Participant.USER,
    ),
    ChatMessage(
        text = "Artificial intelligence (AI) is expected to be a major driver of future technology, with wide-ranging impacts across many sectors. Here are some of the potential roles AI could play:\n" +
                "\n" +
                "**1. Automation:** AI is already being used to automate tasks in various industries, from manufacturing to customer service. In the future, AI could automate even more complex tasks, potentially leading to increased efficiency and productivity.\n" +
                "\n" +
                "**2. Enhanced decision-making:** AI can analyze vast amounts of data to identify patterns and trends that humans might miss. This can be used to improve decision-making in areas like finance, healthcare, and business strategy.\n" +
                "\n" +
                "**3. Scientific advancement:** AI can be used to analyze scientific data and run simulations, accelerating scientific discovery and innovation. For instance, AI could be used to design new materials, drugs, and clean energy technologies.\n" +
                "\n" +
                "**4. Personalized experiences:** AI can be used to personalize user experiences across different technologies. For example, AI-powered recommendation systems can suggest products or content that users are likely to enjoy.\n" +
                "\n" +
                "**5. Robotics:** AI is playing an increasingly important role in robotics. AI-powered robots can perform complex tasks in dangerous or difficult environments, such as search and rescue operations or deep-sea exploration.\n" +
                "\n" +
                "**6. Smarter infrastructure:** AI can be used to create smarter infrastructure systems, such as self-driving cars, which could revolutionize transportation. Additionally, AI could be used to optimize energy grids and improve traffic flow in cities.\n" +
                "\n" +
                "However, there are also challenges to consider with the widespread adoption of AI. These include issues of data privacy, bias in algorithms, and the potential for job displacement due to automation.\n" +
                "\n" +
                "Overall, AI has the potential to significantly transform the future of technology, bringing both benefits and challenges that society will need to address.",
        participant = Participant.MODEL,
    ),
)

@Composable
fun ChatScreen() {

    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 80.dp)
    ) {
        // TODO() Messages List -> replace with messages from VM
        ChatList(
            dummyChatHistory,
            listState,
            Modifier
                .fillMaxSize()
                .padding(16.dp)
        )

        MessageInput(
            onSendMessage = { inputText ->
                // TODO() send message to gemini
            },
            resetScroll = {
                coroutineScope.launch {
                    listState.scrollToItem(0)
                }
            },
            Modifier
                .fillMaxWidth()
        )
    }
}


@Composable
fun ChatList(
    chatMessages: List<ChatMessage>,
    listState: LazyListState,
    modifier: Modifier
) {
    LazyColumn(
        reverseLayout = true,
        state = listState,
        modifier = modifier
    ) {
        items(chatMessages.reversed()) { message ->
            ChatBubbleItem(message)
        }
    }
}


@Composable
fun ChatBubbleItem(
    chatMessage: ChatMessage
) {
    val isModelMessage = chatMessage.participant == Participant.MODEL ||
            chatMessage.participant == Participant.ERROR

    val backgroundColor = when (chatMessage.participant) {
        Participant.MODEL -> MaterialTheme.colorScheme.primaryContainer
        Participant.USER -> MaterialTheme.colorScheme.tertiaryContainer
        Participant.ERROR -> MaterialTheme.colorScheme.errorContainer
    }

    val bubbleShape = if (isModelMessage) {
        RoundedCornerShape(4.dp, 20.dp, 20.dp, 20.dp)
    } else {
        RoundedCornerShape(20.dp, 4.dp, 20.dp, 20.dp)
    }

    val horizontalAlignment = if (isModelMessage) {
        Alignment.Start
    } else {
        Alignment.End
    }

    Column(
        horizontalAlignment = horizontalAlignment,
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = chatMessage.participant.name,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Row {
            if (chatMessage.isPending) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(all = 8.dp)
                )
            }
            BoxWithConstraints {
                Card(
                    colors = CardDefaults.cardColors(containerColor = backgroundColor),
                    shape = bubbleShape,
                    modifier = Modifier.widthIn(0.dp, maxWidth * 0.9f)
                ) {
                    Text(
                        text = chatMessage.text,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun MessageInput(
    onSendMessage: (String) -> Unit,
    resetScroll: () -> Unit = {},
    modifier: Modifier
) {
    var userMessage by rememberSaveable { mutableStateOf("") }

    ElevatedCard(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            OutlinedTextField(
                value = userMessage,
                label = { Text("Message") },
                onValueChange = { userMessage = it },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                ),
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .fillMaxWidth()
                    .weight(0.85f)
            )
            IconButton(
                onClick = {
                    if (userMessage.isNotBlank()) {
                        onSendMessage(userMessage)
                        userMessage = ""
                        resetScroll()
                    }
                },
                modifier = Modifier
                    .padding(start = 16.dp)
                    .align(Alignment.CenterVertically)
                    .fillMaxWidth()
                    .weight(0.15f)
            ) {
                Icon(
                    Icons.Default.Send,
                    contentDescription = "Send",
                    modifier = Modifier
                )
            }
        }
    }
}
