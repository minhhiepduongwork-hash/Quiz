package com.example.quiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quiz.ui.theme.QuizTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuizTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        Column {
                            TopAppBar(
                                title = {
                                    Text(
                                        text = stringResource(R.string.app_name),
                                        fontSize = 28.sp
                                    )
                                },
                                expandedHeight = 48.dp
                            )
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                thickness = 1.dp,
                                color = Color.Gray
                            )
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .consumeWindowInsets(innerPadding)
                            .imePadding()
                    ) {
                        QuizApp()
                    }
                }
            }
        }
    }
}

@Composable
fun QuizApp(
    modifier: Modifier = Modifier
) {
    var q1 by remember {
        mutableStateOf("")
    }
    var q2 by remember {
        mutableStateOf(setOf<String>())
    }
    var q3 by remember {
        mutableStateOf("")
    }
    var score by remember {
        mutableIntStateOf(-1)
    }
    var q1correct by remember {
        mutableStateOf(false)
    }
    var q2correct by remember {
        mutableStateOf(false)
    }
    var q3correct by remember {
        mutableStateOf(false)
    }

    val submitted = score >= 0
    val correctColor = Color(0xFF2E7D32)
    val incorrectColor = Color(0xFFC62828)
    val q1color = when {
        !submitted -> Color.Unspecified
        q1correct -> correctColor
        else -> incorrectColor
    }
    val q2color = when {
        !submitted -> Color.Unspecified
        q2correct -> correctColor
        else -> incorrectColor
    }
    val q3color = when {
        !submitted -> Color.Unspecified
        q3correct -> correctColor
        else -> incorrectColor
    }

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        QuestionOne(
            choice = q1,
            onChoiceChange = {
                q1 = it
                score = -1
            },
            resultColor = q1color
        )
        Divider()
        QuestionTwo(
            selectedChoices = q2,
            onChoiceChange = {
                q2 = it
                score = -1
            },
            resultColor = q2color
        )
        Divider()
        QuestionThree(
            answer = q3,
            onAnswerChange = {
                q3 = it
                score = -1
            },
            resultColor = q3color
        )
        if (score >= 0 && !q3correct) {
            Text(
                text = stringResource(R.string.q3_answer),
                modifier = Modifier.padding(vertical = 4.dp))
        }
        QuizActionButton(
            onClick = {
                q1correct = q1 == "A"
                q2correct = q2 == setOf("A", "C")
                q3correct = q3.trim().equals("Composable", ignoreCase = true)
                score = listOf(q1correct, q2correct, q3correct).count { it }
            },
            text = stringResource(R.string.submit_answers),
            modifier = Modifier.padding(vertical = 8.dp)
        )
        if (score >= 0) {
            Text(
                text = "Your Score: $score/3",
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = stringResource(
                    if (q1correct) R.string.q1_correct
                    else R.string.q1_incorrect
                )
            )
            Text(
                text = stringResource(
                    if (q2correct) R.string.q2_correct
                    else R.string.q2_incorrect
                )
            )
            Text(
                text = stringResource(
                    if (q3correct) R.string.q3_correct
                    else R.string.q3_incorrect
                )
            )
        }
        if (submitted) {
            QuizActionButton(
                onClick = {
                    q1 = ""
                    q2 = emptySet()
                    q3 = ""
                    score = -1
                    q1correct = false
                    q2correct = false
                    q3correct = false
                },
                text = stringResource(R.string.reset_quiz),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.primary
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun QuestionOne(
    modifier: Modifier = Modifier,
    choice: String,
    onChoiceChange: (String) -> Unit,
    resultColor: Color = Color.Unspecified
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.q1),
            fontWeight = FontWeight.Bold
        )
        Text(
            text = stringResource(R.string.q1_text)
        )
        SingleChoice(
            answer = stringResource(R.string.q1_A),
            selected = choice == "A",
            onClick = { onChoiceChange("A") },
            resultColor = resultColor
        )
        SingleChoice(
            answer = stringResource(R.string.q1_B),
            selected = choice == "B",
            onClick = { onChoiceChange("B") },
            resultColor = resultColor
        )
        SingleChoice(
            answer = stringResource(R.string.q1_C),
            selected = choice == "C",
            onClick = { onChoiceChange("C") },
            resultColor = resultColor
        )
        SingleChoice(
            answer = stringResource(R.string.q1_D),
            selected = choice == "D",
            onClick = { onChoiceChange("D") },
            resultColor = resultColor
        )
    }
}

@Composable
fun QuestionTwo(
    modifier: Modifier = Modifier,
    selectedChoices: Set<String>,
    onChoiceChange: (Set<String>) -> Unit,
    resultColor: Color = Color.Unspecified
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.q2),
            fontWeight = FontWeight.Bold
        )
        Text(
            text = stringResource(R.string.q2_text)
        )
        MultipleChoice(
            answer = stringResource(R.string.q2_A),
            checked = "A" in selectedChoices,
            onCheckedChange = { checked ->
                onChoiceChange(
                    if (checked) {
                        selectedChoices + "A"
                    } else {
                        selectedChoices - "A"
                    }
                )
            },
            resultColor = resultColor
        )
        MultipleChoice(
            answer = stringResource(R.string.q2_B),
            checked = "B" in selectedChoices,
            onCheckedChange = { checked ->
                onChoiceChange(
                    if (checked) {
                        selectedChoices + "B"
                    } else {
                        selectedChoices - "B"
                    }
                )
            },
            resultColor = resultColor
        )
        MultipleChoice(
            answer = stringResource(R.string.q2_C),
            checked = "C" in selectedChoices,
            onCheckedChange = { checked ->
                onChoiceChange(
                    if (checked) {
                        selectedChoices + "C"
                    } else {
                        selectedChoices - "C"
                    }
                )
            },
            resultColor = resultColor
        )
        MultipleChoice(
            answer = stringResource(R.string.q2_D),
            checked = "D" in selectedChoices,
            onCheckedChange = { checked ->
                onChoiceChange(
                    if (checked) {
                        selectedChoices + "D"
                    } else {
                        selectedChoices - "D"
                    }
                )
            },
            resultColor = resultColor
        )
    }
}

@Composable
fun QuestionThree(
    modifier: Modifier = Modifier,
    answer: String,
    onAnswerChange: (String) -> Unit,
    resultColor: Color
) {
    Text(
        text = stringResource(R.string.q3),
        fontWeight = FontWeight.Bold
    )
    Text(
        text = stringResource(R.string.q3_text)
    )
    AnswerField(
        label = R.string.q3_label,
        value = answer,
        onValueChange = onAnswerChange,
        resultColor = resultColor,
        modifier = modifier.padding(top = 8.dp)
    )
}

@Composable
fun SingleChoice(
    modifier: Modifier = Modifier,
    answer: String,
    selected: Boolean,
    onClick: () -> Unit,
    resultColor: Color
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = resultColor
            )
        )
        Text(
            text = answer,
            color = if (selected) resultColor else Color.Unspecified
        )
    }
}

@Composable
fun MultipleChoice(
    modifier: Modifier = Modifier,
    answer: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    resultColor: Color
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = resultColor
            )
        )
        Text(
            text = answer,
            color = if (checked) resultColor else Color.Unspecified
        )
    }
}

@Composable
fun AnswerField(
    modifier: Modifier = Modifier,
    @StringRes label: Int,
    value: String,
    onValueChange: (String) -> Unit,
    resultColor: Color = Color.Unspecified
) {
    OutlinedTextField(
        label = { Text(stringResource(label)) },
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = resultColor,
            unfocusedBorderColor = resultColor
        ),
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun Divider(modifier: Modifier = Modifier) {
    HorizontalDivider(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        thickness = 1.dp,
        color = Color.Gray
    )
}

@Composable
fun QuizActionButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    border: BorderStroke? = null
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = onClick,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = colors,
            border = border
        ) {
            Text(
                text = text
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuizPreview() {
    QuizTheme {
        QuizApp()
    }
}
