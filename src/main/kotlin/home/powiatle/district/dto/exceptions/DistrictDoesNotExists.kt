package home.powiatle.district.dto.exceptions

import org.springframework.http.HttpStatus

class DistrictDoesNotExists :
    ApiException(HttpStatus.NOT_FOUND.value(), "Accessed district does not exists")
