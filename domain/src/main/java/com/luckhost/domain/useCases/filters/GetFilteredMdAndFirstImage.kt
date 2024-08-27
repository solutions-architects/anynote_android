package com.luckhost.domain.useCases.filters

class GetFilteredMdAndFirstImage() {
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