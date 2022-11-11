/**
 * Configures Logback.
 * @author Dmitry Dobrynin
 * Date: 24.11.2010 Time: 14:14:23
 */
import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.ConsoleAppender
import static ch.qos.logback.classic.Level.*

appender("STDOUT", ConsoleAppender) {
  encoder(PatternLayoutEncoder) {
    pattern = "%d{HH:mm:ss.SSS} %-5level %logger{15} - %msg%n"
  }
}

root(INFO, ["STDOUT"])
logger("org.nomin", INFO)
