package com.eathemeat.easytimer.easyicons.nav

import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector
import com.eathemeat.easytimer.easyicons.EasyIcons

val EasyIcons.NAV.Recorders: ImageVector
get() {
    if (_recorder != null) {
        return _recorder!!
    }
    _recorder = materialIcon(name = "NAV.Home") {
        materialPath {
            moveTo(11.0f, 2.0f)
            verticalLineToRelative(20.0f)
            curveToRelative(-5.07f, -0.5f, -9.0f, -4.79f, -9.0f, -10.0f)
            reflectiveCurveToRelative(3.93f, -9.5f, 9.0f, -10.0f)
            close()
            moveTo(13.03f, 2.0f)
            verticalLineToRelative(8.99f)
            lineTo(22.0f, 10.99f)
            curveToRelative(-0.47f, -4.74f, -4.24f, -8.52f, -8.97f, -8.99f)
            close()
            moveTo(13.03f, 13.01f)
            lineTo(13.03f, 22.0f)
            curveToRelative(4.74f, -0.47f, 8.5f, -4.25f, 8.97f, -8.99f)
            horizontalLineToRelative(-8.97f)
            close()
        }
    }
    return _recorder!!
}

private var _recorder: ImageVector? = null