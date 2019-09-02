package com.example.demo

import org.springframework.stereotype.Service

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
    fun register(shopName: String): Shop {
        return Shop.register(repository = repository, name = shopName)
    }
    fun update(shopId: Long, shopName: String): Shop? {
        return Shop.get(repository = repository, id = shopId)?.update(repository = repository, name = shopName)
    }
    fun delete(shopId: Long): Shop? {
        return Shop.get(repository = repository, id = shopId)?.delete(repository = repository)
    }
}