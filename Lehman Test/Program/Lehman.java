import java.math.BigInteger;
import java.util.Random;
import java.util.Scanner;

public class Lehman 
{
	// VARIABLES
	long countPrime = 0;
	long countComposite = 0;
	
	// CALCULATION FUNCTIONS
	public static BigInteger random(BigInteger max)
	{
		BigInteger min = new BigInteger("1");
		Random RANDOM = new Random();
        if(max.compareTo(min) < 0)
        {
            BigInteger tmp = min;
            min = max;
            max = tmp;
        }
        else if (max.compareTo(min) == 0)
        {
            return min;
        }
        max = max.add(BigInteger.ONE);
        BigInteger range = max.subtract(min);
        int length = range.bitLength();
        BigInteger result = new BigInteger(length, RANDOM);
        while(result.compareTo(range) >= 0)
        {
            result = new BigInteger(length, RANDOM);
        }
        result = result.add(min);
        return result;
    }
	/*
	private BigInteger random(BigInteger rightLimit)
	{
		Random rand = new Random();
	    BigInteger result = new BigInteger(64, rand);
	    
	    return result;

	}
	*/
	@SuppressWarnings("unused")
	private long gcd(long a, long b)
	{
		long initialA = a;
		long initialB = b;
		
		long r = a % b;
		long q = a / b;
		
		long a1 = 1, b1 = 0, a2 = 0, b2 = 1;
		
		long tempA, tempB;
		
		long gcd = 0;
		
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
		
		System.out.println("GCD is " + gcd);
		
		return gcd;
	}
	 
	// LEHMAN FUNCTIONS
	private long lehmanTest(BigInteger n, int round)
	{
		BigInteger mainNum = n;
		int isPrime = 1;
		
		BigInteger valueOne = new BigInteger("1");
		BigInteger valueTwo = new BigInteger("2");
		BigInteger valueNegOne = new BigInteger("-1");
		
		BigInteger power = (n.subtract(valueOne)).divide(valueTwo);
		
		// CHECK FOR 100 RANDOM VALUES
		for(int i = 0; i < round; i++)
		{
			long j = i + 1;
			
			System.out.println("\nTest " + j);
			System.out.println("---------");
			System.out.println("Power is " + power);
			
			BigInteger max = n.subtract(valueOne);
			BigInteger num = random(max);
			System.out.println("\nRandom value is " + mainNum);
			
			// FIND LHS
			//BigInteger lhs = num.pow(power.intValue());
			
			BigInteger GCD = (num.pow(power.intValue()).gcd(max));
			System.out.println("|" + GCD + "|");
			
			// PRIME OR COMPOSITE
			if(GCD.equals(valueOne) || GCD.equals(valueNegOne))
			{
				countPrime++;
			}
			else
			{
				countComposite++;
			}
		}
		
		System.out.println(countPrime + ", " + countComposite);
		
		if(countPrime > countComposite)
		{
			isPrime = 1;
		}
		else if(countComposite > countPrime)
		{
			isPrime = 0;
		}
		else
		{
			isPrime = 2;
		}
		
		return isPrime;
	}
	
	// MAIN FUNCTIONS
	public static void main(String[] args) 
	{
		Lehman L = new Lehman();
		
		BigInteger value;
		int round;
		
		// OPEN SCANNER
		Scanner sc = new Scanner (System.in);
		
		System.out.print("Enter value: ");
		value = sc.nextBigInteger();
		
		System.out.println("Enter number of rounds: ");
		round = sc.nextInt();
		
		// CHECK IF EVEN OR ODD
		if(!value.mod(new BigInteger("2")).equals(BigInteger.ONE))
		{
			System.out.println("Number is composite");
		}
		else
		{
			// LEHMAN'S TEST
			long isPrime = L.lehmanTest(value, round);
			
			if (isPrime == 0)
			{
				System.out.println("Number is composite");
			}
			else if (isPrime == 1)
			{
				System.out.println("Number is prime");
			}
		}
		// CLOSE SCANNER
		sc.close();

	}

}
