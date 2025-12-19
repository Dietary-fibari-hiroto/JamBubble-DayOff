package com.example.jambubble_client.ui.viewmodel.auths

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.time.LocalDate

class RegisterViewModel: ViewModel() {

    var name by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var gender by mutableStateOf("")
    var birthday by mutableStateOf<LocalDate?>(null)
    var imageUri by mutableStateOf<Uri?>(null)

}
