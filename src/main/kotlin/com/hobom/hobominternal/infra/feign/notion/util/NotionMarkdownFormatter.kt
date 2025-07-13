package com.hobom.hobominternal.infra.feign.notion.util

import com.hobom.hobominternal.infra.feign.notion.dto.NotionBlock
import com.hobom.hobominternal.infra.feign.notion.dto.NotionText

object NotionMarkdownFormatter {
    fun convertToMarkdown(blocks: List<NotionBlock>): String {
        return buildString {
            blocks.forEach { block ->
                when (block.type) {
                    "heading_1" -> appendLine("# ${block.heading_1?.rich_text?.toPlainText()}")
                    "heading_2" -> appendLine("## ${block.heading_2?.rich_text?.toPlainText()}")
                    "heading_3" -> appendLine("### ${block.heading_3?.rich_text?.toPlainText()}")
                    "paragraph" -> appendLine(block.paragraph?.rich_text?.toPlainText())
                    "bulleted_list_item" -> appendLine("- ${block.bulleted_list_item?.rich_text?.toPlainText()}")
                    "numbered_list_item" -> appendLine("1. ${block.numbered_list_item?.rich_text?.toPlainText()}")
                    "quote" -> appendLine("> ${block.quote?.rich_text?.toPlainText()}")
                    "to_do" -> {
                        val check = if (block.to_do?.checked == true) "x" else " "
                        appendLine("- [$check] ${block.to_do?.rich_text?.toPlainText()}")
                    }
                    "code" -> {
                        val lang = block.code?.language ?: ""
                        val code = block.code?.rich_text?.toPlainText()
                        appendLine("```$lang\n$code\n```")
                    }
                    else -> {} // 무시
                }
            }
        }
    }
}

fun List<NotionText>.toPlainText(): String {
    return this.joinToString("") { it.plain_text }
}
