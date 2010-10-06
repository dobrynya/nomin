package org.nomin.integration

import org.nomin.NominMapper
import org.nomin.core.Nomin
import org.nomin.mappings.Device2Router
import org.junit.*

import org.nomin.entity.Device
import org.nomin.entity.Router

/**
 * Tests mapping expressions and method invocations.
 * @author Dmitry Dobrynin
 * Created: 02.05.2010 16:57:59
 */
@SuppressWarnings("GroovyAssignabilityCheck")
class MappingExpressionsTest {
  static boolean isBeforeCalled = false, isAfterCalled = false
  static int counter = 2;
  
  def deviceResolver = new Object() { public String resolveProtocol(String model) { return "http" }}
  NominMapper mapper = new Nomin([upperCase: { it?.toUpperCase() }, deviceResolver: deviceResolver], Device2Router)

  @Test
  void testMapping() {
    def d = new Device(model: "Vendor Model Software", integrated: [new Device(name: "port1", model: "Model1"),
            new Device(name: "port2", model: "Model2")])
    Router r = mapper.map(d, Router)
    assert r
    assert r.vendor == "Vendor" && r.model == "Model" && r.software == "Software"
    assert r.portCount == 2 && r.portNames == "port1;port2" && r.supportedProtocol == "HTTP"
    assert r.portModels && r.portModels.size() == 2 && r.portModels.containsAll(["Model1", "Model2"])
    assert r.frequencies && r.frequencies.size() == 1 && r.frequencies[0] == 5
    // check whether hooks were called during mapping
    assert isBeforeCalled && isAfterCalled
    assert MappingExpressionsTest.counter == 0
  }
}
