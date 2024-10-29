package com.luckhost.domain.useCases.filters

/**
 * Filters the markdown from possible titles, links, and images
 * in order to show a preview of the note
 */
/* TODO expand the functionality */
class GetFilteredMdUseCase {
    fun execute(input: String): String {
        val regex = Regex("""!\[.*?]\(.*?\)""")
        val filteredString = regex.replace(input, "")

        return filteredString
    }
}