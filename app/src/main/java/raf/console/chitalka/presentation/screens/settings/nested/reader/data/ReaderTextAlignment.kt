package raf.console.chitalka.presentation.screens.settings.nested.reader.data

enum class ReaderTextAlignment {
    START, JUSTIFY, CENTER, END
}

fun String.toTextAlignment(): ReaderTextAlignment {
    return ReaderTextAlignment.valueOf(this)
}