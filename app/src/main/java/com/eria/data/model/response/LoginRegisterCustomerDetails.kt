package com.eria.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LoginRegisterCustomerDetails {

    @SerializedName("access_token")
    @Expose
    var accessToken: String? = null

    @SerializedName("user_type")
    @Expose
    private var userType: String? = null

    @SerializedName("updated_at")
    @Expose
    private var updatedAt: Any? = null

    @SerializedName("contact_no")
    @Expose
    var contactNo: String? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("profile_picture")
    @Expose
    private var profilePicture: String? = null

    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("email")
    @Expose
    var email: String? = null

    @SerializedName("created_datetime")
    @Expose
    private var createdDatetime: String? = null

    @SerializedName("status")
    @Expose
    private var status: String? = null

    @JvmName("setAccessToken1")
    fun setAccessToken(accessToken: String?) {
        this.accessToken = accessToken
    }

    @JvmName("getAccessToken1")
    fun getAccessToken(): String? {
        return accessToken
    }

    fun setUserType(userType: String?) {
        this.userType = userType
    }

    fun getUserType(): String? {
        return userType
    }

    fun setUpdatedAt(updatedAt: Any?) {
        this.updatedAt = updatedAt
    }

    fun getUpdatedAt(): Any? {
        return updatedAt
    }

    @JvmName("setContactNo1")
    fun setContactNo(contactNo: String?) {
        this.contactNo = contactNo
    }

    @JvmName("getContactNo1")
    fun getContactNo(): String? {
        return contactNo
    }

    @JvmName("setName1")
    fun setName(name: String?) {
        this.name = name
    }

    @JvmName("getName1")
    fun getName(): String? {
        return name
    }

    fun setProfilePicture(profilePicture: String?) {
        this.profilePicture = profilePicture
    }

    fun getProfilePicture(): String? {
        return profilePicture
    }

    @JvmName("setId1")
    fun setId(id: String?) {
        this.id = id
    }

    @JvmName("getId1")
    fun getId(): String? {
        return id
    }

    @JvmName("setEmail1")
    fun setEmail(email: String?) {
        this.email = email
    }

    @JvmName("getEmail1")
    fun getEmail(): String? {
        return email
    }

    fun setCreatedDatetime(createdDatetime: String?) {
        this.createdDatetime = createdDatetime
    }

    fun getCreatedDatetime(): String? {
        return createdDatetime
    }

    fun setStatus(status: String?) {
        this.status = status
    }

    fun getStatus(): String? {
        return status
    }
}