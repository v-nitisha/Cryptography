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
		int J1 = X.charAt(1) & 1;
		int J2 = X.charAt(2) & 1;
		
		// PUT TOGETHER J1 & J2
		String J1J2 = Integer.toString(J1) + Integer.toString(J2);
		
		return J1J2;
	}
	private String functionBK(String Xbin, int Y)
	{
		// EXPAND 2-BIT 'X' INTO 3-BIT 'X'
		Xbin = Xbin + Xbin.charAt(0);
		
		// CONVERT BINARY TO INT
		int X = Integer.parseInt(Xbin, 2);
		
		// XOR V AND KEY
		int XOR = X ^ Y;
		
		// CONVERT XOR VALUE INTO BINARY STRING
		String xor = Integer.toBinaryString(XOR);
		
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
		
		// ROTATE LEFT
		int z = Integer.parseInt(Z);
		z = leftRotate(z);
		Z = Integer.toBinaryString(z);
		
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
		
		// SPLIT INTO LEFT AND RIGHT
		int VLength = V.length();
		
		int initialA = 0;
		int finalA = VLength - 2;

		int initialB = VLength - 2;
		int finalB = VLength;
		
		String A = V.substring(initialA, finalA);
		String B = V.substring(initialB, finalB);
		
		// PROCESS B AND KEY TO GET Z
		String f = functionBK(B, key1);
		
		// XOR F & A;
		int a = Integer.parseInt(A);
		int F = Integer.parseInt(f, 2);
		
		int XOR = a ^ F;
		f = Integer.toBinaryString(XOR);
		
		// GET A2 & B2
		String A2 = B;
		String B2 = f;
		
		// REMOVE FIRST AND LAST BIT, S PER FIESTEL
		int fLength = f.length();
		
		String lastB = f.substring(1, fLength-1);	// for xor
		
		// ROUND 2
		
		// PROCESS B AND KEY TO GET Z
		B2 = lastB;
		f = functionBK(B2, key2);
		
		// XOR F & A;
		a = Integer.parseInt(A2);
		F = Integer.parseInt(f, 2);
		
		XOR = a ^ F;
		f = Integer.toBinaryString(XOR);
		
		// ROTATE RIGHT
		int end = rightRotate(XOR);
		String END = Integer.toBinaryString(end);
		
		return END;
	}
	private void subBitFunction(int value, int key1, int key2)
	{
		
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
		
		// SPLIT VALUE 4 AT A TIME & ENCRYPT
		String Cfinal = "";
		
		for (int i = 0, j = 4; i < valueBinLength; i += 4, j += 4)
		{
			String four = Value.substring(i, j);
			
			String Csub = encrypt(four, key1, key2);
			
			Cfinal = Cfinal + Csub;
		}
		
		int finalValue = Integer.parseInt(Cfinal, 2);
		System.out.println("Encrypted value is " + finalValue);
	}
	
	// MAIN FUNCTIONS
	public static void main(String[] args)
	{
		Scanner sc = new Scanner (System.in);
		System.out.print("Enter number: ");
		int LR = sc.nextInt();
		int K1 = 2;
		int K2 = 1;
		
		LinearDES L = new LinearDES();
		
		// ENCRYPTIONS
		L.subBitFunction(LR, K1, K2);
		
		sc.close();
	}

}
