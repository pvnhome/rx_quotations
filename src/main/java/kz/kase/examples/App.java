package kz.kase.examples;

import static kz.kase.iris.utils.IrisApiUtils.fromDecimal;
import static kz.kase.iris.utils.IrisApiUtils.toLocalDateTime;

import kz.bips.comps.utils.DateUtils;
import kz.bips.comps.utils.ExtendedPreferencesHocon;
import kz.bips.comps.utils.Log4JLoggerWrapper;

import kz.kase.iris.client.connectors.paho.PahoConnector;
import kz.kase.iris.client.rx.IrisRxClient;
import kz.kase.iris.model.IrisApiBase.MarketSector;
import kz.kase.iris.model.IrisApiBase.Ohlc;
import kz.kase.iris.model.IrisApiDeals.DealsBaseFilter;
import kz.kase.iris.model.IrisApiSecs.InstrumentsFilter;
import kz.kase.iris.model.IrisApiTotals.QuotationsRequest;
import kz.kase.iris.model.IrisApiTotals.Total;

/**
 * <p>Пример клиентского приложения для получения котировок.</p>
 * <p><b>Created:</b> 19.07.2022 19:21:39</p>
 * @author victor
 */
public class App {
   protected static final Log4JLoggerWrapper log = new Log4JLoggerWrapper(App.class);

   public static void main(String[] args) {
      ExtendedPreferencesHocon props = AppProps.getProps();
      props.initLog4j();

      long start = log.time("start");

      InstrumentsFilter instrumentsFilter = InstrumentsFilter.newBuilder().addCodes("CCBN").addCodes("HSBK").addCodes("KZTK").addIds(2530).addIds(6701).addIds(7978).addIds(11056).build();
      DealsBaseFilter dealsFilter = DealsBaseFilter.newBuilder().setInstrumentsFilter(instrumentsFilter).addMarketSector(MarketSector.SECONDARY).build();
      QuotationsRequest request = QuotationsRequest.newBuilder().setFilter(dealsFilter).build();

      log.debug(request.toString());

      try (IrisRxClient client = new IrisRxClient(new PahoConnector(props))) {
         client.getQuotationsTopic().send(request).blockingSubscribe(reply -> {
            log.debug("OK");
            log.debug(reply.toString());
            reply.getTotalsList().forEach(App::printTotal);
         }, error -> {
            log.logStackTrace(error, "Quotations request");
         });
      } catch (Exception e) {
         log.logStackTrace(e, "main");
      }

      log.time("end", start);
   }

   private static void printTotal(Total total) {
      try {
         Ohlc price = total.getPrice();
         String ct = price.hasCloseTime() ? toLocalDateTime(price.getCloseTime()).format(DateUtils.DDMMYYYY_TIME) : "N/A";
         String o = price.hasOpen() ? fromDecimal(price.getOpen()).toPlainString() : "N/A";
         String c = price.hasClose() ? fromDecimal(price.getClose()).toPlainString() : "N/A";
         String l = price.hasLow() ? fromDecimal(price.getLow()).toPlainString() : "N/A";
         String h = price.hasHigh() ? fromDecimal(price.getHigh()).toPlainString() : "N/A";
         log.info("%s (%s): cnt=%d, open=%s, close=%s, low=%s, high=%s", total.getInstrumentCode(), ct, total.getDealCount(), o, c, l, h);
      } catch (Exception e) {
         log.logStackTrace(e, "printTotal");
      }
   }
}
