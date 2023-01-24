package kr.springboot.tradingview;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;

import java.awt.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

public class RobotTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    @SneakyThrows
    public static void main(String[] args) {

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("https://api.binance.com/api/v3/ticker/price"))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        List<BinanceResponse> binanceResponseList = objectMapper.readValue(response.body(), new TypeReference<List<BinanceResponse>>() {
                })
                .parallelStream()
                .filter(binanceResponse -> binanceResponse.getSymbol().substring(binanceResponse.getSymbol().length() - 4).equals("USDT"))
                .collect(Collectors.toList());

        Robot robot = new Robot();

        //5초 딜레이
        robot.delay(3000);

        binanceResponseList.forEach(binanceResponse -> {

            String[] symbols = (binanceResponse.getSymbol() + "PERP").split("");
            Arrays.stream(symbols).forEach(key -> {

                robot.keyPress(KeyCode.findKeyCode(key));

            });

            robot.keyPress(KeyCode.findKeyCode("ENTER"));

            robot.delay(200);

        });

    }

    @Getter
    @AllArgsConstructor
    public enum KeyCode {

        A("A", 0x41),
        B("B", 0x42),
        C("C", 0x43),
        D("D", 0x44),
        E("E", 0x45),
        F("F", 0x46),
        G("G", 0x47),
        H("H", 0x48),
        I("I", 0x49),
        J("J", 0x4A),
        K("K", 0x4B),
        L("L", 0x4C),
        M("M", 0x4D),
        N("N", 0x4E),
        O("O", 0x4F),
        P("P", 0x50),
        Q("Q", 0x51),
        R("R", 0x52),
        S("S", 0x53),
        T("T", 0x54),
        U("U", 0x55),
        V("V", 0x56),
        W("W", 0x57),
        X("X", 0x58),
        Y("Y", 0x59),
        Z("Z", 0x5A),
        ENTER("ENTER", '\n'),

        ;

        private String name;
        private int value;

        public static int findKeyCode(String name) {
            return EnumSet.allOf(KeyCode.class).stream()
                    .filter(keyCode -> keyCode.getName().equals(name))
                    .map(KeyCode::getValue)
                    .findAny()
                    .orElse(E.getValue());
        }

    }

}
