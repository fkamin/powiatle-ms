package home.powiatle.district.domain

import org.springframework.data.mongodb.repository.MongoRepository

interface MongoDistrictRepository : MongoRepository<District, String>