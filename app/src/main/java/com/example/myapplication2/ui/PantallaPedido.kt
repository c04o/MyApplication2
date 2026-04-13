import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PantallaPedido() {
    // variables que guardan lo que el usuario escribe
    var nombre by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var producto by remember { mutableStateOf("") }
    var cantidad by remember { mutableStateOf("") }
    var notas by remember { mutableStateOf("") }

    // estados para la simulación
    var estaCargando by remember { mutableStateOf(false) }
    var mensajeEstado by remember { mutableStateOf("") }

    // alcance para la corrutina (simulación de envío)
    val alcanceCorrutina = rememberCoroutineScope()

    // validaciones
    val hayErrorNombre = nombre.isNotEmpty() && nombre.length < 3
    val hayErrorTelefono = telefono.isNotEmpty() && (!telefono.all { it.isDigit() } || telefono.length < 8)
    val hayErrorCantidad = cantidad.isNotEmpty() && (cantidad.toIntOrNull() ?: 0) <= 0

    val esFormularioValido = nombre.length >= 3 &&
            telefono.all { it.isDigit() } && telefono.length >= 8 &&
            direccion.isNotBlank() &&
            producto.isNotBlank() &&
            (cantidad.toIntOrNull() ?: 0) > 0

    fun limpiarFormulario() {
        nombre = ""
        telefono = ""
        direccion = ""
        producto = ""
        cantidad = ""
        notas = ""
        mensajeEstado = ""
    }

    // ui
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "FORMULARIO DE PEDIDO", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        // Campo Nombre
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre del cliente") },
            isError = hayErrorNombre,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        if (hayErrorNombre) Text("Mínimo 3 caracteres", color = Color.Red, fontSize = 12.sp)

        // campo teléfono
        OutlinedTextField(
            value = telefono,
            onValueChange = { telefono = it },
            label = { Text("Teléfono") },
            isError = hayErrorTelefono,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true
        )
        if (hayErrorTelefono) Text("Solo números, mínimo 8 dígitos", color = Color.Red, fontSize = 12.sp)

        // campo dirección
        OutlinedTextField(
            value = direccion,
            onValueChange = { direccion = it },
            label = { Text("Dirección") },
            modifier = Modifier.fillMaxWidth()
        )

        // campo producto
        OutlinedTextField(
            value = producto,
            onValueChange = { producto = it },
            label = { Text("Producto") },
            modifier = Modifier.fillMaxWidth()
        )

        // campo cantidad
        OutlinedTextField(
            value = cantidad,
            onValueChange = { cantidad = it },
            label = { Text("Cantidad") },
            isError = hayErrorCantidad,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true
        )
        if (hayErrorCantidad) Text("Debe ser mayor a 0", color = Color.Red, fontSize = 12.sp)

        // campo notas
        OutlinedTextField(
            value = notas,
            onValueChange = { notas = it },
            label = { Text("Notas adicionales (Opcional)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // botones
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { limpiarFormulario() },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
            ) {
                Text("Limpiar")
            }

            Button(
                // el botón solo se habilita si el formulario es válido y no está cargando
                enabled = esFormularioValido && !estaCargando,
                onClick = {
                    // simulación de envío usando corrutinas
                    alcanceCorrutina.launch {
                        estaCargando = true
                        mensajeEstado = "Enviando pedido..."

                        delay(2500) // simula una espera de red de 2.5 segundos

                        estaCargando = false
                        mensajeEstado = "¡Pedido enviado con éxito!"
                    }
                }
            ) {
                Text("ENVIAR")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // indicador de carga
        if (estaCargando) {
            CircularProgressIndicator()
        }

        // mensaje de estado final
        if (mensajeEstado.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = mensajeEstado,
                color = if (mensajeEstado.contains("éxito")) Color(0xFF4CAF50) else Color.Gray,
                fontWeight = FontWeight.Bold
            )
        }
    }
}