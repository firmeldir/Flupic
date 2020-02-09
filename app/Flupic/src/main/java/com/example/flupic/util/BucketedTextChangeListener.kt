package com.example.flupic.util

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.widget.EditText
import java.util.*
import kotlin.math.min

class BucketedTextChangeListener(
    private val editText: EditText,
    private val placeHolder: String,
    private val expectedContentLength: Int,
    private val callback: ContentChangeCallback?
) : TextWatcher{

    private val postFixes: Array<String> = generatePostfixArray(placeHolder, expectedContentLength)

    interface ContentChangeCallback {
        /**
         * Idempotent function invoked by the listener when the edit text changes and is of expected
         * length
         */
        fun whenComplete()

        /**
         * Idempotent function invoked by the listener when the edit text changes and is not of
         * expected length
         */
        fun whileIncomplete()
    }

    /**
     * For example, passing in ("-", 6) would return the following result:
     * {"", "-", "--", "---", "----", "-----", "------"}
     *
     * @param repeatableChar the char to repeat to the specified length
     * @param length         the maximum length of repeated chars
     * @return an increasing sequence array of chars up the specified length
     */

    private fun generatePostfixArray(repeatableChar: CharSequence, length: Int): Array<String> {
        val ret = mutableListOf<String>()

        for (i in 0..length) {
            ret.add(TextUtils.join("", Collections.nCopies(i, repeatableChar)))
        }

        return ret.toTypedArray()
    }

    override fun onTextChanged(s: CharSequence?, ignoredParam1: Int, ignoredParam2: Int, ignoredParam3: Int) {
        // The listener is expected to be used in conjunction with the SpacedEditText.

        // Approach
        // 1) Strip all spaces and hyphens introduced by the SET for aesthetics
        val numericContents = s.toString()
            .replace(" ".toRegex(), "")
            .replace(placeHolder.toRegex(), "")

        // 2) Trim the content to acceptable length.
        val enteredContentLength = min(numericContents.length, expectedContentLength)
        val enteredContent = numericContents.substring(0, enteredContentLength)

        // 3) Reset the text to be the content + required hyphens. The SET automatically inserts
        // spaces requires for aesthetics. This requires removing and resetting the listener to
        // avoid recursion.
        editText.removeTextChangedListener(this)
        editText.setText(enteredContent + postFixes[expectedContentLength - enteredContentLength])
        editText.setSelection(enteredContentLength)
        editText.addTextChangedListener(this)

        // 4) Callback listeners waiting on content to be of expected length
        if (enteredContentLength == expectedContentLength && callback != null) callback.whenComplete()
            else callback?.whileIncomplete()
    }


    override fun afterTextChanged(p0: Editable?) {}

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
}