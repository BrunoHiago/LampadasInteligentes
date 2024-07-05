package com.example.projetos7


import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getSystemService
import com.example.projetos7.ui.theme.Projetos7Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            createNotificationChannel(LocalContext.current)
            Projetos7Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Centro()
                }
            }
        }
    }
}


@Composable
fun GradientBackgroundBrush(
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

private fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            "channelId",
            "Nome do Canal",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.description = "Descrição do Canal"
        getSystemService(context, NotificationManager::class.java)?.createNotificationChannel(
            channel
        )
    }
}

@Composable
fun Content(
    modifier: Modifier = Modifier
) {
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
                brush = GradientBackgroundBrush(
                    isVerticalGradient = true,
                    colors = gradientColorList
                )
            ),
        contentAlignment = Alignment.Center
    ) {

    }
}


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun Centro() {
    var ledQuarto by remember { mutableStateOf(false) }
    var ledCozinha by remember { mutableStateOf(false) }
    var ledSala by remember { mutableStateOf(false) }
    var ledBanheiro by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val ok = Api()

    CoroutineScope(Dispatchers.IO).launch {
        ledQuarto = ok.estadoLed("led_quarto")
        ledCozinha = ok.estadoLed("led_cozinha")
        ledSala = ok.estadoLed("led_sala")
        ledBanheiro = ok.estadoLed("led_banheiro")
    }
    Content(modifier = Modifier.fillMaxSize())

    Box() {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {

                        CoroutineScope(Dispatchers.IO).launch {
                            withContext(Dispatchers.IO) {
                                ok.ligarLed("led_quarto", !ledQuarto)
                            }

                            ledQuarto = withContext(Dispatchers.IO) {
                                ok.estadoLed("led_quarto")
                            }
                        }

                    },
                    modifier = Modifier
                        .height(100.dp)
                        .width(120.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(if (ledQuarto) 0xFFE0BB2B else 0xFF000000),
                    ),
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(id = R.drawable.lampada),
                            contentDescription = "Lâmpada",
                            modifier = Modifier
                                .padding(bottom = 8.dp)
                                .height(50.dp),
                            colorFilter = ColorFilter.tint(if (ledQuarto) Color.Black else Color.White)

                        )
                        Text(
                            "Quarto",
                            color = if (ledQuarto) Color.Black else Color.White
                        )
                    }
                }

                Button(
                    onClick = {
                        CoroutineScope(Dispatchers.IO).launch {
                            withContext(Dispatchers.IO) {
                                ok.ligarLed("led_cozinha", !ledCozinha)
                            }

                            ledCozinha = withContext(Dispatchers.IO) {
                                ok.estadoLed("led_cozinha")
                            }
                        }
                    },
                    modifier = Modifier
                        .height(100.dp)
                        .width(120.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(if (ledCozinha) 0xFFE0BB2B else 0xFF000000),
                    ),
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(id = R.drawable.lampada),
                            contentDescription = "Lâmpada",
                            modifier = Modifier
                                .padding(bottom = 8.dp)
                                .height(50.dp),
                            colorFilter = ColorFilter.tint(if (ledCozinha) Color.Black else Color.White)
                        )
                        Text(
                            "Cozinha",
                            color = if (ledCozinha) Color.Black else Color.White
                        )
                    }
                }

            }
            Spacer(modifier = Modifier.height(70.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        CoroutineScope(Dispatchers.IO).launch {
                            withContext(Dispatchers.IO) {
                                ok.ligarLed("led_sala", !ledSala)
                            }

                            ledSala = withContext(Dispatchers.IO) {
                                ok.estadoLed("led_sala")
                            }
                        }
                    },
                    modifier = Modifier
                        .height(100.dp)
                        .width(120.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(if (ledSala) 0xFFE0BB2B else 0xFF000000),
                    ),
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(id = R.drawable.lampada),
                            contentDescription = "Lâmpada",
                            modifier = Modifier
                                .padding(bottom = 8.dp)
                                .height(50.dp),
                            colorFilter = ColorFilter.tint(if (ledSala) Color.Black else Color.White)
                        )
                        Text(
                            "Sala",
                            color = if (ledSala) Color.Black else Color.White
                        )
                    }
                }

                Button(
                    onClick = {
                        CoroutineScope(Dispatchers.IO).launch {
                            withContext(Dispatchers.IO) {
                                ok.ligarLed("led_banheiro", !ledBanheiro)
                            }

                            ledBanheiro = withContext(Dispatchers.IO) {
                                ok.estadoLed("led_banheiro")
                            }
                        }
                    },
                    modifier = Modifier
                        .height(100.dp)
                        .width(120.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(if (ledBanheiro) 0xFFE0BB2B else 0xFF000000),
                    ),
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(id = R.drawable.lampada),
                            contentDescription = "Lâmpada",
                            modifier = Modifier
                                .padding(bottom = 8.dp)
                                .height(50.dp),
                            colorFilter = ColorFilter.tint(if (ledBanheiro) Color.Black else Color.White)
                        )
                        Text(
                            "Banheiro",
                            color = if (ledBanheiro) Color.Black else Color.White
                        )
                    }
                }

            }
        }

        IconButton(onClick = {
            val intent = Intent(context, MainActivity3::class.java)
            context.startActivity(intent)
        },
            Modifier
                .align(Alignment.BottomStart)
                .padding(start = 16.dp, bottom = 16.dp)) {
            Icon(
                painter = painterResource(id = R.drawable.roda_de_cores),
                contentDescription = "",
                tint = Color.Unspecified
            )
        }
        Image(
            painter = painterResource(id = R.drawable.relogio),
            contentDescription = "Relógio",
            modifier = Modifier
                .padding(end = 16.dp, bottom = 16.dp)
                .height(50.dp)
                .width(50.dp)
                .align(Alignment.BottomEnd)
                .clickable {
                    val intent = Intent(context, MainActivity2::class.java)
                    context.startActivity(intent)
                }
        )

    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Projetos7Theme {
        Centro()
    }
}