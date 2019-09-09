package com.example.demo

import javax.persistence.EntityManager
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

class RdbCriteria<T>(
    private val entityClass: Class<T>,
    private val builder: CriteriaBuilder,
    private val query: CriteriaQuery<T>,
    private val root: Root<T>,
    private val condition: RdbCriteriaCondition<T>,
    private val predicates: MutableSet<Predicate> = mutableSetOf()
) {
    fun where(func: (RdbCriteriaCondition<T>) -> (RdbCriteriaCondition<T>)): RdbCriteria<T> {
        this.query.where(*func(condition).build())
        return this
    }
    fun result(): CriteriaQuery<T> {
        return result { it }
    }
    @Suppress("UNCHECKED_CAST")
    fun result(extension: (CriteriaQuery<T>) -> CriteriaQuery<T>): CriteriaQuery<T> {
        return extension(query)
    }
    companion object {
        private const val DefaultAlias = "r"
        fun <T> build(em: EntityManager, entityClass: Class<T>, alias: String = DefaultAlias): RdbCriteria<T> {
            val builder = em.criteriaBuilder
            val query = builder.createQuery(entityClass)
            val root = query.from(entityClass)
            root.alias(alias)
            return RdbCriteria(
                entityClass = entityClass,
                builder = builder,
                query = query,
                root = root,
                condition = RdbCriteriaCondition(root = root, builder = builder)
            )
        }
    }
}