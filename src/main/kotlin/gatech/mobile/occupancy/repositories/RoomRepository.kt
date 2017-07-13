package gatech.mobile.occupancy.repositories

import gatech.mobile.occupancy.entities.Room
import org.springframework.data.mongodb.repository.MongoRepository

interface RoomRepository : MongoRepository<Room, String>
{
    fun findByBuildingCodeAndFloor(building: String, floor: String): List<Room>

    fun findByBuildingCodeAndFloorAndRoom(building: String, floor: String, room: String): Room?
}
