package ru.msnetworks.warehouse_helper

sealed class Screen(val route: String) {
    object ShipmentsListScreen : Screen("shipment_list")
    object ShipmentScreen : Screen("shipment")
//    object FullscreenImageScreen : Screen("fullscreen_image")

    fun withArgs(vararg args: Any): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}
