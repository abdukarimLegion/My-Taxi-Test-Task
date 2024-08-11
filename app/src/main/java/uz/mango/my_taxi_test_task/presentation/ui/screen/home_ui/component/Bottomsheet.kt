package uz.mango.my_taxi_test_task.presentation.ui.screen.home_ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import uz.mango.my_taxi_test_task.R


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
fun BottomSheetContent(
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