package com.luckhost.lockscreen_notes.presentation.main.additional.functions


/**
 * This function is needed to filter md text and remove images from them
 *
 * That`s needed to show text and images in the different parts of NoteBox on the main screen
 */

/* TODO move this guy in to the domain module */
fun getFilteredMdAndFirstImage(input: String): Pair<String, String?> {
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