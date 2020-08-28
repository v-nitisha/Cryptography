import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.LinkedList;

public class LinearDES
{
	List<Integer> digits = new ArrayList<Integer>();
	Queue<Integer> F = new LinkedList<Integer>();
	
	String first, second;
	
	// ROTATE FUNCTIONS
	private int leftRotate(int V)
	{
		V = Integer.rotateLeft(V, 1);
		return V;
	}
	private int rightRotate(int V)
	{
		V = Integer.rotateRight(V, 1);
		return V;
	}
	
	// OTHER FUNCTIONS
	private String linear (String X)
	{
		System.out.println("\nString in linear function is " + X);
		
		int J1 = X.charAt(1) & 1;
		int J2 = X.charAt(2) & 1;
		
		System.out.println("J1 is " + J1);
		System.out.println("J2 is " + J2);
		
		// PUT TOGETHER J1 & J2
		String J1J2 = Integer.toString(J1) + Integer.toString(J2);
		System.out.println("\nJ1J2 is " + J1J2);
		
		return J1J2;
	}
	private String functionBK(String Xbin, int Y)
	{
		System.out.println("\nFunction starts here\n");
		System.out.println("Y is " + Y);
		System.out.println("Y in binary " + Integer.toBinaryString(Y));
		
		// EXPAND 2-BIT 'X' INTO 3-BIT 'X'
		Xbin = Xbin + Xbin.charAt(0);
		System.out.println("\nX after expansion is " + Xbin);
		
		// CONVERT BINARY TO INT
		int X = Integer.parseInt(Xbin, 2);
		System.out.println("\nX is " + X);
		System.out.println("X in binary " + Integer.toBinaryString(X));
		
		// XOR V AND KEY
		int XOR = X ^ Y;
		System.out.println("\nAfter XOR " + XOR);
		System.out.println("XORed binary " + Integer.toBinaryString(XOR));
		
		System.out.println("\n");
		
		// CONVERT XOR VALUE INTO BINARY STRING
		String xor = Integer.toBinaryString(XOR);
		System.out.println("String after XOR is " + xor);
		
		if(xor.length() == 2)
		{
			xor = "0" + xor;
		}
		else if(xor.length() == 1)
		{
			xor = "00" + xor;
		}
		
		// LINEAR FUNCTION
		String Z = linear(xor);
		System.out.println("Z is " + Z);
		
		// ROTATE LEFT
		int z = Integer.parseInt(Z);
		z = leftRotate(z);
		Z = Integer.toBinaryString(z);
		
		System.out.println("Z after left rotate is " + Z);
		
		System.out.println("\nFunction ends here\n");
		return Z;
	}
	
	// ENCRYPTION FUNCTIONS
	private String encrypt(String V, int key1, int key2)
	{
		// CONVERT STRING TO INT TO BIN
		int value = Integer.parseInt(V,2);
		
		// ROTATE LEFT
		value = leftRotate(value);
		V = Integer.toBinaryString(value);
		
		System.out.println("Value is bits is " + V);
		
		// SPLIT INTO LEFT AND RIGHT
		int VLength = V.length();
		
		int initialA = 0;
		int finalA = VLength - 2;

		int initialB = VLength - 2;
		int finalB = VLength;
		
		String A = V.substring(initialA, finalA);
		String B = V.substring(initialB, finalB);
		
		System.out.println("A is " + A);
		System.out.println("B is " + B);
		
		// PROCESS B AND KEY TO GET Z
		String f = functionBK(B, key1);
		
		// XOR F & A;
		int a = Integer.parseInt(A);
		int F = Integer.parseInt(f, 2);
		
		int XOR = a ^ F;
		f = Integer.toBinaryString(XOR);
		System.out.println("XOR of A & f is " + f);
		
		// GET A2 & B2
		String A2 = B;
		String B2 = f;
		
		System.out.println("A2 is " + A2);
		System.out.println("B2 is " + B2);
		
		// REMOVE FIRST AND LAST BIT, S PER FIESTEL
		int fLength = f.length();
		
		String lastB = f.substring(1, fLength-1);	// for xor
		
		System.out.println("lastB is " + lastB);
		
		System.out.println("\n ROUND 2");
		// PROCESS B AND KEY TO GET Z
		B2 = lastB;
		f = functionBK(B2, key2);
		
		// XOR F & A;
		a = Integer.parseInt(A2);
		F = Integer.parseInt(f, 2);
		
		XOR = a ^ F;
		f = Integer.toBinaryString(XOR);
		System.out.println("XOR of A & f is " + f);
		
		// ROTATE RIGHT
		int end = rightRotate(XOR);
		String END = Integer.toBinaryString(end);
		System.out.println("\nFinal value is " + END);
		
		return END;
	}
	private void subBitFunction(int value, int key1, int key2)
	{
		System.out.println("Value is bits is " + Integer.toBinaryString(value));
		
		String Value = Integer.toBinaryString(value);
		int valueBinLength = Value.length();
		
		@SuppressWarnings("unused")
		int pad = 0;
		
		// ADD PADDING
		if(valueBinLength%4 == 1)
		{
			Value = Value + "0";
			pad += 1;
		}
		else if(valueBinLength%4 == 2)
		{
			Value = Value + "00";
			pad += 2;
		}
		else if(valueBinLength%4 == 3)
		{
			Value = Value + "000";
			pad += 3;
		}
		
		System.out.println("Value after padding is " + Value);
		
		// SPLIT VALUE 4 AT A TIME & ENCRYPT
		String Cfinal = "";
		
		for (int i = 0, j = 4; i < valueBinLength; i += 4, j += 4)
		{
			String four = Value.substring(i, j);
			System.out.println("Set " + four);
			
			String Csub = encrypt(four, key1, key2);
			System.out.println("Csub is " + Csub);
			
			Cfinal = Cfinal + Csub;
			System.out.println("Cfinal is " + Cfinal + "\n");
		}
		
		int finalValue = Integer.parseInt(Cfinal, 2);
		System.out.println("Final encrypted value is " + finalValue);
	}
	
	// MAIN FUNCTIONS
	public static void main(String[] args)
	{
		Scanner sc = new Scanner (System.in);
		System.out.print("Enter number: ");
		int LR = sc.nextInt();
		int K1 = 2;
		int K2 = 3;
		
		LinearDES L = new LinearDES();
		
		// ENCRYPTIONS
		L.subBitFunction(LR, K1, K2);
		
		sc.close();
	}

}
