// package com.tinyalgo;

import java.util.ArrayList;
import java.util.Scanner;

public class TEA_4
{
	ArrayList<Integer> E = new ArrayList<Integer>(21);
	
	// ENCRYPTION FUNCTIONS
	private int encrypt(int v[], int k[], int delta)
	{
		 int n = 32;
		 int sum = 0;
		 
		 int y = v[0];
		 int z = v[1];
		 
		 while (n-- > 0) 
		 {    
			 sum += delta;    
			 y += ((z << 4) + k[0]) ^ (z + sum) ^ ((z >>> 5) + k[1]);    
			 z += ((y << 4) + k[2]) ^ (y + sum) ^ ((y >>> 5) + k[3]);   
		 }
		 
		 // MAKE ALL RESULTS POSITIVE
		 if(y < 0)
		 {
			 y = y*(-1);
		 }
		 
		 if(z < 0)
		 {
			 z = z*(-1);
		 }
		 
		 // XOR RESULT WITH PLAINTEXT
		 int Xy = y^v[0];
		 int Xz = z^v[1];
		 int XZ = Xz;
		
		 addToFinal(Xy);
		 addToFinal(XZ);
		 return Xz;
	}
	private void addToFinal(int value)
	{
		E.add(value);
	}
	private void oneCycle()
	{		
		int L, length = 0;
		
		// TAKE IN USER INPUT
		System.out.print("Enter your student ID: ");
		Scanner sc = new Scanner(System.in);
		String value = sc.next();
		int inputVal = Integer.parseInt(value);
		String binaryString = Integer.toBinaryString(inputVal);
		
		int cFinallength = binaryString.length();

		StringBuffer myBuffer = new StringBuffer();
		while (cFinallength % 4 != 0) {

			myBuffer.append("0");
			cFinallength++;

		}
		binaryString = myBuffer.toString() + binaryString;
		
		
		
		if(value.length()%2 != 0)
		{
			length = value.length()+1;
		}
		else
		{
			length = value.length();
		}
		
		// ADD EVERYTHING INTO ARRAY
		int []ID = new int[binaryString.length()];
		
		for (int i = 0; i < binaryString.length(); i+=4)
		{
			ID[i] = Integer.parseInt(binaryString.substring(i,i+4), 2);
			System.out.println("ID:" + ID[i]);
		}
		
		StringBuilder A = new StringBuilder();
		
		if(value.length()%2 != 0)
		{
			L = value.length()+1;
			
			// ODD NUMBER OF DIGITS
			for(int i = 0; i < L; i++)
			{
				A.append(ID[i]);
			}
			
			A.append(0);
		}
		else
		{
			L = value.length();
			
			// EVEN NUMBER OF DIGITS
			for(int i = 0; i < L; i++)
			{
				A.append(ID[i]);
			}
		}
		
		// ENCRYPTION
		int IV = 0x9E3779B9;
		int k[]  = {78945677, 87678687, 234234, 234234};

		
		for(int i = 0; i < L; i+=2)
		{
			int t[] = new int[2];
			t[0] = ID[i];
			t[1] = ID[i+1];
			
			int next = encrypt(t, k, IV);
			
			// CHANGE IV VALUE BY TAKING LAST 8 BITS
			byte Byte = (byte)(next & 0xFFFFFFFF);
			IV = Byte;
		}
		System.out.println("--------------");
		System.out.println("Encrypted");
		System.out.println(E);
		
		sc.close();
	}
		
	// MAIN FUNCTIONS
	public static void main(String[] args) 
	{
		TEA_4 T = new TEA_4();
		
		T.oneCycle();
	}
}