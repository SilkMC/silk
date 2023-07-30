package net.silkmc.silk.core.chat;

import net.minecraft.network.chat.TextColor;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class Colors {

    // DEFAULT COLORS

    /**
     * #0000AA
     * <div style="border:1px solid black;width:120px;height:120px;background-color:#0000AA"></div>
     */
    public static final TextColor DARKBLUE = fromRGB(0, 0, 170);
    /**
     * #00AA00
     * <div style="border:1px solid black;width:120px;height:120px;background-color:#00AA00"></div>
     */
    public static final TextColor DARKGREEN = fromRGB(0, 170, 0);
    /**
     * #00AAAA
     * <div style="border:1px solid black;width:120px;height:120px;background-color:#00AAAA"></div>
     */
    public static final TextColor DARKAQUA = fromRGB(0, 170, 170);
    /**
     * #AA0000
     * <div style="border:1px solid black;width:120px;height:120px;background-color:#AA0000"></div>
     */
    public static final TextColor DARKRED = fromRGB(170, 0, 0);
    /**
     * #AA00AA
     * <div style="border:1px solid black;width:120px;height:120px;background-color:#AA00AA"></div>
     */
    public static final TextColor DARKPURPLE = fromRGB(170, 0, 170);
    /**
     * #FFAA00
     * <div style="border:1px solid black;width:120px;height:120px;background-color:#FFAA00"></div>
     */
    public static final TextColor GOLD = fromRGB(255, 170, 0);
    /**
     * #AAAAAA
     * <div style="border:1px solid black;width:120px;height:120px;background-color:#AAAAAA"></div>
     */
    public static final TextColor GRAY = fromRGB(170, 170, 170);
    /**
     * #555555
     * <div style="border:1px solid black;width:120px;height:120px;background-color:#555555"></div>
     */
    public static final TextColor DARKGRAY = fromRGB(85, 85, 85);
    /**
     * #5555FF
     * <div style="border:1px solid black;width:120px;height:120px;background-color:#5555FF"></div>
     */
    public static final TextColor BLUE = fromRGB(85, 85, 255);
    /**
     * #55FF55
     * <div style="border:1px solid black;width:120px;height:120px;background-color:#55FF55"></div>
     */
    public static final TextColor GREEN = fromRGB(85, 255, 85);
    /**
     * #55FFFF
     * <div style="border:1px solid black;width:120px;height:120px;background-color:#55FFFF"></div>
     */
    public static final TextColor AQUA = fromRGB(85, 255, 255);
    /**
     * #FF5555
     * <div style="border:1px solid black;width:120px;height:120px;background-color:#FF5555"></div>
     */
    public static final TextColor RED = fromRGB(255, 85, 85);
    /**
     * #FF55FF
     * <div style="border:1px solid black;width:120px;height:120px;background-color:#FF55FF"></div>
     */
    public static final TextColor LIGHTPURPLE = fromRGB(255, 85, 255);
    /**
     * #FFFF55
     * <div style="border:1px solid black;width:120px;height:120px;background-color:#FFFF55"></div>
     */
    public static final TextColor YELLOW = fromRGB(255, 255, 85);
    /**
     * #FFFFFF
     * <div style="border:1px solid black;width:120px;height:120px;background-color:#FFFFFF"></div>
     */
    public static final TextColor WHITE = fromRGB(255, 255, 255);

    // ADDITIONAL COLORS

    /**
     * #F0F8FF
     * <div style="width:120px;height:120px;background-color:#F0F8FF"></div>
     */
    public static final TextColor ALICEBLUE = fromRGB(240, 248, 255);
    /**
     * #FAEBD7
     * <div style="width:120px;height:120px;background-color:#FAEBD7"></div>
     */
    public static final TextColor ANTIQUEWHITE = fromRGB(250, 235, 215);
    /**
     * #7FFFD4
     * <div style="width:120px;height:120px;background-color:#7FFFD4"></div>
     */
    public static final TextColor AQUAMARINE = fromRGB(127, 255, 212);
    /**
     * #F0FFFF
     * <div style="width:120px;height:120px;background-color:#F0FFFF"></div>
     */
    public static final TextColor AZURE = fromRGB(240, 255, 255);
    /**
     * #F5F5DC
     * <div style="width:120px;height:120px;background-color:#F5F5DC"></div>
     */
    public static final TextColor BEIGE = fromRGB(245, 245, 220);
    /**
     * #FFE4C4
     * <div style="width:120px;height:120px;background-color:#FFE4C4"></div>
     */
    public static final TextColor BISQUE = fromRGB(255, 228, 196);
    /**
     * #000000
     * <div style="width:120px;height:120px;background-color:#000000"></div>
     */
    public static final TextColor BLACK = fromRGB(0, 0, 0);
    /**
     * #FFEBCD
     * <div style="width:120px;height:120px;background-color:#FFEBCD"></div>
     */
    public static final TextColor BLANCHEDALMOND = fromRGB(255, 235, 205);
    /**
     * #8A2BE2
     * <div style="width:120px;height:120px;background-color:#8A2BE2"></div>
     */
    public static final TextColor BLUEVIOLET = fromRGB(138, 43, 226);
    /**
     * #A52A2A
     * <div style="width:120px;height:120px;background-color:#A52A2A"></div>
     */
    public static final TextColor BROWN = fromRGB(165, 42, 42);
    /**
     * #DEB887
     * <div style="width:120px;height:120px;background-color:#DEB887"></div>
     */
    public static final TextColor BURLYWOOD = fromRGB(222, 184, 135);
    /**
     * #5F9EA0
     * <div style="width:120px;height:120px;background-color:#5F9EA0"></div>
     */
    public static final TextColor CADETBLUE = fromRGB(95, 158, 160);
    /**
     * #7FFF00
     * <div style="width:120px;height:120px;background-color:#7FFF00"></div>
     */
    public static final TextColor CHARTREUSE = fromRGB(127, 255, 0);
    /**
     * #D2691E
     * <div style="width:120px;height:120px;background-color:#D2691E"></div>
     */
    public static final TextColor CHOCOLATE = fromRGB(210, 105, 30);
    /**
     * #FF7F50
     * <div style="width:120px;height:120px;background-color:#FF7F50"></div>
     */
    public static final TextColor CORAL = fromRGB(255, 127, 80);
    /**
     * #6495ED
     * <div style="width:120px;height:120px;background-color:#6495ED"></div>
     */
    public static final TextColor CORNFLOWERBLUE = fromRGB(100, 149, 237);
    /**
     * #FFF8DC
     * <div style="width:120px;height:120px;background-color:#FFF8DC"></div>
     */
    public static final TextColor CORNSILK = fromRGB(255, 248, 220);
    /**
     * #DC143C
     * <div style="width:120px;height:120px;background-color:#DC143C"></div>
     */
    public static final TextColor CRIMSON = fromRGB(220, 20, 60);
    /**
     * #00FFFF
     * <div style="width:120px;height:120px;background-color:#00FFFF"></div>
     */
    public static final TextColor CYAN = fromRGB(0, 255, 255);
    /**
     * #008B8B
     * <div style="width:120px;height:120px;background-color:#008B8B"></div>
     */
    public static final TextColor DARKCYAN = fromRGB(0, 139, 139);
    /**
     * #B8860B
     * <div style="width:120px;height:120px;background-color:#B8860B"></div>
     */
    public static final TextColor DARKGOLDENROD = fromRGB(184, 134, 11);
    /**
     * #BDB76B
     * <div style="width:120px;height:120px;background-color:#BDB76B"></div>
     */
    public static final TextColor DARKKHAKI = fromRGB(189, 183, 107);
    /**
     * #8B008B
     * <div style="width:120px;height:120px;background-color:#8B008B"></div>
     */
    public static final TextColor DARKMAGENTA = fromRGB(139, 0, 139);
    /**
     * #556B2F
     * <div style="width:120px;height:120px;background-color:#556B2F"></div>
     */
    public static final TextColor DARKOLIVEGREEN = fromRGB(85, 107, 47);
    /**
     * #FF8C00
     * <div style="width:120px;height:120px;background-color:#FF8C00"></div>
     */
    public static final TextColor DARKORANGE = fromRGB(255, 140, 0);
    /**
     * #9932CC
     * <div style="width:120px;height:120px;background-color:#9932CC"></div>
     */
    public static final TextColor DARKORCHID = fromRGB(153, 50, 204);
    /**
     * #E9967A
     * <div style="width:120px;height:120px;background-color:#E9967A"></div>
     */
    public static final TextColor DARKSALMON = fromRGB(233, 150, 122);
    /**
     * #8FBC8F
     * <div style="width:120px;height:120px;background-color:#8FBC8F"></div>
     */
    public static final TextColor DARKSEAGREEN = fromRGB(143, 188, 143);
    /**
     * #483D8B
     * <div style="width:120px;height:120px;background-color:#483D8B"></div>
     */
    public static final TextColor DARKSLATEBLUE = fromRGB(72, 61, 139);
    /**
     * #2F4F4F
     * <div style="width:120px;height:120px;background-color:#2F4F4F"></div>
     */
    public static final TextColor DARKSLATEGRAY = fromRGB(47, 79, 79);
    /**
     * #00CED1
     * <div style="width:120px;height:120px;background-color:#00CED1"></div>
     */
    public static final TextColor DARKTURQUOISE = fromRGB(0, 206, 209);
    /**
     * #9400D3
     * <div style="width:120px;height:120px;background-color:#9400D3"></div>
     */
    public static final TextColor DARKVIOLET = fromRGB(148, 0, 211);
    /**
     * #FF1493
     * <div style="width:120px;height:120px;background-color:#FF1493"></div>
     */
    public static final TextColor DEEPPINK = fromRGB(255, 20, 147);
    /**
     * #00BFFF
     * <div style="width:120px;height:120px;background-color:#00BFFF"></div>
     */
    public static final TextColor DEEPSKYBLUE = fromRGB(0, 191, 255);
    /**
     * #696969
     * <div style="width:120px;height:120px;background-color:#696969"></div>
     */
    public static final TextColor DIMGRAY = fromRGB(105, 105, 105);
    /**
     * #1E90FF
     * <div style="width:120px;height:120px;background-color:#1E90FF"></div>
     */
    public static final TextColor DODGERBLUE = fromRGB(30, 144, 255);
    /**
     * #B22222
     * <div style="width:120px;height:120px;background-color:#B22222"></div>
     */
    public static final TextColor FIREBRICK = fromRGB(178, 34, 34);
    /**
     * #FFFAF0
     * <div style="width:120px;height:120px;background-color:#FFFAF0"></div>
     */
    public static final TextColor FLORALWHITE = fromRGB(255, 250, 240);
    /**
     * #228B22
     * <div style="width:120px;height:120px;background-color:#228B22"></div>
     */
    public static final TextColor FORESTGREEN = fromRGB(34, 139, 34);
    /**
     * #FF00FF
     * <div style="width:120px;height:120px;background-color:#FF00FF"></div>
     */
    public static final TextColor FUCHSIA = fromRGB(255, 0, 255);
    /**
     * #DCDCDC
     * <div style="width:120px;height:120px;background-color:#DCDCDC"></div>
     */
    public static final TextColor GAINSBORO = fromRGB(220, 220, 220);
    /**
     * #F8F8FF
     * <div style="width:120px;height:120px;background-color:#F8F8FF"></div>
     */
    public static final TextColor GHOSTWHITE = fromRGB(248, 248, 255);
    /**
     * #DAA520
     * <div style="width:120px;height:120px;background-color:#DAA520"></div>
     */
    public static final TextColor GOLDENROD = fromRGB(218, 165, 32);
    /**
     * #ADFF2F
     * <div style="width:120px;height:120px;background-color:#ADFF2F"></div>
     */
    public static final TextColor GREENYELLOW = fromRGB(173, 255, 47);
    /**
     * #F0FFF0
     * <div style="width:120px;height:120px;background-color:#F0FFF0"></div>
     */
    public static final TextColor HONEYDEW = fromRGB(240, 255, 240);
    /**
     * #FF69B4
     * <div style="width:120px;height:120px;background-color:#FF69B4"></div>
     */
    public static final TextColor HOTPINK = fromRGB(255, 105, 180);
    /**
     * #CD5C5C
     * <div style="width:120px;height:120px;background-color:#CD5C5C"></div>
     */
    public static final TextColor INDIANRED = fromRGB(205, 92, 92);
    /**
     * #4B0082
     * <div style="width:120px;height:120px;background-color:#4B0082"></div>
     */
    public static final TextColor INDIGO = fromRGB(75, 0, 130);
    /**
     * #FFFFF0
     * <div style="width:120px;height:120px;background-color:#FFFFF0"></div>
     */
    public static final TextColor IVORY = fromRGB(255, 255, 240);
    /**
     * #F0E68C
     * <div style="width:120px;height:120px;background-color:#F0E68C"></div>
     */
    public static final TextColor KHAKI = fromRGB(240, 230, 140);
    /**
     * #E6E6FA
     * <div style="width:120px;height:120px;background-color:#E6E6FA"></div>
     */
    public static final TextColor LAVENDER = fromRGB(230, 230, 250);
    /**
     * #FFF0F5
     * <div style="width:120px;height:120px;background-color:#FFF0F5"></div>
     */
    public static final TextColor LAVENDERBLUSH = fromRGB(255, 240, 245);
    /**
     * #7CFC00
     * <div style="width:120px;height:120px;background-color:#7CFC00"></div>
     */
    public static final TextColor LAWNGREEN = fromRGB(124, 252, 0);
    /**
     * #FFFACD
     * <div style="width:120px;height:120px;background-color:#FFFACD"></div>
     */
    public static final TextColor LEMONCHIFFON = fromRGB(255, 250, 205);
    /**
     * #ADD8E6
     * <div style="width:120px;height:120px;background-color:#ADD8E6"></div>
     */
    public static final TextColor LIGHTBLUE = fromRGB(173, 216, 230);
    /**
     * #F08080
     * <div style="width:120px;height:120px;background-color:#F08080"></div>
     */
    public static final TextColor LIGHTCORAL = fromRGB(240, 128, 128);
    /**
     * #E0FFFF
     * <div style="width:120px;height:120px;background-color:#E0FFFF"></div>
     */
    public static final TextColor LIGHTCYAN = fromRGB(224, 255, 255);
    /**
     * #FAFAD2
     * <div style="width:120px;height:120px;background-color:#FAFAD2"></div>
     */
    public static final TextColor LIGHTGOLDENRODYELLOW = fromRGB(250, 250, 210);
    /**
     * #D3D3D3
     * <div style="width:120px;height:120px;background-color:#D3D3D3"></div>
     */
    public static final TextColor LIGHTGRAY = fromRGB(211, 211, 211);
    /**
     * #90EE90
     * <div style="width:120px;height:120px;background-color:#90EE90"></div>
     */
    public static final TextColor LIGHTGREEN = fromRGB(144, 238, 144);
    /**
     * #FFB6C1
     * <div style="width:120px;height:120px;background-color:#FFB6C1"></div>
     */
    public static final TextColor LIGHTPINK = fromRGB(255, 182, 193);
    /**
     * #FFA07A
     * <div style="width:120px;height:120px;background-color:#FFA07A"></div>
     */
    public static final TextColor LIGHTSALMON = fromRGB(255, 160, 122);
    /**
     * #20B2AA
     * <div style="width:120px;height:120px;background-color:#20B2AA"></div>
     */
    public static final TextColor LIGHTSEAGREEN = fromRGB(32, 178, 170);
    /**
     * #87CEFA
     * <div style="width:120px;height:120px;background-color:#87CEFA"></div>
     */
    public static final TextColor LIGHTSKYBLUE = fromRGB(135, 206, 250);
    /**
     * #778899
     * <div style="width:120px;height:120px;background-color:#778899"></div>
     */
    public static final TextColor LIGHTSLATEGRAY = fromRGB(119, 136, 153);
    /**
     * #B0C4DE
     * <div style="width:120px;height:120px;background-color:#B0C4DE"></div>
     */
    public static final TextColor LIGHTSTEELBLUE = fromRGB(176, 196, 222);
    /**
     * #FFFFE0
     * <div style="width:120px;height:120px;background-color:#FFFFE0"></div>
     */
    public static final TextColor LIGHTYELLOW = fromRGB(255, 255, 224);
    /**
     * #00FF00
     * <div style="width:120px;height:120px;background-color:#00FF00"></div>
     */
    public static final TextColor LIME = fromRGB(0, 255, 0);
    /**
     * #32CD32
     * <div style="width:120px;height:120px;background-color:#32CD32"></div>
     */
    public static final TextColor LIMEGREEN = fromRGB(50, 205, 50);
    /**
     * #FAF0E6
     * <div style="width:120px;height:120px;background-color:#FAF0E6"></div>
     */
    public static final TextColor LINEN = fromRGB(250, 240, 230);
    /**
     * #FF00FF
     * <div style="width:120px;height:120px;background-color:#FF00FF"></div>
     */
    public static final TextColor MAGENTA = fromRGB(255, 0, 255);
    /**
     * #800000
     * <div style="width:120px;height:120px;background-color:#800000"></div>
     */
    public static final TextColor MAROON = fromRGB(128, 0, 0);
    /**
     * #66CDAA
     * <div style="width:120px;height:120px;background-color:#66CDAA"></div>
     */
    public static final TextColor MEDIUMAQUAMARINE = fromRGB(102, 205, 170);
    /**
     * #0000CD
     * <div style="width:120px;height:120px;background-color:#0000CD"></div>
     */
    public static final TextColor MEDIUMBLUE = fromRGB(0, 0, 205);
    /**
     * #BA55D3
     * <div style="width:120px;height:120px;background-color:#BA55D3"></div>
     */
    public static final TextColor MEDIUMORCHID = fromRGB(186, 85, 211);
    /**
     * #9370DB
     * <div style="width:120px;height:120px;background-color:#9370DB"></div>
     */
    public static final TextColor MEDIUMPURPLE = fromRGB(147, 112, 219);
    /**
     * #3CB371
     * <div style="width:120px;height:120px;background-color:#3CB371"></div>
     */
    public static final TextColor MEDIUMSEAGREEN = fromRGB(60, 179, 113);
    /**
     * #7B68EE
     * <div style="width:120px;height:120px;background-color:#7B68EE"></div>
     */
    public static final TextColor MEDIUMSLATEBLUE = fromRGB(123, 104, 238);
    /**
     * #00FA9A
     * <div style="width:120px;height:120px;background-color:#00FA9A"></div>
     */
    public static final TextColor MEDIUMSPRINGGREEN = fromRGB(0, 250, 154);
    /**
     * #48D1CC
     * <div style="width:120px;height:120px;background-color:#48D1CC"></div>
     */
    public static final TextColor MEDIUMTURQUOISE = fromRGB(72, 209, 204);
    /**
     * #C71585
     * <div style="width:120px;height:120px;background-color:#C71585"></div>
     */
    public static final TextColor MEDIUMVIOLETRED = fromRGB(199, 21, 133);
    /**
     * #191970
     * <div style="width:120px;height:120px;background-color:#191970"></div>
     */
    public static final TextColor MIDNIGHTBLUE = fromRGB(25, 25, 112);
    /**
     * #F5FFFA
     * <div style="width:120px;height:120px;background-color:#F5FFFA"></div>
     */
    public static final TextColor MINTCREAM = fromRGB(245, 255, 250);
    /**
     * #FFE4E1
     * <div style="width:120px;height:120px;background-color:#FFE4E1"></div>
     */
    public static final TextColor MISTYROSE = fromRGB(255, 228, 225);
    /**
     * #FFE4B5
     * <div style="width:120px;height:120px;background-color:#FFE4B5"></div>
     */
    public static final TextColor MOCCASIN = fromRGB(255, 228, 181);
    /**
     * #FFDEAD
     * <div style="width:120px;height:120px;background-color:#FFDEAD"></div>
     */
    public static final TextColor NAVAJOWHITE = fromRGB(255, 222, 173);
    /**
     * #000080
     * <div style="width:120px;height:120px;background-color:#000080"></div>
     */
    public static final TextColor NAVY = fromRGB(0, 0, 128);
    /**
     * #FDF5E6
     * <div style="width:120px;height:120px;background-color:#FDF5E6"></div>
     */
    public static final TextColor OLDLACE = fromRGB(253, 245, 230);
    /**
     * #808000
     * <div style="width:120px;height:120px;background-color:#808000"></div>
     */
    public static final TextColor OLIVE = fromRGB(128, 128, 0);
    /**
     * #6B8E23
     * <div style="width:120px;height:120px;background-color:#6B8E23"></div>
     */
    public static final TextColor OLIVEDRAB = fromRGB(107, 142, 35);
    /**
     * #FFA500
     * <div style="width:120px;height:120px;background-color:#FFA500"></div>
     */
    public static final TextColor ORANGE = fromRGB(255, 165, 0);
    /**
     * #FF4500
     * <div style="width:120px;height:120px;background-color:#FF4500"></div>
     */
    public static final TextColor ORANGERED = fromRGB(255, 69, 0);
    /**
     * #DA70D6
     * <div style="width:120px;height:120px;background-color:#DA70D6"></div>
     */
    public static final TextColor ORCHID = fromRGB(218, 112, 214);
    /**
     * #EEE8AA
     * <div style="width:120px;height:120px;background-color:#EEE8AA"></div>
     */
    public static final TextColor PALEGOLDENROD = fromRGB(238, 232, 170);
    /**
     * #98FB98
     * <div style="width:120px;height:120px;background-color:#98FB98"></div>
     */
    public static final TextColor PALEGREEN = fromRGB(152, 251, 152);
    /**
     * #AFEEEE
     * <div style="width:120px;height:120px;background-color:#AFEEEE"></div>
     */
    public static final TextColor PALETURQUOISE = fromRGB(175, 238, 238);
    /**
     * #DB7093
     * <div style="width:120px;height:120px;background-color:#DB7093"></div>
     */
    public static final TextColor PALEVIOLETRED = fromRGB(219, 112, 147);
    /**
     * #FFEFD5
     * <div style="width:120px;height:120px;background-color:#FFEFD5"></div>
     */
    public static final TextColor PAPAYAWHIP = fromRGB(255, 239, 213);
    /**
     * #FFDAB9
     * <div style="width:120px;height:120px;background-color:#FFDAB9"></div>
     */
    public static final TextColor PEACHPUFF = fromRGB(255, 218, 185);
    /**
     * #CD853F
     * <div style="width:120px;height:120px;background-color:#CD853F"></div>
     */
    public static final TextColor PERU = fromRGB(205, 133, 63);
    /**
     * #FFC0CB
     * <div style="width:120px;height:120px;background-color:#FFC0CB"></div>
     */
    public static final TextColor PINK = fromRGB(255, 192, 203);
    /**
     * #DDA0DD
     * <div style="width:120px;height:120px;background-color:#DDA0DD"></div>
     */
    public static final TextColor PLUM = fromRGB(221, 160, 221);
    /**
     * #B0E0E6
     * <div style="width:120px;height:120px;background-color:#B0E0E6"></div>
     */
    public static final TextColor POWDERBLUE = fromRGB(176, 224, 230);
    /**
     * #800080
     * <div style="width:120px;height:120px;background-color:#800080"></div>
     */
    public static final TextColor PURPLE = fromRGB(128, 0, 128);
    /**
     * #BC8F8F
     * <div style="width:120px;height:120px;background-color:#BC8F8F"></div>
     */
    public static final TextColor ROSYBROWN = fromRGB(188, 143, 143);
    /**
     * #4169E1
     * <div style="width:120px;height:120px;background-color:#4169E1"></div>
     */
    public static final TextColor ROYALBLUE = fromRGB(65, 105, 225);
    /**
     * #8B4513
     * <div style="width:120px;height:120px;background-color:#8B4513"></div>
     */
    public static final TextColor SADDLEBROWN = fromRGB(139, 69, 19);
    /**
     * #FA8072
     * <div style="width:120px;height:120px;background-color:#FA8072"></div>
     */
    public static final TextColor SALMON = fromRGB(250, 128, 114);
    /**
     * #F4A460
     * <div style="width:120px;height:120px;background-color:#F4A460"></div>
     */
    public static final TextColor SANDYBROWN = fromRGB(244, 164, 96);
    /**
     * #2E8B57
     * <div style="width:120px;height:120px;background-color:#2E8B57"></div>
     */
    public static final TextColor SEAGREEN = fromRGB(46, 139, 87);
    /**
     * #FFF5EE
     * <div style="width:120px;height:120px;background-color:#FFF5EE"></div>
     */
    public static final TextColor SEASHELL = fromRGB(255, 245, 238);
    /**
     * #A0522D
     * <div style="width:120px;height:120px;background-color:#A0522D"></div>
     */
    public static final TextColor SIENNA = fromRGB(160, 82, 45);
    /**
     * #C0C0C0
     * <div style="width:120px;height:120px;background-color:#C0C0C0"></div>
     */
    public static final TextColor SILVER = fromRGB(192, 192, 192);
    /**
     * #87CEEB
     * <div style="width:120px;height:120px;background-color:#87CEEB"></div>
     */
    public static final TextColor SKYBLUE = fromRGB(135, 206, 235);
    /**
     * #6A5ACD
     * <div style="width:120px;height:120px;background-color:#6A5ACD"></div>
     */
    public static final TextColor SLATEBLUE = fromRGB(106, 90, 205);
    /**
     * #708090
     * <div style="width:120px;height:120px;background-color:#708090"></div>
     */
    public static final TextColor SLATEGRAY = fromRGB(112, 128, 144);
    /**
     * #FFFAFA
     * <div style="width:120px;height:120px;background-color:#FFFAFA"></div>
     */
    public static final TextColor SNOW = fromRGB(255, 250, 250);
    /**
     * #00FF7F
     * <div style="width:120px;height:120px;background-color:#00FF7F"></div>
     */
    public static final TextColor SPRINGGREEN = fromRGB(0, 255, 127);
    /**
     * #4682B4
     * <div style="width:120px;height:120px;background-color:#4682B4"></div>
     */
    public static final TextColor STEELBLUE = fromRGB(70, 130, 180);
    /**
     * #D2B48C
     * <div style="width:120px;height:120px;background-color:#D2B48C"></div>
     */
    public static final TextColor TAN = fromRGB(210, 180, 140);
    /**
     * #008080
     * <div style="width:120px;height:120px;background-color:#008080"></div>
     */
    public static final TextColor TEAL = fromRGB(0, 128, 128);
    /**
     * #D8BFD8
     * <div style="width:120px;height:120px;background-color:#D8BFD8"></div>
     */
    public static final TextColor THISTLE = fromRGB(216, 191, 216);
    /**
     * #FF6347
     * <div style="width:120px;height:120px;background-color:#FF6347"></div>
     */
    public static final TextColor TOMATO = fromRGB(255, 99, 71);
    /**
     * #40E0D0
     * <div style="width:120px;height:120px;background-color:#40E0D0"></div>
     */
    public static final TextColor TURQUOISE = fromRGB(64, 224, 208);
    /**
     * #EE82EE
     * <div style="width:120px;height:120px;background-color:#EE82EE"></div>
     */
    public static final TextColor VIOLET = fromRGB(238, 130, 238);
    /**
     * #F5DEB3
     * <div style="width:120px;height:120px;background-color:#F5DEB3"></div>
     */
    public static final TextColor WHEAT = fromRGB(245, 222, 179);
    /**
     * #F5F5F5
     * <div style="width:120px;height:120px;background-color:#F5F5F5"></div>
     */
    public static final TextColor WHITESMOKE = fromRGB(245, 245, 245);
    /**
     * #9ACD32
     * <div style="width:120px;height:120px;background-color:#9ACD32"></div>
     */
    public static final TextColor YELLOWGREEN = fromRGB(154, 205, 50);

    public static List<TextColor> allColors() {
        return Arrays.stream(Colors.class.getFields())
            .filter(field -> field.getType() == TextColor.class)
            .map(field -> {
                try {
                    return (TextColor) field.get(null);
                } catch (IllegalArgumentException | IllegalAccessException ignored) {
                    return null;
                }
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    public static TextColor fromRGB(int r, int g, int b) {
        return TextColor.fromRgb(new Color(r, g, b).getRGB());
    }
}
