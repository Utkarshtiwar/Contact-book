package com.utkarsh.contactapp.presentation.screen

import CustomTextField
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.utkarsh.contactapp.persentation.contactState
import com.utkarsh.contactapp.presentation.utils.ImageCompress

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditScreen(state: contactState, onEvent: () -> Unit, navHostController: NavHostController) {
    val context = LocalContext.current

    val pickMedia = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {
            context.contentResolver.openInputStream(it)?.use { inputStream ->
                val byte = inputStream.readBytes()
                val compressedImage = ImageCompress(byte)

                if (compressedImage.size > 1024 * 1024) {
                    Toast.makeText(context, "Image too large!", Toast.LENGTH_SHORT).show()
                } else {
                    state.image.value = compressedImage
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add / Edit Contact") },
                navigationIcon = {
                    IconButton(onClick = { navHostController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val image = state.image.value?.let {
                BitmapFactory.decodeByteArray(it, 0, it.size)
            }?.asImageBitmap()

            Box(
                modifier = Modifier.size(150.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                if (image != null) {
                    Image(
                        bitmap = image,
                        contentDescription = "Contact Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(140.dp)
                            .clip(CircleShape)
                            .background(Color.Gray)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Contact Image",
                        modifier = Modifier
                            .size(140.dp)
                            .clip(CircleShape)
                            .background(Color.Gray)
                            .padding(24.dp),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                IconButton(
                    onClick = {
                        pickMedia.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            CustomTextField(
                value = state.name.value,
                onValueChange = { state.name.value = it },
                label = "Name",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                leadingIcon = Icons.Default.Person,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            CustomTextField(
                value = state.phoneNumber.value,
                onValueChange = { state.phoneNumber.value = it },
                label = "Phone Number",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                leadingIcon = Icons.Default.Call,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            CustomTextField(
                value = state.email.value,
                onValueChange = { state.email.value = it },
                label = "Email",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                leadingIcon = Icons.Default.Mail,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    onEvent.invoke()
                    navHostController.navigateUp()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save", fontSize = 18.sp)
            }
        }
    }
}
