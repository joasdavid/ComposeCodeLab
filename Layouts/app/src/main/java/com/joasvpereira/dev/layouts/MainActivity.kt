package com.joasvpereira.dev.layouts

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Divider
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
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
        val chipsScroll = rememberScrollState()
        Row(modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(chipsScroll)
            .padding(start = 8.dp, end = 16.dp)) {
            StaggeredGrid(rows = 2) {
                for (topic in topics) {
                    Chip(modifier = Modifier.padding(8.dp), text = topic)
                }
            }
        }

        Row(Modifier.fillMaxWidth()) {
            Button(
                onClick = scrollToTopClickEvent(coroutineScope, listScrollState),
                modifier = Modifier
                    .weight(0.5f)
                    .padding(start = 16.dp, end = 8.dp)
            ) {
                Text(text = "Scroll to the top")
            }

            Button(
                onClick = scrollToBottomClickEvent(coroutineScope, listScrollState, listSize),
                modifier = Modifier
                    .weight(0.5f)
                    .padding(start = 8.dp, end = 16.dp)
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

fun Modifier.firstBaselineToTop(
    firstBaselineToTop: Dp
) = this.then(
    layout { measurable, constraints ->
        val placeable = measurable.measure(constraints = constraints)

        // Check the composable has a first baseline
        check(placeable[FirstBaseline] != AlignmentLine.Unspecified)
        val firstBaseline = placeable[FirstBaseline]

        // Height of the composable with padding - first baseline
        val placeableY = firstBaselineToTop.roundToPx() - firstBaseline
        val height = placeable.height + placeableY
        layout(placeable.width, height) {
            // Where the composable gets placed
            placeable.placeRelative(0, placeableY)
        }
    }
)

@Composable
fun MyOwnColumn(
    modifier: Modifier = Modifier,
    // custom layout attributes
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        // Don't constrain child views further, measure them with given constraints
        // List of measured children
        val placeables = measurables.map { measurable ->
            // Measure each child
            measurable.measure(constraints)
        }

        // Track the y co-ord we have placed children up to
        var yPosition = 0
        // Set the size of the layout as big as it can
        layout(constraints.maxWidth, constraints.maxHeight) {
            // Place children in the parent layout
            placeables.forEach { placeable ->
                // Position item on the screen
                placeable.placeRelative(x = 0, y = yPosition)

                // Record the y co-ord placed up to
                yPosition += placeable.height
            }
        }
    }
}

@Composable
fun StaggeredGrid(
    modifier: Modifier = Modifier,
    rows: Int = 3,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        // Keep track of the width of each row
        val rowWidths = IntArray(rows) { 0 }

        // Keep track of the max height of each row
        val rowHeights = IntArray(rows) { 0 }

        // Don't constrain child views further, measure them with given constraints
        // List of measured children
        val placeables = measurables.mapIndexed { index, measurable ->

            // Measure each child
            val placeable = measurable.measure(constraints)

            // Track the width and max height of each row
            val row = index % rows
            rowWidths[row] += placeable.width
            rowHeights[row] = Math.max(rowHeights[row], placeable.height)

            placeable
        }

        // Grid's width is the widest row
        val width = rowWidths.maxOrNull()
            ?.coerceIn(constraints.minWidth.rangeTo(constraints.maxWidth)) ?: constraints.minWidth

        // Grid's height is the sum of the tallest element of each row
        // coerced to the height constraints
        val height = rowHeights.sumOf { it }
            .coerceIn(constraints.minHeight.rangeTo(constraints.maxHeight))

        // Y of each row, based on the height accumulation of previous rows
        val rowY = IntArray(rows) { 0 }
        for (i in 1 until rows) {
            rowY[i] = rowY[i-1] + rowHeights[i-1]
        }

        // Set the size of the parent layout
        layout(width, height) {
            // x cord we have placed up to, per row
            val rowX = IntArray(rows) { 0 }

            placeables.forEachIndexed { index, placeable ->
                val row = index % rows
                placeable.placeRelative(
                    x = rowX[row],
                    y = rowY[row]
                )
                rowX[row] += placeable.width
            }
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

//@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    LayoutsTheme {
        LayoutsCodelab()
    }
}

//@Preview
@Composable
fun TextWithPaddingToBaselinePreview() {
    LayoutsTheme {
        Text("Hi there!", Modifier.firstBaselineToTop(32.dp))
    }
}

//@Preview
@Composable
fun TextWithNormalPaddingPreview() {
    LayoutsTheme {
        Text("Hi there!", Modifier.padding(top = 32.dp))
    }
}

@Composable
fun Chip(modifier: Modifier = Modifier, text: String) {
    Card(
        modifier = modifier,
        border = BorderStroke(color = Color.Black, width = Dp.Hairline),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(16.dp, 16.dp)
                    .background(color = MaterialTheme.colors.secondary)
            )
            Spacer(Modifier.width(4.dp))
            Text(text = text)
        }
    }
}

val topics = listOf(
    "Arts & Crafts", "Beauty", "Books", "Business", "Comics", "Culinary",
    "Design", "Fashion", "Film", "History", "Maths", "Music", "People", "Philosophy",
    "Religion", "Social sciences", "Technology", "TV", "Writing"
)

//@Preview
@Composable
fun ChipPreview() {
    LayoutsTheme {
        StaggeredGrid(rows = 4) {
            for (topic in topics) {
                Chip(modifier = Modifier.padding(8.dp), text = topic)
            }
        }
    }
}

// Constraint Layout

@ExperimentalComposeUiApi
@Composable
fun ConstraintLayoutContent() {
    ConstraintLayout {
        // Create references for the composables to constrain
        val (button, button2, text) = createRefs()
        val barrier = createEndBarrier(button, text)

        Button(onClick = { /* void */ },
        modifier = Modifier.constrainAs(button) {
            top.linkTo(parent.top, margin = 16.dp)
        }
        ) {
            Text(text = "Button 1")
        }

        Button(onClick = { /* void */ },
            modifier = Modifier.constrainAs(button2) {
                top.linkTo(parent.top, margin = 16.dp)
                start.linkTo(barrier)
            }
        ) {
            Text(text = "Button 2")
        }

        // Assign reference "text" to the Text composable
        // and constrain it to the bottom of the Button composable
        Text("Text", Modifier.constrainAs(text) {
            top.linkTo(button.bottom, margin = 16.dp)
            centerAround(button.end)
        })
    }
}

@Preview
@ExperimentalComposeUiApi
@Composable
fun ConstraintLayoutContentPreview() {
    LayoutsTheme {
        ConstraintLayoutContent()
    }
}

@Composable
fun LargeConstraintLayout() {
    ConstraintLayout {
        val text = createRef()
        val guideline = createGuidelineFromStart(fraction = 0.5f)
        Text(
            text = "This is a very very very very very very very long text",
            modifier = Modifier.constrainAs(text) {
                linkTo(start = guideline, end = parent.end)
                width = Dimension.preferredWrapContent
            }
        )
    }
}

@Composable
fun DecoupledConstraintLayout() {
    BoxWithConstraints {

        ConstraintLayout(decoupledConstraints()) {
            Button(
                onClick = { /* void */ },
                modifier = Modifier.layoutId("bt1")
            ) {
                Text(text = "Button 1")
            }

            Button(
                onClick = { /* void */ },
                modifier = Modifier.layoutId("bt2")
            ) {
                Text(text = "Button 2")
            }

            Text(
                "Text",
                modifier = Modifier.layoutId("txt")
            )
        }
    }
}

private fun decoupledConstraints(): ConstraintSet {
    return ConstraintSet {
        val bt1 = createRefFor("bt1")
        val bt2 = createRefFor("bt2")
        val txt = createRefFor("txt")
        val barrier = createEndBarrier(bt1, txt)

        constrain(bt1){
            top.linkTo(parent.top, margin = 16.dp)
        }

        constrain(bt2){
            top.linkTo(parent.top, margin = 16.dp)
            start.linkTo(barrier)
        }

        constrain(txt){
            top.linkTo(bt1.bottom, margin = 16.dp)
            centerAround(bt1.end)
        }
    }
}

@Preview
@ExperimentalComposeUiApi
@Composable
fun LargeConstraintLayoutPreview() {
    LayoutsTheme {
        DecoupledConstraintLayout()
    }
}

@Composable
fun TwoTexts(modifier: Modifier = Modifier, text1: String, text2: String) {
    Row(modifier = modifier.height(IntrinsicSize.Min)) {
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(start = 4.dp)
                .wrapContentWidth(Alignment.CenterHorizontally),
            text = text1
        )

        Divider(color = Color.Black, modifier = Modifier.fillMaxHeight().width(1.dp))
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(end = 4.dp)
                .wrapContentWidth(Alignment.CenterHorizontally),

            text = text2
        )
    }
}

@Preview
@Composable
fun TwoTextsPreview() {
    LayoutsTheme {
        Surface {
            TwoTexts(text1 = "Hi", text2 = "there")
        }
    }
}