package com.moo.mool.util

import java.text.BreakIterator
import java.util.*

object EdittextCount {
    fun getGraphemeCount(s: String?): Int {
        val boundary: BreakIterator = BreakIterator.getCharacterInstance(Locale.ROOT)
        boundary.setText(s)
        boundary.first()
        var result = 0
        while (boundary.next() !== BreakIterator.DONE) {
            ++result
        }
        return result
    }
}