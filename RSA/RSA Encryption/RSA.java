import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.Random;
import java.util.Scanner;

public class RSA
{
	// VARIABLES
	int BIT_LENGTH = 32;
	String filename = "";
	
	// FILE FUNCTIONS
	private void addToFile(String filename, String str) throws IOException
	{
		File file = new File(filename);
		FileWriter fileWriter = new FileWriter(file);
		
		fileWriter.write(str);
		
		fileWriter.close();

	}
	private String readFromFile(String filename, int position) throws IOException
	{
		String content = new String(Files.readAllBytes(Paths.get(filename)));
		
		String[] x = content.split(" ");
		
		String value = x[position];
		
		return value;
	}
	private String readFromFile(String filename) throws IOException
	{
		String content = new String(Files.readAllBytes(Paths.get(filename)));
		System.out.println("File contents are " + content);
		
		return content;
	}
	
	// VERIFICATION FUNCTIONS
	private void verification(String message) throws IOException
	{
		// GET e
		String E = readFromFile("pk.txt", 1);
		BigInteger e = new BigInteger(E);
		System.out.println("Value of e is " + e);
		
		// GET d
		String D = readFromFile("sk.txt", 3);
		BigInteger d = new BigInteger(D);
		System.out.println("Value of d is " + d);
		
		// GET N
		String n = readFromFile("pk.txt", 0);
		BigInteger N = new BigInteger(n);
		System.out.println("Value of N is " + N);
		
		// GET SIGNATURE
		String sign = readFromFile("sig.txt", 0);
		BigInteger S = new BigInteger(sign);
		System.out.println("Value of sign is " + S);
		
		// GET USER INPUT FOR MESSAGE
		String m = message;
		BigInteger M = new BigInteger(m);
		System.out.println("User message is " + M);
		
		// VERIFY
		BigInteger Sprime = M.modPow(d, N);
		if(Sprime.equals(S))
		{
			System.out.println("True");
		}
		else
		{
			System.out.println("False");
		}
	}
	
	// SIGNING FUNCTIONS
	private void signing() throws IOException
	{
		// GET M
		String m = readFromFile("mssg.txt");
		BigInteger M = new BigInteger(m);
		System.out.println("Message is " + M);
		
		// GET d
		String D = readFromFile("sk.txt", 3);
		BigInteger d = new BigInteger(D);
		System.out.println("Value of d is " + d);
		
		// GET N
		String n = readFromFile("sk.txt", 0);
		BigInteger N = new BigInteger(n);
		System.out.println("Value of N is " + N);
		
		// CALCULATE S
		BigInteger S = calculateSignature(M, d, N);
		System.out.println("Value of S is " + S);
		
		// PUT S INTO sig.txt
		filename = "sig.txt";
		String s = S.toString();
		addToFile(filename, s);
	}
	private BigInteger calculateSignature(BigInteger M, BigInteger d, BigInteger N)
	{
		BigInteger S = M.modPow(d, N);
		
		return S;
	}
	
	// KEY GENERATION FUNCTIONS
	private void keyGeneration() throws IOException
	{
		// GENERATE p & q
		Random rand = new SecureRandom();
		
		BigInteger p = new BigInteger(BIT_LENGTH / 2, 100, rand);
		BigInteger q = new BigInteger(BIT_LENGTH / 2, 100, rand);
		
		System.out.println("p is " + p);
		System.out.println("q is " + q);
		
		// CALCULATE N
		BigInteger N = p.multiply(q);
		System.out.println("N is " + N);
		
		// CALCULATE PHI
		BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
		System.out.println("phi is " + phi);
		
		// CALCULATE e
		BigInteger e;
		do
		{
			e = new BigInteger(phi.bitLength(), rand);
		}while (e.compareTo(BigInteger.ONE) <= 0
				|| e.compareTo(phi) >= 0
				|| !e.gcd(phi).equals(BigInteger.ONE));
		
		System.out.println("e is " + e);
		
		// CALCULATE d
		BigInteger d = e.modInverse(phi);
		System.out.println("d is " + d);
		
		// ADD N & e INTO pk.txt
		filename = "pk.txt";
		String Ne = N + " " + e;
		addToFile(filename, Ne);
		
		// ADD N, p, q & d INTO sk.txt
		filename = "sk.txt";
		String Npqd = N + " " + p + " " + q + " " + d;
		addToFile(filename, Npqd);
	}
	
	// ENCRYPTION
	BigInteger encryption() throws IOException
	{
		// GET M
		String m = readFromFile("mssg.txt");
		BigInteger M = new BigInteger(m);
		System.out.println("Message is " + M);
		
		// Get publicKey
		String pk = readFromFile("pk.txt", 1);
		BigInteger e = new BigInteger(pk);
		System.out.println("publicKey is " + e);
		
		// GET N
		String n = readFromFile("sk.txt", 0);
		BigInteger N = new BigInteger(n);
		System.out.println("Value of N is " + N);
		
		BigInteger encrypted = M.modPow(e, N);
		
		int Enc = encrypted.intValue();
		String enc = Integer.toString(Enc);
		
		addToFile ("encrypted.txt", enc);
		
		return encrypted;
    }

	// DECRYPTION
   BigInteger decryption() throws IOException
   {
	   // GET enc
		String enc = readFromFile("encrypted.txt");
		int IntEnc = Integer.parseInt(enc);
		BigInteger M = BigInteger.valueOf(IntEnc);
		System.out.println("Encrypted message is " + M);
		
		// Get privateKey
		String sk = readFromFile("sk.txt", 3);
		BigInteger d = new BigInteger(sk);
		System.out.println("publicKey is " + d);
		
		// GET N
		String n = readFromFile("sk.txt", 0);
		BigInteger N = new BigInteger(n);
		System.out.println("Value of N is " + N);
		
		BigInteger decrypted = M.modPow(d, N);
		
		int dec = decrypted.intValue();
		String plaintext = Integer.toString(dec);
		
		addToFile ("decrypted.txt", plaintext);
		
		return decrypted;
   }
	
	// MAIN FUNCTIONS
	public static void main(String[] args) throws IOException
	{
		RSA R = new RSA();
		
		// OPEN SCANNER
		Scanner sc = new Scanner(System.in);
				
		String option = "";
		int O = 0;
		// CHOOSE OPTION
		do
		{
			option = R.chooseMenu(sc);
			
			if(option.equals("1"))
			{
				O = 1;
				R.keyGeneration();
			}
			else if(option.equals("2"))
			{
				O = 2;
				R.signing();
			}
			else if (option.equals("3"))
			{
				O = 3;
				String message = R.getMessage(sc);
				R. verification(message);
			}
			else if (option.equals("4"))
			{
				O = 4;
				R.encryption();
			}
			else if (option.equals("5"))
			{
				O = 5;
				R.decryption();
			}
			else
			{
				break;
			}
			
		}while(O != 1 || O != 2 || O != 3 || O != 4 || O != 5);
		
		// CLOSE SCANNER
		sc.close();
	}
	public String chooseMenu(Scanner sc)
	{
		String option = "";
		
		System.out.println("Choose your option");
		System.out.println("------------------");
		System.out.println("1) Key Generation");
		System.out.println("2) Signing");
		System.out.println("3) Verification");
		System.out.println("4) Encryption");
		System.out.println("5) Decryption");
		System.out.print("Option: ");
		
		option = sc.next();
		
		return option;
	}
	private String getMessage(Scanner sc)
	{
		System.out.print("Enter message to verify: ");
		
		String msg = sc.next();
		
		return msg;
	}
}
