package thirdstage.eth.disassembly;

import java.util.List;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

@Configuration
public class EthereumClientConfig{


  final private Logger logger = LoggerFactory.getLogger(this.getClass());

  private String jsonRpcEndpoint;

  private String netVersion;


  @Bean
  public Web3j web3j(
    @Value("${ethereum.client.protocol}") @NotBlank final String protocol,
    @Value("${ethereum.client.host}") @NotBlank final String host,
    @Value("${ethereum.client.port:0}") @PositiveOrZero final int port,
    @Value("${ethereum.client.net-ver:*}") @NotBlank final String netVer){

    return this.buildWeb3j(protocol, host, port, netVer, true);
  }


  @Nonnull
  private Web3j buildWeb3j(@NotBlank final String protocol,
      @NotBlank final String host,
      @PositiveOrZero final int port,
      @NotBlank final String netVer,
      final boolean includeRawResponse) {
    Validate.isTrue(StringUtils.isNotBlank(protocol), "The protocol for the Ethereum JSON-PRC to access should be specified.");
    Validate.isTrue(StringUtils.isNotEmpty(host), "...");
    Validate.isTrue(port >= 0, "...");

    this.jsonRpcEndpoint = String.format("%s://%s%s", protocol, host, (port != 0) ? ":" + port : "");
    this.logger.info("Ethereum client JSON-RPC endpoint: {}", jsonRpcEndpoint);

    Web3j web3j = null;
    try {
      HttpService httpService = new HttpService(this.jsonRpcEndpoint, includeRawResponse);

      web3j = Web3j.build(httpService);
    }catch(Exception ex) {
      logger.error(String.format("Fail to connect Ethereum client via %s", jsonRpcEndpoint));

      ExceptionUtils.wrapAndThrow(ex);
    }

    try {
      this.netVersion = web3j.netVersion().send().getNetVersion();
    }catch(Exception ex) {
      logger.error("Fail to get network ID of the specified Ethereum network.");

      ExceptionUtils.wrapAndThrow(ex);
    }

    if(!"*".equals(netVer) && !StringUtils.equals(netVer, this.netVersion)) {
      throw new IllegalStateException(
          String.format("The network ID of the connected Ethereum network is different from the specified one. - %s, %s", netVer, this.netVersion));
    }else {
      logger.info("The network ID of the connected Ethereum network : {}", this.netVersion);
    }

    BigInteger latest = null;
    try {
      latest = web3j.ethBlockNumber().send().getBlockNumber();

      logger.info(String.format("The latest block number : %,d", latest));
    }catch(Throwable th) {
      logger.warn("Fail to get the latest block number from the connected Ethereum network.", th);
    }

    return web3j;
  }

}
