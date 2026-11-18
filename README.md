# Medición de Tiempo Activo y en Pausa de una Activity (Android)

Aplicación en **Kotlin** con **Jetpack Compose** que mide el tiempo que una `Activity` permanece activa en primer plano y el tiempo que pasa pausada en segundo plano, registrándolo en **Logcat** y mostrándolo en la interfaz de usuario.

## Funcionalidades implementadas

1. **Registro de tiempo activo en `onPause()`:** Al perder el foco, se calcula la diferencia entre `SystemClock.elapsedRealtime()` y el instante registrado en `onResume()`, imprimiendo el resultado en Logcat bajo la etiqueta `CicloVidaApp`.
2. **Visualización en interfaz al reactivarse (`onResume()`):** Cuando la `Activity` vuelve a primer plano, muestra en pantalla el tiempo exacto que estuvo activa antes de la última pausa.
3. **Cálculo del tiempo en pausa:** Guardando la marca temporal al entrar en `onPause()` y comparándola con el instante de regreso en `onResume()`, la aplicación calcula y muestra tanto en Logcat como en pantalla cuánto tiempo permaneció pausada.

## Cómo probarlo

1. Abrir el proyecto en **Android Studio** y ejecutarlo en un emulador o dispositivo Android.
2. Abrir **Logcat** con el filtro:
   ```text
   tag:CicloVidaApp
   ```
3. Pulsar **Home** para enviar la app a segundo plano y volver a abrirla desde **Recientes** para ver los tiempos registrados en consola y en las tarjetas de la pantalla.

## Evidencia de ejecución