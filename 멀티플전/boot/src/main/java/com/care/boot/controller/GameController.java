package com.care.boot.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.care.boot.dto.GameRequest;
import com.care.boot.dto.GameResult;

@Controller
public class GameController {

    @MessageMapping("/play")
    @SendTo("/topic/result")
    public GameResult play(GameRequest request) throws Exception {
        Thread.sleep(500); // 응답 지연 (테스트용)
        
        String playerMove = request.getMove();
        String serverMove = getRandomMove();
        String result = determineWinner(playerMove, serverMove);

        return new GameResult(playerMove, serverMove, result);
    }

    private String getRandomMove() {
        String[] moves = {"가위", "바위", "보"};
        return moves[(int) (Math.random() * 3)];
    }

    private String determineWinner(String player, String server) {
        if (player.equals(server)) return "무승부";
        if ((player.equals("가위") && server.equals("보")) ||
            (player.equals("바위") && server.equals("가위")) ||
            (player.equals("보") && server.equals("바위"))) {
            return "승리";
        }
        return "패배";
    }
}
