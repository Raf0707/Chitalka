package raf.console.chitalka.domain.repository

import raf.console.chitalka.domain.model.History

interface HistoryRepository {

    suspend fun insertHistory(
        history: History
    )

    suspend fun getHistory(): List<History>

    suspend fun getLatestBookHistory(
        bookId: Int
    ): History?

    suspend fun deleteWholeHistory()

    suspend fun deleteBookHistory(
        bookId: Int
    )

    suspend fun deleteHistory(
        history: History
    )
}