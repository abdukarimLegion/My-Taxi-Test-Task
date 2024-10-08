package uz.mango.my_taxi_test_task.presentation.ui.screen.home_ui.component

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposableTarget
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.launch
import uz.mango.my_taxi_test_task.presentation.ui.theme.immutableDark

@Composable
fun TabView(
    modifier: Modifier ,
    selectedTabIndex: Int = 0,
    pages: List<String> = listOf("Band", "Faol"),
    isActiveTabOpen: (Boolean) -> Unit
) {
    val scope = rememberCoroutineScope()
    var selectedTab by remember { mutableStateOf(selectedTabIndex) }

    val indicator = @Composable { tabPositions: List<TabPosition> ->
        CustomIndicator(tabPositions, selectedTab)
    }

    Card(
        modifier = modifier
            .height(56.dp)
            .shadow(
                elevation = 12.dp,
                spotColor = MaterialTheme.colorScheme.onBackground,
                shape = RoundedCornerShape(8.dp)
            ),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
    ) {
        TabRow(
            modifier = Modifier
                .fillMaxHeight()
                .width(200.dp),
            selectedTabIndex = selectedTab,
            indicator = indicator,
            divider = {}
        ) {
            pages.forEachIndexed { index, title ->
                Tab(
                    modifier = Modifier.zIndex(6f),
                    text =  {
                         Text (
                            text = title,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTab == index) immutableDark else MaterialTheme.colorScheme.onBackground
                        )
                    },
                    selected = selectedTab == index,
                    onClick = {
                        scope.launch {
                            selectedTab = index
                            isActiveTabOpen(selectedTab == 1)
                        }
                    }
                )
            }
        }
    }
}


//@Composable
//fun Text(text: String, fontFamily: TextStyle, fontWeight: FontWeight, color: Color) {
//    androidx.compose.material3.Text(modifier = Modifier, text = text, fontFamily, fontWeight, color)
//}


@Composable
private fun CustomIndicator(tabPositions: List<TabPosition>, selectedTab: Int) {
    val transition = updateTransition(selectedTab, label = "")
    val indicatorStart by transition.animateDp(
        transitionSpec = {
            if (initialState < targetState) {
                spring(dampingRatio = 1f, stiffness = 50f)
            } else {
                spring(dampingRatio = 1f, stiffness = 1000f)
            }
        }, label = ""
    ) {
        tabPositions[it].left
    }

    val indicatorEnd by transition.animateDp(
        transitionSpec = {
            if (initialState < targetState) {
                spring(dampingRatio = 1f, stiffness = 1000f)
            } else {
                spring(dampingRatio = 1f, stiffness = 50f)
            }
        }, label = ""
    ) {
        tabPositions[it].right
    }

    val indicatorColor by animateColorAsState(
        targetValue = if (selectedTab == 0) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.primaryContainer
//        animationSpec = tween(durationMillis = 600), label = ""
    )

    Box(
        Modifier
            .offset(x = indicatorStart)
            .wrapContentSize(align = Alignment.CenterStart)
            .width(indicatorEnd - indicatorStart)
            .padding(horizontal = 4.dp)
            .fillMaxSize()
            .background(
                color = indicatorColor,
                RoundedCornerShape(10.dp)
            )
            .zIndex(1f)
    )
}
