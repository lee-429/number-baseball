package com.hyunhak.numberbaseball.domain;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class Game {


    private final String answer; // 정답 숫자
    private int chance; // 시도 횟수

    // 시도 기록 저장용
    private final List<String> history = new ArrayList<>();

    public Game(String answer, int chance) {
        this.answer = answer;
        this.chance = chance;
    }

    // 남은 횟수 감소
    public void decreaseChance() {
        this.chance--;
    }

    // 게임 종료 여부 판단
    public boolean isGameOver() {
        return chance <= 0;
    }

    // 기록 추가
    public void addHistory(String record) {
        history.add(record);
    }
}
