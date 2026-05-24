package com.aguiabranca.backend.util

import java.sql.ResultSet
import java.sql.SQLException

fun ResultSet.hasColumn(column: String): Boolean {
	return try {
		val md = metaData ?: return false
		for (i in 1..md.columnCount) {
			if (md.getColumnLabel(i).equals(column, ignoreCase = true) ||
				md.getColumnName(i).equals(column, ignoreCase = true)
			) {
				return true
			}
		}
		false
	} catch (_: SQLException) {
		false
	}
}

fun ResultSet.safeStringAny(vararg columns: String): String? {
	for (column in columns) {
		if (hasColumn(column)) {
			val value = safeString(column)
			if (!value.isNullOrBlank()) return value
		}
	}
	return null
}

fun ResultSet.safeString(column: String): String? = try {
	getString(column)
} catch (_: SQLException) {
	null
}

fun ResultSet.safeDouble(column: String): Double = try {
	getDouble(column).takeUnless { wasNull() } ?: 0.0
} catch (_: SQLException) {
	0.0
}

fun ResultSet.safeInt(column: String): Int = try {
	getInt(column).takeUnless { wasNull() } ?: 0
} catch (_: SQLException) {
	0
}

fun ResultSet.timestampToMillis(column: String): Long = try {
	getTimestamp(column)?.time ?: 0L
} catch (_: SQLException) {
	0L
}

fun ResultSet.nullableTimestampToMillis(column: String): Long? = try {
	getTimestamp(column)?.time
} catch (_: SQLException) {
	null
}

