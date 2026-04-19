package com.ranum.adventure.bna.generator;

import java.util.Random;

public class IdGenerator {

	public static String generatePenyewaanId() {
        // Prefix "role-"
        String prefix = "RADV-";
        
        // Generate random number (assumed length is 25 digits)
        String randomNumber = generateRandomNumber(10);

        // Combine prefix and random number
        return prefix + randomNumber;
    }
	
	private static String generateRandomNumber(int length) {
        Random random = new Random();
        StringBuilder randomNumberBuilder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            randomNumberBuilder.append(random.nextInt(10));
        }

        return randomNumberBuilder.toString();
    }
}
