package com.example.gerenciamento_usuarios

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import kotlinx.coroutines.launch
import java.util.Calendar
import kotlin.text.ifEmpty

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val userRepository = remember { UserRepository(context) }
    val appointmentRepository = remember { AppointmentRepository(context) }
    val users by userRepository.usersFlow.collectAsState(initial = emptyList())
    val appointments by appointmentRepository.appointmentsFlow.collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()

    NavHost(navController = navController, startDestination = "login") {

        // ------------------ TELA DE LOGIN ------------------
        composable("login") {
            var username by rememberSaveable { mutableStateOf("") }
            var password by rememberSaveable  { mutableStateOf("") }

            Column(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Acesso ao Sistema", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Login / Usuário") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Senha") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val userFound = users.find { it.username == username && it.password == password }
                        if (userFound != null) {
                            navController.navigate("menu/${userFound.id}")
                        } else {
                            Toast.makeText(context, "Usuário ou senha inválidos!", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Entrar")
                }

                OutlinedButton(
                    onClick = { navController.navigate("register") },
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                ) {
                    Text("Cadastrar Novo Usuário")
                }
            }
        }

        // ------------------ TELA DE MENU ------------------
        composable(
            route = "menu/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->

            val userId = backStackEntry.arguments?.getString("userId") ?: ""


            Column(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Menu",
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )


                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = { navController.navigate("register") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cadastro de Usuario")
                }
                Spacer(modifier = Modifier.height(14.dp))


                Button(
                    onClick = { navController.navigate("user_list") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Lista de Usuarios")
                }
                Spacer(modifier = Modifier.height(14.dp))


                Button(
                    onClick = {
                        navController.navigate("new_appointment/${userId}")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Novo Agendamento")
                }
                Spacer(modifier = Modifier.height(14.dp))


                Button(
                    onClick = {
                        navController.navigate("appointment_list/${userId}")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Lista de Agendamentos")
                }
            }
        }

        // ------------------ TELA DE CADASTRO USUARIO ------------------
        composable("register") {
            var name by rememberSaveable { mutableStateOf("") }
            var username by rememberSaveable { mutableStateOf("") }
            var password by rememberSaveable { mutableStateOf("") }

            Column(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Cadastro de Usuário", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nome Completo") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Nome de Usuário (Login)") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Senha") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (name.isNotBlank() && username.isNotBlank() && password.isNotBlank()) {
                            scope.launch {
                                userRepository.addUser(User(name = name, username = username, password = password))
                                Toast.makeText(context, "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show()
                                navController.popBackStack()
                            }
                        } else {
                            Toast.makeText(context, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Salvar Cadastro")
                }
            }
        }



        // ------------------ TELA LISTA DE USUARIOS ------------------
        composable("user_list") {
            Scaffold(
                floatingActionButton = {
                    FloatingActionButton(onClick = { navController.navigate("register") }) {
                        Text("+")
                    }
                }
            ) { innerPadding ->
                Column(modifier = Modifier.padding(innerPadding).padding(16.dp)) {
                    Text(text = "Lista de Usuários Cadastrados", style = MaterialTheme.typography.headlineSmall)
                    Spacer(modifier = Modifier.height(12.dp))

                    if (users.isEmpty()) {
                        Text("Nenhum usuário cadastrado.")
                    } else {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(users) { user ->
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(text = user.name, style = MaterialTheme.typography.titleMedium)
                                            Text(text = "Login: ${user.username}", style = MaterialTheme.typography.bodyMedium)
                                        }

                                        Row {
                                            IconButton(onClick = { navController.navigate("edit_user/${user.id}") }) {
                                                Icon(Icons.Default.Edit, contentDescription = "Editar")
                                            }
                                            IconButton(onClick = {
                                                scope.launch { userRepository.deleteUser(user.id) }
                                            }) {
                                                Icon(Icons.Default.Delete, contentDescription = "Excluir")
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // ------------------ TELA DE EDIÇÃO DE USUARIOS ------------------
        composable(
            route = "edit_user/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            val user = users.find { it.id == userId }

            var newUsername by rememberSaveable(user) { mutableStateOf(user?.username ?: "") }
            var newPassword by rememberSaveable(user) { mutableStateOf(user?.password ?: "") }

            Column(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Alterar Login e Senha", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(16.dp))

                user?.let {
                    Text(text = "Usuário: ${it.name}", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = newUsername,
                        onValueChange = { newUsername = it },
                        label = { Text("Novo Login") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        label = { Text("Nova Senha") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            scope.launch {
                                userRepository.updateUser(it.copy(username = newUsername, password = newPassword))
                                Toast.makeText(context, "Dados atualizados com sucesso!", Toast.LENGTH_SHORT).show()
                                navController.popBackStack()
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Salvar Alterações")
                    }
                } ?: Text("Usuário não encontrado.")
            }
        }

        // ------------------ TELA DE NOVO AGENDAMENTO ------------------
        composable("new_appointment/{userId}") { backStackEntry ->

            var userId = backStackEntry.arguments?.getString("userId") ?: ""
            var exame by rememberSaveable { mutableStateOf("") }
            var selectedDate by rememberSaveable { mutableStateOf("") }
            var selectedTime by rememberSaveable { mutableStateOf("") }

            var showDialog by rememberSaveable { mutableStateOf(false) }

            val user = users.find { it.id == userId }
            val calendar = Calendar.getInstance()

            val datePickerDialog = android.app.DatePickerDialog(
                context, { _, year, month, day ->
                    selectedDate = String.format("%02d/%02d/%02d", day, month + 1, year)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )

            val timePickerDialog = android.app.TimePickerDialog(
                context,
                { _, hour, minute ->
                    selectedTime = String.format("%02d:%02d", hour, minute)
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            )

            Column(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Agendamento de Consulta",
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = exame,
                    onValueChange = { exame = it },
                    label = { Text("Nome do Exame") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = { datePickerDialog.show() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (selectedDate.isEmpty()) "Selecionar Data" else "Data: $selectedDate")
                }
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = { timePickerDialog.show() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (selectedTime.isEmpty()) "Selecionar Horario" else "Horario: $selectedTime")
                }
                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (exame.isNotBlank() && selectedDate.isNotBlank() && selectedTime.isNotBlank()) {
                            showDialog = true
                        } else {
                            Toast.makeText(context, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Prosseguir")
                }


                if (showDialog) {
                    BasicAlertDialog(
                        { showDialog = false }
                    ) {
                        Surface(
                            shape = MaterialTheme.shapes.large,
                            tonalElevation = 6.dp
                        ) {
                            Column (modifier = Modifier.padding(24.dp)){
                                Text (
                                    text = "Deseja Confirmar?",
                                    style = MaterialTheme.typography.headlineSmall,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                                )

                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text("Resumo do Agendamento", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.onSecondaryContainer, modifier = Modifier.padding(bottom = 12.dp))
                                        Text("Paciente: ${user?.name.orEmpty().ifEmpty { "Não preenchido" }}", fontSize = 16.sp, modifier = Modifier.padding(bottom = 4.dp))
                                        Text("Exame: ${exame.ifEmpty { "Não preenchido" }}", fontSize = 16.sp, modifier = Modifier.padding(bottom = 4.dp))
                                        Text("Data selecionada: ${selectedDate.ifEmpty { "Não selecionada" }}", fontSize = 16.sp, modifier = Modifier.padding(bottom = 4.dp))
                                        Text("Horário selecionado: ${selectedTime.ifEmpty { "Não selecionado" }}", fontSize = 16.sp)
                                    }
                                }
                                Spacer(modifier = Modifier.height(24.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Button(
                                        onClick = {
                                                scope.launch {
                                                    appointmentRepository.addAppointment(Appointment(
                                                        userId = userId,
                                                        exame = exame,
                                                        date = selectedDate,
                                                        time = selectedTime))
                                                    showDialog = false
                                                    Toast.makeText(context, "Agendamento concluido!", Toast.LENGTH_SHORT).show()
                                                    navController.popBackStack()
                                                }
                                        }
                                    ) {
                                        Text("Confirmar")
                                    }

                                    OutlinedButton(
                                        onClick = { showDialog = false}
                                    ) {
                                            Text("Voltar")
                                    }
                                }

                            }
                        }
                    }
                }




            }
        }

        // ------------------ TELA LISTA DE AGENDAMENTOS------------------
        composable("appointment_list/{userId}") {backStackEntry ->

            val userId = backStackEntry.arguments?.getString("userId") ?: ""


            Scaffold(
                floatingActionButton = {
                    FloatingActionButton(onClick = { navController.navigate("new_appointment/${userId}") }) {
                        Text("+")
                    }
                }
            ) { innerPadding ->
                Column(modifier = Modifier.padding(innerPadding).padding(16.dp)) {
                    Text(text = "Lista de Agendamentos", style = MaterialTheme.typography.headlineSmall)
                    Spacer(modifier = Modifier.height(12.dp))

                    if (appointments.isEmpty()) {
                        Text("Nenhum agendamento encontrado.")
                    } else {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(appointments) { appointment ->

                                val patient = users.find { it.id == appointment.userId }

                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text("Paciente: ${patient?.name.orEmpty().ifEmpty { "Não preenchido" }}", fontSize = 16.sp, modifier = Modifier.padding(bottom = 4.dp))
                                            Text("Exame: ${appointment.exame.ifEmpty { "Não preenchido" }}", fontSize = 16.sp, modifier = Modifier.padding(bottom = 4.dp))
                                            Text("Data selecionada: ${appointment.date.ifEmpty { "Não selecionada" }}", fontSize = 16.sp, modifier = Modifier.padding(bottom = 4.dp))
                                            Text("Horário selecionado: ${appointment.time.ifEmpty { "Não selecionado" }}", fontSize = 16.sp)
                                        }

                                        Row {
                                            IconButton(onClick = { navController.navigate("edit_appointment/${appointment.id}") }) {
                                                Icon(Icons.Default.Edit, contentDescription = "Editar")
                                            }
                                            IconButton(onClick = {
                                                scope.launch { appointmentRepository.deleteAppointment(appointment.id) }
                                            }) {
                                                Icon(Icons.Default.Delete, contentDescription = "Excluir")
                                            }
                                        }

                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // ------------------ TELA DE EDIÇÃO DE AGENDAMENTOS ------------------
        composable(
            route = "edit_appointment/{appointmentId}",
            arguments = listOf(navArgument("appointmentId") { type = NavType.StringType })
        ) { backStackEntry ->

            val appointmentId = backStackEntry.arguments?.getString("appointmentId")
            val appointment = appointments.find { it.id == appointmentId }
            val user = users.find { it.id == appointment?.userId }

            var newExame by rememberSaveable(user) { mutableStateOf(appointment?.exame ?: "") }
            var newDate by rememberSaveable(user) { mutableStateOf(appointment?.date ?: "") }
            var newTime by rememberSaveable(user) { mutableStateOf(appointment?.time ?: "") }

            val calendar = Calendar.getInstance()

            val datePickerDialog = android.app.DatePickerDialog(
                context, { _, year, month, day ->
                    newDate = String.format("%02d/%02d/%02d", day, month + 1, year)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )

            val timePickerDialog = android.app.TimePickerDialog(
                context,
                { _, hour, minute ->
                    newTime = String.format("%02d:%02d", hour, minute)
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            )

            Column(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Alterar Agendamento",
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                appointment?.let { currentAppointment ->

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp))
                    {
                        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                            Text(
                                text = "PACIENTE",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                            )
                            Text(
                                text = user?.name.orEmpty().ifEmpty { "Paciente não encontrado" },
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = newExame,
                        onValueChange =  { newExame = it},
                        label = { Text("Exame") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedButton(
                        onClick = { datePickerDialog.show() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (newDate.isEmpty()) "Selecionar Data" else "Data: $newDate")
                    }

                    OutlinedButton(
                        onClick = { timePickerDialog.show() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (newTime.isEmpty()) "Selecionar Horário" else "Horário: $newTime")
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                   Button(
                       onClick = {
                           if (newExame.isNotBlank() && newDate.isNotBlank() && newTime.isNotBlank()) {
                               scope.launch {
                                   appointmentRepository.updateAppointment(
                                       currentAppointment.copy(
                                           exame = newExame,
                                           date = newDate,
                                           time = newTime
                                       )
                                   )
                                   Toast.makeText(context, "Agendamento atualizado com sucesso", Toast.LENGTH_SHORT).show()
                                   navController.popBackStack()
                               }
                           } else {
                               Toast.makeText(context, "Preenchar todos os campos", Toast.LENGTH_SHORT).show()
                           }
                       },
                       modifier = Modifier.fillMaxWidth()
                   ) { Text("Salvar Alterações") }

                } ?: Text("Agendamento não encontrado.")
            }
        }

    }
}
