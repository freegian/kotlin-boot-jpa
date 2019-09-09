package com.example.demo

import org.springframework.web.bind.annotation.*

@RequestMapping("/shop")
@RestController
class ShopController(val service: ShopService) {
    @GetMapping("/{shopId}/get")
    fun getShop(@PathVariable shopId: Long): Shop? {
        return service.get(shopId = shopId)
    }
    @GetMapping("/getByJpql")
    fun getByJpql(@RequestBody shopName: String): Shop? {
        return service.getByJpql(shopName = shopName)
    }
    @GetMapping("/getBySql")
    fun getBySql(@RequestBody shopName: String): Shop? {
        return service.getBySql(shopName = shopName)
    }
    @GetMapping("/search")
    fun search(@RequestBody shopName: String): List<Shop> {
        return service.search(shopName = shopName)
    }
    @PostMapping("/register")
    fun register(@RequestBody shopName: String): Shop? {
        return service.register(shopName = shopName)
    }
    @PostMapping("/{shopId}/update")
    fun update(@PathVariable shopId: Long, @RequestBody shopName: String): Shop? {
        return service.update(shopId = shopId, shopName = shopName)
    }
    @PostMapping("/{shopId}/delete")
    fun delete(@PathVariable shopId: Long): Shop? {
        return service.delete(shopId = shopId)
    }
}