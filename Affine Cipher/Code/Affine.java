import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public class Affine 
{
	Queue<Character> encrypt = new LinkedList<Character>(); // For initial plaintext
	Queue<Character> eFinal = new LinkedList<Character>(); // For encrypted text
	Queue<Character> decrypt = new LinkedList<Character>(); // For initial ciphertext
	Queue<Character> dFinal = new LinkedList<Character>(); // For decrypted text
	
	char LowerAlphabet[] = {'a', 'b', 'c', 'd', 'e', 'f', 
							'g', 'h', 'i', 'j', 'k', 'l', 
							'm', 'n', 'o', 'p', 'q', 'r', 's', 
							't', 'u', 'v', 'w', 'x', 'y', 'z'};
	char UpperAlphabet[] = {'A', 'B', 'C', 'D', 'E', 'F',
							'G', 'H', 'I', 'J', 'K', 'L',
							'M', 'N', 'O', 'P', 'Q', 'R', 'S',
							'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
	
	// OTHER FUNCTIONS
	private int keyValidation(int num1, int num2)
	{
		int x;
		
		// Testing for num1
		if(num1 > 0 && num1 < 26 && num1%2 == 1 && num1 != 13)
		{
			x = 0;
		}
		else
		{
			x = 1;
		}
		
		// Testing for num2
		if(num2 > -1 && num2 < 26)
		{
			x = 0;
		}
		else
		{
			x = 1;
		}
		
		return x;
	}
	private int findNumber(char C)
	{	
		int x = 0;
		
		for(int i = 0; i < LowerAlphabet.length; i++)
		{
			if(C == LowerAlphabet[i])
			{
				x = i;
			}
		}
		
		for(int i = 0; i < UpperAlphabet.length; i++)
		{
			if(C == UpperAlphabet[i])
			{
				x = i;
			}
		}
		
		return x;
		
	}
	private char getLetter(char C, int value)
	{
		char x = 0;
		
		if(Character.isUpperCase(C))
		{
			x = UpperAlphabet[value];
			System.out.print(x);
			return x;
		}
		else if(Character.isLowerCase(C))
		{
			x = LowerAlphabet[value];
			System.out.println(x);
			return x;
		}
		
		return x;	
	}
	
	// ENCRYPTION FUNCTIONS
	public void encryptPlaintext(int num1, int num2, String plaintext, String ciphertext) throws IOException
	{
		int A = num1;
		int B = num2;
		
		// OPEN FILE FOR PLAINTEXT
		BufferedReader y = new BufferedReader(new FileReader(plaintext));

		String line;
		
		while ((line = y.readLine()) != null)
		{
			
			for(char c : line.toCharArray())
			{
				encrypt.add(c);
			}
			
		}
		
		int size = encrypt.size();
		
		for(int i = 0; i < size; i++)
		{
			char C = encrypt.remove();
			if(Character.isLetter(C))
			{
				int x = findNumber(C);
				int value = encryptLetter(A, B, x);
				char Cfinal = getLetter(C, value);
				
				// ADD TO NEW QUEUE
				eFinal.add(Cfinal);
			}
			else if(C == ' ')
			{
				// ADD TO NEW QUEUE
				eFinal.add(C);
			}
		}
		
		// OPEN FILE FOR ENCRYPTED TEXT
		File cipherOutput = new File(ciphertext);
		cipherOutput.createNewFile();
		FileOutputStream cipherO = new FileOutputStream(cipherOutput, false);
				
		// ADD ENCRYPTED TEXT INTO FILE
		while (!eFinal.isEmpty())
		{
			char addToFile = eFinal.remove();
			cipherO.write(addToFile);
		}
		
		System.out.println("Encryption Complete");
		
		// CLOSE FILES
		cipherO.close();
		y.close();
	}
	private int encryptLetter(int A, int B, int x)
	{
		// C = Ax + B % 26
		int C = ((A*x)+B)%26;
		return C;
	}
	
	//DECRYPTION FUNCTIONS
	public void decryptCiphertext(int num1, int num2, String plaintext, String ciphertext) throws IOException
	{
		int A = num1;
		int B = num2;
		
		// OPEN FILE FOR PLAINTEXT
		BufferedReader y = new BufferedReader(new FileReader(ciphertext));

		String line;
		
		while ((line = y.readLine()) != null)
		{
			
			for(char c : line.toCharArray())
			{
				decrypt.add(c);
			}
			
		}
		
		int size = decrypt.size();
		
		for(int i = 0; i < size; i++)
		{
			char C = decrypt.remove();
			if(Character.isLetter(C))
			{
				int x = findNumber(C);
				int value = decryptLetter(A, B, x);
				char Cfinal = getLetter(C, value);
				
				// ADD TO NEW QUEUE
				dFinal.add(Cfinal);
			}
			else if(C == ' ' || C == '!' || C == '.' || C == ',' || C == '?' || C == '\n')
			{
				// ADD TO NEW QUEUE
				dFinal.add(C);
			}
		}
		
		// OPEN FILE FOR ENCRYPTED TEXT
		File plainOutput = new File(plaintext);
		plainOutput.createNewFile();
		FileOutputStream plainO = new FileOutputStream(plainOutput, false);
				
		// ADD ENCRYPTED TEXT INTO FILE
		while (!dFinal.isEmpty())
		{
			char addToFile = dFinal.remove();
			plainO.write(addToFile);
		}
		
		System.out.println("Decryption Complete");
		
		// CLOSE FILES
		plainO.close();
		y.close();
	}
	private int decryptLetter(int a, int b, int y)
	{		
		int inv = 0;
		while(true)
		{
			int inverse = a * inv % 26;
			if(inverse == 1)
			{
				break;
			}
			inv++;
		}
		
		// x = (A^-1)(y-B) % 26
		int x = (inv*(y-b)) % 26;
		
		if(x < 0)
		{
			x = 26-((-1)*x);
		}
		
		System.out.println(x);
		
		return x;
	}
	
	// MAIN FUNCTIONS
	public void chooseOption(String key, String keyVal1, String keyVal2, String option, String fileReason1, 
								String fileName1, String fileReason2, String fileName2) throws IOException
	{
		// KEY VALIDATION
		int num1 = Integer.parseInt(keyVal1);
		int num2 = Integer.parseInt(keyVal2);
		
		int x = keyValidation(num1, num2);
		
		String enc = "-encrypt";
		String dec = "-decrypt";
		
		String plaintext = "";
		String ciphertext = "";
		
		String r1 = "-in";
		String r2 = "-out";
		
		if(x == 0)
		{
			if (option.equals(enc))
			{
				// ENCRYPTION
				System.out.println("\n\nEncryption");
				
				if(fileReason1.equals(r1))
				{
					plaintext = fileName1;
					ciphertext = fileName2;
				}
				else if(fileReason1.equals(r2))
				{
					ciphertext = fileName1;
					plaintext = fileName2;
				}
				
				encryptPlaintext(num1, num2, plaintext, ciphertext);
			}
			else if (option.equals(dec))
			{
				// DECRYPTION
				System.out.println("\n\nDecryption");
				
				if(fileReason1.equals(r1))
				{
					ciphertext = fileName1;
					plaintext = fileName2;
				}
				else if(fileReason2.equals(r2))
				{
					plaintext = fileName1;
					ciphertext = fileName2;
				}
				decryptCiphertext(num1, num2, plaintext, ciphertext);
			}
			else
			{
				System.out.println("Work in progress!");
			}
		}
		else if (x == 1)
		{
			System.out.println("Program Exited");
		}
		
		
	}
	public static void main(String[] args) throws IOException 
	{
		int i = 0;
		
		String key;
		String keyVal1;
		String keyVal2;
		String option;
		String fileReason1;
		String fileName1;
		String fileReason2;
		String fileName2;
		
		String value[] = new String[8];
		
		for(String s : args)
		{	
			value[i] = s;
			i++;
		}
		
		key = value[0];
		keyVal1 = value[1];
		keyVal2 = value[2];
		option = value[3];
		fileReason1 = value[4];
		fileName1 = value[5];
		fileReason2 = value[6];
		fileName2 = value[7];
		
		Affine af = new Affine();
		
		af.chooseOption(key, keyVal1, keyVal2, option, fileReason1, 
							fileName1, fileReason2, fileName2);
	}

}
