package com.utkarsh.contactapp.persentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.utkarsh.contactapp.Data.DataBase.Contact

data class contactState (
        val contacts: List<Contact> = emptyList(),
        val id:MutableState<Int> = mutableStateOf(1),
        val name:MutableState<String> = mutableStateOf(""),
        val phoneNumber:MutableState<String> = mutableStateOf(""),
        val email:MutableState<String> = mutableStateOf(""),
        val dateOfCreation:MutableState<Long> = mutableStateOf(0),
        val image:MutableState<ByteArray?> = mutableStateOf(null)
)