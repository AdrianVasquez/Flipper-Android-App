package com.flipperdevices.remotecontrols.impl.grid.composable

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.flipperdevices.ifrmvp.core.ui.button.ButtonItemComposable
import com.flipperdevices.ifrmvp.core.ui.layout.core.GridItemComposable
import com.flipperdevices.ifrmvp.core.ui.layout.core.LocalScaleFactor
import com.flipperdevices.ifrmvp.core.ui.layout.core.rememberScaleFactor
import com.flipperdevices.ifrmvp.core.ui.layout.core.sf
import com.flipperdevices.ifrmvp.core.ui.layout.shared.ErrorComposable
import com.flipperdevices.ifrmvp.core.ui.util.GridConstants
import com.flipperdevices.ifrmvp.model.IfrButton
import com.flipperdevices.ifrmvp.model.IfrKeyIdentifier
import com.flipperdevices.ifrmvp.model.PageLayout
import com.flipperdevices.remotecontrols.grid.impl.R as GridR

@Composable
fun BoxWithConstraintsScope.ButtonsComposable(
    pageLayout: PageLayout?,
    onButtonClicked: (IfrButton, IfrKeyIdentifier) -> Unit,
    onReload: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (pageLayout?.buttons.isNullOrEmpty()) {
        ErrorComposable(
            desc = stringResource(GridR.string.empty_page),
            onReload = onReload
        )
    }
    BoxWithConstraints(
        modifier = modifier
            .align(Alignment.Center)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val scaleFactor = rememberScaleFactor()
        CompositionLocalProvider(LocalScaleFactor provides scaleFactor) {
            BoxWithConstraints(
                modifier = Modifier
                    .width(GridConstants.SCALE_WIDTH.sf)
                    .height(GridConstants.SCALE_HEIGHT.sf),
            ) {
                pageLayout?.buttons
                    .orEmpty()
                    .forEach { button ->
                        GridItemComposable(
                            modifier = Modifier,
                            position = button.position,
                            content = {
                                ButtonItemComposable(
                                    buttonData = button.data,
                                    onKeyDataClicked = { keyIdentifier ->
                                        onButtonClicked.invoke(button, keyIdentifier)
                                    }
                                )
                            }
                        )
                    }
            }
        }
    }
}
