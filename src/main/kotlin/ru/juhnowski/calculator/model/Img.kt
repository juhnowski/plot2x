package ru.juhnowski.calculator.model

import javax.xml.bind.annotation.*

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
class Img {
    @XmlAttribute
    var themes: String? = null

    @XmlAttribute
    var src: String? = null

    @XmlAttribute
    var alt: String? = null

    @XmlAttribute
    var width: Int? = null

    @XmlAttribute
    var colorinvertable: Boolean? = null

    @XmlAttribute
    var title: String? = null

    @XmlAttribute
    var type: String? = null

    @XmlAttribute
    var height: Int? = null

    override fun toString(): String {
        return "ClassPojo [themes = $themes, src = $src, alt = $alt, width = $width, colorinvertable = $colorinvertable, title = $title, type = $type, height = $height]"
    }
}
