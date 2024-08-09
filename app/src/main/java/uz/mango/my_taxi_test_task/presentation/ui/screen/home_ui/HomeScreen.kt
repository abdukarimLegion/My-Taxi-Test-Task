package uz.mango.my_taxi_test_task.presentation.ui.screen.home_ui

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import com.mapbox.geojson.Point
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.Style
import com.mapbox.maps.dsl.cameraOptions
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.MapViewportState
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotation
import com.mapbox.maps.extension.compose.rememberMapState
import com.mapbox.maps.extension.compose.style.MapStyle
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import uz.mango.my_taxi_test_task.R
import uz.mango.my_taxi_test_task.presentation.ui.screen.home_ui.component.TabView
import androidx.hilt.navigation.compose.hiltViewModel


@OptIn(MapboxExperimental::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()
    val sideEffect by viewModel.sideEffects.collectAsState(initial = HomeSideEffects.Nothing)


    val mapViewportState = rememberMapViewportState {
        setCameraOptions {
            center(Point.fromLngLat(69.2401, 41.2995))
            zoom(uiState.mapState.zoom)
        }
    }

    Scaffold(
        modifier = modifier
    ) { contentPadding ->
        HomeScreenContent(
            Modifier
                .fillMaxSize()
                .padding(contentPadding),
            uiState,
            mapViewportState = mapViewportState,
            onMenuPressed = {
                viewModel.dispatch(HomeIntent.OpenSideBar)
            },
            onTabSelected = { selectedTabIndex ->
                viewModel.dispatch(HomeIntent.OnTabSelectionChanged(selectedTabIndex = selectedTabIndex))
            },
            onChevronsUpPressed = {
                viewModel.dispatch(HomeIntent.ChevronsUp)
            },
            onZoomIn = {
                viewModel.dispatch(HomeIntent.OnZoomIn)
            },
            onZoomOut = {
                viewModel.dispatch(HomeIntent.OnZoomOut)
            },
            onLocateUser = {
                uiState.mapState.userLiveLocation?.let {
                    mapViewportState.easeTo(
                        cameraOptions = cameraOptions {
                            center(Point.fromLngLat(it.longitude, it.latitude))
                            zoom(12.0)
                        },
                        MapAnimationOptions.mapAnimationOptions { duration(1_000) }
                    )
                }
            },
            onNavigateToTariff = {
                viewModel.dispatch(HomeIntent.NavigateToTariff)
            },
            onNavigateToOrders = {
                viewModel.dispatch(HomeIntent.NavigateToOrders)
            },
            onNavigateToBordur = {
                viewModel.dispatch(HomeIntent.NavigateToBordur)
            },
            onBottomSheetScrollDown = {
                viewModel.dispatch(HomeIntent.BottomSheetPartiallyExpanded)
            },
            onBottomSheetScrollUp = {
                viewModel.dispatch(HomeIntent.BottomSheetExpanded)
            }
        )
    }

}


    @Composable
    fun HomeScreenContent(
        modifier: Modifier = Modifier,
        uiState: HomeState,
        mapViewportState: MapViewportState,
        onMenuPressed: () -> Unit,
        onChevronsUpPressed: () -> Unit,
        onTabSelected: (Int) -> Unit,
        onZoomIn: () -> Unit,
        onZoomOut: () -> Unit,
        onLocateUser: () -> Unit,
        onNavigateToTariff: () -> Unit,
        onNavigateToOrders: () -> Unit,
        onNavigateToBordur: () -> Unit,
        onBottomSheetScrollUp: () -> Unit,
        onBottomSheetScrollDown: () -> Unit
    ) {

        Box(modifier = modifier) {
            val loc = uiState.mapState.userLiveLocation
            MapboxMap(
                Modifier.fillMaxSize(),
                mapViewportState = mapViewportState,
                mapState = rememberMapState {
//            gesturesSettings = gesturesSettings.toBuilder().setScrollEnabled(false).build()
                },
                style = {
                    MapStyle(style = if (isSystemInDarkTheme()) Style.DARK else Style.MAPBOX_STREETS)
                },
                scaleBar = {}
            ) {
                loc.let { location ->
                    UserLocationPinCar(location = (UserLocation(69.3114,41.3411)))
                    Log.i(TAG, "HomeMapView: $location")
                }
            }

            HomeScreenOverlayContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp),
                uiState = uiState,
                onMenuPressed = onMenuPressed,
                onTabSelected = onTabSelected,
                onChevronsUpPressed = onChevronsUpPressed,
                onZoomIn = onZoomIn,
                onZoomOut = onZoomOut,
                onLocateUser = onLocateUser,
                onNavigateToOrders = onNavigateToOrders,
                onNavigateToTariff = onNavigateToTariff,
                onNavigateToBordur = onNavigateToBordur,
                onBottomSheetScrollUp = onBottomSheetScrollUp,
                onBottomSheetScrollDown = onBottomSheetScrollDown
            )
        }
    }


@OptIn(ExperimentalMotionApi::class)
@Composable
fun HomeScreenOverlayContent(
    modifier: Modifier,
    uiState: HomeState,
    onMenuPressed: () -> Unit,
    onChevronsUpPressed: () -> Unit,
    onTabSelected: (Int) -> Unit,
    onZoomIn: () -> Unit,
    onZoomOut: () -> Unit,
    onLocateUser: () -> Unit,
    onNavigateToTariff: () -> Unit,
    onNavigateToOrders: () -> Unit,
    onNavigateToBordur: () -> Unit,
    onBottomSheetScrollUp: () -> Unit,
    onBottomSheetScrollDown: () -> Unit
) {
    val context = LocalContext.current
    val motionScene = remember {
        context.resources
            .openRawResource(R.raw.motion_animatsiya)
            .readBytes()
            .decodeToString()
    }

    val progress by animateFloatAsState(
        targetValue = if (uiState.mapActionButtonVisible) 0f else 1f,
        animationSpec = tween(durationMillis = 300),
        label = "bottom_sheet_expansion"
    )

    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopStart
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MenuButton(onMenuPressed = onMenuPressed)

            TabView(
                modifier = Modifier.weight(1f).wrapContentWidth(),
                isActiveTabOpen = {}
            )

            SpeedDisplay(
                modifier = Modifier
                    .size(56.dp)
                    .clip(MaterialTheme.shapes.large)
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(4.dp),
                speed = uiState.speed
            )
        }

        MotionLayout(
            modifier = Modifier
                .fillMaxSize(),
            motionScene = MotionScene(content = motionScene),
            progress = progress

        ) {

            ChevronsUp(
                modifier = Modifier.layoutId("chevrons_up"),
                onPressed = onChevronsUpPressed
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.layoutId("map_action_buttons")
            ) {
                MapActionButton(iconResId = R.drawable.ic_plus, onPressed = onZoomIn)

                MapActionButton(
                    iconResId = R.drawable.ic_minus,
                    onPressed = onZoomOut
                )

                MapActionButton(
                    iconResId = R.drawable.ic_navigation,
                    iconTint = MaterialTheme.colorScheme.onSecondaryContainer,
                    onPressed = onLocateUser
                )
            }

            HomeBottomSheetContent(
                modifier = Modifier
                    .layoutId("bottom_sheet")
                    .pointerInput(Unit) {
                        detectVerticalDragGestures(
                            onVerticalDrag = { _, dragAmount ->
                                if (dragAmount > 0) onBottomSheetScrollDown() else onBottomSheetScrollUp()
                            }
                        )
                    },
                onNavigateToTariff = onNavigateToTariff,
                onNavigateToOrders = onNavigateToOrders,
                onNavigateToBordur = onNavigateToBordur
            )
        }
    }
}


@Composable
fun ChevronsUp(modifier: Modifier = Modifier, onPressed: () -> Unit) {
    Button(
        modifier = modifier
            .size(56.dp),
        onClick = onPressed,
        shape = MaterialTheme.shapes.large,
        colors = ButtonDefaults.buttonColors()
            .copy(
                containerColor = MaterialTheme.colorScheme.secondary.copy(.9f),
                contentColor = MaterialTheme.colorScheme.onSecondary
            ),
        contentPadding = PaddingValues(all = 16.dp),
        border = BorderStroke(4.dp, MaterialTheme.colorScheme.primary.copy(.9f))
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_chevrons_up),
            contentDescription = "Menu",
        )
    }
}

@Composable
fun MapActionButton(
    modifier: Modifier = Modifier,
    @DrawableRes iconResId: Int,
    iconTint: Color = MaterialTheme.colorScheme.onSecondary,
    onPressed: () -> Unit
) {
    Button(
        modifier = modifier,
//                .blurredShadow(),
        onClick = onPressed,
        shape = MaterialTheme.shapes.large,
        colors = ButtonDefaults.buttonColors()
            .copy(
                containerColor = MaterialTheme.colorScheme.primary.copy(.9f)
            ),
        contentPadding = PaddingValues(all = 16.dp),
    ) {
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = "Menu",
            tint = iconTint
        )
    }
}

@Composable
fun MenuButton(modifier: Modifier = Modifier, onMenuPressed: () -> Unit) {
    Button(
        modifier = modifier,
        onClick = onMenuPressed,
        shape = MaterialTheme.shapes.large,
        colors = ButtonDefaults.buttonColors()
            .copy(containerColor = MaterialTheme.colorScheme.primary),
        contentPadding = PaddingValues(all = 16.dp),
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_menu),
            contentDescription = "Menu"
        )
    }
}

@Composable
fun SpeedDisplay(modifier: Modifier = Modifier, speed: Int) {
    Box(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(MaterialTheme.shapes.small)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = speed.toString(),
                style = MaterialTheme.typography.displayLarge
            )
        }
    }
}


@Composable
fun UserLocationPinCar(location: UserLocation) {
    val context = LocalContext.current
    val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.ic_car_pin)

    PointAnnotation(
        point = Point.fromLngLat(
            69.2401, 41.2995
        ),
        iconImageBitmap = bitmap,
        onClick = {
            true
        }
    )
}



@Composable
fun BottomSheetDragHandle() {
    Box(
        modifier = Modifier
            .size(32.dp, 5.dp)
            .clip(MaterialTheme.shapes.extraSmall)
            .background(MaterialTheme.colorScheme.onSurface)
    )
}

@Composable
fun HomeBottomSheetContent(
    modifier: Modifier,
    onNavigateToTariff: () -> Unit,
    onNavigateToOrders: () -> Unit,
    onNavigateToBordur: () -> Unit,
) {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        BottomSheetDragHandle()

        Box(
            modifier = Modifier
                .clip(MaterialTheme.shapes.extraLarge)
                .background(MaterialTheme.colorScheme.primary)
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
                    .clip(MaterialTheme.shapes.small)
                    .background(MaterialTheme.colorScheme.secondary)
            ) {
                items.forEachIndexed { index, sheetItem ->
                    BottomSheetListItem(
                        sheetItem = sheetItem,
                        onClick = {
                            when (index) {
                                0 -> onNavigateToTariff()
                                1 -> onNavigateToOrders()
                                2 -> onNavigateToBordur()
                            }
                        },
                    )

                    if (index < items.size - 1) {
                        HorizontalDivider(
                            thickness = 1.dp,
                            color = MaterialTheme.colorScheme.outline,
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BottomSheetListItem(
    modifier: Modifier = Modifier,
    sheetItem: SheetItem,
    onClick: () -> Unit

) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding()
            .clickable(
                onClick = onClick,
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(color = Color.Black)
            )
            .padding(all = 16.dp)
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = sheetItem.iconResId),
            contentDescription = sheetItem.contentDescription,
            tint = MaterialTheme.colorScheme.onSecondary
        )

        Text(
            text = sheetItem.text,
            style = MaterialTheme.typography.labelMedium.copy(color = MaterialTheme.colorScheme.onPrimary),
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        )

        sheetItem.value?.let { value ->
            Text(
                text = value,
                style = MaterialTheme.typography.labelMedium.copy(color = MaterialTheme.colorScheme.onSecondary),
                modifier = Modifier.padding(end = 2.dp)
            )
        }

        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_chevron_right),
            contentDescription = "Chevron right",
            tint = MaterialTheme.colorScheme.onTertiary
        )
    }
}

data class SheetItem(
    @DrawableRes val iconResId: Int,
    val contentDescription: String,
    val text: String,
    val value: String?
)

val items = listOf(
    SheetItem(
        iconResId = R.drawable.ic_tariff,
        contentDescription = "Tariff",
        text = "Tariff",
        value = "6/8"
    ),
    SheetItem(
        iconResId = R.drawable.ic_order,
        contentDescription = "Buyurtmalar",
        text = "Buyurtmalar",
        value = "0"
    ),
    SheetItem(
        iconResId = R.drawable.ic_rocket,
        contentDescription = "Bordur",
        text = "Bordur",
        value = null
    ),
)