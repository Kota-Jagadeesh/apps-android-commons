package fr.free.nrw.commons.explore.map

import android.os.Handler
import android.os.Looper
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapController
import kotlin.math.max

/**
 * Extension function to add a completion callback to MapController.animateTo(GeoPoint).
 * Renamed to animateToWithCallback to avoid ambiguity with osmdroid's native overloads.
 */
fun MapController.animateToWithCallback(geoPoint: GeoPoint?, onAnimationComplete: () -> Unit) {
    if (geoPoint == null) {
        onAnimationComplete()
        return
    }

    // Call the original osmdroid animateTo function
    this.animateTo(geoPoint)

    // Simulate completion callback with a reasonable default delay
    val animationDuration = 300L

    Handler(Looper.getMainLooper()).postDelayed({
        onAnimationComplete()
    }, animationDuration)
}

/**
 * Extension function to add a completion callback to MapController.animateTo(GeoPoint, zoom, speed).
 * Renamed to animateToWithCallback to avoid ambiguity with osmdroid's native overloads.
 */
fun MapController.animateToWithCallback(geoPoint: GeoPoint?, zoom: Double, speed: Long, onAnimationComplete: () -> Unit) {
    if (geoPoint == null) {
        onAnimationComplete()
        return
    }

    // Call the original osmdroid animateTo with zoom and speed (which represents the duration in osmdroid)
    this.animateTo(geoPoint, zoom, speed)

    // Use the provided speed (duration) as the delay for the callback, ensuring it's at least a minimum reasonable value
    val delay = max(speed, 300L)

    // Simulate completion callback
    Handler(Looper.getMainLooper()).postDelayed({
        onAnimationComplete()
    }, delay)
}