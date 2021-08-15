package net.axay.fabrik.core.test

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import net.axay.fabrik.core.text.literalText

class LiteralTextTest : FunSpec({
    context("built literal text raw string should be equal to original string") {
        val baseTexts = listOf(
            "simple",
            "simple with space",
            "special characters: äöü ഞ൝"
        )

        context("only base text") {
            baseTexts.forEach { baseText ->
                test("should work with '$baseText'") {
                    val text = literalText(baseText) {
                        color = 0x2FFF53
                        bold = true
                    }

                    text.string shouldBe baseText
                    text.rawString shouldBe baseText
                }
            }
        }

        context("more complex text object") {
            val additionalText = " and more..."

            baseTexts.forEach { baseText ->
                test("should work with '$baseText'") {
                    val text = literalText(baseText) {
                        color = 0x2FFF53
                        bold = true
                        text(additionalText) {
                            italic = true
                        }
                    }

                    text.string shouldBe "$baseText$additionalText"
                    text.rawString shouldBe baseText
                }
            }
        }
    }

    context("style of built text should be correct") {
        val redColor = 0xFF332C
        val blueColor = 0x64FDFF

        test("simple red colored and bold text") {
            val text = literalText("base text") {
                color = redColor
                bold = true
            }

            text.style.color?.rgb shouldBe redColor
            text.style.isBold shouldBe true
        }

        context("complex styled text") {
            val nestedSubString = "a nested sub text"

            val text = literalText("base text") {
                color = redColor
                bold = true
                text("italic sub text") {
                    italic = true
                }
                text("underlined second sub text") {
                    underline = true
                    color = blueColor
                }
                text("sub text without inheritance", inheritStyle = false) {
                    italic = true
                }
                text("sub text without inheritance and new color", inheritStyle = false) {
                    color = blueColor
                }
                text("sub text with another sub text") {
                    text(nestedSubString)
                }
            }

            val siblings = text.siblings.first().siblings

            test("basic style correctness") {
                siblings.size shouldBe 5

                siblings[0].style.isItalic shouldBe true
                siblings[1].style.isUnderlined shouldBe true
                siblings[2].style.isItalic shouldBe true
            }

            test("check inheritance") {
                siblings[0].style.isBold shouldBe true
            }
            test("check inheritance override") {
                siblings[1].style.color?.rgb shouldBe blueColor
            }
            test("check disabled inheritance") {
                siblings[2].style.isBold shouldBe false
                siblings[2].style.color?.rgb shouldNotBe redColor
            }
            test("check disabled inheritance and new color") {
                siblings[3].style.isBold shouldBe false
                siblings[3].style.color?.rgb shouldBe blueColor
            }

            test("nested sub texts should work") {
                siblings[4].siblings.size shouldBe 1
                siblings[4].siblings.first().siblings.size shouldBe 1

                val nestedSubText = siblings[4].siblings.first().siblings.first()
                nestedSubText.asString() shouldBe nestedSubString

                nestedSubText.style.isBold shouldBe true
                nestedSubText.style.color?.rgb shouldBe redColor
            }
        }
    }
})
