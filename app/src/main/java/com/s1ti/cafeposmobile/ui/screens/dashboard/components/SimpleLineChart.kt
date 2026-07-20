package com.s1ti.cafeposmobile.ui.screens.dashboard.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

/**
 * Grafik garis ringan tanpa dependency chart library eksternal, untuk
 * menghindari nambah-nambah dependency di awal. Kalau nanti butuh chart
 * yang lebih lengkap (mis. Laporan Penjualan bab 3.8), pertimbangkan
 * library seperti Vico atau MPAndroidChart.
 *
 * TODO: ganti dataPoints dummy dengan agregasi penjualan per jam asli.
 */
@Composable
fun SimpleLineChart(
    dataPoints: List<Float>,
    modifier: Modifier = Modifier
) {
    val lineColor = MaterialTheme.colorScheme.primary

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
    ) {
        if (dataPoints.size < 2) return@Canvas

        val maxValue = dataPoints.max()
        val minValue = dataPoints.min()
        val range = (maxValue - minValue).coerceAtLeast(0.01f)
        val stepX = size.width / (dataPoints.size - 1)

        val path = Path()
        dataPoints.forEachIndexed { index, value ->
            val x = stepX * index
            val normalized = (value - minValue) / range
            val y = size.height - (normalized * size.height)
            if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }

        drawPath(path = path, color = lineColor, style = Stroke(width = 4f))
    }
}
