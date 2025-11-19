package com.utkarsh.contactapp.presentation
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.utkarsh.contactapp.Data.DataBase.Contact
import com.utkarsh.contactapp.Data.DataBase.ContactDataBase
import com.utkarsh.contactapp.persentation.contactState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class ContactViewModel @Inject constructor(
    private val dataBase: ContactDataBase
) : ViewModel() {

    private var isSortedByName = MutableStateFlow(true)

    var contact = isSortedByName.flatMapLatest {
        if (it) {
            dataBase.getDao().getContactsSortedByName()
        } else {
            dataBase.getDao().getContactsSortedByDateOfCreation()
        }

    }.stateIn(  viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val _state = MutableStateFlow(contactState())
    val state = combine(_state,contact, isSortedByName) { state, contacts, isSortedByName ->
        state.copy(
            contacts = contacts
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), contactState())

    fun changeisSorting(){
        isSortedByName.value = !isSortedByName.value
    }
    fun saveContact(){
        val contact = Contact(
            id = _state.value.id.value,
            name = _state.value.name.value,
            phoneNumber = _state.value.phoneNumber.value,
            email = _state.value.email.value,
            isActive = true,
            dateOfCreation = System.currentTimeMillis().toLong(),
            image = _state.value.image.value

        )
        viewModelScope.launch{
            dataBase.getDao().upsertContact(contact)
        }
        state.value.id.value=0
        state.value.name.value=""
        state.value.phoneNumber.value=""
        state.value.email.value=""
        state.value.dateOfCreation.value=0
        state.value.image.value=null

    }
    fun deleteContact(){
        val contact = Contact(
            id = _state.value.id.value,
            name = _state.value.name.value,
            phoneNumber = _state.value.phoneNumber.value,
            email = _state.value.email.value,
            isActive = true,
            dateOfCreation = System.currentTimeMillis().toLong(),
            image = _state.value.image.value
        )
        viewModelScope.launch{
            dataBase.getDao().deleteContact(contact)
        }
        state.value.id.value=0
        state.value.name.value=""
        state.value.phoneNumber.value=""
        state.value.email.value=""
        state.value.dateOfCreation.value=0
        state.value.image.value=null
    }

}
