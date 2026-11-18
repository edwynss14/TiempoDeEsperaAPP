package com.edwyn.ciclodevida // <-- Mantén el nombre de paquete de tu proyecto

import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

class MainActivity : ComponentActivity() {

    companion object {
        private const val TAG = "CicloVidaApp"
    }

    // Marcas de tiempo en milisegundos
    private var tiempoInicioActiva: Long = 0L
    private var tiempoInicioPausa: Long = 0L
    private var ultimoTiempoActivaMs: Long = 0L

    // Estados observables para actualizar la interfaz automáticamente
    private var textoTiempoActiva by mutableStateOf("Pulsa Home y vuelve para verlo")
    private var textoTiempoPausada by mutableStateOf("Aún no ha estado en pausa")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) {
            tiempoInicioPausa = savedInstanceState.getLong("TIEMPO_INICIO_PAUSA", 0L)
            ultimoTiempoActivaMs = savedInstanceState.getLong("ULTIMO_TIEMPO_ACTIVA", 0L)
        }

        Log.d(TAG, "onCreate: Activity creada.")

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PantallaTiempos(
                        tiempoActiva = textoTiempoActiva,
                        tiempoPausada = textoTiempoPausada
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val ahora = SystemClock.elapsedRealtime()

        // 2. Muestra en la interfaz el tiempo que estuvo activa antes de la última pausa
        if (ultimoTiempoActivaMs > 0L) {
            val segActiva = ultimoTiempoActivaMs / 1000.0
            textoTiempoActiva = String.format(
                Locale.getDefault(),
                "%.2f segundos (%d ms)",
                segActiva,
                ultimoTiempoActivaMs
            )
        }

        // 3. Calcula cuánto tiempo estuvo pausada desde el último onPause()
        if (tiempoInicioPausa > 0L) {
            val tiempoPausadaMs = ahora - tiempoInicioPausa
            val segPausada = tiempoPausadaMs / 1000.0

            textoTiempoPausada = String.format(
                Locale.getDefault(),
                "%.2f segundos (%d ms)",
                segPausada,
                tiempoPausadaMs
            )

            Log.d(
                TAG,
                String.format(
                    Locale.getDefault(),
                    "onResume: La app estuvo pausada %.3f s (%d ms)",
                    segPausada,
                    tiempoPausadaMs
                )
            )
        }

        // Inicia el contador de actividad
        tiempoInicioActiva = ahora
        Log.d(TAG, "onResume: Activity activa y en primer plano.")
    }

    override fun onPause() {
        super.onPause()
        val ahora = SystemClock.elapsedRealtime()

        // 1. Calcula el tiempo que estuvo activa y lo imprime en Logcat
        ultimoTiempoActivaMs = ahora - tiempoInicioActiva
        val segActiva = ultimoTiempoActivaMs / 1000.0

        Log.d(
            TAG,
            String.format(
                Locale.getDefault(),
                "onPause: La app estuvo activa %.3f s (%d ms)",
                segActiva,
                ultimoTiempoActivaMs
            )
        )

        // Guarda el instante exacto en el que entra en pausa
        tiempoInicioPausa = ahora
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong("TIEMPO_INICIO_PAUSA", tiempoInicioPausa)
        outState.putLong("ULTIMO_TIEMPO_ACTIVA", ultimoTiempoActivaMs)
    }
}

@Composable
fun PantallaTiempos(tiempoActiva: String, tiempoPausada: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Monitor de Ciclo de Vida",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Último tiempo activa (antes de pausar):",
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = tiempoActiva, fontSize = 18.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Último tiempo en pausa:",
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = tiempoPausada, fontSize = 18.sp)
            }
        }
    }
}