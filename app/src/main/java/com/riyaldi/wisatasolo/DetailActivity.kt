package com.riyaldi.wisatasolo

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.riyaldi.wisatasolo.data.PlaceList
import com.riyaldi.wisatasolo.model.Place
import com.riyaldi.wisatasolo.ui.theme.WisataSoloTheme

class DetailActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val placeId = intent.getIntExtra("PLACE_ID", 0)
        val place = PlaceList.placeList.find { it.id == placeId }

        if (place == null) {
            finish()
            return
        }

        setContent {
            WisataSoloTheme {
                val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(
                    rememberTopAppBarState()
                )

                val scrollState = rememberScrollState()

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .nestedScroll(scrollBehavior.nestedScrollConnection),
                    topBar = {
                        CenterAlignedTopAppBar(
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.primary
                            ),
                            title = {
                                Text(
                                    text = place.name,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
                            navigationIcon = {
                                IconButton(onClick = {
                                    finish()
                                }) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Up Button"
                                    )
                                }
                            },
                            scrollBehavior = scrollBehavior
                        )
                    }
                ) { innerPadding ->
                    DetailScreen(
                        place = place,
                        scrollState = scrollState,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun DetailScreen(
    place: Place,
    scrollState: ScrollState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.verticalScroll(state = scrollState)
    ) {
        HeaderImage(place)

        DetailTitle(place)

        PlaceDescription(place.description)

        CarouselGallery(place)

        OpenMapButton(place)
    }

}

@Composable
fun HeaderImage(
    place: Place,
    modifier: Modifier = Modifier
) {
    SubcomposeAsyncImage(
        modifier = modifier
            .fillMaxWidth()
            .height(350.dp),
        model = ImageRequest.Builder(LocalContext.current)
            .data(place.imageUrls[0])
            .crossfade(true)
            .build(),
        contentScale = ContentScale.Crop,
        loading = {
            CircularProgressIndicator()
        },
        contentDescription = "Image of ${place.name}",
    )
}

@Composable
fun DetailTitle(
    place: Place,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp, 8.dp)
            .padding(top = 8.dp)
    ) {
        Text(
            text = place.name,
            modifier = modifier,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight(600)
        )
        Text(
            text = place.location,
            modifier = modifier,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
fun PlaceDescription(
    description: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 8.dp)
    ) {
        Text(
            text = "Deskripsi",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight(600)
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarouselGallery(
    place: Place,
    modifier: Modifier = Modifier
) {
    val carouselMultiBrowseState = rememberCarouselState {
        place.imageUrls.size
    }

    HorizontalMultiBrowseCarousel(
        modifier = modifier.padding(0.dp, 16.dp),
        state = carouselMultiBrowseState,
        preferredItemWidth = 300.dp,
        itemSpacing = 16.dp,
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp)
    ) { index ->
        CarouselImage(
            place.imageUrls[index],
            modifier = Modifier.maskClip(MaterialTheme.shapes.medium)
        )
    }
}

@Composable
fun CarouselImage(
    url: String,
    modifier: Modifier = Modifier
) {
    SubcomposeAsyncImage(
        modifier = modifier
            .height(200.dp),
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .crossfade(true)
            .build(),
        contentScale = ContentScale.FillBounds,
        loading = {
            Box(
                modifier = modifier
                    .fillMaxSize(),
            ) {
                CircularProgressIndicator(
                    modifier = modifier
                        .align(Alignment.Center)
                        .size(48.dp)
                )
            }
        },
        contentDescription = "Image detail",
    )
}

@Composable
fun OpenMapButton(
    place: Place,
    modifier: Modifier = Modifier
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { }

    Button(
        modifier = modifier
            .padding(16.dp, 8.dp)
            .fillMaxWidth(),
        onClick = {
            val intent = Intent(Intent.ACTION_VIEW, place.mapUrl.toUri())
            launcher.launch(intent)
        }
    ) {
        Text("Buka di Map")
    }
}

@Preview(showBackground = true)
@Composable
fun DetailPreview() {
    WisataSoloTheme {
        DetailScreen(place = PlaceList.placeList[0], scrollState = rememberScrollState())
    }
}