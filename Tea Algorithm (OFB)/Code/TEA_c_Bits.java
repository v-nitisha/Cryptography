// package com.tinyalgo;

import java.util.ArrayList;

public class TEA_c_Bits
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
		String stdIdString="5283048";
		System.out.println("Student ID: " + stdIdString);
		
		int c=0;
		int cMod=0;
		for (int i = 0; i < stdIdString.length(); i++) {
			
			c = c + stdIdString.charAt(i)-'0';
		}
		
		cMod=c%8;
		System.out.println("c:"+cMod);
		
		
		// ENCRYPTION
		int IV = 0x9E3779B9;
		int key[]  = {78945677, 87678687, 234234, 234234};

		int t[] = {cMod,cMod} ;
		
			
			int next = encrypt(t, key, IV);
			
			// CHANGE IV VALUE BY TAKING LAST 6 BITS (VALUE OF c IS 6)
			byte Byte = (byte)(next ^ 0x3F3F3F3F);
			IV = Byte;
		
		System.out.println("--------------");
		System.out.println("Encrypted");
		System.out.println(E);
		
	}

	
	// MAIN FUNCTIONS
	public static void main(String[] args) 
	{
		TEA_c_Bits T = new TEA_c_Bits();
		
		T.oneCycle();
	}
}