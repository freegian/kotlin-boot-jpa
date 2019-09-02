package com.example.demo

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Shop(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = -1,
    var name: String? = null
): RdbEntity {
    companion object {
        fun get(repository: RdbRepository, id: Long): Shop? {
            return repository.get(Shop::class.java, id)
        }
        fun getByJpql(repository: RdbRepository, name: String): Shop? {
            return repository.get("from Shop where name != ?1", name)
        }
        fun getBySql(repository: RdbRepository, name: String): Shop? {
            return repository.getBySql("select * from shop where name != ?1", name)
        }
        fun register(repository: RdbRepository, name: String): Shop {
            return repository.save(Shop(name = name))
        }
    }
    fun update(repository: RdbRepository, name: String): Shop {
        this.name = name
        return repository.update(this)
    }
    fun delete(repository: RdbRepository): Shop {
        return repository.delete(this)
    }
}