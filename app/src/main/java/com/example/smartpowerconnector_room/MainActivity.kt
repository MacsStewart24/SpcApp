package com.example.smartpowerconnector_room

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.smartpowerconnector_room.internet.idata.AwsRepository
import com.example.smartpowerconnector_room.ui.theme.SmartPowerConnector_RoomTheme
import javax.inject.Inject

class MainActivity : ComponentActivity() {
    @Inject
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContent {
            SmartPowerConnector_RoomTheme {
                SPCApp()
            }
        }
    }
}
