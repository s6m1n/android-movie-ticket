package woowacourse.movie.model

import woowacourse.movie.model.pricing.PricingSystem
import woowacourse.movie.model.pricing.UniformPricingSystem
import woowacourse.movie.model.screening.Screening
import java.io.Serializable

data class Reservation(
    private val screening: Screening,
    private val priceSystem: PricingSystem = UniformPricingSystem(),
) : Serializable {
    val price = priceSystem.calculatePrice(screening)

    fun getTitle(): String {
        return screening.movie.title
    }

    fun getScreeningTime(): String {
        return screening.schedule.date
    }

    fun getQuantity(): Int = screening.quantity.value
}