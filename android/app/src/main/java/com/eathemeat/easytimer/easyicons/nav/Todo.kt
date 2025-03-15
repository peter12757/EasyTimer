package com.eathemeat.easytimer.easyicons.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.eathemeat.easytimer.R
import com.eathemeat.easytimer.easyicons.EasyIcons


val EasyIcons.NAV.ToDo: ImageVector
    @Composable
    get() {
        if (_home != null) {
            return _home!!
        }
//        _home = materialIcon(name = "NAV.Home") {
//            materialPath {
//
//
//
//            }
//        }
        _home = ImageVector.vectorResource(R.drawable.nav_todo)
        return _home!!
    }

private var _home: ImageVector? = null