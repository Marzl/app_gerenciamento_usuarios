# 🩺 Gerenciamento_usuarios - Sistema de Agendamento e Gestão de Usuários

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9+-7F52FF.svg?style=flat&logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![Android](https://img.shields.io/badge/Android-Jetpack%20Compose-3DDC84.svg?style=flat&logo=android&logoColor=white)](https://developer.android.com/jetpack/compose)
[![Material 3](https://img.shields.io/badge/Material--UI-Material%203-757575.svg?style=flat&logo=material-design&logoColor=white)](https://m3.material.io/)

O **Gerenciamento_usuarios** é um aplicativo Android desenvolvido em **Kotlin** com **Jetpack Compose** e **Material 3**. O projeto oferece uma solução moderna para autenticação de usuários, administração de cadastros e gerenciamento completo de agendamentos de exames e consultas médicas.

---

## 📱 Funcionalidades Principais

### 🔒 Autenticação e Menu
- **Tela de Login (`login`)**: Autenticação com verificação de credenciais de usuário e senha.
- **Menu Principal (`menu/{userId}`)**: Painel central que recebe o `userId` autenticado para direcionar o usuário às demais áreas do app.

### 👤 Gestão de Usuários
- **Cadastro de Usuário (`register`)**: Formulário para registrar novos usuários com nome completo, login e senha.
- **Lista de Usuários (`user_list`)**: Exibição em lista (`LazyColumn`) dos usuários cadastrados, com ações rápidas para edição e exclusão.
- **Edição de Usuário (`edit_user/{userId}`)**: Atualização das credenciais de acesso de um usuário existente.

### 📅 Gestão de Agendamentos
- **Novo Agendamento (`new_appointment/{userId}`)**: Registro de consultas e exames com seletores nativos de data (`DatePickerDialog`) e horário (`TimePickerDialog`).
- **Confirmação em Pop-up (`BasicAlertDialog`)**: Validação dos dados em um resumo expansível antes do salvamento definitivo.
- **Lista de Agendamentos (`appointment_list/{userId}`)**: Visualização de todas as consultas agendadas com detalhes do paciente, exame, data e hora.
- **Alterar Agendamento (`edit_appointment/{appointmentId}`)**: Edição dos dados do agendamento com destaque visual para a identificação do paciente.

---

## 🛠️ Tecnologias Utilizadas

- **Linguagem**: [Kotlin](https://kotlinlang.org/)
- **UI Toolkit**: [Jetpack Compose](https://developer.android.com/jetpack/compose)
- **Design System**: [Material 3](https://m3.material.io/)
- **Navegação**: `androidx.navigation:navigation-compose`
- **Concorrência**: Kotlin Coroutines (`rememberCoroutineScope`)
- **Gerenciamento de Estado**: `rememberSaveable`, `mutableStateOf`, `collectAsState`
- **Persistência / Arquitetura**: Pattern Repository (`UserRepository`, `AppointmentRepository`) com manipulação reativa de `Flow`

---

## 🏗️ Estrutura de Rotas e Navegação

```text
login
 ├── register (Cadastro de Novo Usuário)
 └── menu/{userId} (Menu Principal)
      ├── register
      ├── user_list (Lista de Usuários)
      │    └── edit_user/{userId} (Edição de Usuário)
      ├── new_appointment/{userId} (Novo Agendamento)
      └── appointment_list/{userId} (Lista de Agendamentos)
           └── edit_appointment/{appointmentId} (Alteração de Agendamento)
```

---

## 🚀 Como Executar o Projeto

### Pré-requisitos
- **Android Studio** (Hedgehog 2023.1.1 ou superior recomendado)
- **JDK 17** ou superior
- Dispositivo físico Android (com depuração USB ativa) ou Emulador Android (API 26+)

### Passos
1. **Clonar o Repositório**:
   ```bash
   git clone https://github.com/Marzl/app_gerenciamento_usuarios.git
   ```
2. **Abrir no Android Studio**:
   - Abra o Android Studio, selecione **Open** e navegue até a pasta do projeto clonado.
3. **Sincronizar o Gradle**:
   - Aguarde o término do *Gradle Sync*.
4. **Executar o Aplicativo**:
   - Selecione o seu dispositivo/emulador e clique no botão **Run** (`Shift + F10`).

---

## 📄 Licença

Este projeto é disponibilizado sob a licença [MIT](LICENSE).
