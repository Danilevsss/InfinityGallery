package com.daniladorokhov.gallery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.rememberAsyncImagePainter
import com.daniladorokhov.gallery.ui.theme.GalleryTheme
import com.daniladorokhov.gallery.ui.theme.Purple500
import com.google.accompanist.glide.rememberGlidePainter
import com.google.accompanist.imageloading.rememberDrawablePainter


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var navController = rememberNavController()
            GalleryTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    var viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application).create(MainViewModel::class.java)
                    NavHost(navController = navController, startDestination = "main") {
                        composable("main") { ListScreen(viewModel, navController) }
                        composable("details/{itemId}", arguments = listOf(navArgument("itemId") { type = NavType.IntType })) { 
                            DetailsScreen(navController = navController, viewModel = viewModel, itemId = it.arguments!!.getInt("itemId"))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ListScreen(viewModel: MainViewModel, navController: NavController){
    val lazyListState: LazyListState = rememberLazyListState()
    if(viewModel.galleryList.isEmpty()) {
        viewModel.updateData()
    }
    Scaffold(
        topBar = {
            TopAppBar(title = {Text(stringResource(id = R.string.app_name), color = White)}, backgroundColor = Purple500)
        },
        backgroundColor = Transparent
    ) {
        LazyColumn(state = lazyListState) {
            if (lazyListState.isScrolledToTheEndOfList(viewModel.galleryList)) {
                if (!viewModel.loadInProgress.value) {
                    viewModel.updateData()
                }
            }
            items(viewModel.galleryList) {
                Column(modifier = Modifier.clickable {
                    var itemId = viewModel.galleryList.indexOf(it)
                    navController.navigate("details/$itemId")
                }) {
                    Image(
                        painter = rememberGlidePainter(it.urls.regular),
                        contentScale = ContentScale.Crop,
                        contentDescription = "",
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .padding(8.dp)
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text(text = it.user.name, modifier = Modifier.weight(1f))
                        Image(painter = painterResource(id = if (it.liked.value) R.drawable.like else R.drawable.like_outline),
                            contentDescription = "",
                            modifier = Modifier
                                .clickable {
                                    viewModel.pressLike(viewModel.galleryList.indexOf(it))
                                }
                        )
                    }
                    Divider()
                }
            }
            if (viewModel.loadInProgress.value) {
                item {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
fun DetailsScreen(navController: NavController, viewModel: MainViewModel, itemId: Int){
    var item = viewModel.galleryList[itemId]
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(item.user.name, color = White) }, backgroundColor = Purple500, contentColor = White, navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back", tint = White)
                }
            })
        },
        backgroundColor = Transparent
    ) {
        Column {
            Image(
                painter = rememberGlidePainter(item.urls.regular),
                contentDescription = "",
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(8.dp)) {
                Image(painter = painterResource(id = if (item.liked.value) R.drawable.like else R.drawable.like_outline),
                    contentDescription = "",
                    modifier = Modifier
                        .clickable {
                            viewModel.pressLike(viewModel.galleryList.indexOf(item))
                        }
                )
            }
        }
    }
}

fun LazyListState.isScrolledToTheEndOfList(list: List<Any>) = layoutInfo.visibleItemsInfo.lastOrNull()?.index == list.size - 1

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    GalleryTheme {
        Greeting("Android")
    }
}