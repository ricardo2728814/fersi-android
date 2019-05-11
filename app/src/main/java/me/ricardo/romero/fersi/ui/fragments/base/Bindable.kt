package me.ricardo.romero.fersi.ui.fragments.base

import androidx.lifecycle.ViewModel

interface Bindable {
    val vm: ViewModel
    fun bindViewModel()
}