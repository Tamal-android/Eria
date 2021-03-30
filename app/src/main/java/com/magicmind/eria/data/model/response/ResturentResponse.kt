package com.magicmind.eria.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.magicmind.eria.data.network.BaseResponse

class ResturentResponse: BaseResponse<ResturentDataModel>()

data class ResturentDataModel(

    /*@SerializedName("banners")
    @Expose
    val banners: List<SliderBanner>? = null,*/
    @SerializedName("image")
    @Expose
    val image: SliderImage? = null
)