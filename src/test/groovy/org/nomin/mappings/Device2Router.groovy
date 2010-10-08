package org.nomin.mappings

import java.text.SimpleDateFormat
import org.nomin.Mapping
import org.nomin.entity.Device
import org.nomin.entity.Router
import org.nomin.integration.MappingExpressionsTest

/**
 * Just a mapping.
 * @author Dmitry Dobrynin
 * Created: 02.05.2010 17:09:00
 */
class Device2Router extends Mapping {
  protected void build() {
    handle throwables: Exception, handler: {
      MappingExpressionsTest.counter -= 1
    }

    before {
      if (b.vendor || b.model || b.software || b.portCount || b.supportedProtocol || b.portNames || b.portModels || b.importDate || b.frequencies)
        throw new RuntimeException("b should not be mapped yet!")
      MappingExpressionsTest.isBeforeCalled = true
      throw new Exception("Just a checked exception in before hook!")
    }

    after {
      if (!(b.vendor && b.model && b.software && b.portCount && b.supportedProtocol && b.portNames && b.portModels && b.importDate && b.frequencies))
        throw new RuntimeException("b should be mapped!")
      MappingExpressionsTest.isAfterCalled = true
      throw new Exception("Just a checked exception in after hook!")
    }

    mappingFor a: Device, b: Router
    b.vendor = a.model.split("\\s+")[0]
    b.model = a.model.split("\\s+")[1]
    b.software = a.model.split("\\s+")[2]
    b.portCount = { a.integrated?.size() }
    b.supportedProtocol = { upperCase(deviceResolver.resolveProtocol(b.model)) }
    b.portNames = { a.integrated?.inject("") {x, y -> x + ";" + y.name }?.substring(1) }
    b.portModels = { a.integrated?.collect { it.model }}
    b.importDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date())
    b.frequencies[0] = { 3 + 2 }
  }
}
