package me.ricardo.romero.fersi.di

import me.ricardo.romero.fersi.services.FireStorageService
import me.ricardo.romero.fersi.services.FireStoreService
import me.ricardo.romero.fersi.services.ProductService
import me.ricardo.romero.fersi.ui.vm.HomeFragmentViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { FireStoreService() }
    single { FireStorageService() }
    single { ProductService(get(), get()) }
    viewModel { HomeFragmentViewModel(get()) }
}