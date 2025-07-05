package com.hobom.hobominternal.infra.feign.notion.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class DatabaseQueryResponse(
    val next_cursor: String?,
    val has_more: Boolean,
    val request_id: String,
    val results: List<NotionPage>,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class BlockChildrenResponse(
    val results: List<NotionBlock>,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class NotionBlock(
    val id: String,
    val type: String,
    val paragraph: NotionRichTextBlock? = null,
    val heading_1: NotionRichTextBlock? = null,
    val heading_2: NotionRichTextBlock? = null,
    val heading_3: NotionRichTextBlock? = null,
    val bulleted_list_item: NotionRichTextBlock? = null,
    val numbered_list_item: NotionRichTextBlock? = null,
    val code: NotionCodeBlock? = null,
    val quote: NotionRichTextBlock? = null,
    val to_do: NotionToDoBlock? = null,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class NotionCodeBlock(
    val rich_text: List<NotionText>,
    val language: String,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class NotionToDoBlock(
    val rich_text: List<NotionText>,
    val checked: Boolean,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class NotionRichTextBlock(
    val rich_text: List<NotionText>,
    val color: String? = null,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class NotionPage(
    val id: String,
    val created_time: String,
    val last_edited_time: String,
    val cover: String?,
    val icon: NotionIcon?,
    val archived: Boolean,
    val in_trash: Boolean,
    val properties: Map<String, NotionProperty>,
    val url: String,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class NotionIcon(
    val type: String,
    val emoji: String,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class NotionProperty(
    val id: String,
    val type: String,
    val title: List<NotionText>? = null,
    val rich_text: List<NotionText>? = null,
    val checkbox: Boolean? = null,
    val date: NotionDate? = null,
    val multi_select: List<NotionSelectOption>? = null,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class NotionText(
    val type: String,
    val text: NotionTextContent,
    val annotations: NotionTextAnnotations,
    val plain_text: String,
    val href: String?,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class NotionTextContent(
    val content: String,
    val link: NotionLink? = null,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class NotionLink(
    val url: String,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class NotionTextAnnotations(
    val bold: Boolean,
    val italic: Boolean,
    val strikethrough: Boolean,
    val underline: Boolean,
    val code: Boolean,
    val color: String,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class NotionDate(
    val start: String,
    val end: String?,
    val time_zone: String?,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class NotionSelectOption(
    val id: String,
    val name: String,
    val color: String,
)
