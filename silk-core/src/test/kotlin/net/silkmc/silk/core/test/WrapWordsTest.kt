package net.silkmc.silk.core.test

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import net.silkmc.silk.core.text.wrapWords

class WrapWordsTest : FunSpec({
    test("wraps at spaces") {
        "the quick brown fox".wrapWords(10, true) shouldBe listOf("the quick", "brown fox")
    }

    test("cuts long words only if requested") {
        "a abcdefghij b".wrapWords(4, true) shouldBe listOf("a", "abcd", "efgh", "ij b")
        "a abcdefghij b".wrapWords(4, false) shouldBe listOf("a", "abcdefghij", "b")
    }

    test("empty string yields one empty line") {
        "".wrapWords(5, true) shouldBe listOf("")
    }
})