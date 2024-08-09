package uz.mango.my_taxi_test_task.presentation.ui.screen.home_ui

sealed class HomeSideEffects {
    data object NavigateToTariff : HomeSideEffects()
    data object NavigateToOrders : HomeSideEffects()
    data object NavigateToBorder : HomeSideEffects()
    data object OpenSideBar : HomeSideEffects()
    data object Nothing : HomeSideEffects()
    data class ShowError(val message: String) : HomeSideEffects()

}

sealed class HomeIntent {
    data object OpenSideBar : HomeIntent()
    data class OnTabSelectionChanged(val selectedTabIndex: Int) : HomeIntent()
    data class OnSpeedChange(val speed: Int) : HomeIntent()
    data object OnZoomIn : HomeIntent()
    data object OnZoomOut : HomeIntent()
    data object ChevronsUp : HomeIntent()
    data object NavigateToTariff : HomeIntent()
    data object NavigateToOrders : HomeIntent()
    data object NavigateToBordur : HomeIntent()
    data object BottomSheetExpanded : HomeIntent()
    data object BottomSheetPartiallyExpanded : HomeIntent()
}