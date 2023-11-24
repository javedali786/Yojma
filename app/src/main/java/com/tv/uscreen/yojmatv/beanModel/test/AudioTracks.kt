package com.tv.uscreen.yojmatv.beanModel.test

import com.google.gson.annotations.SerializedName

data class AudioTracks(

	@field:SerializedName("AudioTracks")
	val audioTracks: List<AudioTracksItem?>? = null
)

data class AudioTracksItem(

	@field:SerializedName("langCode")
	val langCode: String? = null,

	@field:SerializedName("encodingRates")
	val encodingRates: List<Any?>? = null,

	@field:SerializedName("errorMessage")
	val errorMessage: Any? = null,

	@field:SerializedName("language")
	val language: String? = null,

	@field:SerializedName("mimeType")
	val mimeType: Any? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("duration")
	val duration: Any? = null,

	@field:SerializedName("originalSizeInBytes")
	val originalSizeInBytes: Any? = null,

	@field:SerializedName("default")
	val jsonMemberDefault: Boolean? = null,

	@field:SerializedName("variant")
	val variant: Any? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("externalIdentifier")
	val externalIdentifier: Any? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("status")
	val status: String? = null
)
