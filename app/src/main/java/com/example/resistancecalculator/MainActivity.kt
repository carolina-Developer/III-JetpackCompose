package com.example.resistancecalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.resistancecalculator.ui.theme.ResistanceCalculatorTheme
import kotlin.math.pow

// Map of color codes to their corresponding values
val colorMap = mapOf(
    "Black" to 0,
    "Brown" to 1,
    "Red" to 2,
    "Orange" to 3,
    "Yellow" to 4,
    "Green" to 5,
    "Blue" to 6,
    "Violet" to 7,
    "Gray" to 8,
    "White" to 9,
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ResistanceCalculatorTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    ResistorCalculatorScreen()
                }
            }
        }
    }
}

@Composable
fun ResistorCalculatorScreen() {
    var band1 by remember { mutableStateOf("Green") }
    var band2 by remember { mutableStateOf("Blue") }
    var multiplier by remember { mutableStateOf("White") }
    var showDialog by remember { mutableStateOf(false) }
    var resistanceValue by remember { mutableStateOf("") }

    // Function to calculate resistance and show the result in a popup
    fun calculateAndShowResult() {
        val resistance = calculateResistance(band1, band2, multiplier)
        resistanceValue = "The resistance value is $resistance."
        showDialog = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        BandDropdown("Band 1", band1) { band1 = it }
        Spacer(modifier = Modifier.height(8.dp))
        BandDropdown("Band 2", band2) { band2 = it }
        Spacer(modifier = Modifier.height(8.dp))
        BandDropdown("Multiplier", multiplier) { multiplier = it }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { calculateAndShowResult() }) {
            Text(text = "Calculate Resistance")
        }
    }

    // Show the result in a popup dialog
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                Button(onClick = { showDialog = false }) {
                    Text("OK")
                }
            },
            text = { Text(resistanceValue) }
        )
    }
}

@Composable
fun BandDropdown(label: String, selectedBand: String, onBandSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text(text = label)
        Box {
            Button(onClick = { expanded = true }) {
                Text(text = selectedBand)
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                colorMap.keys.forEach { color ->
                    DropdownMenuItem(
                        onClick = {
                            onBandSelected(color)
                            expanded = false
                        },
                        text = { Text(text = color) }
                    )
                }
            }
        }
    }
}

// Function to calculate the resistance value based on the selected bands
fun calculateResistance(band1: String, band2: String, multiplier: String): String {
    val digit1 = colorMap[band1] ?: 0
    val digit2 = colorMap[band2] ?: 0
    val factor = 10.0.pow(colorMap[multiplier] ?: 0)
    val resistance = ((digit1 * 10) + digit2) * factor

    return when (multiplier) {
        "Orange", "Yellow", "Green", "Red" -> "${(resistance / 1000).toBigDecimal().toPlainString()}k ohms"
        "Blue", "Violet", "Gray" -> "${(resistance / 1000000).toBigDecimal().toPlainString()}M ohms"
        "White" -> "${(resistance / 1000000000).toBigDecimal().toPlainString()}G ohms"
        else -> "${resistance.toBigDecimal().toPlainString()} ohms"
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewResistorCalculator() {
    ResistanceCalculatorTheme {
        ResistorCalculatorScreen()
    }
}