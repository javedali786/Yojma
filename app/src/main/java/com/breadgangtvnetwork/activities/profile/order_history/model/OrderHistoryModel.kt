package app.doxzilla.activities.order_history.model

import com.google.gson.annotations.SerializedName

data class OrderHistoryModel(
    var data: Data = Data(),
    var isSuccessful: Boolean = false,
    var responseCode: Int = 0,
    var debugMessage: String = ""
)

data class Data(
    var items: List<Item> = emptyList(),
    var pageSize: Int = 0,
    var totalElements: Int = 0,
    var pageNumber: Int = 0,
    var totalPages: Int = 0
)

data class Item (

    @SerializedName("id"                     ) var id                     : Int?          = null,
    @SerializedName("offerIdentifier"        ) var offerIdentifier        : String?       = null,
    @SerializedName("offerTitle"             ) var offerTitle             : String?       = null,
    @SerializedName("orderId"                ) var orderId                : String?       = null,
    @SerializedName("userId"                 ) var userId                 : String?       = null,
    @SerializedName("orderStatus"            ) var orderStatus            : String?       = null,
    @SerializedName("orderAmount"            ) var orderAmount            : Float?          = null,
    @SerializedName("paidAmount"             ) var paidAmount             : Float?          = null,
    @SerializedName("orderCurrency"          ) var orderCurrency          : String?       = null,
    @SerializedName("voDOfferType"           ) var voDOfferType           : String?       = null,
    @SerializedName("subscriptionOfferType"  ) var subscriptionOfferType  : String?       = null,
    @SerializedName("rentalExpiryDate"       ) var rentalExpiryDate       : String?       = null,
    @SerializedName("createdDate"            ) var createdDate            : Long?          = null,
    @SerializedName("subscriptionExpiryDate" ) var subscriptionExpiryDate : String?       = null,
    @SerializedName("renewalDate"            ) var renewalDate            : String?       = null,
    @SerializedName("contentDetails"         ) var contentDetails         : String?       = null,
    @SerializedName("paymentProvider"        ) var paymentProvider        : String?       = null,
    @SerializedName("paymentId"              ) var paymentId              : String?       = null,
    @SerializedName("entitlementState"       ) var entitlementState       : String?       = null,
    @SerializedName("contentSKU"             ) var contentSKU             : String?       = null,
    @SerializedName("paymentStatus"          ) var paymentStatus          : String?       = null,
    @SerializedName("nextChargeDate"         ) var nextChargeDate         : Long?          = null,
    @SerializedName("currentExpiry"          ) var currentExpiry          : Long?          = null,
    @SerializedName("onTrial"                ) var onTrial                : Boolean?      = null,
    @SerializedName("offerDetails"           ) var offerDetails           : OfferDetails? = OfferDetails()

)

data class ContentDetails(
    var id: String = "",
    var title: String = "",
    var contentType: String = "",
)

data class OfferDetails(
    var offerPeriod: String = "",
    var trialPeriod: TrialPeriod = TrialPeriod(),
)

data class TrialPeriod(
    var trialType: String = "",
    var trialDuration: Int = 0,
)