package com.utkarsh.contactapp.persentation.Screen

import android.R.attr.title
import android.content.Intent
import androidx.compose.foundation.lazy.items
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.room.util.TableInfo
import com.utkarsh.contactapp.Data.DataBase.Contact
import com.utkarsh.contactapp.persentation.contactState
import com.utkarsh.contactapp.persentation.navigation.Routes
import com.utkarsh.contactapp.presentation.ContactViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navHostController: NavHostController,
    state: contactState,
    viewModel: ContactViewModel,

    ) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Contacts") },
                actions = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Sort,
                        contentDescription = "Sort",
                        modifier = Modifier.clickable {
                            viewModel.changeisSorting()
                        }
                    )
                }
            )
        }, floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navHostController.navigate(Routes.AddEdit.route)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add"
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            LazyColumn {
                items(state.contacts) { contact ->
                    val bitmap = contact.image?.let {
                        BitmapFactory.decodeByteArray(it, 0, it.size)
                    }?.asImageBitmap()
                    contactCard(
                        viewModel = viewModel,
                        state = state,
                        navHostController = navHostController,
                        name = contact.name,
                        phoneNumber = contact.phoneNumber,
                        email = contact.email,
                        imagebyteArray = contact.image,
                        image = bitmap,
                        id = contact.id,
                        dateOfCreation = contact.dateOfCreation
                    )
                }
            }

        }

    }

}


@Composable
fun contactCard(
    name: String,
    phoneNumber: String,
    email: String,
    imagebyteArray: ByteArray?,
    image: ImageBitmap?,
    dateOfCreation: Long,
    id: Int,
    viewModel: ContactViewModel,
    state: contactState,
    navHostController: NavHostController,

    ) {
    val context = LocalContext.current
    Card(
        onClick = {
            state.id.value = id
            state.name.value = name
            state.phoneNumber.value = phoneNumber
            state.email.value = email
            state.image.value = imagebyteArray
            state.dateOfCreation.value = dateOfCreation
            navHostController.navigate(Routes.AddEdit.route)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(12.dp))
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {

            if (image != null) {
                Image(
                    bitmap = image,
                    contentDescription = "Contact Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Contact Image",
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.onPrimaryContainer)
                        .padding(16.dp),
                    tint = MaterialTheme.colorScheme.onPrimary

                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = phoneNumber,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = email,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )

            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                IconButton(
                    onClick = {
                        state.id.value = id
                        state.name.value = name
                        state.phoneNumber.value = phoneNumber
                        state.email.value = email
                        state.dateOfCreation.value = dateOfCreation
                        viewModel.deleteContact()

                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Contact",
                        tint = MaterialTheme.colorScheme.error
                    )

                }
                IconButton(
                    onClick = {
                        val intent = Intent(Intent.ACTION_CALL)
                        intent.data = Uri.parse("tel:$phoneNumber")
                        context.startActivity(intent)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = "call",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

            }
        }
    }

}


