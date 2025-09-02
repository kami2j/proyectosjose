package com.modasmart.modasmartfinal.actividades

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import com.modasmart.modasmartfinal.R

class TyC: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tyc)
    }

    @Suppress("DEPRECATION")
    override fun onBackPressed() {
        super.onBackPressed()
    }

}