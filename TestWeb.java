// camel-k: language=java
// camel-k: dependency=camel-cxf-soap:3.20.1

import javax.xml.stream.events.Namespace;
import org.apache.camel.support.builder.Namespaces;
import org.oorsprong.websamples_countryinfo.CountryInfoService;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.Processor;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.component.cxf.common.message.CxfConstants;

public class TestWeb extends RouteBuilder {
  @Override
  public void configure() throws Exception {
    String WS_URI="cxf://http://webservices.oorsprong.org/websamples.countryinfo/CountryInfoService.wso?dataFormat=PAYLOAD";
    Namespaces web = new Namespaces("web", "http://www.oorsprong.org/websamples.countryinfo");
    getContext().getRegistry().bind("CountryInfoService", new CountryInfoService());
      from("timer:java?period=100000")
        .routeId("java")
        .bean(CountryInfoService.class)
        .setBody()
        .simple("<web:CountryCurrency xmlns:web=\"http://www.oorsprong.org/websamples.countryinfo\"><web:sCountryISOCode>IN</web:sCountryISOCode></web:CountryCurrency>")

        .setHeader(CxfConstants.OPERATION_NAME, constant("CountryCurrency"))
        .setHeader(CxfConstants.OPERATION_NAMESPACE, constant("http://www.oorsprong.org/websamples.countryinfo"))
        .to("cxf:bean://http://webservices.oorsprong.org/websamples.countryinfo/CountryInfoService.wso?serviceClass=org.oorsprong.websamples_countryinfo.CountryInfoService&wsdlURL=wsdl/CountryInfoService.wsdl&dataFormat=PAYLOAD");
  }
}
