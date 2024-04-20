package woowacourse.movie.model.pricing

import woowacourse.movie.model.screening.Screening
import java.io.Serializable

interface PricingSystem : Serializable {
    fun calculatePrice(screening: Screening): Int
}