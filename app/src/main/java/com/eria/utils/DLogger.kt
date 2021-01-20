package com.eria.utils

import android.text.TextUtils
import android.util.Log

/**
 * DLogger
 * Created by Dipanjan Chakraborty
 */
private const val DEFAULT_LOG_TAG =""
private const val LOG_PREFIX = DEFAULT_LOG_TAG
private const val LOG_PREFIX_LENGTH = LOG_PREFIX.length
private const val MAX_LOG_TAG_LENGTH = 23
private const val LOG_EXCEPTION_MSG = "Log message should not be empty or null"

class DLogger private constructor() {

    companion object {

        var SHOULD_PRINT_LOG = true
        private const val SHOULD_PRINT_LOG_FILE = false

        fun makeLogTag(str: String): String {
            if (TextUtils.isEmpty(str)) {
                return DEFAULT_LOG_TAG
            }
            return if (str.length > MAX_LOG_TAG_LENGTH - LOG_PREFIX_LENGTH) {
                (LOG_PREFIX
                        + str.substring(
                    0, MAX_LOG_TAG_LENGTH - LOG_PREFIX_LENGTH
                            - 1
                ))
            } else LOG_PREFIX + str
        }

        /**
         * Don't use this when obfuscating class names!
         */
        fun makeLogTag(cls: Class<*>): String {
            return makeLogTag(cls.simpleName)
        }

        /**
         * Used for enable or disable Log to be print. Default is True.
         *
         * @param isEnabled
         */
        fun enableLogging(isEnabled: Boolean) {
            SHOULD_PRINT_LOG = isEnabled
        }

        fun d(message: String?) {
            if (SHOULD_PRINT_LOG) {
                if (!TextUtils.isEmpty(message)) Log.i(DEFAULT_LOG_TAG, message!!) else throw Error(
                    LOG_EXCEPTION_MSG
                )
            }
        }

        fun d(exception: Exception) {
            if (SHOULD_PRINT_LOG) {
                if (!TextUtils.isEmpty(exception.toString())) Log.d(
                    DEFAULT_LOG_TAG,
                    exception.localizedMessage
                ) else throw Error(
                    LOG_EXCEPTION_MSG
                )
            }
        }

        fun d(tag: String?, message: String?) {
            if (SHOULD_PRINT_LOG) {
                if (!TextUtils.isEmpty(message)) Log.d(tag, message!!) else throw Error(
                    LOG_EXCEPTION_MSG
                )
            }
        }

        fun d(tag: String?, exception: Exception) {
            if (SHOULD_PRINT_LOG) {
                if (!TextUtils.isEmpty(exception.toString())) Log.d(
                    tag,
                    exception.localizedMessage
                ) else throw Error(
                    LOG_EXCEPTION_MSG
                )
            }
        }

        fun i(message: String?) {
            if (SHOULD_PRINT_LOG) {
                if (!TextUtils.isEmpty(message)) Log.i(DEFAULT_LOG_TAG, message!!) else throw Error(
                    LOG_EXCEPTION_MSG
                )
            }
        }

        fun i(exception: Exception) {
            if (SHOULD_PRINT_LOG) {
                if (!TextUtils.isEmpty(exception.toString())) Log.i(
                    DEFAULT_LOG_TAG,
                    exception.localizedMessage
                ) else throw Error(
                    LOG_EXCEPTION_MSG
                )
            }
        }

        fun i(tag: String?, message: String?) {
            if (SHOULD_PRINT_LOG) {
                if (!TextUtils.isEmpty(message)) Log.i(tag, message!!) else throw Error(
                    LOG_EXCEPTION_MSG
                )
            }
        }

        fun i(tag: String?, exception: Exception) {
            if (SHOULD_PRINT_LOG) {
                if (!TextUtils.isEmpty(exception.toString())) Log.i(
                    tag,
                    exception.localizedMessage
                ) else throw Error(
                    LOG_EXCEPTION_MSG
                )
            }
        }

        fun e(message: String?) {
            if (SHOULD_PRINT_LOG) {
                if (!TextUtils.isEmpty(message)) Log.e(DEFAULT_LOG_TAG, message!!) else throw Error(
                    LOG_EXCEPTION_MSG
                )
            }
        }

        fun e(exception: Exception) {
            if (SHOULD_PRINT_LOG) {
                if (!TextUtils.isEmpty(exception.toString())) Log.e(
                    DEFAULT_LOG_TAG,
                    exception.localizedMessage
                ) else throw Error(
                    LOG_EXCEPTION_MSG
                )
            }
        }

        fun e(tag: String?, message: String?) {
            if (SHOULD_PRINT_LOG) {
                if (!TextUtils.isEmpty(message)) Log.e(tag, message!!) else throw Error(
                    LOG_EXCEPTION_MSG
                )
            }
        }

        fun e(tag: String?, exception: Exception) {
            if (SHOULD_PRINT_LOG) {
                if (!TextUtils.isEmpty(exception.toString())) Log.e(
                    tag,
                    exception.toString()
                ) else throw Error(
                    LOG_EXCEPTION_MSG
                )
            }
        }
    }

    init {
        throw UnsupportedOperationException(
            "Should not create instance of Util class. Please use as static.."
        )
    }
}