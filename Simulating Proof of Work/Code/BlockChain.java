
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import com.google.gson.*;

public class BlockChain {
	public static ArrayList<Block> blockchain = new ArrayList<Block>(); 

	public static void main(String[] args) throws Exception {
		File ledgerFile = new File(args[3]);
		int difficulty = Integer.parseInt(args[1]);
		ArrayList<String> encryptedTransacList = readFile(ledgerFile);
		String preBlockHashString = "0000";
		
		//add our blocks to the blockchain ArrayList:
		//long starTime = System.currentTimeMillis();
		Block gensisBlock = new Block(encryptedTransacList, preBlockHashString);
		blockchain.add(gensisBlock);
		System.out.println("Trying to Mine block 1... ");
		blockchain.get(0).mineBlock(difficulty);
		
		preBlockHashString = blockchain.get(blockchain.size()-1).getHashValue();
		
		Block b1 = new Block(encryptedTransacList, preBlockHashString);
		blockchain.add(b1);
		System.out.println("Trying to Mine block 2... ");
		blockchain.get(1).mineBlock(difficulty);
		
		preBlockHashString = blockchain.get(blockchain.size()-1).getHashValue();
		
		Block b2 = new Block(encryptedTransacList, preBlockHashString);
		blockchain.add(b2);
		System.out.println("Trying to Mine block 3... ");
		blockchain.get(2).mineBlock(difficulty);	
		
		System.out.println("\nBlockchain is Valid: " + isChainValid());
		
		String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);
		System.out.println("\nThe block chain: ");
		System.out.println(blockchainJson);
		
	}
	
	public static Boolean isChainValid() throws Exception {
		Block currentBlock; 
		Block previousBlock;
		
		//loop through blockchain to check hashes:
		for(int i=1; i < blockchain.size(); i++) {
			currentBlock = blockchain.get(i);
			previousBlock = blockchain.get(i-1);
			//compare registered hash and calculated hash:
			if(!currentBlock.genHashValue().equals(currentBlock.genHashValue()) ){
				System.out.println("Current Hashes not equal");			
				return false;
			}
			//compare previous hash and registered previous hash
			if(!previousBlock.genHashValue().equals(currentBlock.getPreviousBlockHash()) ) {
				System.out.println("Previous Hashes not equal");
				return false;
			}
		}
		return true;
	}
	// this function read the files that contains dummy transaction data
			// it is assumed that each line of the file only contains one transaction data.
			private static ArrayList<String> readFile(File file) {
				ArrayList<String > transactions = new ArrayList<>();
				String readString = "";
				
				try(FileReader fReader = new FileReader(file);
					BufferedReader bReader =new BufferedReader(fReader)) {
					
					readString = bReader.readLine();
					while (readString!=null) {
							transactions.add(aESinCBCS(readString));
							readString = bReader.readLine();
						
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return transactions;
			}
//https://www.javacodegeeks.com/2018/03/aes-encryption-and-decryption-in-javacbc-mode.html
			public static String aESinCBCS(String plainText)
					throws Exception {
				KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
				keyGenerator.init(128);
				SecretKey secretKey = keyGenerator.generateKey();
				Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
				byte[] plainTextByte = plainText.getBytes();
				cipher.init(Cipher.ENCRYPT_MODE, secretKey);
				byte[] encryptedByte = cipher.doFinal(plainTextByte);
				Base64.Encoder encoder = Base64.getEncoder();
				String encryptedText = encoder.encodeToString(encryptedByte);
				
				return SecureHash.generateHash(encryptedText);
		}
		
			
}

// This class provide the blueprints for Block


class Block {

	public String getHashValue() {
		return hashValue;
	}

	public void setHashValue(String hashValue) {
		this.hashValue = hashValue;
	}

	public String getPreviousBlockHash() {
		return previousBlockHash;
	}

	public void setPreviousBlockHash(String previousBlockHash) {
		this.previousBlockHash = previousBlockHash;
	}

	public int getNonce() {
		return nonce;
	}

	public void setNonce(int nonce) {
		this.nonce = nonce;
	}

	private String hashValue;
	private String previousBlockHash;
	private ArrayList<String> transactionData; // our data will be a simple message.
	private long timeStamp; // as number of milliseconds since 1/1/1970.
	
	private int nonce;

	// Block Constructor.
	public Block(ArrayList<String> tData, String pbHash) throws Exception {
		transactionData = tData;
		previousBlockHash = pbHash;
		timeStamp =System.currentTimeMillis();
		nonce=0;
		hashValue = genHashValue(); //Making sure we do this after we set the other values.
	}

	public String genHashValue() throws Exception {
		String generatedHash = SecureHash.generateHash(previousBlockHash + Long.toString(timeStamp) + transactionData + Integer.toString(nonce));
		return generatedHash;
	}
	
	public void mineBlock(int difficulty) throws Exception {
		String target = new String(new char[difficulty]).replace('\0', '0'); //Create a string with difficulty * "0" 
		while(!hashValue.substring( 0, difficulty).equals(target)) {
			this.nonce++;
			hashValue = genHashValue();
		}
		System.out.println("Block Mined!!! : " + hashValue);
	}
	

}




 class SecureHash {
	
	 static String generateHash(String value) throws Exception {
		String hash = null;
		
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] bytes = md.digest(value.getBytes(StandardCharsets.UTF_8));
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			hash = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return hash;
	}	
	
	
}
