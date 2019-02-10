package me.ryan.kennel.golab

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
@RestController
class GolabApplication{
	fun main(args: Array<String>) {
		runApplication<GolabApplication>(*args)
	}

	@GetMapping("/")
	fun hello(): String {
		return "Finally!"
	}
}

