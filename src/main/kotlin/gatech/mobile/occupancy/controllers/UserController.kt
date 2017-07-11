package gatech.mobile.occupancy.controllers

import gatech.mobile.occupancy.entities.Favorite
import gatech.mobile.occupancy.entities.User
import gatech.mobile.occupancy.repositories.UserRepository
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.UriComponentsBuilder

@RequestMapping("/users")
@RestController
class UserController(private val userRepository: UserRepository)
{
    @GetMapping("/{username}")
    fun fetchUser(@PathVariable username: String): Any =
            userRepository.findByUsername(username) ?: ResponseEntity.notFound().build<User>()

    @PostMapping(consumes = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    fun registerUser(@RequestBody user: User, uriBuilder: UriComponentsBuilder): ResponseEntity<Map<String, String>>
    {
        userRepository.save(user)
        val location = uriBuilder.pathSegment("users", user.username).build().toUri()
        return ResponseEntity.created(location).body(mapOf("result" to "User created successfully"))
    }

    @GetMapping("/{username}/favorites")
    fun fetchUserFavorites(@PathVariable username: String): Any
    {
        val favorites = userRepository.findByUsername(username)?.favorites
        return if (favorites != null) mapOf("favorites" to favorites) else ResponseEntity.notFound().build<User>()
    }

    @PostMapping("/{username}/favorites", consumes = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    fun addUserFavorite(@PathVariable username: String, @RequestBody favorite: Favorite): Any
    {
        val user = userRepository.findByUsername(username) ?: return ResponseEntity.notFound().build<User>()
        user.favorites.add(favorite)
        userRepository.save(user)
        return mapOf("result" to "Favorite added")
    }

    @DeleteMapping("/{username}/favorites", consumes = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    fun deleteUserFavorite(@PathVariable username: String, @RequestBody favorite: Favorite): Any
    {
        val user = userRepository.findByUsername(username) ?: return ResponseEntity.notFound().build<User>()
        user.favorites.remove(favorite)
        userRepository.save(user)
        return mapOf("result" to "Favorite deleted")
    }
}
