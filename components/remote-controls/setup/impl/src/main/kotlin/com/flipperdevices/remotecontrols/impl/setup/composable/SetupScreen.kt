package com.flipperdevices.remotecontrols.impl.setup.composable

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.flipperdevices.core.ui.dialog.composable.busy.ComposableFlipperBusy
import com.flipperdevices.core.ui.theme.LocalPalletV2
import com.flipperdevices.ifrmvp.core.ui.layout.shared.ErrorComposable
import com.flipperdevices.ifrmvp.core.ui.layout.shared.LoadingComposable
import com.flipperdevices.ifrmvp.core.ui.layout.shared.SharedTopBar
import com.flipperdevices.remotecontrols.impl.setup.composable.components.LoadedContent
import com.flipperdevices.remotecontrols.impl.setup.presentation.decompose.SetupComponent
import com.flipperdevices.rootscreen.api.LocalRootNavigation
import com.flipperdevices.rootscreen.model.RootScreenConfig
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import com.flipperdevices.remotecontrols.setup.impl.R as SetupR

private val SetupComponent.Model.key: Any
    get() = when (this) {
        SetupComponent.Model.Error -> "error"
        is SetupComponent.Model.Loaded -> "loaded"
        is SetupComponent.Model.Loading -> "loading"
    }

@Composable
fun SetupScreen(
    setupComponent: SetupComponent,
    modifier: Modifier = Modifier
) {
    val rootNavigation = LocalRootNavigation.current
    val coroutineScope = rememberCoroutineScope()
    val model by remember(setupComponent, coroutineScope) {
        setupComponent.model(coroutineScope)
    }.collectAsState()
    LaunchedEffect(setupComponent.remoteFoundFlow) {
        setupComponent.remoteFoundFlow
            .onEach { setupComponent.onFileFound(it) }
            .launchIn(this)
    }
    Scaffold(
        modifier = modifier,
        backgroundColor = LocalPalletV2.current.surface.backgroundMain.body,
        topBar = {
            SharedTopBar(
                title = stringResource(SetupR.string.setup_title),
                subtitle = stringResource(SetupR.string.setup_subtitle),
                onBackClicked = setupComponent::onBackClicked
            )
        }
    ) { scaffoldPaddings ->
        AnimatedContent(
            targetState = model,
            modifier = Modifier.padding(scaffoldPaddings),
            transitionSpec = { fadeIn().togetherWith(fadeOut()) },
            contentKey = { it.key }
        ) { model ->
            when (model) {
                SetupComponent.Model.Error -> {
                    ErrorComposable(onReload = setupComponent::onSuccessClicked)
                }

                is SetupComponent.Model.Loaded -> {
                    if (model.isFlipperBusy) {
                        ComposableFlipperBusy(
                            onDismiss = setupComponent::dismissBusyDialog,
                            goToRemote = {
                                setupComponent.dismissBusyDialog()
                                rootNavigation.push(RootScreenConfig.ScreenStreaming)
                            }
                        )
                    }
                    LoadedContent(
                        model = model,
                        modifier = Modifier.padding(scaffoldPaddings),
                        onPositiveClicked = setupComponent::onSuccessClicked,
                        onNegativeClicked = setupComponent::onFailedClicked,
                        onDispatchSignalClicked = setupComponent::dispatchSignal
                    )
                }

                is SetupComponent.Model.Loading -> {
                    LoadingComposable(progress = model.progress)
                }
            }
        }
    }
}
