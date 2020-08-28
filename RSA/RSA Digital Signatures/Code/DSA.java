
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class DSA {

	public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
		File inputFile = new File("input.txt");
		String plainMsg = readFile(inputFile);
		int qLength = 160; // bits
		int plength = 512; //bits
		BigInteger q =qGenerator(qLength);
		BigInteger p =pGenerator(q, plength, qLength);
		BigInteger g =gGenerator(p, q,plength);
		// private key  0 < x < q
		BigInteger x =new BigInteger(160,new Random());
		
		// public key 
		BigInteger y = g.modPow(x, p);
		BigInteger k =new BigInteger(160,new Random());
		BigInteger kInverse = k.modInverse(q);//DSA.nInverse(k, q);
	
		// Signature components 
		//r = (gk mod p) mod q and 
		BigInteger r = g.modPow(k, p).mod(q);
		//s = (kInverse(SHA(M) + xr)) mod q.
		
		BigInteger msgShaInteger = new BigInteger(SHA(plainMsg),16);
		BigInteger s = (kInverse.multiply(msgShaInteger.add(x.multiply(r)))).mod(q);
		BigInteger ws = s.modInverse(q);
		signedFile(r, s);
		//System.out.println("s: " + s);
		
		// reads data from the input and signed file.
		// message on receiving end.
		 String msgReceived =readFile(inputFile);
		 msgReceived.trim();
		 /* Assumptions regarding sign.txt
		  * there are only two lines in that file
		  * first line hold the r value
		  * Second line holds the s value.
		  * */
		 File signedFile = new File("sign.txt");
		 String signatureString = readFile(signedFile); 
		 signatureString.trim();
		 String rString  ="";
		 String sString = "";
		 String[] tokens = signatureString.split(" ");
		 if (tokens.length==2) {
			  rString  =tokens[0];
			  sString = tokens[1];
		}
		 BigInteger rPrime = new BigInteger(rString.trim());
		 BigInteger sPrime = new BigInteger(sString.trim());
		 /*
			 * w = (s')-1 mod q 
			 * u1 = ((SHA(M')w) mod q 
			 * u2 = ((r')w) mod q 
			 * v = (((g)ul (y)u2) mod p) mod q. 
			 * */
		 BigInteger sPrimeInverse = sPrime.modInverse(q);
		 BigInteger w = sPrimeInverse.mod(q);
		 BigInteger shaMsgRec = new BigInteger(SHA(msgReceived),16); 
		 BigInteger u1 = (shaMsgRec.multiply(w)).mod(q);
		 BigInteger u2 = (rPrime.multiply(w)).mod(q);
		 

		 BigInteger v  = ((g.modPow(u1, p)).multiply(y.modPow(u2, p)).mod(p)).mod(q);

		 System.out.println("r: " + rPrime.toString(16));
		 System.out.println("v: " + v.toString(16));

		 
		 if (v.compareTo(rPrime) == 0) {
			System.out.println("True");
		} else {
			System.out.println("False");
		}
		
	}// end main()
	
 public static String readFile(File fname) throws IOException {
	 
	 String readString = "";
	 StringBuffer sb = new StringBuffer();
	 FileReader fReader = new FileReader(fname);
	 BufferedReader bReader = new BufferedReader(fReader);
	 readString = bReader.readLine();
	 while (readString!=null) {
		 
		 sb.append(readString + " "); // whitespace is placed to parse the string;
		 readString = bReader.readLine(); 
		
	}
	 bReader.close();
	 fReader.close();
	 return sb.toString();
 }
	
	
	/**
	 * write the signatures to the file
	 * @param  r BigInteger to be written
	 * @param  s BigInteger to be written
	 * @throws IOException 
	 * 		if any io error occurs
	 */
	private static void signedFile(BigInteger r, BigInteger s) {
		File outFile = new File("sign.txt");
		try(FileWriter fWriter = new FileWriter(outFile);
			BufferedWriter bWriter = new BufferedWriter(fWriter)) {
			
			bWriter.write(r.toString());
			bWriter.newLine();
			bWriter.write(s.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public static BigInteger  qGenerator(int n) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		BigInteger q = BigInteger.ZERO;
		/*Step 1. Choose an arbitrary sequence of at least 160 bits 
		 *and call it SEED. Let g be the length of SEED in bits.
		 * */ 
		// generating the seed of 160 bits.
		int g = n ; // it is length of seed.
		do {
			BigInteger seed = new BigInteger(g, new Random());
			/*Step 2. Compute
			 * U = SHA[SEED] XOR SHA[(SEED+1) mod 2g ].
			 * */
				BigInteger uXORLeftSide = new BigInteger(SHA(seed.toString()),16);
				BigInteger uXORRightSide = new BigInteger(SHA((seed.add(BigInteger.ONE).mod(BigInteger.valueOf(2).pow(g))).toString()),16);
				BigInteger u = uXORLeftSide.xor(uXORRightSide);
				u = u.setBit(159);
				u = u.setBit(0);
				/*
				 * Step 3. Form q from U by setting the most significant bit 
				 * (the 2 pow159 bit) and the least significant bit to 1. In
				 *  terms of boolean operations, q = U OR 2159 OR 1. Note that 2 pow159 < q < 2pow160.
				 *   */
				q = u;
				if (q.isProbablePrime(1)) {
					break;
				}
		} while (true);
		return q;
		
	} // end pGenerator()
	
	
	public static BigInteger pGenerator(BigInteger q,int l,int qLen) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		BigInteger p = BigInteger.ZERO;
		int g = qLen;
		
		// L-1 = n*160 + b where b is an integer 0<=b<160
		Random random = new Random();
		int b = random.nextInt(160); // 0 to 159
		int n = (l-1-b)/160;
		boolean flag = true;
		do {
			BigInteger seed = new BigInteger(g, new Random());
			
			/*
			 * Step 6. Let counter = 0 and offset = 2. 
			 * */
			int counter = 0;
			int offset = 2;
			/*
			 * Step 7. For k = 0,...,n let
			 * Vk = SHA[(SEED + offset + k) mod 2 pow g ]
			 * */
				BigInteger[] vArray = new BigInteger[n];
				BigInteger w = BigInteger.ZERO; // Initialise w to zero.
			for (int k = 0; k < n; k++) {
				vArray[k] = new BigInteger(
						SHA(seed.add(BigInteger.valueOf(offset).add(BigInteger.valueOf(k))).
								mod(BigInteger.valueOf(2).pow(g)).toString()),16);
				
				if (k==0) {
					w=vArray[k];
				}else if (k==(n-1)) {
					w = w.add(vArray[k].mod(BigInteger.valueOf(2).pow(b)).
							multiply(BigInteger.valueOf(2).pow(n*160)));
				} else {
					w = w.add(vArray[k].multiply(BigInteger.valueOf(2).pow(k*160)));
				}
				/*
				 * let X = W + 2 pow L-1. 
				 * Note that 0 < or = to W < 2 pow L-1 and hence 2 pow L-1 < or = to X < 2 pow L.
				 * */
				BigInteger x = w.add(BigInteger.valueOf(2).pow(l-1));
				/*
				 * Step 9. Let c = X mod 2q and
				 *  set p = X - (c - 1). Note that p is congruent to 1 mod 2q.
				 * */
				BigInteger c = x.mod(q.multiply(BigInteger.valueOf(2)));
				p = x.subtract((c.subtract(BigInteger.ONE)));
				/*
				 * Step 10. If p < 2L-1, then go to step 13.
				 * */
				
					
				 if (p.isProbablePrime(1)) {
					 flag = false;
					 break;
				 }
				 counter+=1;
				 offset = offset+n+1;
				if (counter>4096) {
					break;
				}	 
			} // for ends
			
		} while (flag);
		return p;
	} // ends pGenerator
	
	public static BigInteger gGenerator(BigInteger p,BigInteger q,int plength) {
		// g = (h pow ((p-1)/q)) mod q
		BigInteger e = (p.subtract(BigInteger.ONE)).divide(q);
		BigInteger h = BigInteger.ZERO;
		Random random = new Random();
		BigInteger g = BigInteger.ZERO;
		do {
			h =new BigInteger(512, random);
			g = h.modPow(e, p);
		} while (g.compareTo(BigInteger.ONE)==0);
		return g;
	}
	
	public static String SHA(String input) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest mDigest = MessageDigest.getInstance("SHA-1");
		byte[] result = mDigest.digest(input.getBytes());
		StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
        	// result[i] & 0xff is used to eliminate the negative value if 
        	// there is. addition of 0x100 ensure the output number is 
        	// exactly of 8bits.
            sb.append(Integer.toString((result[i] & 0xff)+ 0x100, 16).substring(1));
        }
		return sb.toString();
	} // ends SHA()

public static BigInteger nInverse(BigInteger n,BigInteger q) {
		
		if ((n.compareTo(BigInteger.ZERO)>0) & (q.compareTo(BigInteger.ZERO) >0) & 
				(n.compareTo(q)<0)) {
			
			BigInteger i = q;
			BigInteger h = n;
			BigInteger v = BigInteger.ZERO;
			BigInteger d =BigInteger.ONE;
			BigInteger t =BigInteger.ZERO;
			
			while (true) {
				t = i.divide(h);
				BigInteger x = h;
				 h = i.subtract((h.multiply(t)));
				 i=x;
				 x= d; 
				 d = v.subtract((d.multiply(t)));
				 v=x;
				
				if (h.compareTo(BigInteger.ZERO)>0) {
					continue;
				}
				if (!i.equals(BigInteger.ONE)) {
					return BigInteger.ONE.negate();
				} else {
					return v.mod(q);
				}
			}
		}
		return BigInteger.ZERO;
	}
}
