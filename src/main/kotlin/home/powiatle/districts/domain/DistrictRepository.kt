package home.powiatle.districts.domain

import org.springframework.data.mongodb.repository.MongoRepository

interface DistrictRepository : MongoRepository<District, String>