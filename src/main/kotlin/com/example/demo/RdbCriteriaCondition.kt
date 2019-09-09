package com.example.demo

import java.util.*
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.Path
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

class RdbCriteriaCondition<T>(
    private val builder: CriteriaBuilder,
    private val root: Root<T>,
    private val predicates: MutableSet<Predicate> = mutableSetOf()
) {
    fun build(): Array<Predicate> {
        return this.predicates.toTypedArray()
    }
    private fun add(predicate: Predicate): RdbCriteriaCondition<T> {
        this.predicates.add(predicate)
        return this
    }
    fun equal(field: String, value: Any?): RdbCriteriaCondition<T> {
        if (isValid(value)) {
            val fieldPath: Path<Any> = root.get(field)
            add(builder.equal(fieldPath, value))
        }
        return this
    }
    private fun isValid(value: Any?): Boolean {
        return when (value) {
            is String? -> !value.isNullOrBlank()
            is Optional<*> -> value.isPresent
            else -> value != null
        }
    }
}