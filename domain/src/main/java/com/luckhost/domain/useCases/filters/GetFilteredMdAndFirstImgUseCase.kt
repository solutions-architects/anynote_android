package com.luckhost.domain.useCases.filters

/**
 * This function is needed to filter md text and remove images from them
 *
 * That`s needed to show text and images in the different parts of NoteBox on the main screen
 */
class GetFilteredMdAndFirstImgUseCase {
    fun execute(input: String): Pair<String, List<String>> {
        val regex = Regex("""!\[.*?]\(.*?\)""")

        val matches = regex.findAll(input).map { extractLinkValue(it.value) }.toList()

        val filteredString = regex.replace(input, "")

        return filteredString to matches

    }

    private fun extractLinkValue(input: String): String {
        val regex = Regex("""!\[.*?]\((.*?)\)""")
        val matchResult = regex.find(input)

        matchResult?.let {
            it.groups[1]?.let { group ->
                return group.value
            }
        }

        throw IllegalArgumentException("Input string has no link")
    }
}