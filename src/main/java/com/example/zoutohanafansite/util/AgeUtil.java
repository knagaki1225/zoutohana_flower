package com.example.zoutohanafansite.util;

import java.time.LocalDate;

public class AgeUtil {
    public static int calculateGeneration(int birthYear){
        int currentYear = LocalDate.now().getYear();
        int age = currentYear - birthYear;

        return (age / 10) * 10;
    }
}
