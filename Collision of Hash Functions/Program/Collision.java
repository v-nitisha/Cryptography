import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class Collision
{
	// VARIABLES
	static String[] hashList;
	
	static String mainMessage = "";
	static String mainHash = "";
	
	 public static int random(int min, int max)
	 {
		 max = 15;
		 Random r = new Random();
		 return r.nextInt(max - min) + min;
	 }

	private String getMessage(int x)
	{
		String Message = "The Cat-In-The-Hat owes Nitisha " + x + " dollars";
		
		return Message;
	}	
	private void MessageApos() throws NoSuchAlgorithmException, UnsupportedEncodingException
	{
		String message = "";
		String hash = "", hash2 = "";
		
		// GENERATE RANDOM MODULUS
		Random r = new Random();
		//int mod = r.nextInt(1000);
		int mod = r.nextInt();
		System.out.println("Mod value is " + mod);
		
		String check = "";
		
		int random = r.nextInt(mod) + 1;
		int val = random % mod;
		
		message = getMessage(val);
		hash = SHA1(message);
		
		System.out.println("Original hash for " + message);
		System.out.println(hash + "\n");
		
		int count = 0;
		
		do
		{	
			System.out.println("Round: " + count);
			random = r.nextInt(Integer.MAX_VALUE);
			val = random % mod;
			
			message = getMessage(val);
			hash2 = SHA1(message);
			
			String M = getMessage(random);
			
			// CHECK FOR COLLISION
			check = checkCollision(hash, hash2);
			if (check.equals("1"))
			{
				System.out.println("Current hash for " + M);
				System.out.println(hash2 + "\n");
				break;
			}
			count++;
			
		}while(check.equals("0"));
		
		System.out.println("Number of rounds: " + count);
	}
	
	private String checkCollision(String hash, String hash2)
	{
		String x = "0";
		
		if(hash.equals(hash2))
		{
			x = "1";
		}
		else
		{
			x = "0";
		}
		
		return x;
	}
	
	public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException
	{
		Collision c = new Collision();
		
		c.MessageApos();

	}
	
	// HASH FUNCTIONS
	private static String convertToHex(byte[] data)
    { 
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++)
        { 
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do
            { 
                if ((0 <= halfbyte) && (halfbyte <= 9)) 
                    buf.append((char) ('0' + halfbyte));
                else 
                    buf.append((char) ('a' + (halfbyte - 10)));
                halfbyte = data[i] & 0x0F;
            } while(two_halfs++ < 1);
        } 
        return buf.toString();
    } 
	public String SHA1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException
    { 
	    MessageDigest md;
	    md = MessageDigest.getInstance("SHA-1");
	    byte[] sha1hash = new byte[24];
	    md.update(text.getBytes("UTF-8"), 0, text.length());
	    sha1hash = md.digest();
	    String hash = convertToHex(sha1hash);
	    hash = hash.substring(0, 24);
	    
	    return hash;
    } 

}
