package com.cipher;



public class StreamCipher {
		
	public static String StreamCipherEncryptor(String msg, int key) {
		final int LIMIT = 26; 
		String engAlpha_Z26 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";  
		int[] keyStream = new int[msg.length()];
	
		StringBuffer cipherMessage = new StringBuffer();
		//first element of the keyStream calculated by using key
		keyStream[0] = ((int)Math.pow(key, 2) + 1) % LIMIT; 
		if (key<LIMIT) {
			
			// Calculates the keyStream
			for (int i = 1; i < msg.length(); i++) {
				
				
				keyStream[i] = ((int)Math.pow(keyStream[i-1], 2) + (1+i) ) % LIMIT;
			}
			for (int i = 0; i < msg.length(); i++) {
				
				//System.out.printf(keyStream[i] + ","); //checkpoint
				
				int msgCharCode = engAlpha_Z26.indexOf(msg.charAt(i));
				int cipherCharCode = (Math.abs(keyStream[i] + msgCharCode )) % LIMIT;
				//System.out.printf(msgCharCode +" + " + keyStream[i] + " = " +cipherCharCode +" , ");
				// this line of code get the cipher character according to the cipher code of the character. 
				cipherMessage.append(engAlpha_Z26.charAt(cipherCharCode));
				
				
			}
			return cipherMessage.toString();
			
			
		} else {
			
			return "false"; // this flag insures that key is less than 26
		}
	}// ends StreamCipherEncryptor()
	
	public static String StreamCipherDecryptor(String msg, int key) {
		final int LIMIT = 26; 
		String engAlpha_Z26 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; // holds the all 26 Alphabets
		int[] keyStream = new int[msg.length()]; 
		StringBuffer cipherMessage = new StringBuffer();
		//System.out.println("\nEncrypted stream");
		//first element of the keyStream calculated by using key
		keyStream[0] = ((int)Math.pow(key, 2) + (1)) % LIMIT; 
		if (key<LIMIT) {
			
			// calculates the keyStream.
			for (int i =1; i < msg.length(); i++) {
				keyStream[i] = ((int)Math.pow(keyStream[i-1], 2) + (i+1) ) % LIMIT; 
			
			}for (int i = 0; i < msg.length(); i++) {	
				
				int msgCharCode = engAlpha_Z26.indexOf(msg.charAt(i));
				//here is the Decryption occurs
				int cipherCharCode = (Math.abs(LIMIT + msgCharCode -keyStream[i] )) % LIMIT;
				// this line of code get the cipher character according to the cipher code of the character. 
				cipherMessage.append(engAlpha_Z26.charAt(cipherCharCode));
				
			}
			return cipherMessage.toString();
			
		} else {
			return "false"; // this flag insures that key is less than 26
		}
	}

	public static void main(String[] args) {
		
		int encryptionKey = 10, decryptionKey = 10;
		String plainTextString ="WOLLONGONG"; 
		String cipherTextString="TZFZHQWSMR"; 
		
		String encryptedText = StreamCipherEncryptor(plainTextString,encryptionKey);
		String decryptedText = StreamCipherDecryptor(cipherTextString, decryptionKey);
		if (!encryptedText.equals("false") &&  !decryptedText.equals("false")) {
			System.out.println(plainTextString + " Encrypted as: " + encryptedText);
			System.out.println(cipherTextString + " Decrypted as: " + decryptedText);
		} else {
			System.out.println("Encypted and Decrypted key Must not greater than 26");
		}
		
		
		
	}

}
