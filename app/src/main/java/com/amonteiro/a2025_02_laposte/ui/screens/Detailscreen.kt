package com.amonteiro.a2025_02_laposte.ui.screens

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.amonteiro.a2025_02_laposte.R
import com.amonteiro.a2025_02_laposte.ui.theme._2025_02_laposteTheme
import com.amonteiro.a2025_02_laposte.viewmodel.LONG_TEXT
import com.amonteiro.a2025_02_laposte.viewmodel.PictureBean
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder

@Preview(showBackground = true, showSystemUi = true)
@Preview(showBackground = true, showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun DetailScreenPreview() {
    _2025_02_laposteTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            DetailScreen(modifier = Modifier.padding(innerPadding),
                pictureBean = PictureBean(1, "https://picsum.photos/200", "ABCD", LONG_TEXT)
            )
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DetailScreen(modifier: Modifier = Modifier,
                 pictureBean: PictureBean,
                 onBtBackClick : ()->Unit = {}
) {

    Column(
        modifier = modifier.padding(8.dp)
    )  {
        Text(
            text = pictureBean.title,
            fontSize = 36.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.fillMaxWidth()
        )

        GlideImage(
            model = pictureBean.url,
            contentDescription = "une photo de chat",
            loading = placeholder(R.mipmap.ic_launcher_round),
            failure = placeholder(R.mipmap.ic_launcher),
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxWidth().weight(1f)
        )

        Text(
            text = pictureBean.longText,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.size(16.dp))

        Button(
            onClick = onBtBackClick,
            contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
            modifier = Modifier
                .padding(8.dp)
                .align(CenterHorizontally)
        ) {
            Icon(
                Icons.Filled.ArrowBack,
                contentDescription = "Localized description",
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text("Retour")
        }
    }
}