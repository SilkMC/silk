package net.silkmc.silk.core.test

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import net.silkmc.silk.core.text.literalText

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

            text.style.color?.value shouldBe redColor
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

            val siblings = text.siblings

            println(text.toString())

            test("basic style correctness") {
                siblings.size shouldBe 6

                siblings[1].style.isItalic shouldBe true
                siblings[2].style.isUnderlined shouldBe true
                siblings[3].style.isItalic shouldBe true
            }
            test("check inheritance override") {
                siblings[2].style.color?.value shouldBe blueColor
            }
            test("check disabled inheritance") {
                siblings[3].style.isBold shouldBe false
                siblings[3].style.color?.value shouldNotBe redColor
            }
            test("check disabled inheritance and new color") {
                siblings[4].style.isBold shouldBe false
                siblings[4].style.color?.value shouldBe blueColor
            }

            test("nested sub texts should work") {
                siblings[5].siblings.size shouldBe 2
                siblings[5].siblings.first().siblings.size shouldBe 0

                val nestedSubText = siblings[5].siblings[1]
                nestedSubText.string shouldBe nestedSubString
            }
        }
    }
})
