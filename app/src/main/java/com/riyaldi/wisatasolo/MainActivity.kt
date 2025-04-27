package com.riyaldi.wisatasolo

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.util.CoilUtils.result
import com.riyaldi.wisatasolo.ui.theme.WisataSoloTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WisataSoloTheme {
                val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(
                    rememberTopAppBarState()
                )

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
                                    text = "Wisata Solo",
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
                            scrollBehavior = scrollBehavior
                        )
                    }
                ) { innerPadding ->
                    val places = PlaceList.placeList

                    ListCard(places, modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun ListCard(places: List<Place>, modifier: Modifier = Modifier) {
    LazyColumn (
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        items(places.size) { index ->
            CardPlace(places[index])
        }
    }
}

@Composable
fun CardPlace(place: Place, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
    ) {
        Box {
            SubcomposeAsyncImage (
                modifier = modifier
                    .fillMaxWidth()
                    .height(250.dp),
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

            Card(
                modifier = modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(4.dp),
            ) {
                Row(
                    modifier = modifier
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = modifier
                    ) {
                        Text(
                            modifier = modifier,
                            text = place.name,
                        )
                        Text(
                            modifier = modifier,
                            text = place.location,
                        )
                    }
                    Spacer(modifier = modifier.weight(1f))
                    DetailButton(place.id)
                }
            }

        }
    }
}

@Composable
fun DetailButton(
    placeId: Int,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {  }

    Button(
        modifier = modifier,
        onClick = {
            val intent = Intent(context, DetailActivity::class.java).apply {
                putExtra("PLACE_ID", placeId)
            }
            launcher.launch(intent)
        }
    ) {
        Text("Detail")
    }
}

@Preview(showBackground = true)
@Composable
fun CardPreview() {
    WisataSoloTheme {
        CardPlace(place = PlaceList.placeList[0])
    }
}