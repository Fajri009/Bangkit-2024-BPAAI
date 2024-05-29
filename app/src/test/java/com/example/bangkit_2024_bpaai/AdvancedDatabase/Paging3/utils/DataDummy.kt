package com.example.bangkit_2024_bpaai.AdvancedDatabase.Paging3.utils

import com.example.bangkit_2024_bpaai.AdvancedDatabase.Paging3.network.QuoteResponseItem

object DataDummy {
    fun generateDummyQuoteResponse(): List<QuoteResponseItem> {
        val items: MutableList<QuoteResponseItem> = arrayListOf()
        for (i in 0..100) {
            val quote = QuoteResponseItem(
                i.toString(),
                "author + $i",
                "quote $i",
            )
            items.add(quote)
        }
        return items
    }
}