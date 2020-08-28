import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Knapsack
{
	static int padding;
	
	static int modulus = 0;
	static int multiplier = 0;
	
	static int[] ai;
	static int[] pk; // PUBLIC KEY
	static int[] sk; // PRIVATE KEY
	
	static String bin;
	
	Queue<Integer> cText = new LinkedList<>();
	
	// KEY GENERATION FUNCTIONS
	@SuppressWarnings("unused")
	private void keyGeneration(Scanner sc)
	{
		// GET USER INPUT ON Ai VALUES
		System.out.print("Enter values of ai, with space between numbers: ");
		String[] aiStr = sc.nextLine().split("\\s+");
		
		ai = new int[aiStr.length];
		
		// CHANGE STRING ARRAY INTO INTEGER ARRAY
		for(int i = 0; i < aiStr.length; i++)
		{
			ai[i] = Integer.parseInt(aiStr[i]);
		}
		
		// SORT ARRAY IN INCREASING ORDER
		 Arrays.sort(ai);
		
		// GET USER INPUT ON SIZE OF KNAPSACK
		System.out.print("Enter size of knapsack: ");
		int size = sc.nextInt();
		
		// CALCULATE SUM
		int sumAI = 0;
		for (int i = 0; i < ai.length; i++)
		{
			sumAI += ai[i];
		}
		System.out.println("Sum: " + sumAI);
				
		// GET USER INPUT ON MODULUS
		do
		{
			System.out.print("Enter modulus value: ");
			modulus = sc.nextInt();
		}while(modulus <= sumAI);
		
		// GET USER INPUT ON MULTIPLIER
		int GCD = 0;
		do
		{
			System.out.print("Enter multiplier value: ");
			multiplier = sc.nextInt();

			// CHECK MULTIPLIER CONDITIONS
			GCD = gcd(multiplier, modulus);
		}while(GCD != 1);
		
		// GENERATE PUBLIC KEY
		pk = new int[ai.length];
		
		// CALCULATE PUBLIC KEY
		for(int i = 0; i < ai.length; i++)
		{
			pk[i] = (ai[i] * multiplier) % modulus;
		}
				
		// PRINT PUBLIC KEY
		System.out.print("Public Key");
		System.out.print("----------");
		
		for(int i = 0; i < pk.length; i++)
		{
			System.out.print(pk[i] + ", ");
		}
		
		System.out.println(" ");
		
		// GENERATE SECRET KEY
		sk = new int[ai.length];
		
		// CALCULATE SECRET KEY
		for(int i = 0; i < sk.length; i++)
		{
			sk[i] = ai[i];
		}
	}
	private int gcd(int a, int b)
	{
		int initialA = a;
		int initialB = b;
		
		int r = a % b;
		int q = a / b;
		
		int a1 = 1, b1 = 0, a2 = 0, b2 = 1;
		
		int tempA, tempB;
		
		int gcd = 0;
		
		while(r != 0)
		{
			a = b;
			b = r;
			
			tempA = a1 - (q * a2);
			tempB = b1 - (q * b2);
			
			r = a % b;
			q = a / b;
			
			a1 = a2;
			b1 = b2;
			a2 = tempA;
			b2 = tempB;
		}

		gcd = (a2 * initialA) + (b2 * initialB);
		
		return gcd;
	}
	
	// ENCRYPTION FUNCTIONS
	private void encryption(Scanner sc)
	{
		// USER INPUTS THE MESSAGE
		int mSize = 0;
		System.out.print("Enter message: ");
		String bin = sc.next();
		
		// FIND LENGTH OF BINARY STRING
		mSize = bin.length();
		
		int noOfGroups = ai.length;
		
		// PADDING
		if(mSize % noOfGroups != 0)
		{
			padding = mSize % noOfGroups;
			padding = noOfGroups - padding;
			for(int i = 0; i < padding; i++)
			{
				bin += "0";
			}
		}
		
		// PUT STRING INTO ARRAY
		int start = 0;
		int end = noOfGroups;
		
		// ENCRYPTION
		System.out.print("Ciphertext is: ");
		for(int i = 0; i < (mSize/noOfGroups)+1; i++)
		{
			String sub = bin.substring(start, end);
			
			int enc = 0;
			
			// CONVERT STRING TO ARRAY
			char[] binArray = sub.toCharArray();
			
			for(int j = 0; j < noOfGroups; j++)
			{
				if(binArray[j] == '1')
				{
					enc += pk[j];
				}
			}
			
			System.out.print(enc + " ");
			
			cText.add(enc);
			
			start += noOfGroups;
			end += noOfGroups;
		}			
	}
	public String toBinaryString(String s)
	{
	    char[] cArray = s.toCharArray();

	    StringBuilder sb = new StringBuilder();

	    for(char c:cArray)
	    {
	        String cBinaryString = Integer.toBinaryString((int)c);
	        sb.append(cBinaryString);
	    }

	    return sb.toString();
	}
	
	// DECRYPTION FUNCTIONS
	private void decryption(Scanner sc)
	{
		System.out.println(" ");
		
		// INPUT CIPHERTEXT
		System.out.print("Enter ciphertext: ");
		String[] aiStr = sc.nextLine().split(" ");
		
		// FIND INVERSE OF MULTIPIER
		int invMultiplier = inverse(multiplier, modulus);
		
		multiplier = invMultiplier;
		
		StringBuilder sb = new StringBuilder();
		
		// CALCULATE MULTIPLIER MOD MODULUS
		for(int i = 0; i < aiStr.length; i++)
		{
			int cipher = Integer.parseInt(aiStr[i]);
			
			// EMPTY THE KNAPSACK
			for(int j = 0; j < pk.length; j++)
			{				
				if(cipher == 0)
				{
					sb.append("0");
				}
				else if(cipher == pk[j])
				{
					sb.append("1");
					cipher -= pk[j];
				}
				else if(cipher > pk[j])
				{
					sb.append("1");
					cipher -= pk[j];
				}
				else if(cipher < pk[j])
				{
					sb.append("0");
				}
				else
				{
					sb.append("0");
				}
			}			
		}
		
		String preM = sb.toString();
		
		// REMOVE PADDING
		int pad = preM.length() - padding;
		preM = preM.substring(0, pad);
		
		System.out.println("Message is " + preM);
	}
	@SuppressWarnings("unused")
	private int inverse(int a, int b)
	{
		int initialA = a;
		int initialB = b;
		
		int r = a % b;
		int q = a / b;
		
		int a1 = 1, b1 = 0, a2 = 0, b2 = 1;
		
		int tempA, tempB;
		
		int gcd = 0;
		
		while(r != 0)
		{
			a = b;
			b = r;
			
			tempA = a1 - (q * a2);
			tempB = b1 - (q * b2);
			
			r = a % b;
			q = a / b;
			
			a1 = a2;
			b1 = b2;
			a2 = tempA;
			b2 = tempB;
		}

		int inverse = b2 + initialA;
		
		return inverse;
	}
	
	// CONVERSION FUNCTIONS
	public static String stringToBinary(String text)
	 {
	     String bString="";
	     String temp="";
	     for(int i=0;i<text.length();i++)
	     {
	         temp=Integer.toBinaryString(text.charAt(i));
	         for(int j=temp.length();j<8;j++)
	         {
	             temp="0"+temp;
	         }
	         bString+=temp;
	     }

	     System.out.println(bString);
	     return bString;
	 }
	public static String BinaryToString(String binaryCode)
	 {
	     String[] code = binaryCode.split("");
	     String word="";
	     for(int i=0;i<code.length;i++)
	     {
	         word+= (char)Integer.parseInt(code[i],2);
	     }
	     System.out.println(word);
	     return word;
	 }

	
	// MAIN FUNCTIONS
	public static void main(String[] args)
	{
		// OPEN SCANNER
		Scanner sc = new Scanner(System.in);
		Scanner SC = new Scanner(System.in);
		
		// CREATE NEW INSTANCE
		Knapsack Ks = new Knapsack();
		
		// GENERATE PUBLIC AND PRIVATE KEYS
		Ks.keyGeneration(sc);
		
		// ENCRYPT PLAINTEXT
		Ks.encryption(sc);
		
		System.out.println("\nEncryption done\n");
		
		// DECRYPT CIPHERTEXT
		Ks.decryption(SC);
		
		System.out.println("\nDecryption done\n");
		
		// CLOSE SCANNER
		sc.close();
		SC.close();
	}
}
