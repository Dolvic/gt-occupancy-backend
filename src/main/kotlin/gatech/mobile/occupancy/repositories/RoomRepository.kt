package gatech.mobile.occupancy.repositories

import gatech.mobile.occupancy.entities.Room
import org.springframework.data.mongodb.repository.MongoRepository

interface RoomRepository : MongoRepository<Room, String>
{
    fun findByBuildingAndFloor(building: String, floor: Int): List<Room>

    fun findByBuildingAndFloorAndRoom(building: String, floor: Int, room: String): Room?
}
