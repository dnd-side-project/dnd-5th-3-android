package com.moo.mool.util

import android.content.Context
import com.moo.mool.database.SharedManager

fun getIsMineUtil(context: Context, writerName: String) =
    (writerName == SharedManager(context).getCurrentUser().nickname)