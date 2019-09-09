package com.example.demo

import org.springframework.stereotype.Repository
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.Query
import javax.persistence.criteria.CriteriaQuery

@Repository
class RdbRepository (@PersistenceContext private val em: EntityManager) {
    fun <T : RdbEntity> get(clazz: Class<T>, id: java.io.Serializable ) : T? {
        return em.find(clazz, id)
    }
    fun <T : RdbEntity> get(jpql: String, vararg args: Any) : T? {
        return bindQueryParams(em.createQuery(jpql), args).resultList.firstOrNull() as T?
    }
    fun <T : RdbEntity> getBySql(sql: String, vararg args: Any) : T? {
        return bindQueryParams(em.createNativeQuery(sql), *args).resultList.firstOrNull() as T?
    }
    private fun bindQueryParams(query: Query, vararg args: Any): Query {
        for (i in args.indices) {
            query.setParameter(i + 1, args[i])
        }
        return query
    }

    fun <T : RdbEntity> save(entity: T): T {
        em.persist(entity)
        return entity
    }
    fun <T : RdbEntity> update(entity: T): T {
        return em.merge(entity)
    }
    fun <T : RdbEntity> delete(entity: T): T {
        em.remove(entity)
        return entity
    }
    fun <T> findByCriteria(entityClass: Class<T>, func: (RdbCriteria<T>) -> CriteriaQuery<T>): List<T> {
        return em.createQuery(func(RdbCriteria.build(em, entityClass))).resultList
    }
}