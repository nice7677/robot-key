package kr.springboot.tradingview;

import lombok.Data;

@Data
public class BinanceResponse {

    private String symbol;
    private String price;

}