package com.example.demo

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/shop")
@RestController
class ShopController(val service: ShopService) {
    // add endpoint
}