package raf.console.chitalka.domain.repository

import raf.console.chitalka.domain.model.ColorPreset

interface ColorPresetRepository {

    suspend fun updateColorPreset(
        colorPreset: ColorPreset
    )

    suspend fun selectColorPreset(
        colorPreset: ColorPreset
    )

    suspend fun getColorPresets(): List<ColorPreset>

    suspend fun reorderColorPresets(
        orderedColorPresets: List<ColorPreset>
    )

    suspend fun deleteColorPreset(
        colorPreset: ColorPreset
    )
}