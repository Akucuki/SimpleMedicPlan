package com.example.simplemedicplan.feature.home

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.simplemedicplan.application.NavDirection
import com.example.simplemedicplan.application.theme.DarkRedColor
import com.example.simplemedicplan.application.theme.MudColor
import com.example.simplemedicplan.application.theme.YellowColor
import com.example.simplemedicplan.model.HomeNavigationBarItem

@Composable
fun HomeNavigationBar(
    modifier: Modifier = Modifier,
    onNavigate: (NavDirection) -> Unit,
    selectedNavItem: MutableState<HomeNavigationBarItem> = rememberSaveable {
        mutableStateOf(HomeNavigationBarItem.PILLS)
    }
) {
    NavigationBar(
        modifier = modifier.fillMaxWidth(),
        windowInsets = WindowInsets.navigationBars,
        containerColor = YellowColor,
        contentColor = DarkRedColor
    ) {
        HomeNavigationBarItem.values().forEach { item ->
            NavigationBarItem(
                selected = item == selectedNavItem.value,
                onClick = {
                    selectedNavItem.value = item
                    onNavigate(item.navDirection)
                },
                icon = {
                    Icon(painter = painterResource(item.iconId), contentDescription = null)
                },
                label = {
                    Text(
                        text = stringResource(item.labelId),
                        style = MaterialTheme.typography.bodySmall,
                    )
                },
                colors = navigationItemColors()
            )
        }
    }
}

@Composable
private fun navigationItemColors() = NavigationBarItemDefaults.colors(
    indicatorColor = Color.White,
    selectedIconColor = DarkRedColor,
    selectedTextColor = DarkRedColor,
    unselectedIconColor = MudColor,
    unselectedTextColor = MudColor
)