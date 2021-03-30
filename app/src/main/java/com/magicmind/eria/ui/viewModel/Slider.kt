package com.magicmind.eria.ui.viewModel

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class Slider(@SerializedName("image")
             @Expose
             private var url: String? = null) {
    /*@SerializedName("id")
    @Expose
    private var id: String? = null

    @SerializedName("short_description")
    @Expose
    private var short_description: String? = null

    @SerializedName("heading")
    @Expose
    private var heading: String? = null*/



/*

    fun getId(): String? {
        return id
    }

    fun setId(id: String?) {
        this.id = id
    }
*/

    fun getUrl(): String? {
        return url
    }

    fun setUrl(url: String?) {
        this.url = url
    }

    /*fun getShort_description(): String? {
        return short_description
    }

    fun setShort_description(short_description: String?) {
        this.short_description = short_description
    }

    fun getHeading(): String? {
        return heading
    }

    fun setHeading(heading: String?) {
        this.heading = heading
    }*/
}