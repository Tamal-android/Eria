package com.magicmind.eria.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.magicmind.eria.data.network.BaseResponse

class SliderImageResponse : BaseResponse<SliderDataModel>()

data class SliderDataModel(

    @SerializedName("banners")
    @Expose
    val banners: List<SliderBanner>? = null,
    @SerializedName("image")
    @Expose
    val image: SliderImage? = null
)
data class SliderBanner(

    @SerializedName("id")
    @Expose
    val id: Int? = 0,
    @SerializedName("heading")
    @Expose
    val heading: String? = null,
    @SerializedName("short_description")
    @Expose
    val short_description: String? = null,
    @SerializedName("image")
    @Expose
    val image: String? = null,
    @SerializedName("status")
    @Expose
    val status: String? = null,
)
data class SliderImage(

    @SerializedName("default_image_path")
    @Expose
    val default_image_path: String? = null,
    @SerializedName("banner_image_path")
    @Expose
    val banner_image_path: String? = null
)