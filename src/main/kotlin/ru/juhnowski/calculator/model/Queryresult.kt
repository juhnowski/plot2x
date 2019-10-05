package ru.juhnowski.calculator.model

import javax.xml.bind.annotation.*

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
class Queryresult {
    @XmlAttribute
    var server: Int? = null

    @XmlAttribute
    var timedout: String? = null

    @XmlElement
    var pod: Array<Pod>? = null

    @XmlAttribute
    var timing: String? = null

    @XmlAttribute
    var parsetimedout: Boolean? = null

    @XmlAttribute
    var error: Boolean? = null

    @XmlAttribute
    var version: String? = null

    @XmlAttribute
    var datatypes: String? = null

    @XmlAttribute
    var related: String? = null

    @XmlAttribute
    var success: Boolean? = null

    @XmlAttribute
    var host: String? = null

    @XmlAttribute
    var parsetiming: String? = null

    @XmlAttribute
    var id: String? = null

    @XmlAttribute
    var numpods: Int? = null

    @XmlAttribute
    var timedoutpods: String? = null

    @XmlAttribute
    var recalculate: String? = null

    override fun toString(): String {
        return "ClassPojo [server = $server, timedout = $timedout, pod = $pod, timing = $timing, parsetimedout = $parsetimedout, error = $error, version = $version, datatypes = $datatypes, related = $related, success = $success, host = $host, parsetiming = $parsetiming, id = $id, numpods = $numpods, timedoutpods = $timedoutpods, recalculate = $recalculate]"
    }
}
