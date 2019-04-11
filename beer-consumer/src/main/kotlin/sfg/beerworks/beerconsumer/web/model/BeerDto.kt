/*
 *  Copyright 2019 the original author or authors.
 *
 * This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package sfg.beerworks.beerconsumer.web.model

import java.math.BigDecimal
import java.time.OffsetDateTime

data class BeerDto(
        val id: String? = null,
        val version: Int? = null,
        val createdDate: OffsetDateTime? = null,
        val lastModifiedDate: OffsetDateTime? = null,
        val beerName: String? = null,
        val beerStyle: String? = null,
        val price: BigDecimal? = null,
        val upc: Long = 0
)