package pl.lambada.songsync.ui

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import pl.lambada.songsync.MainViewModel
import pl.lambada.songsync.data.remote.UserSettingsController
import pl.lambada.songsync.ui.screens.AboutScreen
import pl.lambada.songsync.ui.screens.home.HomeScreen
import pl.lambada.songsync.ui.screens.home.HomeViewModel
import pl.lambada.songsync.ui.screens.search.SearchScreen
import pl.lambada.songsync.ui.screens.search.SearchViewModel
import pl.lambada.songsync.util.dataStore

/**
 * Composable function for handling navigation within the app.
 *
 * @param navController The navigation controller.
 * @param viewModel The main view model.
 */
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Navigator(
    navController: NavHostController,
    viewModel: MainViewModel
) {
    val dataStore = LocalContext.current.dataStore
    // its ok for this to recreate on config changes, no need to retain
    val userSettingsController = remember { UserSettingsController(dataStore) }

    SharedTransitionLayout {
        NavHost(
            navController = navController,
            startDestination = ScreenHome
        ) {
            composable<ScreenHome> {
                HomeScreen(
                    navController = navController,
                    viewModel = viewModel { HomeViewModel(userSettingsController) },
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this,
                )
            }
            composable<ScreenSearch> {
                val args = it.toRoute<ScreenSearch>()
                SearchScreen(
                    id = args.id,
                    viewModel = viewModel { SearchViewModel(userSettingsController) },
                    songName = args.songName,
                    artists = args.artists,
                    coverUri = args.coverUri,
                    filePath = args.filePath,
                    navController = navController,
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this,
                )
            }
            composable<ScreenAbout> {
                AboutScreen(
                    viewModel = viewModel,
                    navController = navController
                )
            }
        }
    }
}

@Serializable
object ScreenHome

@Serializable
data class ScreenSearch(
    val id: String? = null,
    val songName: String? = null,
    val artists: String? = null,
    val coverUri: String? = null,
    val filePath: String? = null,
)

@Serializable
object ScreenAbout