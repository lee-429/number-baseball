package com.hyunhak.numberbaseball.controller;

import com.hyunhak.numberbaseball.domain.Game;
import com.hyunhak.numberbaseball.service.GameService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    /**
     * 1. 첫 접속 시 게임 시작
     * - 렌덤 정답 생성
     * - Game 객체 생성
     * - 세션에 저장
     */
    @GetMapping("/")
    public String start(HttpSession session, Model model) {

        String answer = gameService.createAnswer();
        Game game = new Game(answer, 5);

        session.setAttribute("game", game);

        model.addAttribute("history", game.getHistory());
        model.addAttribute("chance", game.getChance());

        return "game";
    }

    /**
     * 2. 숫자 입력 처리
     */
    @PostMapping("/guess")
    public String guess(@RequestParam String input, HttpSession session, Model model) {

        Game game = (Game) session.getAttribute("game");

        // 혹시 세션이 없으면 다시 시작
        if (game == null) {
            return "redirect:/";
        }

        int[] result = gameService.check(game.getAnswer(), input);

        game.decreaseChance();

        // 기록 저장
        String record = input + " -> " + result[0] + "S " + result[1] + "B";
        game.addHistory(record);

        model.addAttribute("history", game.getHistory());
        model.addAttribute("strike", result[0]);
        model.addAttribute("ball", result[1]);
        model.addAttribute("chance", game.getChance());

        // 4 strike면 성공
        if (result[0] == 4) {
            return "success";
        }

        // 기회 소진 시 실패
        if (game.isGameOver()) {
            model.addAttribute("answer", game.getAnswer());
            return "fail";
        }

        return "game";
    }

    // 리셋
    @PostMapping("/reset")
    public String reset(HttpSession session) {

        String answer = gameService.createAnswer();
        Game game = new Game(answer, 5);

        session.setAttribute("game", game);

        return "redirect:/";
    }
}
