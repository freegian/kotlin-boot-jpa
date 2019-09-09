package com.example.demo

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ShopService(val repository: RdbRepository) {
    fun get(shopId: Long): Shop? {
        return Shop.get(repository = repository, id = shopId)
    }
    fun getByJpql(shopName: String): Shop? {
        return Shop.getByJpql(repository = repository, name = shopName)
    }
    fun getBySql(shopName: String): Shop? {
        return Shop.getBySql(repository = repository, name = shopName)
    }
    fun search(shopName: String): List<Shop> {
        return Shop.search(repository = repository, name = shopName)
    }
    @Transactional
    fun register(shopName: String): Shop {
        return Shop.register(repository = repository, name = shopName)
    }
    @Transactional
    fun update(shopId: Long, shopName: String): Shop? {
        return Shop.get(repository = repository, id = shopId)?.update(repository = repository, name = shopName)
    }
    @Transactional
    fun delete(shopId: Long): Shop? {
        return Shop.get(repository = repository, id = shopId)?.delete(repository = repository)
    }
}