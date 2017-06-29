package gatech.mobile.occupancy.controllers

import gatech.mobile.occupancy.entities.User
import gatech.mobile.occupancy.repositories.UserRepository
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.UriComponentsBuilder

@RequestMapping("/users")
@RestController
class UserController(val userRepository: UserRepository)
{
    @GetMapping("/{username}")
    fun fetchUser(@PathVariable username: String): Any =
            userRepository.findByUsername(username) ?: ResponseEntity.notFound().build<User>()

    @PostMapping(consumes = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    fun registerUser(@RequestBody user: User, uriBuilder: UriComponentsBuilder): ResponseEntity<String>
    {
        userRepository.save(user)
        val location = uriBuilder.pathSegment("users", user.username).build().toUri()
        return ResponseEntity.created(location).build<String>()
    }
}
