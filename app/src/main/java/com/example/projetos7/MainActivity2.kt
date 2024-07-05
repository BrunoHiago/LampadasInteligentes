package com.example.projetos7

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.example.projetos7.alarm.AlarmScheduler
import com.example.projetos7.ui.theme.Projetos7Theme
import java.util.Calendar

class MainActivity2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Projetos7Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CronometroScreen()
                }

            }
        }
    }
}

@Composable
fun gradientBackgroundBrush1(
    isVerticalGradient: Boolean,
    colors: List<Color>
): Brush {
    val endOffset = if (isVerticalGradient) {
        Offset(0f, Float.POSITIVE_INFINITY)
    } else {
        Offset(Float.POSITIVE_INFINITY, 0f)
    }
    return Brush.linearGradient(
        colors = colors,
        start = Offset.Zero,
        end = endOffset
    )
}

@Composable
fun Content1(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val gradientColorList = listOf(
        Color(0xFF000000),
        Color(0xFF071D33),
        Color(0xFF021529),
        Color(0xFF021C36),
        Color(0xFF000000),
    )
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = gradientBackgroundBrush1(
                    isVerticalGradient = true,
                    colors = gradientColorList
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.casa),
            contentDescription = "Voltar para pagina principal",
            modifier = Modifier
                .padding(end = 16.dp, bottom = 16.dp)
                .height(50.dp)
                .width(50.dp)
                .align(Alignment.BottomEnd)
                .clickable {
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                }
        )
    }
}

@SuppressLint("RememberReturnType", "DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CronometroScreen() {
    val mContext = LocalContext.current
    val scheduler = AlarmScheduler(mContext)

    // Declaring and initializing a calendar
    val mCalendar = Calendar.getInstance()
    val mHour = mCalendar[Calendar.HOUR_OF_DAY]
    val mMinute = mCalendar[Calendar.MINUTE]

    // Value for storing time as a string
    val timeBanheiro = remember { mutableStateOf("00:00:00")}
    val timeQuarto = remember { mutableStateOf("00:00:00") }
    val timeCozinha = remember { mutableStateOf("00:00:00") }
    val timeSala = remember { mutableStateOf("00:00:00") }
    val timeBanheiro2 = remember { mutableStateOf("00:00:00") }
    val timeQuarto2 = remember { mutableStateOf("00:00:00") }
    val timeCozinha2 = remember { mutableStateOf("00:00:00") }
    val timeSala2 = remember { mutableStateOf("00:00:00") }
    val escolha = remember { mutableIntStateOf(0) }

    val banheiroTimes = scheduler.getScheduledAlarmTimesFormatted(mContext, 4)
    val quartoTimes = scheduler.getScheduledAlarmTimesFormatted(mContext, 1)
    val cozinhaTimes = scheduler.getScheduledAlarmTimesFormatted(mContext, 2)
    val salaTimes = scheduler.getScheduledAlarmTimesFormatted(mContext, 3)

    val (banheiroStart, banheiroEnd) = banheiroTimes
    val (quartoStart, quartoEnd) = quartoTimes
    val (cozinhaStart, cozinhaEnd) = cozinhaTimes
    val (salaStart, salaEnd) = salaTimes

    banheiroStart?.let { timeBanheiro.value = it }
    banheiroEnd?.let { timeBanheiro2.value = it }
    quartoStart?.let { timeQuarto.value = it }
    quartoEnd?.let { timeQuarto2.value = it }
    cozinhaStart?.let { timeCozinha.value = it }
    cozinhaEnd?.let { timeCozinha2.value = it }
    salaStart?.let { timeSala.value = it }
    salaEnd?.let { timeSala2.value = it }


    // Creating a TimePicker dialod
    val mTimePickerDialog = TimePickerDialog(
        ContextThemeWrapper(mContext, R.style.Dialog),
        { _, mHour: Int, mMinute: Int ->

            when (escolha.intValue) {
                11 -> {
                    timeQuarto.value = String.format("%02d", mHour) + ":" + String.format(
                        "%02d", mMinute
                    ) + ":00"
                    scheduler.scheduleAlarm(mHour, mMinute, "quarto", true, escolha.intValue) {
                        Toast.makeText(mContext, "Ação executada!", Toast.LENGTH_SHORT).show()
                        println("Funciopnou")
                    }
                }

                12 -> {
                    timeQuarto2.value = String.format("%02d", mHour) + ":" + String.format(
                        "%02d", mMinute
                    ) + ":00"
                    scheduler.scheduleAlarm(mHour, mMinute, "quarto", false, escolha.intValue) {
                        Toast.makeText(mContext, "Ação executada!", Toast.LENGTH_SHORT).show()
                        println("Funciopnou")
                    }
                }

                21 -> {
                    timeCozinha.value = String.format("%02d", mHour) + ":" + String.format(
                        "%02d", mMinute
                    ) + ":00"
                    scheduler.scheduleAlarm(mHour, mMinute, "cozinha", true, escolha.intValue) {
                        Toast.makeText(mContext, "Ação executada!", Toast.LENGTH_SHORT).show()
                        println("Funciopnou")
                    }
                }

                22 -> {
                    timeCozinha2.value = String.format("%02d", mHour) + ":" + String.format(
                        "%02d", mMinute
                    ) + ":00"
                    scheduler.scheduleAlarm(mHour, mMinute, "cozinha", false, escolha.intValue) {
                        Toast.makeText(mContext, "Ação executada!", Toast.LENGTH_SHORT).show()
                        println("Funciopnou")
                    }
                }

                31 -> {
                    timeSala.value = String.format("%02d", mHour) + ":" + String.format(
                        "%02d", mMinute
                    ) + ":00"
                    scheduler.scheduleAlarm(mHour, mMinute, "sala", true, escolha.intValue) {
                        Toast.makeText(mContext, "Ação executada!", Toast.LENGTH_SHORT).show()
                        println("Funciopnou")
                    }
                }

                32 -> {
                    timeSala2.value = String.format("%2d", mHour) + ":" + String.format(
                        "%2d", mMinute
                    ) + ":00"
                    scheduler.scheduleAlarm(mHour, mMinute, "sala", false, escolha.intValue) {
                        Toast.makeText(mContext, "Ação executada!", Toast.LENGTH_SHORT).show()
                        println("Funciopnou")
                    }
                }

                41 -> {
                    timeBanheiro.value = String.format("%2d", mHour) + ":" + String.format(
                        "%2d", mMinute
                    ) + ":00"
                    scheduler.scheduleAlarm(mHour, mMinute, "banheiro", true, escolha.intValue) {
                        Toast.makeText(mContext, "Ação executada!", Toast.LENGTH_SHORT).show()
                        println("Funciopnou")
                    }
                }

                42 -> {
                    timeBanheiro2.value = String.format("%2d", mHour) + ":" + String.format(
                        "%2d", mMinute
                    ) + ":00"
                    scheduler.scheduleAlarm(mHour, mMinute, "banheiro", false, escolha.intValue) {
                        Toast.makeText(mContext, "Ação executada!", Toast.LENGTH_SHORT).show()
                        println("Funciopnou")
                    }
                }
            }

        }, mHour, mMinute, false
    )
    Content1(modifier = Modifier.fillMaxSize())
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TimerItem(
            nome = "Quarto", tempoInicio = timeQuarto.value, tempoFim = timeQuarto2.value,
            clickableInicio = {
                escolha.intValue = 11
                mTimePickerDialog.show()
            },
            clickableFim = {
                escolha.intValue = 12
                mTimePickerDialog.show()
            },
        )
        TimerItem(
            nome = "Cozinha",
            tempoInicio = timeCozinha.value,
            tempoFim = timeCozinha2.value,
            clickableInicio = {
                escolha.intValue = 21
                mTimePickerDialog.show()
            },
            clickableFim = {
                escolha.intValue = 22
                mTimePickerDialog.show()
            },
        )
        TimerItem(
            nome = "Sala",
            tempoInicio = timeSala.value,
            tempoFim = timeSala2.value,
            clickableInicio = {
                escolha.intValue = 31
                mTimePickerDialog.show()
            },
            clickableFim = {
                escolha.intValue = 32
                mTimePickerDialog.show()
            },
        )
        TimerItem(
            nome = "Banheiro",
            tempoInicio = timeBanheiro.value,
            tempoFim = timeBanheiro2.value,
            clickableInicio = {
                escolha.intValue = 41
                mTimePickerDialog.show()
            },
            clickableFim = {
                escolha.intValue = 42
                mTimePickerDialog.show()
            },
        )
    }


}


@Composable
fun TimerItem(
    nome: String,
    tempoInicio: String,
    tempoFim: String,
    clickableInicio: () -> Unit,
    clickableFim: () -> Unit
) {
    Surface(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        color = Color(0xFF9B9797)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = nome,
                color = Color(0xFFFBC02D),
                style = MaterialTheme.typography.bodyLarge
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = tempoInicio,
                    modifier = Modifier.clickable { clickableInicio() },
                    color = Color.Black,
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = ":",
                    color = Color.Black,
                    fontSize = TextUnit(20f, TextUnitType.Sp),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = tempoFim,
                    modifier = Modifier.clickable { clickableFim() },
                    color = Color.Black,
                    style = MaterialTheme.typography.bodyLarge
                )
            }


        }

    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    Projetos7Theme {
        CronometroScreen()
    }
}