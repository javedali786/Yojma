package com.tv.uscreen.yojmatv.jwplayer.cast

import com.google.gson.annotations.SerializedName

data class PlayDetailResponse(

	@field:SerializedName("playlist")
	val playlist: List<PlaylistItem?>? = null,

	@field:SerializedName("feed_instance_id")
	val feedInstanceId: String? = null,

	@field:SerializedName("kind")
	val kind: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("title")
	val title: String? = null
)

data class Variations(
	val any: Any? = null
)

data class TracksItem(

	@field:SerializedName("file")
	val file: String? = null,

	@field:SerializedName("kind")
	val kind: String? = null,

	@field:SerializedName("language")
	val language: String? = null,

	@field:SerializedName("label")
	val label: String? = null
)

data class PlaylistItem(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("images")
	val images: List<ImagesItem?>? = null,

	@field:SerializedName("sources")
	val sources: List<SourcesItem?>? = null,

	@field:SerializedName("mediaContentId")
	val mediaContentId: String? = null,

	@field:SerializedName("link")
	val link: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("mediaid")
	val mediaid: String? = null,

	@field:SerializedName("tracks")
	val tracks: List<TracksItem?>? = null,

	@field:SerializedName("duration")
	val duration: Int? = null,

	@field:SerializedName("variations")
	val variations: Variations? = null,

	@field:SerializedName("tenantId")
	val tenantId: String? = null,

	@field:SerializedName("projectId")
	val projectId: String? = null,

	@field:SerializedName("pubdate")
	val pubdate: Int? = null
)

data class SourcesItem(

	@field:SerializedName("file")
	val file: String? = null,

	@field:SerializedName("framerate")
	val framerate: Any? = null,

	@field:SerializedName("width")
	val width: Int? = null,

	@field:SerializedName("bitrate")
	val bitrate: Int? = null,

	@field:SerializedName("label")
	val label: String? = null,

	@field:SerializedName("filesize")
	val filesize: Int? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("height")
	val height: Int? = null
)

data class ImagesItem(

	@field:SerializedName("src")
	val src: String? = null,

	@field:SerializedName("width")
	val width: Int? = null,

	@field:SerializedName("type")
	val type: String? = null
)
