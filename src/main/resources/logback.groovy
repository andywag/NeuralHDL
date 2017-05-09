//
// Built on Sat May 06 00:15:52 UTC 2017 by logback-translator
// For more information on configuration files in Groovy
// please see http://logback.qos.ch/manual/groovy.html

// For assistance related to this tool or configuration files
// in general, please contact the logback user mailing list at
//    http://qos.ch/mailman/listinfo/logback-user

// For professional support please see
//   http://www.qos.ch/shop/products/professionalSupport

import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.classic.filter.LevelFilter
import ch.qos.logback.classic.filter.ThresholdFilter
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.rolling.RollingFileAppender
import ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy
import ch.qos.logback.core.status.NopStatusListener



import java.nio.charset.Charset

import static ch.qos.logback.classic.Level.DEBUG
import static ch.qos.logback.core.spi.FilterReply.ACCEPT
import static ch.qos.logback.core.spi.FilterReply.DENY


scan()
statusListener(NopStatusListener)


appender("CONSOLE", ConsoleAppender) {
    filter(ThresholdFilter) {
        level = WARN
    }
    encoder(PatternLayoutEncoder) {
        charset = Charset.forName("UTF-8")
        pattern = "%-5level %logger{35} - %msg%n"
    }

}

appender("FILE", FileAppender) {
    append = false
    file = "tests/logs/output.txt"
    encoder(PatternLayoutEncoder) {
        charset = Charset.forName("UTF-8")
        pattern = "%-5level - %msg%n"
    }
}



//logger("Main", INFO, ["FILE"])

root(ALL,["FILE","CONSOLE"])
//root(ERROR,["CONSOLE"])
