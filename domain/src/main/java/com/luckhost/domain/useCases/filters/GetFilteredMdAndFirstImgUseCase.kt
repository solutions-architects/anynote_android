package com.luckhost.domain.useCases.filters

/**
 * This function is needed to filter md text and remove images from them
 *
 * That`s needed to show text and images in the different parts of NoteBox on the main screen
 */
class GetFilteredMdAndFirstImgUseCase() {
    fun execute(input: String): Pair<String, String?> {
        val regex = Regex("""!\[.*?\]\(.*?\)""")

        val firstImageLink = regex.find(input)

        val filteredString = regex.replace(input, "")

        if (firstImageLink != null) {
            return filteredString to extractLinkValue(firstImageLink.value)
        }
        return filteredString to null
    }

    private fun extractLinkValue(input: String): String? {
        val regex = Regex("""!\[.*?\]\((.*?)\)""")
        val matchResult = regex.find(input)
        return matchResult?.groups?.get(1)?.value
    }
}