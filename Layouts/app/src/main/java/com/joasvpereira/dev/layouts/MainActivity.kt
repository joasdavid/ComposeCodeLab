package com.joasvpereira.dev.layouts

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.joasvpereira.dev.layouts.ui.theme.LayoutsTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LayoutsTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    LayoutsCodelab()
                }
            }
        }
    }
}

@Composable
fun LayoutsCodelab() {
    Scaffold(
        topBar = { ToolbarContent() }
    ) { innerPadding ->
        BodyContent(
            modifier = Modifier
                .padding(innerPadding)
                .padding(start = 16.dp, end = 16.dp)
        )
    }
}

@Composable
fun ToolbarContent() {
    TopAppBar(
        title = { Text(text = "LayoutsCodeLab") },
        actions = { ToolbarActions() }
    )
}

@Composable
fun ToolbarActions() {
    IconButton(onClick = { /*TODO*/ }) {
        Icon(imageVector = Icons.Filled.Favorite, contentDescription = null)
    }
}

@Composable
private fun BodyContent(modifier: Modifier) {
    val listScrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val listSize = 100

    Column(Modifier.fillMaxWidth()) {

        Row(Modifier.fillMaxWidth()) {
            Button(
                onClick = scrollToTopClickEvent(coroutineScope, listScrollState),
                modifier = Modifier.weight(0.5f)
            ) {
                Text(text = "Scroll to the top")
            }

            Button(
                onClick = scrollToBottomClickEvent(coroutineScope, listScrollState, listSize),
                modifier = Modifier.weight(0.5f)
            ) {
                Text(text = "Scroll to the bottom")
            }
        }

        LazyColumn(modifier = Modifier.fillMaxWidth() then modifier, state = listScrollState) {
            item {
                Text(text = "Hi there!")
                Text(text = "Thanks for going through the Layouts codelab")
                Spacer(modifier = Modifier.padding(top = 18.dp))
            }
            SimpleList(scope = this, listSize = listSize)
        }
    }
}

@Composable
private fun scrollToTopClickEvent(
    coroutineScope: CoroutineScope,
    listScrollState: LazyListState
): () -> Unit = {
    coroutineScope.launch {
        listScrollState.animateScrollToItem(index = 0)
    }
}

@Composable
private fun scrollToBottomClickEvent(
    coroutineScope: CoroutineScope,
    listScrollState: LazyListState,
    listSize: Int
): () -> Unit = {
    coroutineScope.launch {
        listScrollState.animateScrollToItem(index = listSize - 1)
    }
}

fun SimpleList(listSize: Int, scope: LazyListScope) {
    scope.items(listSize) { pos: Int ->
        PhotographerCard(name = "Item #$pos")
    }
}

@Composable
fun PhotographerCard(modifier: Modifier = Modifier, name: String = "Alfred Siskey") {
    val context = LocalContext.current
    Row(modifier = modifier
        .clip(RoundedCornerShape(4.dp))
        .background(MaterialTheme.colors.surface)
        .clickable {
            Toast
                .makeText(context, "this is row click", Toast.LENGTH_SHORT)
                .show()
        }
        .padding(16.dp)
    ) {
        PhotographerAvatar()
        PhotographerCardDisplayInfo(
            modifier = Modifier
                .padding(start = 8.dp)
                .align(CenterVertically),
            name = name
        )
    }
}

@Composable
fun PhotographerAvatar() {
    Surface(
        modifier = Modifier.size(50.dp),
        shape = CircleShape,
        color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f)
    ) {
        Image(
            painter = rememberImagePainter(data = "https://developer.android.com/images/brand/Android_Robot.png"),
            contentDescription = "Android Logo",
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun PhotographerCardDisplayInfo(modifier: Modifier, name: String) {
    Column(
        modifier = modifier
    ) {
        Text(text = name, fontWeight = FontWeight.Bold)
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text("3 minutes ago")
        }
    }
}

//@Preview(showBackground = true)
@Composable
fun PhotographerCardPreview() {
    val context = LocalContext.current
    LayoutsTheme {
        PhotographerCard(
            modifier = Modifier.clickable {
                Toast.makeText(context, "override", Toast.LENGTH_SHORT).show()
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val context = LocalContext.current
    LayoutsTheme {
        LayoutsCodelab()
    }
}