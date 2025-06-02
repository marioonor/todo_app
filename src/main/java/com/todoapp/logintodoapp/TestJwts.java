package com.todoapp.logintodoapp;

import io.jsonwebtoken.Jwts;

public class TestJwts {
    public static void main(String[] args) {
        // just call parserBuilder to check compilation
        Jwts.parserBuilder();
        System.out.println("parserBuilder() method is accessible.");
    }
}

